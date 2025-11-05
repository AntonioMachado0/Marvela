package com.ues.edu.controllers;



import com.ues.edu.models.dao.DevolucionDao;
import com.ues.edu.models.DevolucionDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MostrarVentasServlet")
public class MostrarVentasServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        DevolucionDao devolucionDao = new DevolucionDao();
        ArrayList<DevolucionDTO> listaVentas = devolucionDao.mostrarVentasRecientes();
        
        request.setAttribute("listaVentas", listaVentas);
        request.getRequestDispatcher("frmDevolucion.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(MostrarVentasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(MostrarVentasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



