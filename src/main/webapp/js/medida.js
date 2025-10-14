/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(function () {
    cargarTabla(); //Es para llamar la funcion donde se esta mandando a llamar los id y parametros
   $(document).on("submit", "#formulario_insert", function (e) {
        e.preventDefault();

        var nombre = $("#nombre").val();
        var abreviacion = $("#abreviacion").val();

        var datos = {
            consultar_datos: "insertar",
            nombre: nombre,
            abreviacion: abreviacion
        };

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "MedidaController",
            data: datos
        }).done(function (json) {
            console.log("JSON recibido:", json);

            if (json[0].resultado === "exito") {
                $("#nombre").val("");
                $("#abreviacion").val("");
                $("#myModal").modal("hide");
                cargarTabla();
                console.log("Insert exitoso");

                mensaje("Excelente", "La medida fue agregada correctamente", "success");

            } else if (json[0].resultado === "duplicado") {
                Swal.fire({
                    title: "Medida duplicada",
                    text: json[0].mensaje || "Ya existe una medida con ese nombre o abreviación.",
                    icon: "warning",
                    timer: 5000,
                    showConfirmButton: false
                });

                $("#nombre").addClass("is-invalid");
                $("#abreviacion").addClass("is-invalid");
                $("#nombre")[0].setCustomValidity("Nombre duplicado.");
                $("#abreviacion")[0].setCustomValidity("Abreviación duplicada.");

            } else {
                Swal.fire({
                    title: "Acción no completada",
                    text: "No se pudo registrar la nueva medida.",
                    icon: "error",
                    timer: 5000,
                    showConfirmButton: false
                });
            }
        }).fail(function () {
            Swal.fire({
                title: "Error de conexión",
                text: "No se pudo contactar al servidor.",
                icon: "error",
                timer: 5000,
                showConfirmButton: false
            });
        }).always(function () {
            $("#nombre, #abreviacion").on("input", function () {
                this.classList.remove("is-invalid");
                this.setCustomValidity("");
            });
        });
    });

$(document).on("click", ".btn_editar", function (e) {
    e.preventDefault();
    cargarDatosModal($(this).attr('data-id'));
});

$(document).on("submit", "#formulario_editar", function (e) {
    e.preventDefault();

    var id_medida = $("#id_medida").val();
    var nombre_medida = $("#categoriaE").val();
    var abreviacion = $("#categoriaA").val(); // ⚠️ ¿Usas el mismo campo para ambos?

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "MedidaController",
        data: {
            "id_medida": id_medida,
            "nombre_medida": nombre_medida,
            "abreviacion": abreviacion,
            "consultar_datos": "editar"
        }
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#id_medida").val("");
            $("#nombre_medida").val("");
            $("#abreviacion").val("");
            $("#myModalE").modal("hide");
            cargarTabla();
            mensaje("Excelente", "La medida fue modificada correctamente", "success");

        } else if (json[0].resultado === "duplicado") {
            Swal.fire({
                title: "Medida duplicada",
                text: json[0].mensaje || "Ya existe otra medida con ese nombre o abreviación.",
                icon: "warning",
                timer: 5000,
                showConfirmButton: false
            });

            $("#categoriaE").addClass("is-invalid");
            $("#categoriaA")[0].setCustomValidity("Nombre o abreviación duplicada.");
        } else {
            Swal.fire({
                title: "Acción no completada",
                text: "No se pudo modificar la medida.",
                icon: "error",
                timer: 5000,
                showConfirmButton: false
            });
        }
    }).fail(function () {
        Swal.fire({
            title: "Error de conexión",
            text: "No se pudo contactar al servidor.",
            icon: "error",
            timer: 5000,
            showConfirmButton: false
        });
    }).always(function () {
        $("#categoriaE").on("input", function () {
            this.classList.remove("is-invalid");
            this.setCustomValidity("");
        });
    });
});

    

    $(document).on("click", ".btn_eliminar", function (e) {
        e.preventDefault();
        Swal.fire({
            title: '¿Desea eliminar el registro?',
            text: 'Al continuar, no podrá ser revertido y los datos serán borrados completamente',
            showDenyButton: true,
            showCancelButton: false,
            confirmButton: 'si',
            denyButton: 'NO'
        }).then((result) => {
            if (result.isConfirmed) {
                eliminar($(this).attr('data-id'));
            } else if (result.isDenied) {
                Swal.fire("Opcion cancelada por el usuario", '', 'info');
            }
        });
    });
});

function cargarTabla(estado = 1) {
    var datos = {"consultar_datos": "mostrar", "estado": estado};

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "MedidaController",
        data: datos
    }).done(function (json) {
        console.log("Respuesta JSON:", json);
        if (json[0].resultado == "exito") {
            $("#nombre_medida").empty().html(json[0].tabla);
            $("#tabla_listMedida").DataTable({
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


//
function cargarDatosModal(id_medida) {
    var datos = {"consultar_datos": "cargarDatos", "id_medida": id_medida};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "MedidaController",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $('#id_medida').val(json[0].id_medida);
            $('#categoriaE').val(json[0].nombre_medida);
             $('#categoriaA').val(json[0].abrevacion);
            $('#myModalE').modal('show');
        } else {
            Swal.fire(
                    'Error',
                    'No existe el Identificador en la Base Datos de la Tabla',
                    'error'
                    );
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
        timer: 1000,

    });
}