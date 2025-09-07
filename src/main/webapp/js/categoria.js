$(function () {
    cargarTabla(); //Es para llamar la funcion donde se esta mandando a llamar los id y parametros
    $(document).on("submit", "#formulario_insert", function (e) {
        e.preventDefault();
        var datos = $("#nombre").val();
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "CategoriaController",
            data: {"nombre": datos, "consultar_datos": "insertar"}
        }).done(function (json) {
            console.log("JSON recibido:", json);
            if (json[0].resultado === "exito") {
                $("#nombre").val("");

                $("#myModal").modal("hide");
                cargarTabla();
                console.log("Insert exitoso");

                mensaje("Excelente", "Categoría fue agregado", "success");
            } else {
                Swal.fire('Accion no completada', "No se puede registrar una nuevo categoria", "Error");

            }
        }).fail(function () {



        }).always(function () {
        });
    });


    $(document).on("submit", "#formulario_editar", function (e) {
        e.preventDefault();
        var codigo_categoria = $("#codigo_categoria").val();
        var nombre = $("#categoriaE").val();

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "CategoriaController",
            data: {"codigo_categoria": codigo_categoria, "nombre": nombre, "consultar_datos": "editar"}
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#codigo_categoria").val("");
                $("#categoriaE").val("");
                $("#myModalE").modal("hide");
                cargarTabla();
                mensaje("Excelente", "Categoría modificado", "success");
            } else {
                Swal.fire('Accion no completada', "No se puede modificar la categoria", "Error");
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
        url: "CategoriaController",
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



function cargarDatosModal(codigo_categoria) {
    var datos = {"consultar_datos": "cargarDatos", "codigo_categoria": codigo_categoria};
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "CategoriaController",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $('#codigo_categoria').val(json[0].codigo_categoria);
            $('#categoriaE').val(json[0].nombre);
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