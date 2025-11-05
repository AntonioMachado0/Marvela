$(function () {
    cargarTabla();
    $(document).on("submit", "#formulario_insert", function (e) {
        e.preventDefault();
        var nombre = $("#nombre").val();
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "MarcaController",
            data: {"nombre": nombre, "consultar_datos": "insertar"}
        }).done(function (json) {
            console.log("JSON recibido:", json);
            if (json[0].resultado === "exito") {
                $("#nombre").val("");
                $("#myModal").modal("hide");
                cargarTabla();
                console.log("Insert exitoso");
                mensaje("Excelente", "Marca fue agregado", "success");
            } else if (json[0].resultado === "duplicado") {
                mensaje("Marca duplicada", "La marca ya existe. Por favor ingrese una diferente.", "warning");
            } else {
                Swal.fire('Accion no completada', "No se puede registrar una nueva marca", "Error");
            }
        }).fail(function () {
        }).always(function () {
        });
    });


    $(document).on("submit", "#formulario_editar", function (e) {
        e.preventDefault();
        var id_marca = $("#id_marca").val();
        var nombre_marca = $("#categoriaE").val(); // no $("#nombre_marca")
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "MarcaController",
            data: {
                "id_marca": id_marca,
                "nombre_marca": nombre_marca,
                "consultar_datos": "editar"
            }
        }).done(function (json) {
    if (json[0].resultado === "exito") {
        $("#id_marca").val("");
        $("#nombre_marca").val("");
        $("#myModalE").modal("hide");
        cargarTabla();
        mensaje("Excelente", "Marca modificada", "success");
    } else if (json[0].resultado === "duplicado") {
        Swal.fire({
            title: "Marca duplicada",
            text: json[0].mensaje || "Ya existe otra marca con ese nombre.",
            icon: "warning",
            timer: 2500,
            showConfirmButton: false
        });
        $("#nombre_marca").addClass("is-invalid");
        $("#nombre_marca")[0].setCustomValidity("Ya existe otra marca con ese nombre.");
    } else {
        Swal.fire({
            title: "Acción no completada",
            text: "No se puede modificar la Marca.",
            icon: "error",
            timer: 2500,
            showConfirmButton: false
        });
    }
}).fail(function () {
        }).always(function () {
        });
    });

    $(document).on("click", ".btn_editar", function (e) {
        e.preventDefault();
        cargarDatosModal($(this).attr('data-id'));
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
        url: "MarcaController",
        data: datos
    }).done(function (json) {
        console.log("Respuesta JSON:", json);
        if (json[0].resultado == "exito") {
            $("#nombre_marca").empty().html(json[0].tabla);
            $("#tabla_listMarca").DataTable({
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



function cargarDatosModal(id_marca) {
    var datos = {"consultar_datos": "cargarDatos", "id_marca": id_marca};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "MarcaController",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $('#id_marca').val(json[0].id_marca);
            $('#categoriaE').val(json[0].nombre_marca);
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


function mensaje(titulo, texto, tipo) {
    Swal.fire({
        title: titulo,
        text: texto,
        icon: tipo,
        timer: 2500,
        showConfirmButton: false
    });
}
