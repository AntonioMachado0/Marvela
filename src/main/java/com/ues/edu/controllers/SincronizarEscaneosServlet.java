/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author thebe
 */
@WebServlet("/sincronizar")
public class SincronizarEscaneosServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, String>> estaticos = ScanReceiver.getEscaneos();
        List<Map<String, String>> sesion = ScanReceiver.getEscaneos(request);

        sesion.clear();
        for (Map<String, String> p : estaticos) {
            sesion.add(new HashMap<>(p));
        }

        System.out.println("🔄 Productos sincronizados desde lista estática a sesión: " + sesion.size());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
