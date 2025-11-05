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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author thebe
 */
@WebServlet("/actualizarCantidad")
public class ActualizarCantidadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(body);

        String codigo = json.getString("codigo");
        int nuevaCantidad = json.getInt("cantidad");

        boolean actualizado = false;
        for (Map<String, String> producto : ScanReceiver.getEscaneos()) {
            if (producto.get("codigo").equals(codigo)) {
                producto.put("cantidad", String.valueOf(nuevaCantidad));
                actualizado = true;
                break;
            }
        }

        JSONObject resultado = new JSONObject();
        resultado.put("exito", actualizado);
        response.setContentType("application/json");
        response.getWriter().write(resultado.toString());
    }
}
