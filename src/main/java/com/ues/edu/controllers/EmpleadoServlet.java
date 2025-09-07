package com.ues.edu.controllers;

import com.ues.edu.models.Empleado;
import com.ues.edu.models.Roles;
import com.ues.edu.models.dao.EmpleadoDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "EmpleadoServlet", urlPatterns = {"/EmpleadoServlet"})
public class EmpleadoServlet extends HttpServlet {

    private ArrayList<Empleado> lista;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONArray arrayResponse = new JSONArray();
        JSONObject jsonResponse = new JSONObject();

        try {
            String filtro = request.getParameter("consultar_datos");
            System.out.println("=== SERVLET EMPLEADO INICIADO ===");
            System.out.println("PARÁMETRO consultar_datos: " + filtro);

            // Debug: imprimir todos los parámetros
            java.util.Enumeration<String> params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String paramName = params.nextElement();
                System.out.println(paramName + ": " + request.getParameter(paramName));
            }

            if (filtro == null) {
                jsonResponse.put("resultado", "error");
                jsonResponse.put("mensaje", "Parámetro 'consultar_datos' es requerido");
                arrayResponse.put(jsonResponse);
                response.getWriter().write(arrayResponse.toString());
                return;
            }

            switch (filtro) {
                case "si_consulta":
                    procesarConsulta(request, response);
                    break;

                case "si_registro":
                    procesarRegistro(request, response);
                    break;

                case "si_actualizalo":
                    procesarActualizacion(request, response);
                    break;

                case "obtener_uno":
                    procesarObtenerUno(request, response);
                    break;

                default:
                    jsonResponse.put("resultado", "error");
                    jsonResponse.put("mensaje", "Parámetro no válido: " + filtro);
                    arrayResponse.put(jsonResponse);
                    response.getWriter().write(arrayResponse.toString());
                    break;
            }

        } catch (Exception e) {
            System.err.println("ERROR NO MANEJADO EN SERVLET: " + e.getMessage());
            e.printStackTrace();
            
            jsonResponse.put("resultado", "error");
            jsonResponse.put("mensaje", "Error interno del servidor: " + e.getMessage());
            arrayResponse.put(jsonResponse);
            response.getWriter().write(arrayResponse.toString());
        }
    }

    // 🔹 MÉTODO PARA CONSULTA GENERAL
    private void procesarConsulta(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        System.out.println("=== PROCESANDO CONSULTA ===");
        JSONArray array_mostrar = new JSONArray();
        JSONObject json_mostrar = new JSONObject();

        String el_estado = request.getParameter("estado");
        if (el_estado == null) {
            el_estado = "1";
        }

        try {
            EmpleadoDao dao = new EmpleadoDao();
            this.lista = dao.mostrar(Integer.valueOf(el_estado), "todos");

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            String html = construirHTMLTabla(formatoFecha);

            json_mostrar.put("resultado", "exito");
            json_mostrar.put("tabla", html);
            json_mostrar.put("cuantos", this.lista.size());

        } catch (SQLException e) {
            System.err.println("ERROR SQL en consulta: " + e.getMessage());
            json_mostrar.put("resultado", "error_sql");
            json_mostrar.put("mensaje", e.getMessage());
            json_mostrar.put("code_error", e.getErrorCode());
        }

        array_mostrar.put(json_mostrar);
        response.getWriter().write(array_mostrar.toString());
        System.out.println("=== CONSULTA COMPLETADA ===");
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
        throws IOException, SQLException {

    System.out.println("=== INICIANDO REGISTRO DE EMPLEADO ===");
    JSONArray array_empleado = new JSONArray();
    JSONObject json_empleado = new JSONObject();
    try {
        Empleado empleado = new Empleado();
        EmpleadoDao dao = new EmpleadoDao();

        // Obtener parámetros
        String nombreCompleto = request.getParameter("nombre_completo");
        String fechaString = request.getParameter("fecha_nacimiento");
        String numeroTelefono = request.getParameter("numero_telefono");
        String codigoRolStr = request.getParameter("codigo_roles");

        System.out.println("Datos recibidos:");
        System.out.println("Nombre: " + nombreCompleto);
        System.out.println("Fecha: " + fechaString);
        System.out.println("Teléfono: " + numeroTelefono);
        System.out.println("Rol: " + codigoRolStr);

        // Validar nombre
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            json_empleado.put("resultado", "error");
            json_empleado.put("mensaje", "El nombre es obligatorio");
            array_empleado.put(json_empleado);
            response.getWriter().write(array_empleado.toString());
            return;
        }

        empleado.setNombreCompleto(nombreCompleto);
        empleado.setNumeroTelefono(numeroTelefono != null ? numeroTelefono : "");

        // Validar formato de teléfono (####-####)
        if (numeroTelefono != null && !numeroTelefono.isEmpty()) {
            if (!numeroTelefono.matches("^\\d{4}-\\d{4}$")) {
                json_empleado.put("resultado", "error");
                json_empleado.put("mensaje", "El formato del teléfono debe ser ####-####");
                array_empleado.put(json_empleado);
                response.getWriter().write(array_empleado.toString());
                return;
            }
        }

        // Manejar fecha de nacimiento
        java.sql.Date fechaNacimiento = null;
        if (fechaString != null && !fechaString.isEmpty()) {
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(fechaString);
                fechaNacimiento = new java.sql.Date(utilDate.getTime());

                // Validar edad con LocalDate
                LocalDate nacimiento = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate hoy = LocalDate.now();
                int edad = Period.between(nacimiento, hoy).getYears();

                if (edad < 17) {
                    json_empleado.put("resultado", "error");
                    json_empleado.put("mensaje", "El empleado debe tener al menos 18 años");
                    array_empleado.put(json_empleado);
                    response.getWriter().write(array_empleado.toString());
                    return;
                }

                if (edad > 65) {
                    json_empleado.put("resultado", "error");
                    json_empleado.put("mensaje", "El empleado no puede tener más de 65 años");
                    array_empleado.put(json_empleado);
                    response.getWriter().write(array_empleado.toString());
                    return;
                }

            } catch (ParseException e) {
                System.err.println("Error parseando fecha: " + e.getMessage());
                json_empleado.put("resultado", "error");
                json_empleado.put("mensaje", "Formato de fecha incorrecto");
                array_empleado.put(json_empleado);
                response.getWriter().write(array_empleado.toString());
                return;
            }
        } else {
            json_empleado.put("resultado", "error");
            json_empleado.put("mensaje", "La fecha de nacimiento es obligatoria");
            array_empleado.put(json_empleado);
            response.getWriter().write(array_empleado.toString());
            return;
        }
        empleado.setFechaNacimiento(fechaNacimiento);

        // Manejar rol
        if (codigoRolStr != null && !codigoRolStr.isEmpty()) {
            try {
                int codigoRol = Integer.parseInt(codigoRolStr);
                Roles rol = new Roles();
                rol.setCodigoRoles(codigoRol);
                empleado.setRol(rol);
            } catch (NumberFormatException e) {
                json_empleado.put("resultado", "error");
                json_empleado.put("mensaje", "Formato inválido para el código de rol");
                array_empleado.put(json_empleado);
                response.getWriter().write(array_empleado.toString());
                return;
            }
        } else {
            json_empleado.put("resultado", "error");
            json_empleado.put("mensaje", "Debe seleccionar un rol");
            array_empleado.put(json_empleado);
            response.getWriter().write(array_empleado.toString());
            return;
        }

        // Insertar empleado
        System.out.println("Insertando empleado en BD...");
        String resultado_insert = dao.insertEmp(empleado);
        System.out.println("Resultado del INSERT: " + resultado_insert);

        if ("exito".equals(resultado_insert)) {
            json_empleado.put("resultado", "exito");
            json_empleado.put("mensaje", "Empleado registrado exitosamente");
            json_empleado.put("nombre_completo", empleado.getNombreCompleto());
        } else {
            json_empleado.put("resultado", "error");
            json_empleado.put("mensaje", resultado_insert);
        }

    } catch (SQLException e) {
        System.err.println("ERROR SQL en registro: " + e.getMessage());
        e.printStackTrace();
        json_empleado.put("resultado", "error_sql");
        json_empleado.put("mensaje", "Error de base de datos: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("ERROR inesperado en registro: " + e.getMessage());
        e.printStackTrace();
        json_empleado.put("resultado", "error");
        json_empleado.put("mensaje", "Error inesperado: " + e.getMessage());
    }

    System.out.println("Respuesta JSON: " + json_empleado.toString());
    array_empleado.put(json_empleado);
    response.getWriter().write(array_empleado.toString());
    System.out.println("=== REGISTRO COMPLETADO ===");
}

    // 🔹 MÉTODO PARA ACTUALIZACIÓN
    private void procesarActualizacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== INICIANDO ACTUALIZACIÓN ===");
        JSONArray array_actualizar = new JSONArray();
        JSONObject json_actualizar = new JSONObject();

        try {
            Empleado em = new Empleado();
            EmpleadoDao daoUpdate = new EmpleadoDao();

            // Obtener parámetros
            String codigoEmpleadoStr = request.getParameter("codigo_empleadoE");
            String nombreCompletoUpd = request.getParameter("nombre_completoE");
            String fechaStringUpd = request.getParameter("fecha_nacimientoE");
            String numeroTelefonoUpd = request.getParameter("numero_telefonoE");
            String codigoRolUpdStr = request.getParameter("codigo_rolesE");

            System.out.println("Datos para actualizar:");
            System.out.println("ID: " + codigoEmpleadoStr);
            System.out.println("Nombre: " + nombreCompletoUpd);
            System.out.println("Fecha: " + fechaStringUpd);
            System.out.println("Teléfono: " + numeroTelefonoUpd);
            System.out.println("Rol: " + codigoRolUpdStr);

            // Validar ID empleado
            if (codigoEmpleadoStr == null || codigoEmpleadoStr.isEmpty()) {
                json_actualizar.put("resultado", "error");
                json_actualizar.put("mensaje", "El código del empleado es obligatorio");
                array_actualizar.put(json_actualizar);
                response.getWriter().write(array_actualizar.toString());
                return;
            }
            em.setCodigoEmpleado(Integer.parseInt(codigoEmpleadoStr));

            // Validar nombre
            if (nombreCompletoUpd == null || nombreCompletoUpd.isEmpty()) {
                json_actualizar.put("resultado", "error");
                json_actualizar.put("mensaje", "El nombre es obligatorio");
                array_actualizar.put(json_actualizar);
                response.getWriter().write(array_actualizar.toString());
                return;
            }
            em.setNombreCompleto(nombreCompletoUpd);

            // Validar teléfono
            em.setNumeroTelefono(numeroTelefonoUpd != null ? numeroTelefonoUpd : "");

            // Validar fecha de nacimiento
            if (fechaStringUpd != null && !fechaStringUpd.isEmpty()) {
                try {
                    java.util.Date utilDateUpd = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStringUpd);
                    java.sql.Date fechaNacimientoUpd = new java.sql.Date(utilDateUpd.getTime());
                    em.setFechaNacimiento(fechaNacimientoUpd);
                } catch (ParseException e) {
                    json_actualizar.put("resultado", "error");
                    json_actualizar.put("mensaje", "Formato de fecha incorrecto");
                    array_actualizar.put(json_actualizar);
                    response.getWriter().write(array_actualizar.toString());
                    return;
                }
            } else {
                json_actualizar.put("resultado", "error");
                json_actualizar.put("mensaje", "La fecha de nacimiento es obligatoria");
                array_actualizar.put(json_actualizar);
                response.getWriter().write(array_actualizar.toString());
                return;
            }

            // Validar rol
            if (codigoRolUpdStr != null && !codigoRolUpdStr.isEmpty()) {
                Roles rolUpd = new Roles();
                rolUpd.setCodigoRoles(Integer.parseInt(codigoRolUpdStr));
                em.setRol(rolUpd);
            } else {
                json_actualizar.put("resultado", "error");
                json_actualizar.put("mensaje", "Debe seleccionar un rol");
                array_actualizar.put(json_actualizar);
                response.getWriter().write(array_actualizar.toString());
                return;
            }

            // Actualizar empleado
            String result_actualizar = daoUpdate.updateEmpleado(em);

            if ("exito".equals(result_actualizar)) {
                json_actualizar.put("resultado", "exito");
                json_actualizar.put("mensaje", "Empleado actualizado correctamente");
            } else {
                json_actualizar.put("resultado", "error");
                json_actualizar.put("mensaje", result_actualizar);
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL en actualización: " + e.getMessage());
            json_actualizar.put("resultado", "error_sql");
            json_actualizar.put("mensaje", e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR inesperado en actualización: " + e.getMessage());
            json_actualizar.put("resultado", "error");
            json_actualizar.put("mensaje", "Error inesperado: " + e.getMessage());
        }

        array_actualizar.put(json_actualizar);
        response.getWriter().write(array_actualizar.toString());
        System.out.println("=== ACTUALIZACIÓN COMPLETADA ===");
    }

    // 🔹 MÉTODO PARA OBTENER UN EMPLEADO
    private void procesarObtenerUno(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== OBTENIENDO UN EMPLEADO ===");
        JSONArray array_uno = new JSONArray();
        JSONObject json_uno = new JSONObject();

        try {
            String codigoEmpleadoStr = request.getParameter("codigo_empleado");

            if (codigoEmpleadoStr == null || codigoEmpleadoStr.isEmpty()) {
                json_uno.put("resultado", "error");
                json_uno.put("mensaje", "El código del empleado es obligatorio");
                array_uno.put(json_uno);
                response.getWriter().write(array_uno.toString());
                return;
            }

            int codigoEmpleado = Integer.parseInt(codigoEmpleadoStr);
            EmpleadoDao daoEmp = new EmpleadoDao();
            
            // 🔥 IMPORTANTE: Este método debe existir en tu EmpleadoDao
            Empleado emp = daoEmp.obtenerEmpleadoPorId(codigoEmpleado);

            if (emp != null) {
                json_uno.put("resultado", "exito");
                json_uno.put("codigo_empleadoE", emp.getCodigoEmpleado());
                json_uno.put("nombre_completoE", emp.getNombreCompleto());
                json_uno.put("numero_telefonoE", emp.getNumeroTelefono());
                json_uno.put("fecha_nacimientoE", emp.getFechaNacimiento() != null ? emp.getFechaNacimiento().toString() : "");
                json_uno.put("codigo_rolesE", emp.getRol() != null ? emp.getRol().getCodigoRoles() : "");
            } else {
                json_uno.put("resultado", "error");
                json_uno.put("mensaje", "Empleado no encontrado");
            }

        } catch (NumberFormatException e) {
            json_uno.put("resultado", "error");
            json_uno.put("mensaje", "Formato de código inválido");
        } catch (SQLException e) {
            System.err.println("ERROR SQL obteniendo empleado: " + e.getMessage());
            json_uno.put("resultado", "error_sql");
            json_uno.put("mensaje", e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR inesperado obteniendo empleado: " + e.getMessage());
            json_uno.put("resultado", "error");
            json_uno.put("mensaje", "Error al obtener empleado: " + e.getMessage());
        }

        array_uno.put(json_uno);
        response.getWriter().write(array_uno.toString());
        System.out.println("=== OBTENCIÓN COMPLETADA ===");
    }

    // 🔹 MÉTODO AUXILIAR PARA CONSTRUIR HTML
    private String construirHTMLTabla(SimpleDateFormat formatoFecha) {
        StringBuilder html = new StringBuilder();

        html.append("<table id='tabla_empleados' class='table-responsive'>")
            .append("<thead><tr class='mb-2 bg-green text-white text-center'>")
            .append("<th class='text-center' style='color: white !important;'>NOMBRE</th>")
            .append("<th class='text-center' style='color: white !important;'>FECHA NACIMIENTO</th>")
            .append("<th class='text-center' style='color: white !important;'>NUMERO TELEFONO</th>")
            .append("<th class='text-center' style='color: white !important;'>ROL</th>")
            .append("<th class='text-center' style='color: white !important;'>ACCIONES</th>")
            .append("</tr></thead><tbody>");

        for (Empleado empleado : this.lista) {
            html.append("<tr class='text-center'>")
                .append("<td class='text-center'>").append(empleado.getNombreCompleto()).append("</td>")
                .append("<td class='text-center'>")
                .append(empleado.getFechaNacimiento() != null ? formatoFecha.format(empleado.getFechaNacimiento()) : "Sin fecha")
                .append("</td>")
                .append("<td class='text-center'>").append(empleado.getNumeroTelefono()).append("</td>")
                .append("<td class='text-center'>").append(empleado.getRol() != null ? empleado.getRol().getNombreRol() : "").append("</td>")
                .append("<td><button type='button' class='btn btn-success btn_editar' data-id='")
                .append(empleado.getCodigoEmpleado())
                .append("'><i class='fas fa-edit'></i></button></td>")
                .append("</tr>");
        }

        html.append("</tbody></table>");
        return html.toString();
    }

    @Override
    public String getServletInfo() {
        return "Servlet para manejo de empleados";
    }
}