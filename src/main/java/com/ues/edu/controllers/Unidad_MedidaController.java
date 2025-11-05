/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;


import com.ues.edu.models.Medida;
import com.ues.edu.models.dao.MarcaDao;

import com.ues.edu.models.dao.Unidad_MedidaDao;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author thebe
 */
@WebServlet(name = "MedidaController", urlPatterns = {"/MedidaController"})
public class Unidad_MedidaController extends HttpServlet {

//cambios de prueba
    private ArrayList<Medida> listaMedidaActividad;
    private ArrayList<Medida> listaMedidaActividad2;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String filtro = request.getParameter("consultar_datos");
        Medida MedidaIns = new Medida();
        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
         Unidad_MedidaDao medidaDao = new Unidad_MedidaDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {

            case "mostrar":
                JSONArray array_mostrarMedida = new JSONArray();
                JSONObject json_mostrarMedida= new JSONObject();
                String html1 = "";
                String estadoM = request.getParameter("estado");
                try {      
                    Unidad_MedidaDao obaut = new Unidad_MedidaDao();
                    this.listaMedidaActividad2 = new ArrayList<>();               
                   this.listaMedidaActividad2 = obaut.selectALL(Integer.valueOf(estadoM), "todos");
                    html1 += "<div class=\"table-responsive\">"
                            + "<table id=\"tabla_listMedida\" class=\"table table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                            // + "<th class=\"text-center\" style=\" color: white !important;\">N</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">MEDIDA</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">ABREVIACIÓN</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">ACCIÓN</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (Medida obj : listaMedidaActividad2) {
                        html1 += "<tr class=\"text-center\">";
                        // html1 += "<td class=\"text-center\">" + obj.getCodigoCategoria() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getMedida()+ "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getAbreviacion()+ "</td>";
                        html1 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + obj.getId_medida()+ "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";

                        html1 += "</tr>";
                    }
                    html1 += "</tbody>"
                            + "</table>";
                    html1 += "</div>";

                    json_mostrarMedida.put("resultado", "exito");
                    json_mostrarMedida.put("tabla", html1);
                } catch (SQLException e) {
                    json_mostrarMedida.put("resultado", "error sql");
                    json_mostrarMedida.put("error", e.getMessage());
                    json_mostrarMedida.put("code error", e.getErrorCode());
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    Logger.getLogger(Unidad_MedidaController.class.getName()).log(Level.SEVERE, null, ex);
                }

                array_mostrarMedida.put(json_mostrarMedida);
                response.getWriter().write(array_mostrarMedida.toString());

                break;

            case "insertar": {
    String resultado_insert;
    try {
        String nombre = request.getParameter("nombre");
        String abreviacion = request.getParameter("abreviacion");

        // Validación de parámetros
        if (nombre == null || nombre.trim().isEmpty() || abreviacion == null || abreviacion.trim().isEmpty()) {
            jsonObjet.put("resultado", "error_parametro");
            jsonObjet.put("detalle", "Nombre o abreviación vacíos o no enviados.");
            jsonArray.put(jsonObjet);
            response.setContentType("application/json");
            response.getWriter().write(jsonArray.toString());
            break;
        }

        // Validación de duplicado
        if (medidaDao.existeMedida(nombre.trim(), abreviacion.trim())) {
            jsonObjet.put("resultado", "duplicado");
            jsonObjet.put("mensaje", "Ya existe una medida con ese nombre o abreviación.");
            jsonArray.put(jsonObjet);
            response.setContentType("application/json");
            response.getWriter().write(jsonArray.toString());
            break;
        }

        MedidaIns.setMedida(nombre.trim());
        MedidaIns.setAbreviacion(abreviacion.trim());
        System.out.println(MedidaIns);

        resultado_insert = medidaDao.insert(MedidaIns);
        if ("exito".equals(resultado_insert)) {
            jsonObjet.put("resultado", "exito");
            jsonObjet.put("nombre_marca", MedidaIns.getMedida());
            jsonObjet.put("abreviacion", MedidaIns.getAbreviacion());
        } else {
            jsonObjet.put("resultado", "error");
            jsonObjet.put("resultado_insertar", resultado_insert);
        }

    } catch (SQLException e) {
        jsonObjet.put("resultado", "error_sql");
        jsonObjet.put("error_mostrado", e.getMessage());
        System.out.println("Error Code error:" + e.getErrorCode());
        throw new RuntimeException("Error al insertar medida", e);
    }

    jsonArray.put(jsonObjet);
    response.setContentType("application/json");
    response.getWriter().write(jsonArray.toString());
    break;
}
            case "cargarDatos":
                JSONArray array_cargar = new JSONArray();
                JSONObject json_carga = new JSONObject();
                Medida medidaC = new Medida();
                Medida medida = null;
                Unidad_MedidaDao MedidaDao = null;
                try {
                    MedidaDao = new Unidad_MedidaDao();
                    medida = new Medida();
                    medidaC.setId_medida(Integer.parseInt(request.getParameter("id_medida")));
                    medida = MedidaDao.cargarDatos(medidaC);

                    if (medida != null) {
                        json_carga.put("resultado", "exito");
                        json_carga.put("id_medida", medida.getId_medida());
                        json_carga.put("nombre_medida", medida.getMedida());
                        json_carga.put("abrevacion", medida.getAbreviacion());
                    } else {
                        json_carga.put("resultado", "error_eliminar");
                    }
                } catch (SQLException e) {
                    json_carga.put("resultado", "error_sql");
                    json_carga.put("exception", e.getMessage());
                }
                array_cargar.put(json_carga);
                response.getWriter().write(array_cargar.toString());
                break;

            case "editar": 
    JSONArray array_nombre_update = new JSONArray();
    JSONObject json_nombre_update = new JSONObject();
    String resultado_update = "";
    Medida MedidaUpdate = new Medida();

    try {
        Unidad_MedidaDao medDao = new Unidad_MedidaDao();

        int idMedida = Integer.parseInt(request.getParameter("id_medida"));
        String nombre = request.getParameter("nombre_medida");
        String abreviacion = request.getParameter("abreviacion");

        // 🔍 Validación de duplicado
        if (medDao.existeMedidaConOtroId(nombre.trim(), abreviacion.trim(), idMedida)) {
            json_nombre_update.put("resultado", "duplicado");
            json_nombre_update.put("mensaje", "Ya existe otra medida con ese nombre o abreviación.");
        } else {
            MedidaUpdate.setId_medida(idMedida);
            MedidaUpdate.setMedida(nombre.trim());
            MedidaUpdate.setAbreviacion(abreviacion.trim());

            resultado_update = medDao.update(MedidaUpdate);

            if ("exito".equals(resultado_update)) {
                json_nombre_update.put("resultado", "exito");
                json_nombre_update.put("nombre_medida", MedidaUpdate.getMedida());
                json_nombre_update.put("abreviacion", MedidaUpdate.getAbreviacion());
            } else {
                json_nombre_update.put("resultado", "error");
                json_nombre_update.put("resultado_insertar", resultado_update);
            }
        }

    } catch (SQLException e) {
        json_nombre_update.put("resultado", "error_sql");
        json_nombre_update.put("error_mostrado", e.getMessage());
        System.out.println("Error Code error:" + e.getErrorCode());
        throw new RuntimeException("Error al editar medida", e);
    }

    array_nombre_update.put(json_nombre_update);
    response.getWriter().write(array_nombre_update.toString());
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
