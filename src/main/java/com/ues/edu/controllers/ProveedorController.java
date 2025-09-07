package com.ues.edu.controllers;

import com.ues.edu.models.Proveedores;
import com.ues.edu.models.dao.ProveedorDao;
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
 * @author Maris
 */
@WebServlet(name = "ProveedorController", urlPatterns = {"/ProveedorController"})

public class ProveedorController extends HttpServlet {

    private ArrayList<Proveedores> listaCategoriasActividad;
    private ArrayList<Proveedores> listaCategoriasActividad2;

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
        response.setContentType("application/json;charset=utf-8");
        Proveedores pro = new Proveedores();
        ProveedorDao proveedorDao = new ProveedorDao();
        PrintWriter out = response.getWriter();
        String filtro = request.getParameter("consultar_datos");
        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();

        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {

            case "mostrar": {
                String html1 = "";
                String estadoM = request.getParameter("estado");

                try {
                    System.out.println("ENTRO");

                    this.listaCategoriasActividad = new ArrayList<>();
                    System.out.println("ENTRO 2");

                    this.listaCategoriasActividad2 = proveedorDao.selectALL(Integer.valueOf(estadoM), "todos");
                    System.out.println("ENTRO 3");

                    html1 += "<div class=\"tabla_listCategoria\">"
                            + "<table id=\"tabla_listCategoria\" class=\"table table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                            
                            + "<th class=\"text-center\" style=\"color: white !important;\">NOMBRE</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">CORREO</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">DIRECCION</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">CONTACTO 1</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">CONTACTO 2</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">ESTADO</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">ACCIONES</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (Proveedores obj : listaCategoriasActividad2) {
                        String estadoTexto = obj.isEstado() ? "Activo" : "Inactivo";

                        html1 += "<tr class=\"text-center\">";
                        
                        html1 += "<td class=\"text-center\">" + obj.getNombreProveedor() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getCorreo() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getDirrecion() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getNumeroTelefono() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getNumeroTelefono1() + "</td>";
                        html1 += "<td class=\"text-center\">" + estadoTexto + "</td>";
                        html1 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + obj.getCodigoProveedor() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";
                        html1 += "</tr>";
                    }

                    html1 += "</tbody></table></div>";

                    jsonObjet.put("resultado", "exito");
                    jsonObjet.put("tabla", html1);

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error sql");
                    jsonObjet.put("error", e.getMessage());
                    jsonObjet.put("code error", e.getErrorCode());
                    throw new RuntimeException(e);
                } catch (Exception ex) {
                    Logger.getLogger(ProveedorController.class.getName()).log(Level.SEVERE, null, ex);
                }

                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
                break;
            }

            case "insertar": {

                String resultado_insert = "";
                try {
                    pro.setNombreProveedor(request.getParameter("nombre_proveedor"));
                    pro.setCorreo(request.getParameter("correo"));
                    pro.setDirrecion(request.getParameter("direccion"));
                    pro.setNumeroTelefono(request.getParameter("numero_telefono"));
                    pro.setNumeroTelefono1(request.getParameter("numero_telefono1"));
                    String estadoParam = request.getParameter("estado");
                    boolean estado = "true".equalsIgnoreCase(estadoParam != null ? estadoParam.trim() : "");
                    pro.setEstado(estado);
                    resultado_insert = proveedorDao.insert(pro);
                    if (resultado_insert.equals("exito")) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("nombre_proveedor", pro.getNombreProveedor());
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
            case "editar": {
                String resultado_editar = "";
                try {
                    pro.setCodigoProveedor(Integer.parseInt(request.getParameter("codigo_proveedor")));
                    pro.setNombreProveedor(request.getParameter("nombre_proveedor"));
                    pro.setCorreo(request.getParameter("correo"));
                    pro.setDirrecion(request.getParameter("direccion"));
                    pro.setNumeroTelefono(request.getParameter("numero_telefono"));
                    pro.setNumeroTelefono1(request.getParameter("numero_telefono1"));
                    pro.setEstado(Boolean.valueOf(request.getParameter("estado")));

                    resultado_editar = proveedorDao.update(pro);
                    if (resultado_editar.equals("exito")) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("nombre_proveedor", pro.getNombreProveedor());
                    } else {
                        jsonObjet.put("resultado", "error");
                        jsonObjet.put("resultado_insertar", resultado_editar);
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

            case "cargarDatos": {
                Proveedores carga = new Proveedores();
                try {
                    pro.setCodigoProveedor(Integer.parseInt(request.getParameter("codigo_proveedor")));
                    carga = proveedorDao.cargarDatos(pro);

                    if (carga != null) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("codigo_proveedor", carga.getCodigoProveedor());
                        jsonObjet.put("nombre_proveedor", carga.getNombreProveedor());
                        jsonObjet.put("correo", carga.getCorreo());
                        jsonObjet.put("direccion", carga.getDirrecion());
                        jsonObjet.put("numero_telefono", carga.getNumeroTelefono());
                        jsonObjet.put("numero_telefono1", carga.getNumeroTelefono1());
                        jsonObjet.put("estado", carga.isEstado());

                    } else {
                        jsonObjet.put("resultado", "error_eliminar");
                    }
                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("exception", e.getMessage());
                }
                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
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