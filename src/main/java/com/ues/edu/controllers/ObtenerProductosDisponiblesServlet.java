/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

/**
 *
 * @author mayel
 */


import com.ues.edu.models.dao.DevolucionDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

@WebServlet("/ObtenerProductosDisponiblesServlet")
public class ObtenerProductosDisponiblesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            DevolucionDao devolucionDao = new DevolucionDao();
            List<Map<String, Object>> productos = devolucionDao.obtenerProductosDisponibles();
            
            JSONArray jsonArray = new JSONArray(productos);
            response.getWriter().write(jsonArray.toString());
            
        } catch (SQLException e) {
            Logger.getLogger(ObtenerProductosDisponiblesServlet.class.getName()).log(Level.SEVERE, null, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error al obtener productos disponibles\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ObtenerProductosDisponiblesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}