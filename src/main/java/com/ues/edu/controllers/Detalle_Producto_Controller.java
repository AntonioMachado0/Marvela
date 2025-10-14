/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.Compras;
import com.ues.edu.models.DetalleCompra;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Medida;
import com.ues.edu.models.Productos;
import com.ues.edu.models.dao.ComprasDao;
import com.ues.edu.models.dao.DetalleCompraDao;
import com.ues.edu.models.dao.MarcaDao;
import com.ues.edu.models.dao.ProductoDao;
import com.ues.edu.models.dao.Unidad_MedidaDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Maris
 */
@WebServlet(name = "Detalle_Producto_Controller", urlPatterns = {"/Detalle_Producto_Controller"})
public class Detalle_Producto_Controller extends HttpServlet {

    private ArrayList<DetalleCompra> listaCompraActividad;
    private ArrayList<DetalleCompra> listaCompra2;

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
        DetalleCompra catIns = new DetalleCompra();
        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
        DetalleCompraDao comprasDaos = new DetalleCompraDao();
        ComprasDao comprasDao = new ComprasDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {
//            case "mostrar":
//
//                JSONArray array_mostrarCompra = new JSONArray();
//                JSONObject json_mostrarCompra = new JSONObject();
//                String html1 = "";
//                String estadoM = request.getParameter("estado");
//
//                try {
//                    System.out.println("🔄 ENTRÓ AL CASO 'mostrar'");
//                    DetalleCompraDao obaut = new DetalleCompraDao();
//
//                    this.listaCompraActividad = new ArrayList<>();
//                    this.listaCompra2 = obaut.selectAllDetalleCompra(Integer.valueOf(estadoM), "todos");
//
//                    html1 += "<div class=\"tabla_listCom\">"
//                            + "<table id=\"tabla_listCom\" class=\"table table-hover datanew\">"
//                            + "<thead>"
//                            + "<tr class=\"mb-2 green text-center\">"
//                            + "<th class=\"text-center\" style=\"color: white !important;\">NOMBRE</th>"
//                            + "<th class=\"text-center\" style=\"color: white !important;\">CANTIDA</th>"
//                            + "<th class=\"text-center\" style=\"color: white !important;\">COSTO</th>"
//                            + "<th class=\"text-center\" style=\"color: white !important;\">PRECIO UNITARIO </th>"
//                            + "<th class=\"text-center\" style=\"color: white !important;\">TOTAL VENTA</th>"
//                            + "<th class=\"text-center\" style=\"color: white !important;\">ACCIÓN</th>"
//                            + "</tr>"
//                            + "</thead>"
//                            + "<tbody>";
//
//                    for (DetalleCompra obj : listaCompra2) {
//                        int codigoDetalleCompra = obj.getCodigoDetalleCompra();
//
//                        html1 += "<tr class=\"text-center\">";
//                        html1 += "<td class=\"text-center\">" + obj.getProductos().getNombre() + "</td>";
//                        html1 += "<td class=\"text-center\">" + obj.getCantidad() + "</td>";
//
//                        html1 += "<td class=\"text-center\">" + obj.getPrecioCompra() + "</td>";
//                        html1 += "<td class=\"text-center\">" + obj.getPrecioVentaUnitario() + "</td>";
//                        html1 += "<td class=\"text-center\">" + obj.getTotalVenta() + "</td>";
//                       
//                        html1 += "<td>"
//       + "<button class='btn btn-success btn_editar' data-id='" + obj.getCodigoDetalleCompra() + "'>"
//       + "<i class='fas fa-edit'></i></button>"
//       + "</td>";
//                        html1 += "</tr>";
//                         System.out.println("ID generado para botón: " + obj.getCodigoDetalleCompra());
//                    }
//
//                    html1 += "</tbody></table></div>";
//                    json_mostrarCompra.put("resultado", "exito");
//                    json_mostrarCompra.put("tabla", html1);
//
//                } catch (Exception ex) {
//                    Logger.getLogger(Detalle_Producto_Controller.class.getName()).log(Level.SEVERE, null, ex);
//                    json_mostrarCompra.put("resultado", "error");
//                    json_mostrarCompra.put("mensaje", ex.getMessage());
//                }
//
//                array_mostrarCompra.put(json_mostrarCompra);
//                response.getWriter().write(array_mostrarCompra.toString());
//
//                break;
            case "mostrar": {
                JSONArray array_mostrarCompra = new JSONArray();
                JSONObject json_mostrarCompra = new JSONObject();
                String html1 = "";
                String codigoCompra = request.getParameter("estado"); // ← representa el código de compra

                try {
                    System.out.println("🔄 ENTRÓ AL CASO 'mostrar'");
                    DetalleCompraDao obaut = new DetalleCompraDao();

                    this.listaCompra2 = obaut.selectAllDetalleCompra(Integer.valueOf(codigoCompra));

                    html1 += "<div class=\"tabla_listCom\">"
                            + "<table id=\"tabla_listCom\" class=\"table table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                            + "<th class=\"text-center\" style=\"color: white !important;\">NOMBRE</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">CANTIDA</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">COSTO</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">PRECIO UNITARIO </th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">TOTAL VENTA</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">ACCIÓN</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (DetalleCompra obj : listaCompra2) {
                        html1 += "<tr class=\"text-center\">";
                        html1 += "<td class=\"text-center\">" + obj.getProductos().getNombre() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getCantidad() + "</td>";
                        html1 += "<td class=\"text-center\">$" + obj.getPrecioCompra() + "</td>";
                        html1 += "<td class=\"text-center\">$" + obj.getPrecioVentaUnitario() + "</td>";
                        html1 += "<td class=\"text-center\">$" + obj.getTotalVenta() + "</td>";
                        html1 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + obj.getCodigoDetalleCompra() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";
                        html1 += "</tr>";

                        System.out.println("ID generado para botón: " + obj.getCodigoDetalleCompra());
                    }

                    html1 += "</tbody></table></div>";
                    json_mostrarCompra.put("resultado", "exito");
                    json_mostrarCompra.put("tabla", html1);

                } catch (Exception ex) {
                    Logger.getLogger(Detalle_Producto_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    json_mostrarCompra.put("resultado", "error");
                    json_mostrarCompra.put("mensaje", ex.getMessage());
                }

                array_mostrarCompra.put(json_mostrarCompra);
                response.getWriter().write(array_mostrarCompra.toString());
                break;
            }
            case "cargarCombo": {
                ArrayList<Marca> listEm = new ArrayList();
                MarcaDao proDao = null;
                String html3 = "";
                try {
                    proDao = new MarcaDao();
                    listEm = proDao.cargarComboMarca();

                    if (listEm != null) {
                        for (Marca x : listEm) {
                            html3 += "<option value='" + x.getCodigoMarca() + "'>" + x.getMarca() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("marca", html3);
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
                ArrayList<Medida> listEm = new ArrayList();
                Unidad_MedidaDao proDao = null;
                String html3 = "";
                try {
                    proDao = new Unidad_MedidaDao();
                    listEm = proDao.cargarComboMedida();

                    if (listEm != null) {
                        for (Medida x : listEm) {
                            html3 += "<option value='" + x.getId_medida() + "'>" + x.getMedida() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("medida", html3);
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

            case "cargarComboB": {
                ArrayList<Productos> listEm = new ArrayList();
                ProductoDao proDao = null;
                String html3 = "";
                try {
                    proDao = new ProductoDao();
                    listEm = proDao.cargarComboP();

                    if (listEm != null) {
                        for (Productos x : listEm) {
                            html3 += "<option value='" + x.getIdProducto() + "'>" + x.getNombre() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("producto", html3);
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

            case "insertar": {
                String resultado_insert = "";
                try {
                    //Validación de número de orden
                    String codigo_producto = request.getParameter("codigo_producto");
//                    if (codigo_producto == null || codigo_producto.trim().isEmpty()) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "El número de codigo_producto está vacío o no fue enviado.");
//                        break;
//                    }
                    //catIns.setCodigo_producto(codigo_producto.trim());
                    catIns.setCodigo_producto(codigo_producto);
                    // Conversión segura de fecha en formato dd/MM/yyyy
                    String fechaStr = request.getParameter("fecha_vencimiento");
                    if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                        try {
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            formato.setLenient(false);
                            java.util.Date fechaUtil = formato.parse(fechaStr.trim());
                            java.sql.Date fechaSQL = new java.sql.Date(fechaUtil.getTime());
                            catIns.setFecha_vencimiento(fechaSQL);
                            System.out.println("[FECHA] Fecha convertida correctamente: " + fechaSQL);
                        } catch (ParseException e) {
                            System.out.println("[ERROR FECHA] Formato inválido: " + fechaStr);
                            jsonObjet.put("resultado", "error_fecha");
                            jsonObjet.put("detalle", "La fecha debe estar en formato dd/MM/yyyy");
                            break;
                        }
                    }

                    String cantidad = request.getParameter("cantidad_producto");


                    try {
                        int cantidad_producto = Integer.parseInt(cantidad);
                        catIns.setCantidad(cantidad_producto); // ← Asignación al objeto
                    } catch (NumberFormatException e) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El campo 'id_marca' debe ser un número entero válido.");
                        break;
                    }

                    String precioCompraStr = request.getParameter("precio_compra");

//                    if (precioCompraStr == null || precioCompraStr.trim().isEmpty()) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "El campo 'precio_compra' está vacío o no fue enviado.");
//                        break;
//                    }
                    try {
                        float precioCompra = Float.parseFloat(precioCompraStr);
                        catIns.setPrecioCompra(precioCompra); // ← Asignación al objeto
                    } catch (NumberFormatException e) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El campo 'precio_compra' debe ser un número decimal válido.");
                        break;
                    }

                    String porcentajeStr = request.getParameter("porcentaje");

                    if (porcentajeStr == null || porcentajeStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El campo 'porcentaje' está vacío o no fue enviado.");
                        break;
                    }

                    try {
                        int porcentaje = Integer.parseInt(porcentajeStr.trim());
                        catIns.setPorcentaje(porcentaje); // ← Asignación al objeto
                    } catch (NumberFormatException e) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El campo 'porcentaje' debe ser un número entero válido.");
                        break;
                    }
                    String medidaProducto = request.getParameter("medida_producto");

                    if (medidaProducto == null || medidaProducto.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El campo 'medida_producto' está vacío o no fue enviado.");
                        break;
                    }

                    catIns.setMedida_producto(medidaProducto.trim()); // ← Asignación al objeto

                    // Validación segura de código de compra
                    String codigoCompradoStr = request.getParameter("numero_de_orden");
                    if (codigoCompradoStr == null || codigoCompradoStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de empleado está vacío o no fue enviado.");
                        break;
                    }
                    Compras co = new Compras();
                    co.setIdCompra(Integer.parseInt(codigoCompradoStr.trim()));
                    catIns.setCompra(co);

                    // Validación segura de código de proveedor
                    String codigoPStr = request.getParameter("id_producto");
                    if (codigoPStr == null || codigoPStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de proveedor está vacío o no fue enviado.");
                        break;
                    }
                    Productos pro = new Productos();
                    pro.setIdProducto(Integer.parseInt(codigoPStr.trim()));
                    catIns.setProductos(pro);

                    String codigoMarStr = request.getParameter("id_marca");
                    if (codigoMarStr == null || codigoMarStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de proveedor está vacío o no fue enviado.");
                        break;
                    }
                    Marca M = new Marca();
                    M.setCodigoMarca(Integer.parseInt(codigoMarStr.trim()));
                    catIns.setMarca(M);

                    String codigoMeStr = request.getParameter("id_medida");
                    if (codigoMeStr == null || codigoMeStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de proveedor está vacío o no fue enviado.");
                        break;
                    }
                    Medida me = new Medida();
                    me.setId_medida(Integer.parseInt(codigoMeStr.trim()));
                    catIns.setMedida(me);

                    // Inserción
                    resultado_insert = comprasDaos.insert(catIns);
                    if ("exito".equals(resultado_insert)) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("codigo_producto", catIns.getCodigoDetalleCompra());
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
                DetalleCompra carga = new DetalleCompra();
                try {

                    catIns.setCodigoDetalleCompra(Integer.parseInt(request.getParameter("codigo_detalle_compra")));
                    System.out.println("Recibido en controlador: " + request.getParameter("codigo_detalle_compra"));
                    carga = comprasDaos.cargarDatos(catIns);

                    if (carga != null) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("codigo_detalle_compra", carga.getCodigoDetalleCompra());
                        jsonObjet.put("codigo_producto", carga.getCodigo_producto());
                        jsonObjet.put("fecha_vencimiento", carga.getFecha_vencimiento());
                        jsonObjet.put("cantidad_producto", carga.getCantidad());
                        jsonObjet.put("precio_compra", carga.getPrecioCompra());
                        jsonObjet.put("porcentaje", carga.getPorcentaje());
                        jsonObjet.put("medida_producto", carga.getMedida_producto());
                        jsonObjet.put("codigo_compra", carga.getCompra().getIdCompra());
                         jsonObjet.put("numero_de_orden", carga.getCompra().getNumeroOrden());
                        jsonObjet.put("id_producto", carga.getProductos().getIdProducto());
                        jsonObjet.put("nombre_producto", carga.getProductos().getNombre());
                        jsonObjet.put("id_marca", carga.getMarca().getCodigoMarca());
                        jsonObjet.put("nombre_marca", carga.getMarca().getMarca());
                        jsonObjet.put("id_medida", carga.getMedida().getId_medida());
                        jsonObjet.put("nombre_medida", carga.getMedida().getMedida());

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

                DetalleCompra catUpdate = new DetalleCompra();
                Compras com = new Compras();
                Productos pro = new Productos();
                Medida me = new Medida();
                Marca mar = new Marca();

                try {
                    DetalleCompraDao catDao = new DetalleCompraDao();
                    // ✅ Cargar número de orden
                    System.out.println("ENTRO a editar 56");
//        
                    catUpdate.setCodigoDetalleCompra(Integer.parseInt(request.getParameter("codigo_detalle_compra")));
                    catUpdate.setCodigo_producto(request.getParameter("codigo_producto"));
                    String fechaFormateada = request.getParameter("fecha_vencimiento"); // Ej: "28/09/2025"
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaConvertida = LocalDate.parse(fechaFormateada, formatter);
                    catUpdate.setFecha_vencimiento(java.sql.Date.valueOf(fechaConvertida));

                    String cantidadStr = request.getParameter("cantidad_producto");

                    if (cantidadStr != null && !cantidadStr.isEmpty()) {
                        try {
                            int cantidad = Integer.parseInt(cantidadStr);
                            catUpdate.setCantidad(cantidad);
                        } catch (NumberFormatException e) {
                            // Diagnóstico y retroalimentación
                            System.err.println("Error: cantidad_producto no es un entero válido → " + cantidadStr);
                            // Aquí puedes agregar lógica de fallback o mostrar un mensaje al usuario
                        }
                    } else {
                        // Fallback si el parámetro está ausente o vacío
                        System.err.println("Error: parámetro 'cantidad_producto' ausente o vacío.");
                    }

                    String precioStr = request.getParameter("precio_compra");

                    if (precioStr != null && !precioStr.isEmpty()) {
                        try {
                            float precioCompra = Float.parseFloat(precioStr);
                            catUpdate.setPrecioCompra(precioCompra);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: precio_compra no es un número válido → " + precioStr);
                            // Aquí puedes agregar lógica de fallback o mostrar un mensaje al usuario
                        }
                    } else {
                        System.err.println("Error: parámetro 'precio_compra' ausente o vacío.");
                    }
                    // ✅ Conversión de fecha DD/MM/YYYY

                    String porcentajeStr = request.getParameter("porcentaje");
                    int porcentaje = 0; // Valor por defecto o fallback

                    if (porcentajeStr != null && !porcentajeStr.isEmpty()) {
                        try {
                            porcentaje = Integer.parseInt(porcentajeStr);
                            catUpdate.setPorcentaje(porcentaje);
                        } catch (NumberFormatException e) {
                            System.err.println("Porcentaje inválido: " + porcentajeStr);
                            // Puedes lanzar una excepción controlada o registrar el error
                        }
                    } else {
                        System.err.println("Parámetro 'porcentaje' ausente o vacío.");
                        // Puedes decidir si lanzar error, usar valor por defecto, o ignorar
                    }

                    catUpdate.setMedida_producto(request.getParameter("medida_producto"));

// ✅ Cargar empleado
                    com.setIdCompra(Integer.parseInt(request.getParameter("codigo_compra")));
                    catUpdate.setCompra(com);

                    // ✅ Cargar proveedor
                    pro.setIdProducto(Integer.parseInt(request.getParameter("id_producto")));
                    catUpdate.setProductos(pro);

                    mar.setCodigoMarca(Integer.parseInt(request.getParameter("id_marca")));
                    catUpdate.setMarca(mar);

                    me.setId_medida(Integer.parseInt(request.getParameter("id_medida")));
                    catUpdate.setMedida(me);

                    // ✅ Ejecutar actualización
                    resultado_update = catDao.update(catUpdate);

                    // ✅ Construir respuesta
                    if (resultado_update.equals("exito")) {
                        json_nombre_update.put("resultado", "exito");
                        json_nombre_update.put("codigo_producto", catUpdate.getCodigo_producto());
                    } else {
                        json_nombre_update.put("resultado", "error");
                        json_nombre_update.put("resultado_insertar", resultado_update);
                    }

                } catch (DateTimeParseException e) {
                    json_nombre_update.put("resultado", "error_fecha");
                    json_nombre_update.put("error_mostrado", "Formato de fecha inválido: " + request.getParameter("fecha_vencimiento"));
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

}
