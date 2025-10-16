/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.Compras;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Proveedores;

import com.ues.edu.models.dao.ComprasDao;
import com.ues.edu.models.dao.EmpleadoDao;
import com.ues.edu.models.dao.ProveedorDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.ws.rs.client.Entity.json;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "ComprasController", urlPatterns = {"/ComprasController"})
public class ComprasController extends HttpServlet {

    private ArrayList<Compras> listaCompraActividad;
    private ArrayList<Compras> listaCompra2;

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
        Compras catIns = new Compras();
        JSONArray array_mostrarCompra = new JSONArray(); // ← Solo si no existe antes
        JSONObject json_mostrarCompra = new JSONObject();

        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
        ComprasDao comprasDao = new ComprasDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {
            case "mostrar":

    String html1 = "";
    String estadoM = request.getParameter("estado");

    try {
        ComprasDao obaut = new ComprasDao();
        this.listaCompraActividad = new ArrayList<>();
        this.listaCompra2 = obaut.selectAllCompra(Integer.valueOf(estadoM), "todos");

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaActual = new Date();

        html1 += "<div class=\"tabla_listCom\">"
                + "<table id=\"tabla_listCom\" class=\"table table-hover datanew\">"
                + "<thead>"
                + "<tr class=\"mb-2 green text-center\">"
                + "<th class=\"text-center\" style=\"color: white !important;\">NÚMERO DE ORDEN</th>"
                + "<th class=\"text-center\" style=\"color: white !important;\">FECHA</th>"
                + "<th class=\"text-center\" style=\"color: white !important;\">PROVEEDOR</th>"
                + "<th class=\"text-center\" style=\"color: white !important;\">EMPLEADO</th>"
                + "<th class=\"text-center\" style=\"color: white !important;\">ESTADO</th>"
                + "<th class=\"text-center\" style=\"color: white !important;\">ACCIÓN</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";

        for (Compras obj : listaCompra2) {
            String fechaFormateada = formatoFecha.format(obj.getFechaCompra());

            long diferenciaMillis = fechaActual.getTime() - obj.getFechaCompra().getTime();
            long diasPasados = diferenciaMillis / (1000 * 60 * 60 * 24);
            boolean vencida = diasPasados >= 3;
            long diasRestantes = Math.max(0, 3 - diasPasados);

            html1 += "<tr class=\"text-center\">";
            html1 += "<td class=\"text-center\">" + obj.getNumeroOrden() + "</td>";
            html1 += "<td class=\"text-center\">" + fechaFormateada + "</td>";
            html1 += "<td class=\"text-center\">" + obj.getProveedores().getNombreProveedor() + "</td>";
            html1 += "<td class=\"text-center\">" + obj.getEmpleado().getNombreCompleto() + "</td>";

            // Estado con mensaje superior en letras negras
            html1 += "<td class=\"text-center\">";
            if (!vencida) {
                html1 += "<div style='font-size: 0.8em; color: black;'>Restan " + diasRestantes + " día(s)</div>";
                html1 += "<span style='color:green;'><i class='fas fa-play-circle'></i> <span style='color:black;'>En proceso</span></span>";
            } else {
                html1 += "<div style='font-size: 0.8em; color: black;'>Plazo vencido</div>";
                html1 += "<span style='color:red;'><i class='fas fa-lock'></i> <span style='color:black;'>Finalizada</span></span>";
            }
            html1 += "</td>";

            // Botones
            html1 += "<td>";
            html1 += "<button class='btn btn-success btn_editar' data-id='" + obj.getIdCompra() + "'>"
                    + "<i class='fas fa-edit'></i></button> ";

            if (!vencida) {
                html1 += "<button class='btn btn-success' onclick=\"window.location.href='detalle_producto_crud.jsp?codigo_compra=" + obj.getIdCompra() + "&numero_de_orden=" + obj.getNumeroOrden() + "'\">"
                        + "<i class='fas fa-circle-plus'></i></button>";
            } else {
                html1 += "<button class='btn btn-secondary' onclick=\"alert('Ya pasó el tiempo límite para añadir productos a esta orden.')\" title='Orden finalizada'>"
                        + "<i class='fas fa-ban'></i></button>";
            }

            html1 += "</td></tr>";
        }

        html1 += "</tbody></table></div>";
        json_mostrarCompra.put("resultado", "exito");
        json_mostrarCompra.put("tabla", html1);

    } catch (Exception ex) {
        Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
        json_mostrarCompra.put("resultado", "error");
        json_mostrarCompra.put("mensaje", ex.getMessage());
    }

    array_mostrarCompra.put(json_mostrarCompra);
    response.getWriter().write(array_mostrarCompra.toString());

    break;
            case "cargarCombo": {
                ArrayList<Proveedores> listPro = new ArrayList();
                ProveedorDao proDao = null;
                String html3 = "";
                try {
                    proDao = new ProveedorDao();
                    listPro = proDao.cargarComboProveedor();

                    if (listPro != null) {
                        for (Proveedores x : listPro) {
                            html3 += "<option value='" + x.getCodigoProveedor() + "'>" + x.getNombreProveedor() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("proveedor", html3);
                    } else {
                        jsonObjet.put("resultado", "error_cargar");
                    }

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("exception", e.getMessage());
                }
                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
                break;
            }

            case "cargarComboE": {
                ArrayList<Empleado> listEm = new ArrayList();
                EmpleadoDao proDao = null;
                String html3 = "";
                try {
                    proDao = new EmpleadoDao();
                    listEm = proDao.cargarComboEmpleado();

                    if (listEm != null) {
                        for (Empleado x : listEm) {
                            html3 += "<option value='" + x.getCodigoEmpleado() + "'>" + x.getNombreCompleto() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("empleado", html3);
                    } else {
                        jsonObjet.put("resultado", "error_cargar");
                    }

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("exception", e.getMessage());
                }
                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
                break;
            }
            case "cargarComboProveedor": {

                ArrayList<Proveedores> listPro = new ArrayList<>();
                ProveedorDao proDao = null;
                String html3 = "";

                try {
                    proDao = new ProveedorDao();
                    listPro = proDao.cargarComboProveedor(); // ← Este método debe retornar nombre y código

                    if (listPro != null && !listPro.isEmpty()) {
                        for (Proveedores x : listPro) {
                            html3 += "<option value='" + x.getCodigoProveedor() + "'>" + x.getNombreProveedor() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("proveedor", html3);
                    } else {
                        jsonObjet.put("resultado", "error_cargar");
                        jsonObjet.put("proveedor", "");
                    }

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("exception", e.getMessage());
                    jsonObjet.put("proveedor", "");
                }

                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
                break;

            }

            case "insertar": {
                String resultado_insert = "";
                try {
                    // Validación de número de orden
                    String numeroOrden = request.getParameter("numero_de_orden");
                    if (numeroOrden == null || numeroOrden.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El número de orden está vacío o no fue enviado.");
                        break;
                    }
                    catIns.setNumeroOrden(numeroOrden.trim());

                    // Conversión segura de fecha en formato dd/MM/yyyy
                    String fechaStr = request.getParameter("fecha_de_orden");
                    if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                        try {
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            formato.setLenient(false);
                            java.util.Date fechaUtil = formato.parse(fechaStr.trim());
                            java.sql.Date fechaSQL = new java.sql.Date(fechaUtil.getTime());
                            catIns.setFechaCompra(fechaSQL);
                            System.out.println("[FECHA] Fecha convertida correctamente: " + fechaSQL);
                        } catch (ParseException e) {
                            System.out.println("[ERROR FECHA] Formato inválido: " + fechaStr);
                            jsonObjet.put("resultado", "error_fecha");
                            jsonObjet.put("detalle", "La fecha debe estar en formato dd/MM/yyyy");
                            break;
                        }
                    }

                    // Validación segura de código de empleado
                    String codigoEmpleadoStr = request.getParameter("codigo_empleado");
                    if (codigoEmpleadoStr == null || codigoEmpleadoStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de empleado está vacío o no fue enviado.");
                        break;
                    }
                    Empleado empleado = new Empleado();
                    empleado.setCodigoEmpleado(Integer.parseInt(codigoEmpleadoStr.trim()));
                    catIns.setEmpleado(empleado);

                    // Validación segura de código de proveedor
                    String codigoProveedorStr = request.getParameter("codigo_proveedor");
                    if (codigoProveedorStr == null || codigoProveedorStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de proveedor está vacío o no fue enviado.");
                        break;
                    }
                    Proveedores proveedor = new Proveedores();
                    proveedor.setCodigoProveedor(Integer.parseInt(codigoProveedorStr.trim()));
                    catIns.setProveedores(proveedor);

                    // Inserción
                    resultado_insert = comprasDao.insert(catIns);
                    if ("exito".equals(resultado_insert)) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("nombre", catIns.getNumeroOrden());
                    } else {
                        jsonObjet.put("resultado", "error");
                        jsonObjet.put("detalle", resultado_insert);
                    }

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("error_mostrado", e.getMessage());
                    System.out.println("Error SQL: " + e.getErrorCode());
                    throw new RuntimeException("Error al insertar orden de compra", e);
                }

                jsonArray.put(jsonObjet);
                response.getWriter().write(jsonArray.toString());
                break;
            }

            case "cargarDatos": {
                Compras carga = new Compras();
                try {
                    catIns.setIdCompra(Integer.parseInt(request.getParameter("codigo_compra")));
                    carga = comprasDao.cargarDatos(catIns);

                    if (carga != null) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("codigo_compra", carga.getIdCompra());
                        jsonObjet.put("numero_de_orden", carga.getNumeroOrden());
                        jsonObjet.put("fecha_de_orden", carga.getFechaCompra());
                        jsonObjet.put("codigo_empleado", carga.getEmpleado().getCodigoEmpleado());
                        jsonObjet.put("nombre_completo", carga.getEmpleado().getNombreCompleto());
                        jsonObjet.put("codigo_proveedor", carga.getProveedores().getCodigoProveedor());
                        jsonObjet.put("nombre_proveedor", carga.getProveedores().getNombreProveedor());

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

            case "editar": {
                JSONArray array_nombre_update = new JSONArray();
                JSONObject json_nombre_update = new JSONObject();
                String resultado_update = "";

                Compras catUpdate = new Compras();
                Empleado empleado = new Empleado();
                Proveedores pro = new Proveedores();

                try {
                    ComprasDao catDao = new ComprasDao();
                    // ✅ Cargar número de orden
                    System.out.println("ENTRO a editar 56");
//        
                    catUpdate.setIdCompra(Integer.parseInt(request.getParameter("codigo_compra")));
                    catUpdate.setNumeroOrden(request.getParameter("numero_de_orden"));

                    // ✅ Conversión de fecha DD/MM/YYYY
                    String fechaFormateada = request.getParameter("fecha_de_orden"); // Ej: "28/09/2025"
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaConvertida = LocalDate.parse(fechaFormateada, formatter);
                    catUpdate.setFechaCompra(java.sql.Date.valueOf(fechaConvertida));

                    // ✅ Cargar empleado
                    empleado.setCodigoEmpleado(Integer.parseInt(request.getParameter("codigo_empleado")));
                    catUpdate.setEmpleado(empleado);

                    // ✅ Cargar proveedor
                    pro.setCodigoProveedor(Integer.parseInt(request.getParameter("codigo_proveedor")));
                    catUpdate.setProveedores(pro);

                    // ✅ Ejecutar actualización
                    resultado_update = catDao.update(catUpdate);

                    // ✅ Construir respuesta
                    if (resultado_update.equals("exito")) {
                        json_nombre_update.put("resultado", "exito");
                        json_nombre_update.put("numero_de_orden", catUpdate.getNumeroOrden());
                    } else {
                        json_nombre_update.put("resultado", "error");
                        json_nombre_update.put("resultado_insertar", resultado_update);
                    }

                } catch (DateTimeParseException e) {
                    json_nombre_update.put("resultado", "error_fecha");
                    json_nombre_update.put("error_mostrado", "Formato de fecha inválido: " + request.getParameter("fecha_de_orden"));
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
//ikkj
}
