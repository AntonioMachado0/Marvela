/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.CorreoUtil;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Encriptar;
import com.ues.edu.models.Usuario;
import com.ues.edu.models.Util;
import com.ues.edu.models.dao.BloqueoDao;
import com.ues.edu.models.dao.EmpleadoDao;
import com.ues.edu.models.dao.RolDao;
import com.ues.edu.models.dao.UsuarioDao;
import static com.ues.edu.models.dao.UsuarioDao.generarCodigo;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Maris
 */
@WebServlet(name = "UsuarioController", urlPatterns = {"/UsuarioController"})
public class UsuarioController extends HttpServlet {

    private ArrayList<Usuario> listaUsuario;
    private ArrayList<Usuario> listaUsuarios;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);

    }

    private String generarCodigo() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("aplication/json;charset=utf-8");

        JSONArray array_Usuario = new JSONArray();
        JSONObject json_Usuario = new JSONObject();
        JSONObject json_Usuario_Datos = new JSONObject();

        Empleado rolUsuario = new Empleado();
        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = new Usuario();

        String filtro = request.getParameter("consultar_datos");

        if (filtro == null) {
            return;
        }

        switch (filtro) {

            case "mostrar": {
                String html2 = "";
                String estado2 = request.getParameter("estado");

                try {
                    listaUsuario = new ArrayList();
                    listaUsuario = usuarioDao.selectAllUsuario(Integer.valueOf(estado2), "todos");

                    html2 += "<div class=\"table-responsive\">"
                            + "<table id=\"tabla_listCategoria\" class=\" table-hover datanew\">"
                            + "<thead>"
                            + "<tr class=\"mb-2 green text-center\">"
                            + "<th class=\"text-center\" style=\" color: white !important;\">NOMBRE</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">CORREO</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">CLAVE</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">ESTADO</th>"
                            + "<th class=\"text-center\" style=\" color: white !important;\">ACCIÓN</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";

                    for (Usuario x : listaUsuario) {
                        html2 += "<tr class=\"text-center\">";
                        //html2 += "<td class=\"text-center\">" + x.getIdUsuario() + "</td>";
                        html2 += "<td class=\"text-center\">" + x.getEmpleado().getNombreCompleto() + "</td>";
                        html2 += "<td class=\"text-center\">" + x.getCorreo() + "</td>";
                        html2 += "<td class=\"text-center\">" + x.getContraseña() + "</td>";
                        html2 += "<td class=\"text-center\">" + (x.isEstado() ? "Activo" : "Inactivo") + "</td>";
                        html2 += "<td>"
                                + "<button class='btn btn-success btn_editar' data-id='" + x.getCodigoUsuario() + "'>"
                                + "<i class='fas fa-edit'></i></button>"
                                + "</td>";
                        html2 += "</tr>";
                    }
                    html2 += "</tbody>"
                            + "</table>";
                    html2 += "</div>";
                    html2 += "</div>";

                    json_Usuario.put("resultado", "exito");
                    json_Usuario.put("tabla", html2);
                } catch (SQLException e) {
                    json_Usuario.put("resultado", "error sql");
                    json_Usuario.put("error", e.getMessage());
                    json_Usuario.put("code error", e.getErrorCode());
                    throw new RuntimeException(e);
                } catch (JSONException ex) {
                    Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
                array_Usuario.put(json_Usuario);
                response.getWriter().write(array_Usuario.toString());
            }
            break;
            case "cargarCombo": {
                ArrayList<Empleado> listEm = new ArrayList<>();
                EmpleadoDao proDao = null;
                String html3 = "";

                try {
                    proDao = new EmpleadoDao();
                    listEm = proDao.cargarComboEmpleados(); // ← empleados sin usuario

                    // 🧠 Detectar si estamos en modo edición
                    String codigoEmpleado = request.getParameter("codigo_empleado");
                    if (codigoEmpleado != null && !codigoEmpleado.equals("0")) {
                        int cod = Integer.parseInt(codigoEmpleado);
                        boolean yaIncluido = listEm.stream().anyMatch(e -> e.getCodigoEmpleado() == cod);
                        if (!yaIncluido) {
                            Empleado actual = proDao.obtenerEmpleadoPorCodigo(cod); // ← método adicional en DAO
                            if (actual != null) {
                                listEm.add(0, actual); // ← lo agregamos al inicio del combo
                            }
                        }
                    }

                    if (listEm != null) {
                        for (Empleado x : listEm) {
                            html3 += "<option value='" + x.getCodigoEmpleado() + "'>" + x.getNombreCompleto() + "</option>";
                        }
                        json_Usuario.put("resultado", "exito");
                        json_Usuario.put("empleado", html3);
                    } else {
                        json_Usuario.put("resultado", "error_cargar");
                    }

                } catch (SQLException e) {
                    json_Usuario.put("resultado", "error_sql");
                    json_Usuario.put("exception", e.getMessage());
                }

                array_Usuario.put(json_Usuario);
                response.getWriter().write(array_Usuario.toString());
            }
            break;

            case "insertar": {
                String resultado_insert = "";

                String clave = request.getParameter("clave");

                try {
                    // 🟢 Validación de clave antes de encriptar
                    if (clave == null || clave.trim().isEmpty()) {
                        json_Usuario.put("resultado", "error_clave_vacia");
                        break;
                    }

                    if (clave.length() < 8 || clave.length() > 12) {
                        json_Usuario.put("resultado", "error_pass");
                        break;
                    }

                    String correo = request.getParameter("correo");
                    if (correo == null || correo.trim().isEmpty()) {
                        json_Usuario.put("resultado", "error_correo_vacio");
                        array_Usuario.put(json_Usuario);
                        response.getWriter().write(array_Usuario.toString());
                        break;
                    }

                    if (usuarioDao.correoExiste(correo)) {
                        json_Usuario.put("resultado", "error_correo_duplicado");
                        array_Usuario.put(json_Usuario);
                        response.getWriter().write(array_Usuario.toString());
                        break;
                    }

                    usuario.setCorreo(correo);

                    // 2. Clave encriptada
                    String claveEncriptada = Encriptar.getStringMessageDigest(clave, Encriptar.SHA256);
                    usuario.setContraseña(claveEncriptada);

                    // 3. Estado lógico
                    String estadoParam = request.getParameter("estado");
                    boolean estado = "true".equalsIgnoreCase(estadoParam != null ? estadoParam.trim() : "");
                    usuario.setEstado(estado);

                    // 4. Código de empleado
                    rolUsuario.setCodigoEmpleado(Integer.parseInt(request.getParameter("codigo_empleado")));
                    usuario.setEmpleado(rolUsuario);

                    // 🟢 Inserción en base de datos
                    resultado_insert = usuarioDao.insert(usuario);

                    if ("exito".equals(resultado_insert)) {
                        json_Usuario.put("resultado", "exito");
                        json_Usuario.put("correo", usuario.getCorreo());
                    } else {
                        json_Usuario.put("resultado", "error_insertar");
                        json_Usuario.put("detalle", resultado_insert);
                    }

                } catch (SQLException e) {
                    json_Usuario.put("resultado", "error_sql");
                    json_Usuario.put("error_mostrado", e.getMessage());
                    System.out.println(" Error SQL: " + e.getErrorCode());
                } catch (Exception e) {
                    json_Usuario.put("resultado", "error_general");
                    json_Usuario.put("error_mostrado", e.getMessage());
                    System.out.println("⚠️ Error inesperado: " + e.getMessage());
                }

                array_Usuario.put(json_Usuario);
                response.getWriter().write(array_Usuario.toString());
                break;
            }
            case "cargarDatos": {
                Usuario cargaTarea = new Usuario();
                try {
                    usuario.setCodigoUsuario(Integer.parseInt(request.getParameter("codigo_usuario")));
                    cargaTarea = usuarioDao.cargarDatos(usuario);

                    if (cargaTarea != null) {
                        json_Usuario.put("resultado", "exito");
                        json_Usuario.put("codigo_usuario", cargaTarea.getCodigoUsuario());
                        json_Usuario.put("correo", cargaTarea.getCorreo());
                        json_Usuario.put("clave", cargaTarea.getContraseña());
                        json_Usuario.put("estado", cargaTarea.isEstado());
                        json_Usuario.put("codigo_empleado", cargaTarea.getEmpleado().getCodigoEmpleado());
                        json_Usuario.put("nombre_completo", cargaTarea.getEmpleado().getNombreCompleto());

                    } else {
                        json_Usuario.put("resultado", "error_eliminar");
                    }
                } catch (SQLException e) {
                    json_Usuario.put("resultado", "error_sql");
                    json_Usuario.put("exception", e.getMessage());
                }
                array_Usuario.put(json_Usuario);
                response.getWriter().write(array_Usuario.toString());
                break;
            }
            case "editar": {
                String resultado_editar = "";

                try {
                    // Captura de parámetros
                    usuario.setCodigoUsuario(Integer.parseInt(request.getParameter("codigo_usuario")));
                    usuario.setCorreo(request.getParameter("correo"));

                    // Encriptación de contraseña
                    String clave = request.getParameter("clave");
                    String claveEncriptada = Encriptar.getStringMessageDigest(clave, Encriptar.SHA256);
                    usuario.setContraseña(claveEncriptada);

                    // Conversión de estado (boolean)
                    usuario.setEstado(Boolean.parseBoolean(request.getParameter("estado")));

                    // Asignación de empleado
                    rolUsuario.setCodigoEmpleado(Integer.parseInt(request.getParameter("codigo_empleado")));
                    usuario.setEmpleado(rolUsuario);

                    // Ejecución de actualización
                    resultado_editar = usuarioDao.update(usuario);

                    // Construcción de respuesta JSON
                    if ("exito".equals(resultado_editar)) {
                        json_Usuario.put("resultado", "exito");
                        json_Usuario.put("cargo", usuario.getCorreo()); // Puedes cambiar a otro campo si lo deseas
                    } else if ("admin_error".equals(resultado_editar)) {
                        json_Usuario.put("resultado", "admin_error");
                        json_Usuario.put("resultado_insertar", resultado_editar);
                    } else {
                        json_Usuario.put("resultado", "error");
                        json_Usuario.put("resultado_insertar", resultado_editar);
                    }

                } catch (SQLException e) {
                    json_Usuario.put("resultado", "error_sql");
                    json_Usuario.put("error_mostrado", e.getMessage());
                    System.out.println("Error Code error:" + e.getErrorCode());
                    throw new RuntimeException();
                }

                array_Usuario.put(json_Usuario);
                response.getWriter().write(array_Usuario.toString());
                break;
            }
    

            case "ingresar": {
    Usuario ingresar = new Usuario();

    String user = request.getParameter("correo");
    String clavePlano = request.getParameter("clave");

    if (user == null || user.isEmpty() || clavePlano == null || clavePlano.isEmpty()) {
        json_Usuario.put("resultado", "error");
        json_Usuario.put("mensaje", "Correo y clave son obligatorios");
        array_Usuario.put(json_Usuario);
        response.getWriter().write(array_Usuario.toString());
        break;
    }

    user = user.trim().toLowerCase();
    String clave = Encriptar.getStringMessageDigest(clavePlano, Encriptar.SHA256);
    ingresar.setCorreo(user);
    ingresar.setContraseña(clave);

    try {
        // Verificar si está bloqueado
        if (BloqueoDao.estaBloqueado(user)) {
            long restanteMs = BloqueoDao.obtenerTiempoRestante(user);
            int minutos = (int) (restanteMs / 60000);
            int segundos = (int) ((restanteMs % 60000) / 1000);

            json_Usuario.put("resultado", "bloqueado");
            json_Usuario.put("mensaje", "⏳ Bloqueado. Intenta en " + minutos + "m " + segundos + "s.");
            array_Usuario.put(json_Usuario);
            response.getWriter().write(array_Usuario.toString());
            break;
        }

        // Buscar usuario
        ingresar = usuarioDao.buscarUsuario(ingresar);

        if (ingresar != null && ingresar.getEmpleado() != null) {
            // Éxito: reiniciar intentos y guardar sesión
            BloqueoDao.reiniciarIntentos(user);
            HttpSession session = request.getSession(true);
            session.setAttribute("codigo_usuario", String.valueOf(ingresar.getCodigoUsuario()));
            session.setAttribute("Usuario", ingresar);

            json_Usuario.put("resultado", "exito");
        } else {
            // Fallo: registrar intento
            BloqueoDao.registrarIntentoFallido(user);
            String estado = BloqueoDao.obtenerEstadoBloqueo(user);

            json_Usuario.put("resultado", "error");
            json_Usuario.put("mensaje", "Usuario o clave incorrecta. " + estado);
        }
    } catch (Exception e) {
        e.printStackTrace();
        json_Usuario.put("resultado", "error");
        json_Usuario.put("mensaje", "Error interno: " + e.getMessage());
    }

    array_Usuario.put(json_Usuario);
    response.getWriter().write(array_Usuario.toString());
    break;
}
            case "salir": {
                try {
                    // Obtén la sesión
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        // Elimina el atributo "Usuario" de la sesión
                        session.removeAttribute("Usuario");

                        // Invalida la sesión
                        session.invalidate();
                    }

                    // Retorna un mensaje de éxito
                    json_Usuario.put("resultado", "exito");
                    array_Usuario.put(json_Usuario);
                } catch (Exception e) {
                    json_Usuario.put("resultado", "error_sql");
                    json_Usuario.put("exception", e.getMessage());
                }
                array_Usuario.put(json_Usuario);
                response.getWriter().write(array_Usuario.toString());
                break;
            }

            case "recuperar": {
                try {
                    // 1. Captura el correo desde el formulario
                    String correo = request.getParameter("correo");

                    // 2. Validación: ¿el correo está vacío?
                    if (correo == null || correo.trim().isEmpty()) {
                        request.setAttribute("error", "Debes ingresar un correo.");
                        request.getRequestDispatcher("recuperar_contraseña.jsp").forward(request, response);
                        break;
                    }

                    correo = correo.trim();

                    // 3. Validación: ¿el correo existe en la base?
                    if (!UsuarioDao.existeCorreo(correo)) {
                        request.setAttribute("error", "El correo no está registrado.");
                        request.getRequestDispatcher("recuperar_contraseña.jsp").forward(request, response);
                        break;
                    }

                    // 4. Genera un código temporal de 6 dígitos
                    String codigoTemporal = UsuarioDao.generarCodigo();

                    // 5. Guarda el código en la base de datos
                    UsuarioDao.guardarCodigoRecuperacion(correo, codigoTemporal);

                    // 6. Envía el código por correo
                    CorreoUtil.enviarCodigoRecuperacion(correo, codigoTemporal);

                    // 7. Redirige al login con mensaje
                    request.setAttribute("mensaje", "Se ha enviado una contraseña temporal a tu correo.");
                    request.setAttribute("correo", correo); // para mantenerlo en la vista
                    request.getRequestDispatcher("validar_codigo.jsp").forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace(); // ✅ Muestra el error técnico en consola
                    request.setAttribute("error", "Error al enviar correo: " + e.getMessage());
                    request.getRequestDispatcher("recuperar_contraseña.jsp").forward(request, response);
                }
                break;
            }
            case "validarCodigo": {
                String correo = request.getParameter("correo").trim();
                String codigo = request.getParameter("codigo").trim();

                try {
                    if (UsuarioDao.validarCodigo(correo, codigo)) {
                        // Aquí se confirma que el código es válido
                        request.setAttribute("correo", correo);
                        request.getRequestDispatcher("cambiar_clave.jsp").forward(request, response);
                    } else {
                        //  Si el código no coincide
                        request.setAttribute("error", "El código ingresado es incorrecto.");
                        request.getRequestDispatcher("validar_codigo.jsp").forward(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error al validar el código: " + e.getMessage());
                    request.getRequestDispatcher("validar_codigo.jsp").forward(request, response);
                }
                break;
            }
case "actualizarClave": {
    String correo = request.getParameter("correo").trim();
    String nuevaClave = request.getParameter("nuevaClave").trim();
    String confirmarClave = request.getParameter("confirmarClave").trim();

    if (!nuevaClave.equals(confirmarClave)) {
        request.setAttribute("error", "Las contraseñas no coinciden.");
        request.setAttribute("correo", correo);
        request.getRequestDispatcher("cambiar_clave.jsp").forward(request, response);
        break;
    }

    try {
        if (UsuarioDao.actualizarClave(correo, nuevaClave)) {
            request.setAttribute("mensaje", "Contraseña actualizada correctamente.");
            request.getRequestDispatcher("acceso.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "No se pudo actualizar la contraseña.");
            request.setAttribute("correo", correo);
            request.getRequestDispatcher("cambiar_clave.jsp").forward(request, response);
        }
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "Error al actualizar la contraseña: " + e.getMessage());
        request.setAttribute("correo", correo);
        request.getRequestDispatcher("cambiar_clave.jsp").forward(request, response);
    }
    break;
}

           
            case "login": {
                String correo = request.getParameter("correo").trim();
                String clave = request.getParameter("clave").trim();

                try {
                    if (UsuarioDao.validarAcceso(correo, clave)) {
                        request.setAttribute("mensaje", "Bienvenido/a.");
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Correo o contraseña incorrectos.");
                        request.getRequestDispatcher("acceso.jsp").forward(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error al validar el acceso: " + e.getMessage());
                    request.getRequestDispatcher("acceso.jsp").forward(request, response);
                }
                break;
            }
            case "enviarCodigo": {
                String correo = request.getParameter("correo");

                if (correo == null || correo.trim().isEmpty()) {
                    request.setAttribute("error", "Debes ingresar un correo.");
                    request.getRequestDispatcher("recuperar_clave.jsp").forward(request, response);
                    break;
                }

                correo = correo.trim();

                if (!UsuarioDao.existeCorreo(correo)) {
                    request.setAttribute("error", "El correo no está registrado.");
                    request.getRequestDispatcher("recuperar_clave.jsp").forward(request, response);
                    break;
                }

                try {
                    String codigo = UsuarioDao.generarCodigo();
                    UsuarioDao.guardarCodigoRecuperacion(correo, codigo);

                    // Simulación de envío (puedes reemplazar con JavaMail)
                    System.out.println("Código enviado a " + correo + ": " + codigo);

                    request.setAttribute("mensaje", "Código enviado al correo registrado.");
                    request.setAttribute("correo", correo);
                    request.getRequestDispatcher("verificar_codigo.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error al enviar el código: " + e.getMessage());
                    request.getRequestDispatcher("recuperar_clave.jsp").forward(request, response);
                }

                break;
            }

            case "registrarEmpleado": {
                // Validación: ¿ya existe un empleado?
                if (UsuarioDao.contarEmpleados() > 0) {
                    request.setAttribute("error", "Ya existe un Administrador registrado. No se pueden agregar más.");
                    request.getRequestDispatcher("registrar_empleado.jsp").forward(request, response);
                    break;
                }

                String nombre = request.getParameter("nombre").trim();
                String fechaNacimientoStr = request.getParameter("fecha_nacimiento").trim();
                String telefono = request.getParameter("telefono").trim();

                // Validación de campos obligatorios
                if (nombre.isEmpty() || fechaNacimientoStr.isEmpty() || telefono.isEmpty()) {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    request.getRequestDispatcher("registrar_empleado.jsp").forward(request, response);
                    break;
                }

                // Validación: nombre solo letras (mayúsculas, minúsculas y espacios)
                if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                    request.setAttribute("error", "El nombre solo debe contener letras y espacios.");
                    request.getRequestDispatcher("registrar_empleado.jsp").forward(request, response);
                    break;
                }

                // Conversión de fecha
                Date fechaNacimiento = Date.valueOf(fechaNacimientoStr);
                LocalDate nacimiento = fechaNacimiento.toLocalDate();
                LocalDate hoy = LocalDate.now();
                Period edad = Period.between(nacimiento, hoy);

                // Validación de mayoría de edad
                if (edad.getYears() < 18) {
                    request.setAttribute("error", "El empleado debe ser mayor de edad (mínimo 18 años).");
                    request.getRequestDispatcher("registrar_empleado.jsp").forward(request, response);
                    break;
                }

                // Registro
                int codigoRol = UsuarioDao.obtenerOCrearRol("Administrador");
                int codigoEmpleado = UsuarioDao.crearEmpleadoBase(nombre, fechaNacimiento, telefono, codigoRol);

                if (codigoEmpleado > 0) {
                    request.setAttribute("mensaje", "Empleado registrado exitosamente.");
                    request.setAttribute("codigo_empleado", codigoEmpleado);
                    request.setAttribute("nombre_empleado", nombre);
                } else {
                    request.setAttribute("error", "Error al registrar el empleado.");
                }

                request.getRequestDispatcher("registrar_usuario.jsp").forward(request, response);
                break;
            }

            case "registrarUsuario": {
                // Validar que el formulario fue enviado explícitamente
                String enviado = request.getParameter("formulario_enviado");
                if (!"true".equals(enviado)) {
                    request.setAttribute("error", "Acceso inválido al registro de usuario.");
                    request.getRequestDispatcher("registrar_usuario.jsp").forward(request, response);
                    break;
                }

                // Captura y validación de campos
                String correo = request.getParameter("correo") != null ? request.getParameter("correo").trim() : "";
                String clave = request.getParameter("clave") != null ? request.getParameter("clave").trim() : "";
                String codigoEmpleadoStr = request.getParameter("codigo_empleado");

                if (correo.isEmpty() || clave.isEmpty() || codigoEmpleadoStr == null || codigoEmpleadoStr.trim().isEmpty()) {
                    manejarError(request, response, "Todos los campos son obligatorios.", codigoEmpleadoStr);
                    break;
                }

                // Validación de contraseña segura: 8-12 caracteres, al menos un carácter especial
                if (clave.length() < 8 || clave.length() > 12 || !clave.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/\\\\|].*")) {
                    manejarError(request, response, "La contraseña debe tener entre 8 y 12 caracteres y al menos un carácter especial.", codigoEmpleadoStr);
                    break;
                }

                int codigoEmpleado = Integer.parseInt(codigoEmpleadoStr);

                // Validaciones de negocio
                if (UsuarioDao.existeCorreo(correo)) {
                    manejarError(request, response, "El correo ya está registrado.", codigoEmpleado);
                    break;
                }

                if (UsuarioDao.empleadoYaTieneUsuario(codigoEmpleado)) {
                    manejarError(request, response, "Este empleado ya tiene un usuario registrado.", codigoEmpleado);
                    break;
                }

                // Registro
                String claveHash = Util.encriptarSHA256(clave);
                boolean registrado = UsuarioDao.registrarUsuario(correo, claveHash, codigoEmpleado);

                if (registrado) {
                    request.setAttribute("mensaje", "Usuario registrado exitosamente.");
                    request.setAttribute("codigo_empleado", codigoEmpleado);
                    request.setAttribute("nombre_empleado", UsuarioDao.obtenerNombrePorCodigo(codigoEmpleado));
                    request.getRequestDispatcher("acceso.jsp").forward(request, response);
                } else {
                    manejarError(request, response, "Error al registrar el usuario.", codigoEmpleado);
                }

                break;
            }
            case "mostrarFormularioRegistro": {
                String codigoEmpleadoStr = request.getParameter("codigo_empleado");

                if (codigoEmpleadoStr != null && !codigoEmpleadoStr.isEmpty()) {
                    try {
                        int codigoEmpleado = Integer.parseInt(codigoEmpleadoStr);
                        String nombreEmpleado = UsuarioDao.obtenerNombrePorCodigo(codigoEmpleado);

                        request.setAttribute("codigo_empleado", codigoEmpleado);
                        request.setAttribute("nombre_empleado", nombreEmpleado);
                    } catch (NumberFormatException e) {
                        request.setAttribute("codigo_empleado", "");
                        request.setAttribute("nombre_empleado", "");
                    }
                }

                request.getRequestDispatcher("registrar_usuario.jsp").forward(request, response);
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

    private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensajeError, Object codigoEmpleadoObj)
            throws ServletException, IOException {

        request.setAttribute("error", mensajeError);
        request.setAttribute("codigo_empleado", codigoEmpleadoObj);

        try {
            int codigoEmpleado = Integer.parseInt(codigoEmpleadoObj.toString());
            String nombreEmpleado = UsuarioDao.obtenerNombrePorCodigo(codigoEmpleado);
            request.setAttribute("nombre_empleado", nombreEmpleado);
        } catch (NumberFormatException e) {
            request.setAttribute("nombre_empleado", "");
        }

        request.getRequestDispatcher("registrar_usuario.jsp").forward(request, response);
    }
}
