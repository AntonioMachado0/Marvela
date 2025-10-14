/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


$(function () {
    cargarTabla();

$(document).on("submit", "#formulario_insert", function (e) {
    e.preventDefault();

    const imagen = $("#imagen")[0].files[0];

    // 🧪 Validación básica de imagen
    if (!imagen || !imagen.type.startsWith("image/")) {
        Swal.fire({
            title: "Imagen inválida",
            text: "Debes seleccionar un archivo de imagen válido.",
            icon: "error"
        });
        return;
    }

    const formData = new FormData();
    formData.append("consultar_datos", "insertar");
    formData.append("nombre_producto", $("#nombre_producto").val());
    formData.append("descripcion", $("#descripcion").val());
    formData.append("imagen", imagen);
    formData.append("codigo_categoria", $("#codigo_categoria").val());
    formData.append("id_marca", $("#id_marca").val());
    formData.append("id_medida", $("#id_medida").val());

    // 🧾 Trazabilidad de envío
    console.log("📦 Enviando producto:", $("#nombre_producto").val());

    $.ajax({
        type: "POST",
        url: "ProductosController",
        data: formData,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (json) {
            if (json[0].resultado === "exito") {
                mostrarExito(json);
            } else {
                mostrarError(json[0].detalle || "No se pudo registrar el producto");
            }
        },
        error: function () {
            mostrarError("No se pudo procesar el formulario. Verifica los datos o la conexión.");
        }
    });
});
    // 🧾 Envío del formulario de edición
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

        $.ajax({
            dataType: "json",
            method: "POST",
            url: "ProductosController",
            data: datos
        }).done(function (json) {
            if (json[0].resultado === "exito") {
                $("#formulario_editar")[0].reset();
                $("#myModalE").modal("hide");
                cargarTabla();
                mensaje("Proveedor guardado", "Se modificó correctamente", "success");
            } else {
                mostrarError("No se puede modificar el proveedor");
            }
        }).fail(function () {
            mostrarError("No se pudo contactar al servidor");
        });
    });

    // 🖱️ Botón editar
    $(document).on("click", ".btn_editar", function (e) {
        e.preventDefault();
        cargarDatosModal($(this).attr('data-id'));
    });
});

// ✅ Mostrar éxito con imagen
function mostrarExito(json) {
    const rutaRelativa = json[0].imagen; // ejemplo: "imagenes_productos/prod_123.png"
    const rutaCompleta = window.location.origin + "/Marvela/img/" + rutaRelativa;

    $("#preview_imagen").attr("src", rutaCompleta).show();
    $("#formulario_insert")[0].reset();
    mensaje("Producto guardado", "Se agregó correctamente", "success");
    $("#myModal").modal("hide");
    cargarTabla();
}

// ❌ Mostrar error
function mostrarError(texto) {
    mensaje("Acción no completada", texto, "error");
}

// 🔔 SweetAlert con temporizador
function mensaje(titulo, mensaje, icono) {
    Swal.fire({
        title: titulo,
        text: mensaje,
        icon: icono,
        timer: 1000
    });
}

// 🔄 Cargar tabla
function cargarTabla(estado = 1) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProductosController",
        data: {consultar_datos: "mostrar", estado}
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            // 🧹 Destruir DataTable si ya existe
            if ($.fn.DataTable.isDataTable("#tabla_listCom")) {
                $("#tabla_listCom").DataTable().destroy();
            }

            // 🧼 Limpiar contenedor antes de insertar
            $("#proveedorA").empty().html(json[0].tabla);

            // ✅ Inicializar DataTable
            $("#tabla_listCom").DataTable({
                language: {url: "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"},
                lengthMenu: [[5, 10, 15, -1], [5, 10, 15, "Todos"]],
                pageLength: 5,
                columnDefs: [
                    {responsivePriority: 1, targets: 0},
                    {responsivePriority: 2, targets: -1}
                ]
            });
        } else {
            mostrarError("No se pudo obtener los datos");
        }
    });
}

// 🔄 Cargar combos
function cargarCombo(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProductosController",
        data: datos
    }).done(function (json) {
        const esEdicion = datos.codigo_categoria && datos.codigo_categoria !== "0";
        const combo = esEdicion ? $("#codigo_categoriaE") : $("#codigo_categoria");

        combo.empty().append('<option disabled value="0">Seleccione un proveedor</option>');

        if (json[0].resultado === "exito") {
            combo.append(json[0].categoria);
            const valor = datos.codigo_categoria?.toString().trim();
            const existe = combo.find("option[value='" + valor + "']").length > 0;
            combo.val(existe ? valor : "0");
        } else {
            mostrarError("No se pudo cargar el combo de categoría");
        }
    });
}

function cargarComboE(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProductosController",
        data: datos
    }).done(function (json) {
        const combo = datos.id_marca !== "0" ? "#id_marcaE" : "#id_marca";
        $(combo).empty().append('<option disabled selected value="0">Seleccione una marca</option>');

        if (json[0].resultado === "exito") {
            $(combo).append(json[0].marca);
            const valor = datos.id_marca?.toString().trim();
            const existe = $(combo).find("option[value='" + valor + "']").length > 0;
            if (existe) $(combo).val(valor);
        } else {
            mostrarError("No se pudo cargar el combo de marca");
        }
    });
}

function cargarComboB(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProductosController",
        data: datos
    }).done(function (json) {
        const comboId = datos.id_medida !== "0" ? "#id_medidaE" : "#id_medida";
        $(comboId).empty().append('<option disabled selected value="0">Seleccione una medida</option>');

        if (json[0].resultado === "exito") {
            $(comboId).append(json[0].medida);
            const valor = datos.id_medida?.toString().trim();
            const existe = $(comboId).find("option[value='" + valor + "']").length > 0;
            if (existe) $(comboId).val(valor);
        } else {
            mostrarError("No se pudo cargar el combo de medida");
        }
    });
}

// 🗓️ Inicialización de fecha y combos
$(document).ready(function () {
    $("#fecha_de_orden").flatpickr({dateFormat: "d-m-y"});

    const params = new URLSearchParams(window.location.search);
    const idCompra = params.get('idCompra');
    if (idCompra) {
        $('#numero_de_orden').val(idCompra).prop('readonly', true);
    }

    cargarCombo({consultar_datos: "cargarCombo", codigo_categoria: "0"});
    cargarComboE({consultar_datos: "cargarComboE", id_marca: "0"});
    cargarComboB({consultar_datos: "cargarComboB", id_medida: "0"});
});


$(document).ready(function () {
    $("#imagen").on("change", function () {
        const archivo = this.files[0];
        if (archivo && archivo.type.startsWith("image/")) {
            const lector = new FileReader();
            lector.onload = function (e) {
                $("#imagePreview").attr("src", e.target.result);
                $("#previewContainer").show();
            };
            lector.readAsDataURL(archivo);
        } else {
            $("#imagePreview").attr("src", "#");
            $("#previewContainer").hide();
        }
    });
});