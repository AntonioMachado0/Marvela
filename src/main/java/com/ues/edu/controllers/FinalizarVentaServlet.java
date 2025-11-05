/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.ues.edu.models.Usuario;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.servlet.annotation.WebServlet;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

/**
 *
 * @author thebe
 */
@WebServlet("/finalizarVenta")
public class FinalizarVentaServlet extends HttpServlet {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/FerreteriaBD", "postgres", "root");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response); 
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession();

    
    List<Map<String, String>> resumenVenta = ScanReceiver.getEscaneos(request);
    if (resumenVenta == null || resumenVenta.isEmpty()) {
        response.setContentType("text/plain");
        response.getWriter().write("No hay productos escaneados.");
        return;
    }

    
    Usuario usuario = (Usuario) session.getAttribute("Usuario");
    String nombreVendedor = usuario != null && usuario.getEmpleado() != null
        ? usuario.getEmpleado().getNombreCompleto()
        : "Vendedor desconocido";

    try (Connection conn = getConnection()) {
        conn.setAutoCommit(false);

        int numeroVenta = generarNumeroVenta(conn);
        String numeroVentaFormateado = String.format("%04d", numeroVenta); 

        PreparedStatement ventaStmt = conn.prepareStatement(
            "INSERT INTO ventas (numero_venta, hora, fecha, codigo_empleado) VALUES (?, ?, ?, ?) RETURNING codigo_ventas"
        );
        ventaStmt.setString(1, numeroVentaFormateado);
        ventaStmt.setTime(2, new java.sql.Time(System.currentTimeMillis()));
        ventaStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
        ventaStmt.setInt(4, obtenerEmpleadoActual(request));

        ResultSet rsVenta = ventaStmt.executeQuery();
        int codigoVenta = rsVenta.next() ? rsVenta.getInt("codigo_ventas") : -1;
        rsVenta.close();
        ventaStmt.close();

        if (codigoVenta == -1) {
            conn.rollback();
            response.getWriter().write("No se pudo generar el código de venta.");
            return;
        }

        PreparedStatement detalleStmt = conn.prepareStatement(
            "INSERT INTO detalle_ventas (cantidad_producto, total_ventas, codigo_ventas, id_producto, codigo_detalle_compra) VALUES (?, ?, ?, ?, ?)"
        );
        PreparedStatement actualizarStockStmt = conn.prepareStatement(
            "UPDATE detalle_compra SET cantidad_producto = cantidad_producto - ? WHERE codigo_producto = ?"
        );

        for (Map<String, String> producto : resumenVenta) {
            String codigoProducto = producto.get("codigo");
            int cantidad = Integer.parseInt(producto.get("cantidad"));
            double precio = Double.parseDouble(producto.get("precio de venta unitario"));
            double total = cantidad * precio;

            int idProducto = obtenerIdProducto(codigoProducto, conn);
            int codigoDetalleCompra = obtenerCodigoDetalleCompra(codigoProducto, conn);

            detalleStmt.setInt(1, cantidad);
            detalleStmt.setDouble(2, total);
            detalleStmt.setInt(3, codigoVenta);
            detalleStmt.setInt(4, idProducto);
            detalleStmt.setInt(5, codigoDetalleCompra);
            detalleStmt.addBatch();

            actualizarStockStmt.setInt(1, cantidad);
            actualizarStockStmt.setString(2, codigoProducto);
            actualizarStockStmt.addBatch();
        }

        detalleStmt.executeBatch();
        actualizarStockStmt.executeBatch();
        detalleStmt.close();
        actualizarStockStmt.close();

        conn.commit();

        
        generarTicketPDF(response, "#" + numeroVentaFormateado, nombreVendedor, resumenVenta);

       
        ScanReceiver.limpiarEscaneos(request);

    } catch (Exception e) {
        e.printStackTrace();
        response.setContentType("text/plain");
        response.getWriter().write("Error al registrar la venta.");
    }
}

   private void generarTicketPDF(HttpServletResponse response, String numeroVenta, String nombreVendedor, List<Map<String, String>> productos)
        throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A6, 20, 20, 20, 20);

    try {
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        Font titulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 9);
        Font negrita = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font totalGrande = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font ventaGrande = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font mensajeGrande = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font encabezadoTabla = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);

        Paragraph encabezado = new Paragraph("FERRETERÍA MARVELA", titulo);
        encabezado.setAlignment(Element.ALIGN_CENTER);
        doc.add(encabezado);

        Paragraph direccion = new Paragraph("Calle Principal #123 - San Vicente\nTel: (503) 2222-3333\nwww.marvela.com", normal);
        direccion.setAlignment(Element.ALIGN_CENTER);
        doc.add(direccion);

        doc.add(new Paragraph("____________________________________"));

        Paragraph venta = new Paragraph(" TICKET DE VENTA " + numeroVenta, ventaGrande);
        venta.setAlignment(Element.ALIGN_CENTER);
        doc.add(venta);

        doc.add(new Paragraph("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "   Hora: " + LocalTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("HH:mm")), normal));
        doc.add(new Paragraph("Vendedor: " + nombreVendedor, normal));
        doc.add(new Paragraph("____________________________________"));

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{3.5f, 1f, 1.2f, 1.3f});
        tabla.setWidthPercentage(100);

        Stream.of("Producto", "Cant.", "Precio", "Total").forEach(tituloColumna -> {
            PdfPCell celda = new PdfPCell(new Phrase(tituloColumna, encabezadoTabla));
            celda.setBackgroundColor(new BaseColor(0, 128, 0));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(4);
            tabla.addCell(celda);
        });

        int totalCantidad = 0;
        double totalVenta = 0.0;

        Map<String, Map<String, String>> agrupados = new LinkedHashMap<>();
        for (Map<String, String> p : productos) {
            String codigo = p.get("codigo");
            String nombre = p.get("nombre");
            String cantidadStr = p.get("cantidad");
            String precioStr = p.get("precio de venta unitario");

            if (codigo == null || nombre == null || cantidadStr == null || precioStr == null) continue;

            try {
                int cantidad = Integer.parseInt(cantidadStr);
                double precio = Double.parseDouble(precioStr);

                if (agrupados.containsKey(codigo)) {
                    Map<String, String> existente = agrupados.get(codigo);
                    int cantidadExistente = Integer.parseInt(existente.get("cantidad"));
                    existente.put("cantidad", String.valueOf(cantidadExistente + cantidad));
                } else {
                    Map<String, String> nuevo = new HashMap<>();
                    nuevo.put("codigo", codigo);
                    nuevo.put("nombre", nombre);
                    nuevo.put("cantidad", String.valueOf(cantidad));
                    nuevo.put("precio de venta unitario", String.valueOf(precio));
                    agrupados.put(codigo, nuevo);
                }
            } catch (NumberFormatException ex) {
                System.out.println("⚠ Error al convertir cantidad o precio: " + p);
            }
        }

        for (Map<String, String> p : agrupados.values()) {
            try {
                String nombre = p.get("nombre");
                int cantidad = Integer.parseInt(p.get("cantidad"));
                double precio = Double.parseDouble(p.get("precio de venta unitario"));
                double total = cantidad * precio;

                tabla.addCell(new PdfPCell(new Phrase(nombre, normal)));
                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(cantidad), normal)));
                tabla.addCell(new PdfPCell(new Phrase("$" + String.format("%.2f", precio), normal)));
                tabla.addCell(new PdfPCell(new Phrase("$" + String.format("%.2f", total), normal)));

                totalCantidad += cantidad;
                totalVenta += total;
            } catch (Exception ex) {
                System.out.println("⚠ Error al procesar producto en ticket: " + p + " → " + ex.getMessage());
            }
        }

        doc.add(tabla);
        doc.add(new Paragraph("TOTAL PRODUCTOS: " + totalCantidad, negrita));
        doc.add(new Paragraph("____________________________________"));

        Paragraph totalPagar = new Paragraph("TOTAL A PAGAR:   $" + String.format("%.2f", totalVenta), totalGrande);
        totalPagar.setAlignment(Element.ALIGN_CENTER);
        doc.add(totalPagar);

        
                        doc.add(new Paragraph("____________________________________"));

        
        float centerX = doc.getPageSize().getWidth() / 2;
        float baseY = doc.bottomMargin() - 5;

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
            new Phrase(" Gracias por su compra", mensajeGrande), centerX, baseY + 20, 0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
            new Phrase(" Si tiene dudas, contáctenos", normal), centerX, baseY + 10, 0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
            new Phrase(" Cambios de producto dentro de 3 días hábiles presentando este ticket", normal),
            centerX, baseY, 0);

        doc.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ticket_" + numeroVenta + ".pdf");
        response.setContentLength(baos.size());

        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();

    } catch (Exception e) {
        e.printStackTrace();
        response.setContentType("text/plain");
        response.getWriter().write("Error al generar el ticket PDF.");
    }
}

    private int generarNumeroVenta(Connection conn) throws SQLException {
        String sql = "SELECT MAX(CAST(numero_venta AS INTEGER)) FROM ventas";
        try ( PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        }
    }

    private int obtenerEmpleadoActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("codigo_empleado") != null)
                ? (int) session.getAttribute("codigo_empleado") : 1;
    }

    private int obtenerIdProducto(String codigoProducto, Connection conn) throws SQLException {
        String sql = "SELECT p.id_producto FROM productos p JOIN detalle_compra dc ON p.id_producto = dc.id_producto WHERE dc.codigo_producto = ? LIMIT 1";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoProducto);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("id_producto") : -1;
            }
        }
    }

    private int obtenerCodigoDetalleCompra(String codigoProducto, Connection conn) throws SQLException {
        String sql = "SELECT codigo_detalle_compra FROM detalle_compra WHERE codigo_producto = ? LIMIT 1";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoProducto);
            try ( ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("codigo_detalle_compra") : -1;
            }
        }
    }
}


