/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

/**
 *
 * @author mayel
 */
import com.google.gson.Gson;
import com.ues.edu.models.dao.InventarioDao;
import com.ues.edu.connection.Conexion;
import com.ues.edu.models.ProductoCompra;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@WebServlet("/InventarioServlet")
public class InventarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private Conexion conexion;
   private InventarioDao dao;
   
    public InventarioServlet() {
        this.conexion = new Conexion();
        dao = new InventarioDao();
    }
    

   protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    response.setContentType("application/json;charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    
    PrintWriter out = response.getWriter();
    
    try {
        conexion = new Conexion();
        
        ArrayList<ProductoCompra> productos = dao.mostrarProductosAgrupados();
        
        // Usar TreeMap para orden automático por fecha (más antigua primero)
        Map<String, ArrayList<ProductoCompra>> productosPorFecha = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String fecha1, String fecha2) {
                // Ordenar cronológicamente (más antigua primero)
                return fecha1.compareTo(fecha2);
            }
        });
        
        for (ProductoCompra producto : productos) {
            String fechaCompra = producto.getFechaCompra().toString();
            System.out.println("ESTAS SON LAS FECHAS "+fechaCompra);
            if (!productosPorFecha.containsKey(fechaCompra)) {
                productosPorFecha.put(fechaCompra, new ArrayList<>());
            }
            productosPorFecha.get(fechaCompra).add(producto);
        }
        
        // Convertir a JSON
        Gson gson = new Gson();
        String json = gson.toJson(productosPorFecha);
        
        out.print(json);
        
    } catch (SQLException e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"error\": \"Error al cargar el inventario: " + e.getMessage() + "\"}");
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"error\": \"Error inesperado: " + e.getMessage() + "\"}");
    } finally {
        out.close();
    }
}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
