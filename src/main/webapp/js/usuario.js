/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
$(function () {
    cargarTabla();
    cargarCombo({consultar_datos: "cargarCombo", codigo_empleado: "0"});

    // 🧩 Conteo dinámico de caracteres en la contraseña
    $("#clave").on("input", function () {
        const longitud = $(this).val().length;
        $("#contadorClave").text(longitud + " caracteres");
    });

    $(document).on("submit", "#formulario_insert", function (e) {
        e.preventDefault();

        // 🧩 Captura de datos
        var correo = $("#correo").val().trim();
        var clave = $("#clave").val().trim();
        var estado = $("#estado").val();
        var codigo_empleado = $("#codigo_empleado").val();

        // 🧩 Validación de campos obligatorios
        if (!correo || !clave || !codigo_empleado) {
            mensaje("Campos incompletos", "Por favor llena todos los campos obligatorios", "warning");
            return;
        }

        // 🧩 Validación: no debe contener espacios
        if (/\s/.test(clave)) {
            mensaje("Contraseña inválida", "No debe contener espacios", "warning");
            return;
        }

        // 🧩 Validación de longitud de contraseña
        if (clave.length < 8) {
            mensaje("Contraseña muy corta", "Debe tener al menos 8 caracteres", "warning");
            return;
        }

        if (clave.length > 12) {
            mensaje("Contraseña muy larga", "La contraseña no debe exceder los 12 caracteres", "warning");
            return;
        }

        // 🧩 Validación de carácter especial
        const tieneCaracterEspecial = /[!@#$%^&*(),.?":{}|<>_\-+=~`[\]\\;/]/.test(clave);
        if (!tieneCaracterEspecial) {
            mensaje("Contraseña insegura", "Debe contener al menos un carácter especial", "warning");
            return;
        }

        // 🧩 Envío AJAX
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "UsuarioController",
            data: {
                consultar_datos: "insertar",
                correo: correo,
                clave: clave,
                estado: estado,
                codigo_empleado: codigo_empleado
            }
        }).done(function (json) {
            console.log("JSON recibido:", json);
            const resultado = Array.isArray(json) ? json[0]?.resultado : json.resultado;

            // 🧩 Manejo de respuesta
            switch (resultado) {
                case "exito":
                    $("#correo").val("");
                    $("#clave").val("");
                    $("#estado").val("true");
                    $("#codigo_empleado").val("0");
                    $("#myModal").modal("hide");
                    cargarTabla();
                    cargarCombo({consultar_datos: "cargarCombo", codigo_empleado: "0"});
                    mensaje("Excelente", "Usuario fue agregado correctamente", "success");
                    break;

                case "error_correo_duplicado":
                    mensaje("Correo duplicado", "Ya existe un usuario con ese correo", "warning");
                    break;

                case "error_pass":
                    mensaje("Contraseña inválida", "Debe tener entre 8 y 12 caracteres", "warning");
                    break;

                case "error_clave_vacia":
                    mensaje("Clave vacía", "Debes ingresar una contraseña", "warning");
                    break;

                case "error_correo_vacio":
                    mensaje("Correo vacío", "Debes ingresar un correo válido", "warning");
                    break;

                case "error_insertar":
                    mensaje("Error", "No se pudo registrar el usuario", "error");
                    break;

                case "error_sql":
                    mensaje("Error SQL", json[0]?.error_mostrado || "Error en la base de datos", "error");
                    break;

                case "error_general":
                    mensaje("Error inesperado", json[0]?.error_mostrado || "Ocurrió un error desconocido", "error");
                    break;

                default:
                    mensaje("Error", "Respuesta no reconocida del servidor", "error");
            }
        }).fail(function () {
            mensaje("Error de conexión", "No se pudo contactar al servidor", "error");
        });
    });
});
//$(document).on("submit", "#formulario_editar", function (e) {
//    e.preventDefault();
//    var codigo_categoria = $("#codigo_usuario").val();
//    var nombre = $("#categoriaE").val();
//
//    $.ajax({
//        dataType: "json",
//        method: "POST",
//        url: "CategoriaController",
//        data: {"codigo_categoria": codigo_categoria, "nombre": nombre, "consultar_datos": "editar"}
//    }).done(function (json) {
//        if (json[0].resultado === "exito") {
//            $("#codigo_categoria").val("");
//            $("#categoriaE").val("");
//            $("#myModalE").modal("hide");
//            cargarTabla();
//            mensaje("Excelente", "Categoría modificado", "success");
//        } else {
//            Swal.fire('Accion no completada', "No se puede modificar la categoria", "Error");
//        }
//    }).fail(function () {
//    }).always(function () {
//    });
//});
 $(document).on("submit", "#formulario_editar", function (e) {
        e.preventDefault();
        var codigo_usuario = $("#codigo_usuario").val();
        var correo = $("#correoE").val();
        
        var clave = $("#claveE").val();
           var   estado=$('input[name="estadoE"]:checked').val();
        var codigo_empleado = $("#codigo_empleadoE").val();
        var datos = {"consultar_datos": "editar", "codigo_usuario": codigo_usuario,  "correo": correo, "clave": clave, "estado": estado,
            "codigo_empleado": codigo_empleado};
        console.log(datos);
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "UsuarioController",
            data: datos
        }).done(function (json) {
            console.log("Datos del editar: ", datos);
            if (json[0].resultado === "exito") {
                $("#correoE").val("");
          
                $("#claveE").val("");
                $("#estadoE").val("");
                $("#codigo_empeladoE").val("0");

                $("#myModalE").modal("hide");
                cargarTabla();

                mensaje("Excelente", "El dato fue modificado", "success");
            } else if (json[0].resultado === "admin_error") {
                mensaje("Error", "El administrador es el último activo", "Error");
            } else if (json[0].resultado === "error_user") {
                mensaje('Accion no completada', "El Usuario debe tener entre 3 y 20 caracteres", "Error");
            } else {
                Swal.fire('Accion no completada', "No se puede modificar los dato de usuario", "Error");
            }
        }).fail(function () {
        }).always(function () {
        });
    });
$(document).on("click", ".btn_editar", function (e) {
    e.preventDefault();
    cargarDatosModal($(this).attr('data-id'));
});




function cargarTabla(estado = 1) {
    var datos = {"consultar_datos": "mostrar", "estado": estado};

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "UsuarioController",
        data: datos
    }).done(function (json) {
        console.log("Respuesta JSON:", json);
        if (json[0].resultado == "exito") {
            $("#categoriaA").empty().html(json[0].tabla);



            $("#tabla_listCategoria").DataTable({
                "language": {
                    "url": "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
                },
                lengthMenu: [[5, 10, 15, -1], [5, 10, 15, "Todos"]],
                pageLength: 5,
                columnDefs: [
                    {responsivePriority: 1, targets: 0},
                    {responsivePriority: 2, targets: -1}
                ]
            });
        } else {
            Swal.fire('Accion no completada', 'No pudo obtener los datos', 'error');
        }
    }).fail(function () {
    }).always(function () {
    });
}



function cargarDatosModal(codigo_usuario) {
    const datos = {
        consultar_datos: "cargarDatos",
        codigo_usuario: codigo_usuario
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "UsuarioController",
        data: datos
    }).done(function (json) {
        const usuario = Array.isArray(json) ? json[0] : json;

        if (usuario?.resultado === "exito") {
            $("#codigo_usuario").val(usuario.codigo_usuario || "");
            $("#correoE").val(usuario.correo || "");
            $("#claveE").val(usuario.clave || "");

            const estado = String(usuario.estado).toUpperCase();
            const activo = estado === "TRUE" || estado === "ACTIVO";
            $("#estado_activo").prop("checked", activo);
            $("#estado_inactivo").prop("checked", !activo);
            //$("#codigo_empleadoE").val(usuario.codigo_empleado || "");
            // ✅ Corrección aquí
            var datosEmpleado = {
                consultar_datos: "cargarCombo",
                codigo_empleado: json[0].codigo_empleado
            };
            cargarCombo(datosEmpleado);

            $("#myModalE").modal("show");
        } else {
            Swal.fire("Error", "No existe el identificador en la base de datos", "error");
            console.warn("Respuesta inesperada:", json);
        }
    }).fail(function () {
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    });
}
function mensaje(titulo, mensaje, icono) {
    Swal.fire({
        title: titulo,
        text: mensaje,
        icon: icono,
        timer: 2000,

    });
}


cargarCombo({consultar_datos: "cargarCombo", codigo_empleado: "0"});



function validarLongitud(campo, nombre, min, max) {
    if (campo.length < min) {
        mensaje(`${nombre} muy corto`, `${nombre} debe tener al menos ${min} caracteres`, "warning");
        return false;
    }
    if (campo.length > max) {
        mensaje(`${nombre} muy largo`, `${nombre} no debe exceder los ${max} caracteres`, "warning");
        return false;
    }
    return true;
}
const estadoTexto = usuario.estado === true ? "Activo" : "Inactivo";

// 🧩 Conteo dinámico de caracteres en la contraseña
$("#clave").on("input", function () {
    const longitud = $(this).val().length;
    $("#contadorClave").text(longitud + " caracteres");
});



function cargarCombo(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "UsuarioController",
        data: datos
    }).done(function (json) {
        const valor = datos.codigo_empleado?.toString().trim();

        if (datos.codigo_empleado !== "0") {
            const $combo = $("#codigo_empleadoE");
            $combo.empty().append('<option disabled selected value="0">Seleccione un empleado</option>');

            if (json[0].resultado === "exito") {
                $combo.append(json[0].empleado);

                // 🔍 Verifica que las opciones tengan el value correcto
                const opciones = $combo.find("option").map(function () {
                    return $(this).val()?.trim();
                }).get();

                console.log("📦 Opciones disponibles:", opciones);
                console.log("🎯 Valor a seleccionar:", valor);

                // ✅ Espera a que el DOM reconozca las opciones
                requestAnimationFrame(() => {
                    const existe = opciones.includes(valor);
                    if (existe) {
                        $combo.val(valor).trigger('change');
                        console.log("✅ Empleado seleccionado:", valor);
                    } else {
                        console.warn("⚠️ El valor '" + valor + "' no está en las opciones.");
                    }
                });
            } else {
                Swal.fire('Acción no completada', 'No pudo obtener los datos', 'error');
            }
        } else {
            const $combo = $("#codigo_empleado");
            $combo.empty().append('<option disabled selected value="0">Seleccione un empleado</option>');

            if (json[0].resultado === "exito") {
                $combo.append(json[0].empleado);
            } else {
                Swal.fire('Acción no completada', 'No pudo obtener los datos', 'error');
            }
        }
    });
}



