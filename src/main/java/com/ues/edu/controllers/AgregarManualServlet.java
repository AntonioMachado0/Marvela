/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author thebe
 */
@WebServlet("/agregarManual")
public class AgregarManualServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String json = reader.lines().collect(Collectors.joining());
        Gson gson = new Gson();
        Map<String, String> producto = gson.fromJson(json, Map.class);

        List<Map<String, String>> escaneos = ScanReceiver.getEscaneos();
        boolean yaExiste = escaneos.stream().anyMatch(p -> p.get("codigo").equals(producto.get("codigo")));

        if (!yaExiste) escaneos.add(producto);

        response.setContentType("application/json");
        response.getWriter().write("{\"exito\":true}");
    }
}
