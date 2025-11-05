package com.ues.edu.controllers;


import com.ues.edu.models.Productos;
import com.ues.edu.models.dao.AlertaDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;


@WebServlet("/alertas")
public class AlertaController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AlertaDAO dao = new AlertaDAO();

        List<Productos> bajaExistencia = dao.getProductosBajaExistencia();
        List<Productos> agotados = dao.getProductosAgotados();
        List<Productos> proximosVencer = dao.getProductosProximosAVencer();
        List<Map<String, Object>> comprasSinDetalles = dao.getComprasSinDetalles();

        request.setAttribute("bajaExistencia", bajaExistencia);
        request.setAttribute("agotados", agotados);
        request.setAttribute("proximosVencer", proximosVencer);
        request.setAttribute("comprasSinDetalles", comprasSinDetalles);

        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }
}
