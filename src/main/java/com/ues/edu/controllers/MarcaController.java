/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.Marca;
import com.ues.edu.models.dao.MarcaDao;
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
@WebServlet(name = "MarcaController", urlPatterns = {"/MarcaController"})
public class MarcaController extends HttpServlet {

//cambios de prueba
    private ArrayList<Marca> listaMarcaActividad;
    private ArrayList<Marca> listaMarcaActividad2;

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
        Marca catIns = new Marca();
        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
        MarcaDao MarcaDao = new MarcaDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {

            case "mostrar":

                JSONArray array_mostrarMarca = new JSONArray();
                JSONObject json_mostrarMarca = new JSONObject();
                String html1 = "";
                String estadoM = request.getParameter("estado");
                try {
                    System.out.println("ENTRO");
                    MarcaDao obaut = new MarcaDao();
                    this.listaMarcaActividad = new ArrayList<>();
                    System.out.println("ENTRO 2");
                    this.listaMarcaActividad2 = obaut.selectALL(Integer.valueOf(estadoM), "todos");

                    System.out.println("ENTRO 3");

                    html1 += "<div class=\"table-responsive\">"
                            + "<table id=\"tabla_listMarca\" class=\"table table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                            // + "<th class=\"text-center\" style=\" color: white !important;\">N</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">MARCA</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">ACCIÓN</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (Marca obj : listaMarcaActividad2) {
                        html1 += "<tr class=\"text-center\">";
                        // html1 += "<td class=\"text-center\">" + obj.getCodigoCategoria() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getMarca() + "</td>";
                        html1 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + obj.getCodigoMarca() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";

                        html1 += "</tr>";
                    }
                    html1 += "</tbody>"
                            + "</table>";
                    html1 += "</div>";

                    json_mostrarMarca.put("resultado", "exito");
                    json_mostrarMarca.put("tabla", html1);
                } catch (SQLException e) {
                    json_mostrarMarca.put("resultado", "error sql");
                    json_mostrarMarca.put("error", e.getMessage());
                    json_mostrarMarca.put("code error", e.getErrorCode());
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    Logger.getLogger(MarcaController.class.getName()).log(Level.SEVERE, null, ex);
                }

                array_mostrarMarca.put(json_mostrarMarca);
                response.getWriter().write(array_mostrarMarca.toString());

                break;

           case "insertar": {
    String resultado_insert;
    String nombre = request.getParameter("nombre");

    try {
        // Validación: ¿ya existe la marca?
        if (MarcaDao.existeMarca(nombre)) {
            jsonObjet.put("resultado", "duplicado");
            jsonObjet.put("mensaje", "La marca '" + nombre + "' ya está registrada.");
        } else {
            catIns.setMarca(nombre);
            resultado_insert = MarcaDao.insert(catIns);

            if ("exito".equals(resultado_insert)) {
                jsonObjet.put("resultado", "exito");
                jsonObjet.put("nombre_marca", catIns.getMarca());
            } else {
                jsonObjet.put("resultado", "error");
                jsonObjet.put("resultado_insertar", resultado_insert);
            }
        }
    } catch (SQLException e) {
        jsonObjet.put("resultado", "error_sql");
        jsonObjet.put("error_mostrado", e.getMessage());
        System.out.println("Error Code error:" + e.getErrorCode());
        throw new RuntimeException("Error al insertar marca", e);
    }

    jsonArray.put(jsonObjet);
    response.getWriter().write(jsonArray.toString());
    break;
}
            case "cargarDatos":
                JSONArray array_cargar = new JSONArray();
                JSONObject json_carga = new JSONObject();
                Marca catC = new Marca();
                Marca marca = null;
                MarcaDao marcaDao = null;
                try {
                    MarcaDao = new MarcaDao();
                    marca = new Marca();
                    catC.setCodigoMarca(Integer.parseInt(request.getParameter("id_marca")));
                    marca = MarcaDao.cargarDatos(catC);

                    if (marca != null) {
                        json_carga.put("resultado", "exito");
                        json_carga.put("id_marca", marca.getCodigoMarca());
                        json_carga.put("nombre_marca", marca.getMarca());
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

            case "editar": {
    JSONArray array_nombre_update = new JSONArray();
    JSONObject json_nombre_update = new JSONObject();
    String resultado_update = "";
    Marca MarcaUpdate = new Marca();

    try {
        MarcaDao marDao = new MarcaDao();

        int idMarca = Integer.parseInt(request.getParameter("id_marca"));
        String nuevoNombre = request.getParameter("nombre_marca");

        // Validar si ya existe otra marca con ese nombre (ignorando mayúsculas y espacios)
        if (marDao.existeMarcaConOtroId(nuevoNombre, idMarca)) {
            json_nombre_update.put("resultado", "duplicado");
            json_nombre_update.put("mensaje", "Ya existe otra marca con ese nombre.");
        } else {
            MarcaUpdate.setCodigoMarca(idMarca);
            MarcaUpdate.setMarca(nuevoNombre);
            resultado_update = marDao.update(MarcaUpdate);

            if ("exito".equals(resultado_update)) {
                json_nombre_update.put("resultado", "exito");
                json_nombre_update.put("nombre_marca", MarcaUpdate.getMarca());
            } else {
                json_nombre_update.put("resultado", "error");
                json_nombre_update.put("resultado_insertar", resultado_update);
            }
        }
    } catch (SQLException e) {
        json_nombre_update.put("resultado", "error_sql");
        json_nombre_update.put("error_mostrado", e.getMessage());
        System.out.println("Error Code error:" + e.getErrorCode());
        throw new RuntimeException("Error al editar marca", e);
    }

    array_nombre_update.put(json_nombre_update);
    response.getWriter().write(array_nombre_update.toString());
    break;
}
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
