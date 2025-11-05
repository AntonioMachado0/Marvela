/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

/**
 *
 * @author mayel
 */




import com.itextpdf.text.BaseColor;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@WebServlet("/generarTicketDevolucion")
public class GenerarTicketDevolucionServlet extends HttpServlet {

    // Conexión a PostgreSQL
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://localhost:5434/FerreteriaBD", "postgres", "dd22005");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numeroVenta = request.getParameter("numeroVenta");
        if (numeroVenta == null || numeroVenta.isEmpty()) {
            response.setContentType("text/plain");
            response.getWriter().write("Debe proporcionar un número de venta.");
            return;
        }

        try (Connection conn = getConnection()) {

            // 🔹 1. Obtener la información de la venta original
          String sqlVenta =
    "SELECT v.codigo_ventas, v.numero_venta, v.fecha, v.hora, e.nombre_completo " +
    "FROM ventas v " +
    "JOIN empleado e ON v.codigo_empleado = e.codigo_empleado " +
    "WHERE v.numero_venta = ?";

            PreparedStatement psVenta = conn.prepareStatement(sqlVenta);
            psVenta.setString(1, numeroVenta);
            ResultSet rsVenta = psVenta.executeQuery();

            if (!rsVenta.next()) {
                response.setContentType("text/plain");
                response.getWriter().write("No se encontró la venta #" + numeroVenta);
                return;
            }

            int codigoVentas = rsVenta.getInt("codigo_ventas");
            String fecha = rsVenta.getString("fecha");
            String hora = rsVenta.getString("hora");
            String empleado = rsVenta.getString("nombre_completo");

            rsVenta.close();
            psVenta.close();

            // 🔹 2. Obtener los productos actuales de esa venta (ya modificada tras la devolución)
            // 2️⃣ Obtener los productos actuales de esa venta (ya modificada tras la devolución)
String sqlProductos =
    "SELECT p.nombre_producto, m.nombre_marca, dv.cantidad_producto, " +
    "       ROUND((dv.total_ventas / NULLIF(dv.cantidad_producto, 0))::numeric, 2) AS precio_unitario, " +
    "       dv.total_ventas " +
    "FROM detalle_ventas dv " +
    "JOIN productos p ON dv.id_producto = p.id_producto " +
    "JOIN detalle_compra dc ON dv.codigo_detalle_compra = dc.codigo_detalle_compra " +
    "JOIN marca m ON dc.id_marca = m.id_marca " +
    "WHERE dv.codigo_ventas = ? " +
    "ORDER BY p.nombre_producto ASC";


            PreparedStatement psProductos = conn.prepareStatement(sqlProductos);
            psProductos.setInt(1, codigoVentas);
            ResultSet rsProductos = psProductos.executeQuery();

            List<Map<String, Object>> productos = new ArrayList<>();
            while (rsProductos.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("nombre", rsProductos.getString("nombre_producto"));
                p.put("marca", rsProductos.getString("nombre_marca"));
                p.put("cantidad", rsProductos.getInt("cantidad_producto"));
                p.put("precio", rsProductos.getDouble("precio_unitario"));
                p.put("total", rsProductos.getDouble("total_ventas"));
                productos.add(p);
            }

            rsProductos.close();
            psProductos.close();

            if (productos.isEmpty()) {
                response.setContentType("text/plain");
                response.getWriter().write("No hay productos registrados para esta venta.");
                return;
            }

            // 🔹 3. Generar el PDF del ticket
            generarTicketDevolucionPDF(response, numeroVenta,  empleado, productos);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error al generar ticket de devolución: " + e.getMessage());
        }
    }

    // ==============================================================
    // ===============   GENERAR PDF DE DEVOLUCIÓN   ================
    // ==============================================================

 private void generarTicketDevolucionPDF(HttpServletResponse response,
                                        String numeroVenta,
                                        String empleado,
                                        List<Map<String, Object>> productos)
        throws IOException {

    // ---- FECHA Y HORA LOCAL ----
    java.time.LocalDateTime ahora = java.time.LocalDateTime.now(); // <-- obtiene fecha y hora local
    java.time.format.DateTimeFormatter formatoFecha = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    java.time.format.DateTimeFormatter formatoHora = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
    String fecha = ahora.format(formatoFecha); // <-- formatea fecha local
    String hora = ahora.format(formatoHora);   // <-- formatea hora local

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A6, 20, 20, 20, 20);

    try {
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        // ---- FUENTES ----
        Font titulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 9);
        Font negrita = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font totalGrande = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font encabezadoTabla = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);

        // ---- ENCABEZADO ----
        Paragraph encabezado = new Paragraph("FERRETERÍA MARVELA", titulo);
        encabezado.setAlignment(Element.ALIGN_CENTER);
        doc.add(encabezado);

        Paragraph direccion = new Paragraph(
            "Calle Principal #123 - San Vicente\nTel: (503) 2222-3333\nwww.marvela.com", normal);
        direccion.setAlignment(Element.ALIGN_CENTER);
        doc.add(direccion);

        doc.add(Chunk.NEWLINE);

        // ---- TITULO DE DEVOLUCIÓN ----
        Paragraph devolucion = new Paragraph("TICKET DE DEVOLUCIÓN DE VENTA #" + numeroVenta, totalGrande);
        devolucion.setAlignment(Element.ALIGN_CENTER);
        doc.add(devolucion);

        doc.add(Chunk.NEWLINE);

        // ---- DATOS DE LA VENTA ----
        Paragraph info = new Paragraph("Fecha: " + fecha + "   Hora: " + hora, normal);
        info.setAlignment(Element.ALIGN_LEFT);
        doc.add(info);

        Paragraph empleadoTxt = new Paragraph("Atendido por: " + empleado, normal);
        empleadoTxt.setAlignment(Element.ALIGN_LEFT);
        doc.add(empleadoTxt);

        doc.add(Chunk.NEWLINE);

        // ---- TABLA DE PRODUCTOS ----
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{3.5f, 1f, 1.2f, 1.3f});
        tabla.setWidthPercentage(100);

        Stream.of("Producto", "Cant.", "Precio", "Total").forEach(col -> {
            PdfPCell celda = new PdfPCell(new Phrase(col, encabezadoTabla));
            celda.setBackgroundColor(new BaseColor(0, 128, 0));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(4);
            tabla.addCell(celda);
        });

        int totalCantidad = 0;
        double totalMonto = 0.0;

        for (Map<String, Object> p : productos) {
            String nombre = p.get("nombre") + " (" + p.get("marca") + ")";
            int cantidad = (int) p.get("cantidad");
            double precio = (double) p.get("precio");
            double total = (double) p.get("total");

            tabla.addCell(new Phrase(nombre, normal));

            PdfPCell celdaCantidad = new PdfPCell(new Phrase(String.valueOf(cantidad), normal));
            celdaCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celdaCantidad);

            PdfPCell celdaPrecio = new PdfPCell(new Phrase("$" + String.format("%.2f", precio), normal));
            celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaPrecio);

            PdfPCell celdaTotal = new PdfPCell(new Phrase("$" + String.format("%.2f", total), normal));
            celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaTotal);

            totalCantidad += cantidad;
            totalMonto += total;
        }

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);

        Paragraph totalProd = new Paragraph("TOTAL PRODUCTOS: " + totalCantidad, negrita);
        totalProd.setAlignment(Element.ALIGN_LEFT);
        doc.add(totalProd);

        doc.add(Chunk.NEWLINE);

        Paragraph totalPagar = new Paragraph("TOTAL ACTUAL: $" + String.format("%.2f", totalMonto), totalGrande);
        totalPagar.setAlignment(Element.ALIGN_CENTER);
        doc.add(totalPagar);

        doc.add(Chunk.NEWLINE);

        Paragraph gracias = new Paragraph("Gracias por su visita", negrita);
        gracias.setAlignment(Element.ALIGN_CENTER);
        doc.add(gracias);

        Paragraph nota = new Paragraph("Su devolución fue procesada correctamente", normal);
        nota.setAlignment(Element.ALIGN_CENTER);
        doc.add(nota);

        // ---- CERRAR PDF ----
        doc.close();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ticket_devolucion_" + numeroVenta + ".pdf");
        response.setContentLength(baos.size());

        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();

    } catch (Exception e) {
        e.printStackTrace();
        response.setContentType("text/plain");
        response.getWriter().write("Error al generar PDF de devolución: " + e.getMessage());
    }
}


}

