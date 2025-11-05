/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;
import com.ues.edu.models.dao.VentaDAO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.servlet.annotation.WebServlet;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thebe
 */
@WebServlet("/RegistrarVentaServlet")
public class RegistrarVentaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Map<String, String>> escaneos = ScanReceiver.getEscaneos();

        // 🚫 Validación: no permitir venta vacía
        if (escaneos == null || escaneos.isEmpty()) {
            JsonObject error = new JsonObject();
            error.addProperty("exito", false);
            error.addProperty("mensaje", "No se puede registrar una venta sin productos.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(error.toString());
            return;
        }

        VentaDAO dao = new VentaDAO();
        String numeroVentaFormateado = dao.generarNumeroVentaFormateado();

        int codigoEmpleado = 1;
        int codigoVenta = dao.insertarVenta(numeroVentaFormateado, codigoEmpleado);

        int totalCantidad = 0;
        double totalVenta = 0.0;

        for (Map<String, String> producto : escaneos) {
            String codigo = producto.get("codigo");
            int cantidad = Integer.parseInt(producto.get("cantidad"));
            double precio = Double.parseDouble(producto.get("precio de venta unitario"));
            int idProducto = dao.obtenerIdProductoPorCodigo(codigo);
            dao.insertarDetalleVenta(cantidad, cantidad * precio, codigoVenta, idProducto);

            totalCantidad += cantidad;
            totalVenta += cantidad * precio;
        }

        escaneos.clear();

        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("exito", true);
        respuesta.addProperty("numeroVenta", numeroVentaFormateado);
        respuesta.addProperty("cantidadTotal", totalCantidad);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respuesta.toString());
    }
}