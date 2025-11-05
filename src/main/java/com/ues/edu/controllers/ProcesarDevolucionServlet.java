package com.ues.edu.controllers;

import com.ues.edu.models.dao.DevolucionDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/ProcesarDevolucionServlet")
public class ProcesarDevolucionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String numeroVenta = request.getParameter("numeroVenta");
        String productoDevueltoId = request.getParameter("productoDevueltoId");
        String productoNuevoId = request.getParameter("productoNuevoId");
        String cantidadStr = request.getParameter("cantidad");
        String codigoDetalleVentaStr = request.getParameter("codigoDetalleVenta");
        String codigoDetalleCompraStr = request.getParameter("codigoDetalleCompra");
        String cantidadOriginalStr = request.getParameter("cantidadOriginal");
        
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // Validar parámetros
            if (numeroVenta == null || productoDevueltoId == null || productoNuevoId == null || 
                cantidadStr == null || codigoDetalleVentaStr == null || codigoDetalleCompraStr == null ||
                cantidadOriginalStr == null) {
                throw new Exception("Parámetros incompletos para procesar la devolución");
            }
            
            int productoDevuelto = Integer.parseInt(productoDevueltoId);
            int productoNuevo = Integer.parseInt(productoNuevoId);
            int cantidad = Integer.parseInt(cantidadStr);
            int codigoDetalleVenta = Integer.parseInt(codigoDetalleVentaStr);
            int codigoDetalleCompra = Integer.parseInt(codigoDetalleCompraStr);
            int cantidadOriginal = Integer.parseInt(cantidadOriginalStr);
            
            // Validar cantidad positiva y no mayor a la original
            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser mayor a cero");
            }
           
            DevolucionDao devolucionDao = new DevolucionDao();
            
            // Verificar stock del nuevo producto
            int stockDisponible = devolucionDao.verificarStockDisponible(productoNuevo);
            if (stockDisponible < cantidad) {
                throw new Exception("Stock insuficiente para el nuevo producto. Stock disponible: " + stockDisponible);
            }
            
            // Procesar devolución con cantidades
            boolean devolucionExitosa = devolucionDao.procesarDevolucionIntercambio(
                numeroVenta, 
                productoDevuelto, 
                productoNuevo, 
                cantidad,
                codigoDetalleVenta,
                codigoDetalleCompra,
                cantidadOriginal
            );
            
            if (devolucionExitosa) {
                resultado.put("success", true);
                resultado.put("message", "Devolución e intercambio procesados exitosamente");
                resultado.put("numeroVenta", numeroVenta);
                resultado.put("productoDevuelto", productoDevuelto);
                resultado.put("productoNuevo", productoNuevo);
                resultado.put("cantidad", cantidad);
                resultado.put("cantidadOriginal", cantidadOriginal);
            } else {
                resultado.put("success", false);
                resultado.put("message", "Error al procesar la devolución");
            }
            
        } catch (NumberFormatException e) {
            resultado.put("success", false);
            resultado.put("message", "Error en el formato de los datos: " + e.getMessage());
            Logger.getLogger(ProcesarDevolucionServlet.class.getName()).log(Level.SEVERE, "Error de formato", e);
        } catch (SQLException e) {
            resultado.put("success", false);
            resultado.put("message", "Error en la base de datos: " + e.getMessage());
            Logger.getLogger(ProcesarDevolucionServlet.class.getName()).log(Level.SEVERE, "Error SQL", e);
        } catch (Exception e) {
            resultado.put("success", false);
            resultado.put("message", e.getMessage());
            Logger.getLogger(ProcesarDevolucionServlet.class.getName()).log(Level.SEVERE, "Error general", e);
        }
        
        JSONObject json = new JSONObject(resultado);
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ProcesarDevolucionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}