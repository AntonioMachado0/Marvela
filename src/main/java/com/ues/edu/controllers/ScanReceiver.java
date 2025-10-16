package com.ues.edu.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/scan")
public class ScanReceiver extends HttpServlet {

    private static final List<Map<String, String>> escaneos = new ArrayList<>();

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de PostgreSQL", e);
        }

        String url = "jdbc:postgresql://localhost:5432/FerreteriaBD";
        String user = "postgres";
        String pass = "root";
        return DriverManager.getConnection(url, user, pass);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String barcode = request.getParameter("code");
        if (barcode != null) {
            barcode = barcode.trim();
        }

        String resultado = "Código no encontrado";

        try ( Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT dc.codigo_producto, p.nombre_producto, "
                    + "m.nombre_marca, "
                    + "ROUND(CAST(dc.precio_compra * (1 + dc.porcentaje / 100.0) AS numeric), 2) AS precio_venta_unitario "
                    + "FROM productos p "
                    + "JOIN detalle_compra dc ON p.id_producto = dc.id_producto "
                    + "JOIN marca m ON dc.id_marca = m.id_marca "
                    + "WHERE dc.codigo_producto = ?"
            );
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String codigoProducto = rs.getString("codigo_producto").trim();
                String nombre = rs.getString("nombre_producto");
                String marca = rs.getString("nombre_marca");
                String precio = rs.getBigDecimal("precio_venta_unitario").toString();

                resultado = nombre + " - " + marca + " - $" + precio;

                boolean yaEscaneado = escaneos.stream()
                        .anyMatch(p -> p.get("codigo").equals(codigoProducto));

                if (!yaEscaneado) {
                    Map<String, String> datos = new HashMap<>();
                    datos.put("codigo", codigoProducto);
                    datos.put("nombre", nombre);
                    datos.put("marca", marca);
                    datos.put("precio de venta unitario", precio);
                    escaneos.add(datos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resultado = "Error en la base de datos";
        }

        // Enviar solo texto plano como respuesta
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(resultado);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("escaneos", escaneos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/vistaEscaneos.jsp");
        dispatcher.forward(request, response);
    }
}
