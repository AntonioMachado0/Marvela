package com.ues.edu.controllers;

import com.ues.edu.models.Categoria;
import com.ues.edu.models.dao.CategoriaDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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
 * @author Erick
 */
@WebServlet(name = "CategoriaController", urlPatterns = {"/CategoriaController"})
public class CategoriaController extends HttpServlet {

    private ArrayList<Categoria> listaCategoriasActividad;
    private ArrayList<Categoria> listaCategoriasActividad2;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
 Categoria catIns = new Categoria();
JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
      CategoriaDao categoriaDao = new CategoriaDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {

            case "mostrar":

                JSONArray array_mostrarCategoria = new JSONArray();
                JSONObject json_mostrarCategoria = new JSONObject();
                String html1 = "";
                String estadoM = request.getParameter("estado");
                try {
                    System.out.println("ENTRO");
                    CategoriaDao obaut = new CategoriaDao();
                    this.listaCategoriasActividad = new ArrayList<>();
                    System.out.println("ENTRO 2");
                  this.listaCategoriasActividad2 = obaut.selectALL(Integer.valueOf(estadoM), "todos");

                    System.out.println("ENTRO 3");

                    html1 += "<div class=\"table-responsive\">"
                            + "<table id=\"tabla_listCategoria\" class=\"table table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                           // + "<th class=\"text-center\" style=\" color: white !important;\">N</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">CATEGORIA</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">ACCION</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (Categoria obj : listaCategoriasActividad2) {
                        html1 += "<tr class=\"text-center\">";
                       // html1 += "<td class=\"text-center\">" + obj.getCodigoCategoria() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getNombre() + "</td>";
                        html1 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + obj.getCodigoCategoria() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";

                        html1 += "</tr>";
                    }
                    html1 += "</tbody>"
                            + "</table>";
                    html1 += "</div>";
                    

                    json_mostrarCategoria.put("resultado", "exito");
                    json_mostrarCategoria.put("tabla", html1);
                } catch (SQLException e) {
                    json_mostrarCategoria.put("resultado", "error sql");
                    json_mostrarCategoria.put("error", e.getMessage());
                    json_mostrarCategoria.put("code error", e.getErrorCode());
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    Logger.getLogger(CategoriaController.class.getName()).log(Level.SEVERE, null, ex);
                }

                array_mostrarCategoria.put(json_mostrarCategoria);
                response.getWriter().write(array_mostrarCategoria.toString());

                break;
 
            case "insertar": {

                String resultado_insert = "";
                try {
                    catIns.setNombre(request.getParameter("nombre"));
                    resultado_insert = categoriaDao.insert(catIns);
                    if (resultado_insert.equals("exito")) {
                        jsonObjet.put("resultado", "exito");
         jsonObjet.put("nombre", catIns.getNombre());

                    } else {
                        jsonObjet.put("resultado", "error");
                        jsonObjet.put("resultado_insertar", resultado_insert);
                    }
                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("error_mostrado", e.getMessage());
                    System.out.println("Error Code error:" + e.getErrorCode());
                    throw new RuntimeException();
                }

                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
                break;
            
                
                
                
                
                
                
                
                
                
        
            }
            case "cargarDatos":
                JSONArray array_cargar = new JSONArray();
                JSONObject json_carga = new JSONObject();
                Categoria catC = new Categoria();
                Categoria cargaCategoria = null;
                CategoriaDao CategoriaDao = null;
                try {
                    CategoriaDao = new CategoriaDao();
                    cargaCategoria = new Categoria();
                    catC.setCodigoCategoria(Integer.parseInt(request.getParameter("codigo_categoria")));
                    cargaCategoria = CategoriaDao.cargarDatos(catC);

                    if (cargaCategoria != null) {
                        json_carga.put("resultado", "exito");
                        json_carga.put("codigo_categoria", cargaCategoria.getCodigoCategoria());
                        json_carga.put("nombre", cargaCategoria.getNombre());
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
                Categoria catUpdate = new Categoria();

                try {
                    CategoriaDao catDao = new CategoriaDao();
                    catUpdate.setCodigoCategoria(Integer.parseInt(request.getParameter("codigo_categoria")));
                    catUpdate.setNombre(request.getParameter("nombre"));
                    resultado_update = catDao.update(catUpdate);
                    if (resultado_update.equals("exito")) {// 
                        json_nombre_update.put("resultado", "exito");
                        json_nombre_update.put("nombre", catUpdate.getNombre());
                    } else {
                        json_nombre_update.put("resultado", "error");
                        json_nombre_update.put("resultado_insertar", resultado_update);
                    }
                } catch (SQLException e) {
                    json_nombre_update.put("resultado", "error_sql");
                    json_nombre_update.put("error_mostrado", e.getMessage());
                    System.out.println("Error Code error:" + e.getErrorCode());
                    throw new RuntimeException();
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
