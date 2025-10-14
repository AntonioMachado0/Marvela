/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.Productos;
import com.ues.edu.models.dao.ProductoDao;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Maris
 */
@WebServlet("/ImagenServlet")
public class ImagenServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                System.out.println("⚠️ ID no proporcionado");
                sendDefaultImage(response); // ✅ desde base
                return;
            }

            int id = Integer.parseInt(idParam.trim());
            ProductoDao dao = new ProductoDao();
            Productos producto = dao.obtenerProductoPorId(id);

            if (producto == null || producto.getImagen() == null || producto.getImagen().length == 0) {
                System.out.println("⚠️ Imagen no encontrada para ID: " + id);
                sendDefaultImage(response); // ✅ desde base
                return;
            }

            response.setContentType("image/jpeg"); // o dinámico si tienes MIME
            OutputStream out = response.getOutputStream();
            out.write(producto.getImagen());
            out.flush();

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            sendDefaultImage(response); // ✅ fallback seguro
        }
    }

    private void sendDefaultImage(HttpServletResponse response) throws IOException {
        ProductoDao dao = new ProductoDao();
        Productos defecto = dao.obtenerProductoPorId(0); // 👈 ID reservado

        if (defecto != null && defecto.getImagen() != null) {
            response.setContentType("image/jpeg");
            OutputStream out = response.getOutputStream();
            out.write(defecto.getImagen());
            out.flush();
            System.out.println("✅ Imagen por defecto enviada desde base");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imagen por defecto no disponible");
        }
    }
}