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
@WebServlet("/quitarProducto")
public class QuitarProductoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String json = reader.lines().collect(Collectors.joining());
        Gson gson = new Gson();
        Map<String, String> datos = gson.fromJson(json, Map.class);

        String codigo = datos.get("codigo");

        List<Map<String, String>> escaneos = ScanReceiver.getEscaneos();
        escaneos.removeIf(p -> p.get("codigo").equals(codigo));

        response.setContentType("application/json");
        response.getWriter().write("{\"exito\":true}");
    }
}

