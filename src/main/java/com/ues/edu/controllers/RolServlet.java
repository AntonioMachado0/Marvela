/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.Categoria;
import com.ues.edu.models.Roles;
import com.ues.edu.models.dao.RolDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author MINEDUCYT
 */
@WebServlet(name = "RolServlet", urlPatterns = {"/RolServlet"})
public class RolServlet extends HttpServlet {
//cambii
    private ArrayList<Roles> lista;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RolServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RolServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("aplication/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String filtro = request.getParameter("consultar_datos");

        System.out.println("EL SERVLET RESIVE EL PARAMETRO XXXXX: " + filtro + " DE LA JSP");
        if (filtro == null) {
            return;
        }

        switch (filtro) {
            case "si_registro":
                JSONArray array_roles = new JSONArray();
                JSONObject json_roles = new JSONObject();
                String resultado_insert = "";

                Roles roles = new Roles();
                try {
                    RolDao rolInsert = new RolDao();
                    String nombreRol = request.getParameter("rol");

                    // Validar si el rol ya existe
                    if (rolInsert.existeNombreRol(nombreRol)) {
                        json_roles.put("resultado", "error_duplicado");
                        json_roles.put("mensaje", "Ya existe un rol con ese nombre");
                    } else {
                        roles.setNombreRol(nombreRol);
                        resultado_insert = rolInsert.insertRol(roles);

                        if ("exito".equals(resultado_insert)) {
                            json_roles.put("resultado", "exito");
                            json_roles.put("rol", roles.getNombreRol());
                        } else {
                            json_roles.put("resultado", "error");
                            json_roles.put("mensaje", resultado_insert);
                        }
                    }
                } catch (SQLException e) {
                    json_roles.put("resultado", "error_sql");
                    json_roles.put("error_mostrado", e.getMessage());
                    System.out.println("Error Code error: " + e.getErrorCode());
                    throw new RuntimeException();
                }
                array_roles.put(json_roles);
                response.getWriter().write(array_roles.toString());
                break;
            case "si_consulta":
                System.out.println("ENTRO AL CASO");
                JSONArray array_mostrar = new JSONArray();
                JSONObject json_mostrar = new JSONObject();
                String html = "";
                String el_estado = request.getParameter("estado");

                try {
                    RolDao dao = new RolDao();
                    this.lista = new ArrayList<>();
                    this.lista = dao.mostrar(Integer.valueOf(el_estado), "todos");

                    html += "<table id='tabla_roles' class='table-responsive'>"
                            + "<thead>"
                            + "<tr class='mb-2 bg-green text-white text-center'>"
                            
                            + "<th class='text-center' style='color: white !important;'>ROL</th>"
                            + "<th class='text-center' style='color: white !important;'>ACCIONES</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";
                    

                    for (Roles rol : this.lista) {

                        

                        html += "<tr class='text-center'>";
                       
                        html += "<td class='text-center'>" + rol.getNombreRol() + "</td>";
                        html += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + rol.getCodigoRoles() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";
                        html += "</tr>";
                    }

                    html += "</tbody>"
                            + "</table>";

                    json_mostrar.put("resultado", "exito");
                    json_mostrar.put("tabla", html);
                    json_mostrar.put("cuantos", this.lista.size());

                } catch (SQLException e) {
                    json_mostrar.put("resultado", "error sql");
                    json_mostrar.put("error", e.getMessage());
                    json_mostrar.put("code error", e.getErrorCode());
                    throw new RuntimeException(e);
                }
                array_mostrar.put(json_mostrar);
                response.getWriter().write(array_mostrar.toString());
                break;

            case "editar":
                JSONArray array_nombre_update = new JSONArray();
                JSONObject json_nombre_update = new JSONObject();
                String resultado_update = "";
                Roles rolUpdate = new Roles();

                try {
                    RolDao catDao = new RolDao();
                    int codigoRol = Integer.parseInt(request.getParameter("codigo_rol"));
                    String nombreRol = request.getParameter("rolE");

                    // Validar si el nombre ya existe (excluyendo el registro actual)
                    if (catDao.existeNombreRolExcluyendoActual(nombreRol, codigoRol)) {
                        json_nombre_update.put("resultado", "error_duplicado");
                        json_nombre_update.put("mensaje", "Ya existe un rol con ese nombre");
                    } else {
                        rolUpdate.setCodigoRoles(codigoRol);
                        rolUpdate.setNombreRol(nombreRol);
                        resultado_update = catDao.updateRol(rolUpdate);

                        if (resultado_update.equals("exito")) {
                            json_nombre_update.put("resultado", "exito");
                            json_nombre_update.put("rol", rolUpdate.getNombreRol());
                        } else {
                            json_nombre_update.put("resultado", "error");
                            json_nombre_update.put("mensaje", resultado_update);
                        }
                    }
                } catch (SQLException e) {
                    json_nombre_update.put("resultado", "error_sql");
                    json_nombre_update.put("error_mostrado", e.getMessage());
                    System.out.println("Error Code error:" + e.getErrorCode());
                    throw new RuntimeException();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(RolServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                array_nombre_update.put(json_nombre_update);
                response.getWriter().write(array_nombre_update.toString());
                break;

            case "cargarDatos":
                JSONArray array_cargar = new JSONArray();
                JSONObject json_carga = new JSONObject();
                Roles catC = new Roles();
                Roles cargaRoles = null;
                RolDao rolDao = null;
                rolDao = new RolDao();
                cargaRoles = new Roles();
                catC.setCodigoRoles(Integer.parseInt(request.getParameter("codigo_rol")));
                 {
                    try {
                        cargaRoles = rolDao.cargarDatos(catC);
                    } catch (SQLException ex) {
                        Logger.getLogger(RolServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (cargaRoles != null) {
                    json_carga.put("resultado", "exito");
                    json_carga.put("codigo_rol", cargaRoles.getCodigoRoles());
                    json_carga.put("rolE", cargaRoles.getNombreRol());
                } else {
                    json_carga.put("resultado", "error_eliminar");
                }
                array_cargar.put(json_carga);
                response.getWriter().write(array_cargar.toString());
                break;

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
