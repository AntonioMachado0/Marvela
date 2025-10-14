
package com.ues.edu.controllers;

import com.ues.edu.models.Categoria;
import com.ues.edu.models.Compras;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Medida;
import java.time.LocalDate;
import com.ues.edu.models.Productos;
import com.ues.edu.models.dao.CategoriaDao;
import com.ues.edu.models.dao.MarcaDao;
import java.text.SimpleDateFormat;
import com.ues.edu.models.dao.ProductoDao;
import com.ues.edu.models.dao.Unidad_MedidaDao;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Maris
 */
@MultipartConfig
@WebServlet(name = "ProductosController", urlPatterns = {"/ProductosController"})
public class ProductosController extends HttpServlet {

    private ArrayList<Productos> listaCompraActividad;
    private ArrayList<Productos> listaCompra2;

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
        Productos catIns = new Productos();
        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
        ProductoDao comprasDao = new ProductoDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {
            case "mostrar":

                JSONArray array_mostrarCompra = new JSONArray();
                JSONObject json_mostrarCompra = new JSONObject();
                String html1 = "";
                String estadoM = request.getParameter("estado");

                try {
                    System.out.println("🔄 ENTRÓ AL CASO 'mostrar'");
                    ProductoDao obaut = new ProductoDao();

                    this.listaCompraActividad = new ArrayList<>();
                    this.listaCompra2 = obaut.selectAllProductos(Integer.valueOf(estadoM), "todos");

                    html1 += "<div class=\"tabla_listCom\">"
                            + "<table id=\"tabla_listCom\" class=\"table table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                            + "<th class=\"text-center\" style=\"color: white !important;\">NOMBRE</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">DESCRIPCION</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">IMAGEN</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">CATEGORIA</th>"
                            + "<th class=\"text-center\" style=\"color: white !important;\">ACCIÓN</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (Productos obj : listaCompra2) {
                        int idProducto = obj.getIdProducto();
                        System.out.println("🖼️ ID para imagen: " + idProducto);

                        html1 += "<tr class=\"text-center\">";
                        //html1 += "<td class=\"text-center\">" + obj.getCodigoProducto() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getNombre() + "</td>";
                        html1 += "<td class=\"text-center\">" + obj.getDescripcion() + "</td>";
                        html1 += "<td class='text-center'>"
                                + "<img src='" + request.getContextPath() + "/ImagenServlet?id=" + idProducto + "' "
                                + "alt='Producto' width='50' height='50' class='img-thumbnail' "
                                + "onerror=\"this.src='" + request.getContextPath() + "/ImagenServlet?id=0'\" />"
                                + "</td>";

                        html1 += "<td class=\"text-center\">" + obj.getCategoria().getNombre() + "</td>";

                        html1 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + obj.getIdProducto() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";
                        html1 += "</tr>";
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
                ArrayList<Categoria> listPro = new ArrayList();
                CategoriaDao proDao = null;
                String html3 = "";
                try {
                    proDao = new CategoriaDao();
                    listPro = proDao.cargarComboProveedor();

                    if (listPro != null) {
                        for (Categoria x : listPro) {
                            html3 += "<option value='" + x.getCodigoCategoria() + "'>" + x.getNombre() + "</option>";
                        }
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("categoria", html3);
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

//            case "insertar": {
//                String resultado_insert = "";
//                try {
//                    // 🧾 Validación: nombre de producto
//                    String nombre_producto = request.getParameter("nombre_producto");
//                    if (nombre_producto == null || nombre_producto.trim().isEmpty()) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "El nombre del producto está vacío o no fue enviado.");
//                        jsonArray.put(jsonObjet);
//                        response.setContentType("application/json");
//                        response.getWriter().write(jsonArray.toString());
//                        break;
//                    }
//                    catIns.setNombre(nombre_producto.trim());
//
//                    // 🧾 Validación: descripción
//                    String descripcion = request.getParameter("descripcion");
//                    if (descripcion == null || descripcion.trim().isEmpty()) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "La descripción está vacía o no fue enviada.");
//                        jsonArray.put(jsonObjet);
//                        response.setContentType("application/json");
//                        response.getWriter().write(jsonArray.toString());
//                        break;
//                    }
//                    catIns.setDescripcion(descripcion.trim());
//
//                    // 🖼️ Validación: imagen
//                    Part imagenPart = request.getPart("imagen");
//                    if (imagenPart == null || imagenPart.getSize() == 0) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "La imagen del producto está vacía o no fue enviada.");
//                        jsonArray.put(jsonObjet);
//                        response.setContentType("application/json");
//                        response.getWriter().write(jsonArray.toString());
//                        break;
//                    }
//
//                    String tipoMime = imagenPart.getContentType();
//                    if (!tipoMime.startsWith("image/")) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "El archivo enviado no es una imagen válida.");
//                        jsonArray.put(jsonObjet);
//                        response.setContentType("application/json");
//                        response.getWriter().write(jsonArray.toString());
//                        break;
//                    }
//
//                    // 🗂️ Guardado físico de imagen
//                    String nombreArchivo = Paths.get(imagenPart.getSubmittedFileName()).getFileName().toString();
//                    String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();
//                    String nombreFinal = "prod_" + System.currentTimeMillis() + "." + extension;
//
//                    // ✅ Carpeta pública accesible desde navegador
//                    String rutaImagenes = getServletContext().getRealPath("/img/imagenes_productos/");
//                    File carpeta = new File(rutaImagenes);
//                    if (!carpeta.exists()) {
//                        carpeta.mkdirs();
//                    }
//
//                    File archivoImagen = new File(carpeta, nombreFinal);
//                    try ( InputStream input = imagenPart.getInputStream()) {
//                        Files.copy(input, archivoImagen.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    }
//
//                    // ✅ Ruta relativa para mostrar en frontend
//                    byte[] imagenBytes = imagenPart.getInputStream().readAllBytes();
//                    catIns.setImagen(imagenBytes);
//
//                    // 📋 Validación: categoría
//                    String codigoCategoriaStr = request.getParameter("codigo_categoria");
//                    if (codigoCategoriaStr == null || codigoCategoriaStr.trim().isEmpty()) {
//                        jsonObjet.put("resultado", "error_parametro");
//                        jsonObjet.put("detalle", "El código de categoría está vacío o no fue enviado.");
//                        jsonArray.put(jsonObjet);
//                        response.setContentType("application/json");
//                        response.getWriter().write(jsonArray.toString());
//                        break;
//                    }
//                    Categoria cate = new Categoria();
//                    cate.setCodigoCategoria(Integer.parseInt(codigoCategoriaStr.trim()));
//                    catIns.setCategoria(cate);
//
//                    // 🧩 Inserción en base de datos
//                    resultado_insert = comprasDao.insert(catIns);
//                    if ("exito".equals(resultado_insert)) {
//                        jsonObjet.put("resultado", "exito");
//                        jsonObjet.put("nombre_producto", catIns.getNombre());
//                        jsonObjet.put("imagen", catIns.getImagen());
//                    } else {
//                        jsonObjet.put("resultado", "error");
//                        jsonObjet.put("detalle", resultado_insert);
//                    }
//
//                } catch (SQLException e) {
//                    jsonObjet.put("resultado", "error_sql");
//                    jsonObjet.put("error_mostrado", e.getMessage());
//                    System.out.println("Error SQL: " + e.getErrorCode());
//                }
//
//                // 📤 Respuesta JSON final
//                jsonArray.put(jsonObjet);
//                response.setContentType("application/json");
//                response.getWriter().write(jsonArray.toString());
//                break;
//            }
            case "insertar": {
                String resultado_insert = "";
                try {
                    String nombre_producto = request.getParameter("nombre_producto");
                    if (nombre_producto == null || nombre_producto.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El nombre del producto está vacío o no fue enviado.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }

                    // 🔍 Validación de duplicado
                    if (comprasDao.existeProducto(nombre_producto.trim())) {
                        jsonObjet.put("resultado", "duplicado");
                        jsonObjet.put("detalle", "Ya existe un producto con ese nombre.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                     catIns.setNombre(nombre_producto.trim());
                    // 🧾 Validación: descripción
                    String descripcion = request.getParameter("descripcion");
                    if (descripcion == null || descripcion.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "La descripción sin datos.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    catIns.setDescripcion(descripcion.trim());

                    // 🖼️ Validación: imagen
                    Part imagenPart = request.getPart("imagen");
                    if (imagenPart == null || imagenPart.getSize() == 0) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "La imagen del producto está vacía o no fue enviada.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }

                    String tipoMime = imagenPart.getContentType();
                    if (!tipoMime.startsWith("image/")) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El archivo enviado no es una imagen válida.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }

                    // 🗂️ Guardado físico de imagen
                    String nombreArchivo = Paths.get(imagenPart.getSubmittedFileName()).getFileName().toString();
                    String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();
                    String nombreFinal = "prod_" + System.currentTimeMillis() + "." + extension;

                    // ✅ Carpeta pública accesible desde navegador
                    String rutaImagenes = getServletContext().getRealPath("/img/imagenes_productos/");
                    File carpeta = new File(rutaImagenes);
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }

                    File archivoImagen = new File(carpeta, nombreFinal);
                    try ( InputStream input = imagenPart.getInputStream()) {
                        Files.copy(input, archivoImagen.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    // ✅ Ruta relativa para mostrar en frontend
                    byte[] imagenBytes = imagenPart.getInputStream().readAllBytes();
                    catIns.setImagen(imagenBytes);

                    // 📋 Validación: categoría
                    String codigoCategoriaStr = request.getParameter("codigo_categoria");
                    if (codigoCategoriaStr == null || codigoCategoriaStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de categoría está vacío o no fue enviado.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    Categoria cate = new Categoria();
                    cate.setCodigoCategoria(Integer.parseInt(codigoCategoriaStr.trim()));
                    catIns.setCategoria(cate);

                    // 🧩 Inserción en base de datos
                    resultado_insert = comprasDao.insert(catIns);
                    if ("exito".equals(resultado_insert)) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("nombre_producto", catIns.getNombre());
                        jsonObjet.put("imagen", catIns.getImagen());
                    } else {
                        jsonObjet.put("resultado", "error");
                        jsonObjet.put("detalle", resultado_insert);
                    }

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("error_mostrado", e.getMessage());
                    System.out.println("Error SQL: " + e.getErrorCode());
                }

                // 📤 Respuesta JSON final
                jsonArray.put(jsonObjet);
                response.setContentType("application/json");
                response.getWriter().write(jsonArray.toString());
                break;
            }
            case "cargarDatos": {
                Productos carga = new Productos();
                try {
                    System.out.println("ID recibido: " + request.getParameter("id_producto"));
                    catIns.setIdProducto(Integer.parseInt(request.getParameter("id_producto")));
                    carga = comprasDao.cargarDatos(catIns);

                    if (carga != null) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("id_producto", carga.getIdProducto());
                        jsonObjet.put("nombre_producto", carga.getNombre());
                        jsonObjet.put("descripcion", carga.getDescripcion());

                        jsonObjet.put("codigo_categoria", carga.getCategoria().getCodigoCategoria());
                        jsonObjet.put("nombre", carga.getCategoria().getNombre());

                        byte[] imagenBytes = carga.getImagen();
                        System.out.println("Bytes recuperados: " + (imagenBytes != null ? imagenBytes.length : "null"));

                        if (imagenBytes != null && imagenBytes.length > 0) {
                            String base64 = Base64.getEncoder().encodeToString(imagenBytes);
                            jsonObjet.put("imagen_base64", base64);
                            // o dinámico si lo tienes
                        } else {
                            jsonObjet.put("imagen_base64", "");

                        }
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

    Productos catUpdate = new Productos();
    Categoria empleado = new Categoria();
    Marca pro = new Marca();
    Medida pros = new Medida();

    try {
        ProductoDao catDao = new ProductoDao();

        int idProducto = Integer.parseInt(request.getParameter("id_producto"));
        String nuevoNombre = request.getParameter("nombre_producto");

        // 🔍 Validación de duplicado
        if (catDao.existeProductoConOtroId(nuevoNombre.trim(), idProducto)) {
            json_nombre_update.put("resultado", "duplicado");
            json_nombre_update.put("mensaje", "Ya existe otro producto con ese nombre.");
        } else {
            catUpdate.setIdProducto(idProducto);
            catUpdate.setNombre(nuevoNombre.trim());
            catUpdate.setDescripcion(request.getParameter("descripcion"));

            Part filePart = request.getPart("imagen");
            byte[] imagenBytes = null;

            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream()) {
                    imagenBytes = inputStream.readAllBytes();
                }
            }

            catUpdate.setImagen(imagenBytes);

            empleado.setCodigoCategoria(Integer.parseInt(request.getParameter("codigo_categoria")));
            catUpdate.setCategoria(empleado);

            resultado_update = catDao.update(catUpdate);

            if ("exito".equals(resultado_update)) {
                json_nombre_update.put("resultado", "exito");
                json_nombre_update.put("nombre_producto", catUpdate.getNombre());
            } else {
                json_nombre_update.put("resultado", "error");
                json_nombre_update.put("resultado_insertar", resultado_update);
            }
        }

    } catch (SQLException e) {
        json_nombre_update.put("resultado", "error_sql");
        System.out.println("Error Code error:" + e.getErrorCode());
        throw new RuntimeException("Error al editar producto", e);
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
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.controllers;


/**
 *
 * @author thebe
 */import com.ues.edu.models.Categoria;
import com.ues.edu.models.Compras;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Medida;
import java.time.LocalDate;
import com.ues.edu.models.Productos;
import com.ues.edu.models.dao.CategoriaDao;
import com.ues.edu.models.dao.MarcaDao;
import com.ues.edu.models.dao.ProductoDao;
import java.text.SimpleDateFormat;
import com.ues.edu.models.dao.Unidad_MedidaDao;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Maris
 */
@MultipartConfig
@WebServlet(name = "ProductosController", urlPatterns = {"/ProductosController"})
public class ProductosController extends HttpServlet {

    private ArrayList<Productos> listaCompraActividad;
    private ArrayList<Productos> listaCompra2;

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
        Productos catIns = new Productos();
        JSONObject jsonObjet = new JSONObject(); // ← única declaración aqu
        JSONArray jsonArray = new JSONArray();
        ProductoDao comprasDao = new ProductoDao();
        System.out.println(filtro);
        if (filtro == null) {
            return;
        }

        switch (filtro) {
            case "mostrar":

    JSONArray array_mostrarCompra = new JSONArray();
    JSONObject json_mostrarCompra = new JSONObject();
    String html1 = "";
    String estadoM = request.getParameter("estado");

    try {
        System.out.println("🔄 ENTRÓ AL CASO 'mostrar'");
        ProductoDao obaut = new ProductoDao();
        this.listaCompraActividad = new ArrayList<>();
        this.listaCompra2 = obaut.selectAllProductos(Integer.valueOf(estadoM), "todos");

        html1 += "<div class=\"tabla_listCom\">" +
                 "<table id=\"tabla_listCom\" class=\"table table-hover datanew\">" +
                 "<thead>" +
                 "<tr class=\"mb-2 green text-center\">" +
                 "<th class=\"text-center\" style=\"color: white !important;\">CÓDIGO</th>" +
                 "<th class=\"text-center\" style=\"color: white !important;\">NOMBRE</th>" +
                 "<th class=\"text-center\" style=\"color: white !important;\">IMAGEN</th>" +
                 "<th class=\"text-center\" style=\"color: white !important;\">CATEGORIA</th>" +
                 "<th class=\"text-center\" style=\"color: white !important;\">MARCA</th>" +
                 "<th class=\"text-center\" style=\"color: white !important;\">MEDIDA</th>" +
                 "<th class=\"text-center\" style=\"color: white !important;\">ACCIÓN</th>" +
                 "</tr>" +
                 "</thead>" +
                 "<tbody>";

        for (Productos obj : listaCompra2) {
            int idProducto = obj.getIdProducto();
            System.out.println("🖼️ ID para imagen: " + idProducto);

            html1 += "<tr class=\"text-center\">";
            html1 += "<td class=\"text-center\">" + obj.getCodigoProducto() + "</td>";
            html1 += "<td class=\"text-center\">" + obj.getNombre() + "</td>";

            html1 += "<td class='text-center'>" +
         "<img src='" + request.getContextPath() + "/ImagenServlet?id=" + idProducto + "' " +
         "alt='Producto' width='50' height='50' class='img-thumbnail' " +
         "onerror=\"this.src='" + request.getContextPath() + "/ImagenServlet?id=0'\" />" +
         "</td>";

            html1 += "<td class=\"text-center\">" + obj.getCategoria().getNombre() + "</td>";
            html1 += "<td class=\"text-center\">" + obj.getMarca().getMarca() + "</td>";
            html1 += "<td class=\"text-center\">" + obj.getMedida().getMedida() + "</td>";
            html1 += "<td>" +
                     "<button class='btn btn-success btn_editar' data-id='" + obj.getCodigoProducto() + "'>" +
                     "<i class='fas fa-edit'></i></button>" +
                     "</td>";
            html1 += "</tr>";
        }

        html1 += "</tbody></table></div>";
        json_mostrarCompra.put("resultado", "exito");
        json_mostrarCompra.put("tabla", html1);

    } catch (Exception ex) {
        //Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
        json_mostrarCompra.put("resultado", "error");
        json_mostrarCompra.put("mensaje", ex.getMessage());
    }

    array_mostrarCompra.put(json_mostrarCompra);
    response.getWriter().write(array_mostrarCompra.toString());

    break;
//            case "cargarCombo": {
//                ArrayList<Categoria> listPro = new ArrayList();
//                CategoriaDao proDao = null;
//                String html3 = "";
//                try {
//                    proDao = new CategoriaDao();
//                    listPro = proDao.cargarComboProveedor();
//
//                    if (listPro != null) {
//                        for (Categoria x : listPro) {
//                            html3 += "<option value='" + x.getCodigoCategoria() + "'>" + x.getNombre() + "</option>";
//                        }
//                        jsonObjet.put("resultado", "exito");
//                        jsonObjet.put("categoria", html3);
//                    } else {
//                        jsonObjet.put("resultado", "error_cargar");
//                    }
//
//                } catch (SQLException e) {
//                    jsonObjet.put("resultado", "error_sql");
//                    jsonObjet.put("exception", e.getMessage());
//                }
//                jsonArray.put(jsonObjet);
//                response.getWriter().write(jsonArray.toString());
//                break;
//            }
//            case "cargarComboE": {
//                ArrayList<Marca> listEm = new ArrayList();
//                MarcaDao proDao = null;
//                String html3 = "";
//                try {
//                    proDao = new MarcaDao();
//                    listEm = proDao.cargarComboMarca();
//
//                    if (listEm != null) {
//                        for (Marca x : listEm) {
//                            html3 += "<option value='" + x.getCodigoMarca() + "'>" + x.getMarca() + "</option>";
//                        }
//                        jsonObjet.put("resultado", "exito");
//                        jsonObjet.put("marca", html3);
//                    } else {
//                        jsonObjet.put("resultado", "error_cargar");
//                    }
//
//                } catch (SQLException e) {
//                    jsonObjet.put("resultado", "error_sql");
//                    jsonObjet.put("exception", e.getMessage());
//                }
//                jsonArray.put(jsonObjet);
//                response.getWriter().write(jsonArray.toString());
//                break;
//            }
//            case "cargarComboB": {
//                ArrayList<Medida> listEm = new ArrayList();
//                Unidad_MedidaDao proDao = null;
//                String html3 = "";
//                try {
//                    proDao = new Unidad_MedidaDao();
//                    listEm = proDao.cargarComboMedida();
//
//                    if (listEm != null) {
//                        for (Medida x : listEm) {
//                            html3 += "<option value='" + x.getId_medida() + "'>" + x.getMedida() + "</option>";
//                        }
//                        jsonObjet.put("resultado", "exito");
//                        jsonObjet.put("medida", html3);
//                    } else {
//                        jsonObjet.put("resultado", "error_cargar");
//                    }
//
//                } catch (SQLException e) {
//                    jsonObjet.put("resultado", "error_sql");
//                    jsonObjet.put("exception", e.getMessage());
//                }
//                jsonArray.put(jsonObjet);
//                response.getWriter().write(jsonArray.toString());
//                break;
//            }

            case "insertar": {
                String resultado_insert = "";
                try {
                    // 🧾 Validación: nombre de producto
                    String nombre_producto = request.getParameter("nombre_producto");
                    if (nombre_producto == null || nombre_producto.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El nombre del producto está vacío o no fue enviado.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    catIns.setNombre(nombre_producto.trim());

                    // 🧾 Validación: descripción
                    String descripcion = request.getParameter("descripcion");
                    if (descripcion == null || descripcion.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "La descripción está vacía o no fue enviada.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    catIns.setDescripcion(descripcion.trim());

                    // 🖼️ Validación: imagen
                    Part imagenPart = request.getPart("imagen");
                    if (imagenPart == null || imagenPart.getSize() == 0) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "La imagen del producto está vacía o no fue enviada.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }

                    String tipoMime = imagenPart.getContentType();
                    if (!tipoMime.startsWith("image/")) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El archivo enviado no es una imagen válida.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }

                    // 🗂️ Guardado físico de imagen
                    String nombreArchivo = Paths.get(imagenPart.getSubmittedFileName()).getFileName().toString();
                    String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1).toLowerCase();
                    String nombreFinal = "prod_" + System.currentTimeMillis() + "." + extension;

                    // ✅ Carpeta pública accesible desde navegador
                    String rutaImagenes = getServletContext().getRealPath("/img/imagenes_productos/");
                    File carpeta = new File(rutaImagenes);
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }

                    File archivoImagen = new File(carpeta, nombreFinal);
                    try ( InputStream input = imagenPart.getInputStream()) {
                        Files.copy(input, archivoImagen.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    // ✅ Ruta relativa para mostrar en frontend
                    byte[] imagenBytes = imagenPart.getInputStream().readAllBytes();
                    catIns.setImagen(imagenBytes);

                    // 📋 Validación: categoría
                    String codigoCategoriaStr = request.getParameter("codigo_categoria");
                    if (codigoCategoriaStr == null || codigoCategoriaStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de categoría está vacío o no fue enviado.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    Categoria cate = new Categoria();
                    cate.setCodigoCategoria(Integer.parseInt(codigoCategoriaStr.trim()));
                    catIns.setCategoria(cate);

                    // 📋 Validación: marca
                    String id_marcaStr = request.getParameter("id_marca");
                    if (id_marcaStr == null || id_marcaStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de marca está vacío o no fue enviado.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    Marca mar = new Marca();
                    mar.setCodigoMarca(Integer.parseInt(id_marcaStr.trim()));
                    catIns.setMarca(mar);

                    // 📋 Validación: medida
                    String id_medidaStr = request.getParameter("id_medida");
                    if (id_medidaStr == null || id_medidaStr.trim().isEmpty()) {
                        jsonObjet.put("resultado", "error_parametro");
                        jsonObjet.put("detalle", "El código de medida está vacío o no fue enviado.");
                        jsonArray.put(jsonObjet);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonArray.toString());
                        break;
                    }
                    Medida mad = new Medida();
                    mad.setId_medida(Integer.parseInt(id_medidaStr.trim()));
                    catIns.setMedida(mad);

                    // 🧩 Inserción en base de datos
                    resultado_insert = comprasDao.insert(catIns);
                    if ("exito".equals(resultado_insert)) {
                        jsonObjet.put("resultado", "exito");
                        jsonObjet.put("nombre_producto", catIns.getNombre());
                        jsonObjet.put("imagen", catIns.getImagen());
                    } else {
                        jsonObjet.put("resultado", "error");
                        jsonObjet.put("detalle", resultado_insert);
                    }

                } catch (SQLException e) {
                    jsonObjet.put("resultado", "error_sql");
                    jsonObjet.put("error_mostrado", e.getMessage());
                    System.out.println("Error SQL: " + e.getErrorCode());
                }

                // 📤 Respuesta JSON final
                jsonArray.put(jsonObjet);
                response.setContentType("application/json");
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
//ikkj

}

