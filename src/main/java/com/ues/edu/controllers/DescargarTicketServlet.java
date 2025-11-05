/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author thebe
 */
@WebServlet("/descargarTicket")
public class DescargarTicketServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String numeroVenta = request.getParameter("numero");
        if (numeroVenta == null || numeroVenta.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Número de venta faltante.");
            return;
        }

        String ruta = getServletContext().getRealPath("/tickets/ticket_" + numeroVenta + ".pdf");
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ticket no encontrado.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ticket_" + numeroVenta + ".pdf");
        response.setContentLength((int) archivo.length());

        try (FileInputStream fis = new FileInputStream(archivo);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
