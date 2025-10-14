/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
$(function () {
    cargarTabla();

    $(document).on("submit", "#formulario_insert", function (e) {
        e.preventDefault();

        var nombre_proveedor = $("#nombre_proveedor").val();
        var correo = $("#correo").val();
        var direccion = $("#direccion").val();

        var numero_telefono = $("#numero_telefono").val();
        var numero_telefono1 = $("#numero_telefono1").val();
        var estado = $("#estado").val(); // Captura el valor del campo oculto

        var datos = {
            consultar_datos: "insertar",
            nombre_proveedor: nombre_proveedor,
            correo: correo,
            direccion: direccion,
            numero_telefono: numero_telefono,
            numero_telefono1: numero_telefono1,
            estado: estado //  Incluye estado en el envío
        };

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "ProveedorController",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#nombre_proveedor").val("");
                $("#correo").val("");
                $("#direccion").val("");

                $("#numero_telefono").val("");
                $("#numero_telefono1").val("");
                $("#estado").val("true");
                $("#myModal").modal("hide");

                cargarTabla();
                mensaje("Excelente", "El Proveedor fue agregado", "success");
            } else {
                Swal.fire({
                    title: 'Accion no completada',
                    text: 'Dato de proveedor Existente',
                    icon: 'error',
                    confirmButtonText: 'Entendido'
                });
            }
        });
    });


    $(document).on("submit", "#formulario_editar", function (e) {
        e.preventDefault();

        const datos = {
            consultar_datos: "editar",
            codigo_proveedor: $("#codigo_proveedor").val(),
            correo: $("#correoE").val(),
            nombre_proveedor: $("#nombre_proveedorE").val(),
            direccion: $("#direccionE").val(),
            numero_telefono: $("#numero_telefonoE").val(),
            numero_telefono1: $("#numero_telefono1E").val(),
            estado: $('input[name="estadoE"]:checked').val()
        };
        console.log(datos);
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "ProveedorController",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#formulario_editar")[0].reset();
                $("#myModalE").modal("hide");
                cargarTabla();
                mensaje("Excelente", "El Proveedor fue modificado", "success");
            } else {
                Swal.fire('Accion no completada', "No se puede modificar el proveedor", "error");
            }
        }).fail(function () {
            Swal.fire('Error de conexión', "No se pudo contactar al servidor", "error");
        });
    });

    $(document).on("click", ".btn_editar", function (e) {
        e.preventDefault();
        cargarDatosModal($(this).attr('data-id'));
    });
});






function cargarDatosModal(codigo_proveedor) {
    var datos = {
        consultar_datos: "cargarDatos",
        codigo_proveedor: codigo_proveedor
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProveedorController",
        data: datos
    }).done(function (json) {
        // Manejo flexible: si es array, usa json[0]; si es objeto, usa json directamente
        const proveedor = Array.isArray(json) ? json[0] : json;

        if (proveedor && proveedor.resultado === "exito") {
            $('#codigo_proveedor').val(proveedor.codigo_proveedor);
            $('#nombre_proveedorE').val(proveedor.nombre_proveedor);
            $('#correoE').val(proveedor.correo);
            $('#direccionE').val(proveedor.direccion);
            $('#numero_telefonoE').val(proveedor.numero_telefono);
            $('#numero_telefono1E').val(proveedor.numero_telefono1);
            // ✅ Marcar el radio button según el estado
            const estado = proveedor.estado;
            if (estado === true || estado === "true" || estado === "ACTIVO") {
                $('#estado_activo').prop('checked', true);
            } else {
                $('#estado_inactivo').prop('checked', true);
            }

            $('#myModalE').modal('show');
        } else {
            Swal.fire(
                    'Error',
                    'No existe el Identificador en la Base Datos de la Tabla',
                    'error'
                    );
            console.warn("Respuesta inesperada:", json);
        }
    }).fail(function () {
        Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
    });
}


function cargarTabla(estado = 1) {
    var datos = {"consultar_datos": "mostrar", "estado": estado};

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProveedorController",
        data: datos
    }).done(function (json) {
        if (json[0].resultado == "exito") {
            $("#proveedorA").empty().html(json[0].tabla);
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

function mensaje(titulo, mensaje, icono) {
    Swal.fire({
        title: titulo,
        text: mensaje,
        icon: icono,
        timer: 1000
    });
}
