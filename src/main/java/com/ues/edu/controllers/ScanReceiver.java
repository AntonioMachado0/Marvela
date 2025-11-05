package com.ues.edu.controllers;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/scan")
public class ScanReceiver extends HttpServlet {

    private static final List<Map<String, String>> escaneos = new ArrayList<>();
    private static String ultimoCodigoEscaneado = "";
    private static long tiempoUltimoEscaneo = 0;

    public static List<Map<String, String>> getEscaneos() {
        return escaneos;
    }

    public static void limpiarEscaneos() {
        escaneos.clear();
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getEscaneos(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Map<String, String>> escaneosSesion = (List<Map<String, String>>) session.getAttribute("escaneos");
        if (escaneosSesion == null) {
            escaneosSesion = new ArrayList<>();
            session.setAttribute("escaneos", escaneosSesion);
        }
        return escaneosSesion;
    }

    public static void limpiarEscaneos(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("escaneos");
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/FerreteriaBD", "postgres", "root"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String barcode = request.getParameter("code");
        System.out.println("📦 Código recibido desde Android: " + barcode);

        // ⛔ Bloqueo de escaneo duplicado en menos de 1 segundo
        long ahora = System.currentTimeMillis();
        if (barcode.equals(ultimoCodigoEscaneado) && (ahora - tiempoUltimoEscaneo < 1000)) {
    // Buscar el producto en la lista escaneos y devolver sus datos sin modificar cantidad
    for (Map<String, String> producto : escaneos) {
        if (producto.get("codigo").equals(barcode)) {
            String nombre = producto.get("nombre");
            String marca = producto.get("marca");
            String precio = producto.get("precio de venta unitario");
            String cantidad = producto.get("cantidad");

            String resultado = nombre + " - " + marca + " - $" + precio + " - Cantidad: " + cantidad + " (escaneo repetido)";
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultado);
            return;
        }
    }

    // Si no lo encuentra, devolver mensaje genérico
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("Escaneo duplicado ignorado.");
    return;
}
        ultimoCodigoEscaneado = barcode;
        tiempoUltimoEscaneo = ahora;

        String resultado = "Código no encontrado";

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT dc.codigo_producto, p.nombre_producto, m.nombre_marca, dc.cantidad_producto, " +
                "ROUND(CAST(COALESCE(dc.precio_compra, 0) * (1 + COALESCE(dc.porcentaje, 0) / 100.0) AS numeric), 2) AS precio_venta_unitario " +
                "FROM productos p " +
                "JOIN detalle_compra dc ON p.id_producto = dc.id_producto " +
                "JOIN marca m ON dc.id_marca = m.id_marca " +
                "WHERE dc.codigo_producto = ?"
            );
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String codigoProducto = rs.getString("codigo_producto").trim();
                String nombre = rs.getString("nombre_producto");
                String marca = rs.getString("nombre_marca");
                int stockDisponible = rs.getInt("cantidad_producto");
if (stockDisponible <= 0) {
    resultado = "Este producto no tiene unidades disponibles en inventario.";
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(resultado);
    return;
}
                String precio = rs.getBigDecimal("precio_venta_unitario").toString();

                boolean actualizado = false;

                for (Map<String, String> producto : escaneos) {
                    if (producto.get("codigo").equals(codigoProducto)) {
                        int cantidadActual = Integer.parseInt(producto.get("cantidad"));
                        if (cantidadActual >= stockDisponible) {
                            resultado = "Ya se escanearon todas las unidades disponibles de este producto.";
                        } else {
                            producto.put("cantidad", String.valueOf(cantidadActual + 1));
                            resultado = nombre + " - " + marca + " - $" + precio + " - Cantidad: " + (cantidadActual + 1);
                            System.out.println("🔁 Cantidad actualizada en lista estática: " + producto.get("cantidad"));
                        }
                        actualizado = true;
                        break;
                    }
                }
                

                if (!actualizado) {
                    Map<String, String> datos = new HashMap<>();
                    datos.put("codigo", codigoProducto);
                    datos.put("nombre", nombre);
                    datos.put("marca", marca);
                    datos.put("cantidad", "1");
                    datos.put("stock", String.valueOf(stockDisponible));
                    datos.put("precio de venta unitario", precio);
                    escaneos.add(datos);
                    resultado = nombre + " - " + marca + " - $" + precio + " - Cantidad: 1";
                    System.out.println("✅ Producto agregado a lista estática: " + codigoProducto);
                }

                // 🔄 Sincronizar con la sesión
                List<Map<String, String>> escaneosSesion = getEscaneos(request);
                escaneosSesion.clear();
                for (Map<String, String> p : escaneos) {
                    escaneosSesion.add(new HashMap<>(p));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = "Error en la base de datos";
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultado);
    }

    private int generarNumeroVenta(Connection conn) throws SQLException {
        String sql = "SELECT MAX(numero_venta) FROM ventas";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) + 1 : 1;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, String>> escaneosGlobales = getEscaneos();

        StringBuilder tablaHTML = new StringBuilder();
        tablaHTML.append("<div id='tabla-container'>");
        tablaHTML.append("<table class='table table-bordered table-striped'>");
        tablaHTML.append("<thead><tr><th>Código</th><th>Nombre</th><th>Marca</th><th>Cantidad</th><th>Precio</th></tr></thead><tbody>");

        if (escaneosGlobales.isEmpty()) {
            tablaHTML.append("<tr><td colspan='5' class='text-center text-muted'>");
            tablaHTML.append("<i class='fas fa-box-open'></i> Aún no se ha escaneado ningún producto.");
            tablaHTML.append("</td></tr>");
        } else {
            for (Map<String, String> dato : escaneosGlobales) {
                tablaHTML.append("<tr>");
                tablaHTML.append("<td>").append(dato.get("codigo")).append("</td>");
                tablaHTML.append("<td>").append(dato.get("nombre")).append("</td>");
                tablaHTML.append("<td>").append(dato.get("marca")).append("</td>");
                tablaHTML.append("<td>").append(dato.get("cantidad")).append("</td>");
                tablaHTML.append("<td>$").append(dato.get("precio de venta unitario")).append("</td>");
                tablaHTML.append("</tr>");
            }
        }

        tablaHTML.append("</tbody></table>");
        tablaHTML.append("</div>");

        String ajax = request.getParameter("ajax");
        if ("true".equals(ajax)) {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(tablaHTML.toString());
            return;
        }

        try (Connection conn = getConnection()) {
            int numero = generarNumeroVenta(conn);
            String numeroVenta = String.format("%05d", numero);
            request.setAttribute("numeroVenta", numeroVenta);
            System.out.println("✔ Enviando número de venta anticipado: " + numeroVenta);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("numeroVenta", null);
        }

        request.setAttribute("tablaHTML", tablaHTML.toString());
        request.setAttribute("escaneos", escaneosGlobales);
        request.getRequestDispatcher("/vistaEscaneos.jsp").forward(request, response);
    }
}