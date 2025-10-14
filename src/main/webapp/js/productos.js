
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

    formData.append("consultar_datos", "editar");
    formData.append("id_producto", $("#id_producto").val());
    formData.append("nombre_producto", $("#nombre_productoE").val());
    formData.append("descripcion", $("#descripcionE").val());
    formData.append("codigo_categoria", $("#codigo_categoriaE").val());

    const archivo = $("#imagenE")[0].files[0];
    if (archivo) {
        formData.append("imagen", archivo);
    }

    $.ajax({
        method: "POST",
        url: "ProductosController",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "json"
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#formulario_editar")[0].reset();
            $("#myModalE").modal("hide");

            mensaje("Producto guardado", "Se modificó correctamente", "success");

            // ✅ Refrescar tabla con anti-caché en imágenes
            cargarTablaDespuesDeEditar(); // ← función especializada
        } else {
            mostrarError("No se puede modificar el producto");
        }
    }).fail(function () {
        mostrarError("No se pudo contactar al servidor");
    });
});
function cargarTablaDespuesDeEditar(estado = 1) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProductosController",
        data: { consultar_datos: "mostrar", estado }
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            if ($.fn.DataTable.isDataTable("#tabla_listCom")) {
                $("#tabla_listCom").DataTable().destroy();
            }

            const timestamp = new Date().getTime();
            const htmlConAntiCache = json[0].tabla.replace(
                /(<img[^>]+src=['"]data:image\/[^'"]+)(['"])/g,
                `$1?v=${timestamp}$2`
            );

            $("#proveedorA").empty().html(htmlConAntiCache);

            // 🔁 Refuerzo visual: reconstruir src con timestamp
            $("#proveedorA img").each(function () {
                const src = $(this).attr("src");
                $(this).attr("src", src + "&force=" + timestamp);
            });

            $("#tabla_listCom").DataTable({
                language: {
                    url: "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
                },
                lengthMenu: [[5, 10, 15, -1], [5, 10, 15, "Todos"]],
                pageLength: 5,
                columnDefs: [
                    { responsivePriority: 1, targets: 0 },
                    { responsivePriority: 2, targets: -1 }
                ]
            });
        } else {
            mostrarError("No se pudo obtener los datos");
        }
    });
}

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


function mostrarError(texto) {

    mensaje("Duplicado", texto, "error");
    mensaje("Acción no completada", texto, "error");

}


function mensaje(titulo, mensaje, icono) {
    Swal.fire({
        title: titulo,
        text: mensaje,
        icon: icono,

        timer: 2500
    });
}



        timer: 1000
    });
}


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

         
            $("#proveedorA").empty().html(json[0].tabla);

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
$(document).ready(function () {
    $("#imagenE").on("change", function () {
        const archivo = this.files[0];
        if (archivo && archivo.type.startsWith("image/")) {
            const lector = new FileReader();
            lector.onload = function (e) {
                $("#imagePreviewE").attr("src", e.target.result);
                $("#previewContainerE").show();
            };
            lector.readAsDataURL(archivo);
        } else {
            $("#imagePreviewE").attr("src", "#");
            $("#previewContainerE").hide();
        }
    });
});








function cargarDatosModal(id_producto) {
  const datos = {
    consultar_datos: "cargarDatos",
    id_producto: id_producto
  };

  console.log("ID enviado:", id_producto);

  $.ajax({
    dataType: "json",
    method: "POST",
    url: "ProductosController",
    data: datos
  }).done(function (json) {
    if (json[0].resultado === "exito") {
      // Rellenar campos
      
      $('#id_producto').val(json[0].id_producto);
      $('#nombre_productoE').val(json[0].nombre_producto);
      $('#descripcionE').val(json[0].descripcion);
      $('#codigo_categoriaE').val(json[0].codigo_categoria);

      // Vista previa de imagen desde BD
      const mime = json[0].mime_type || "imageE/jpeg";
      const base64 = json[0].imagen_base64 || "";
      const src = base64.length > 0 ? `data:${mime};base64,${base64}` : "#";

      $("#imagePreviewE").attr("src", src);
      $("#imagen_base64E").val(base64);
      base64.length > 0 ? $("#previewContainerE").show() : $("#previewContainerE").hide();

      console.log("Vista previa cargada:", src);

      // Cargar combo de categoría
      const datosCategoria = {
        consultar_datos: "cargarCombo",
        codigo_categoria: json[0].codigo_categoria
      };
      cargarCombo(datosCategoria);

      // Mostrar modal
      $('#myModalE').modal('show');
    } else {
      Swal.fire('Error', 'No existe el Identificador en la Base de Datos', 'error');
    }
  });
}
$(document).ready(function () {
  const $imagenInput = $("#imagen");

  if ($imagenInput.length) {
    $imagenInput.on("change", function () {
      const archivo = this.files[0];
      const $preview = $("#imagePreview");
      const $container = $("#previewContainer");

      if (archivo && archivo.type.startsWith("image/")) {
        const lector = new FileReader();
        lector.onload = function (e) {
          $preview.attr("src", e.target.result);
          $container.show();
          $("#imagen_base64").val(e.target.result.split(',')[1]);
        };
        lector.readAsDataURL(archivo);
      } else {
        $preview.attr("src", "#");
        $container.hide();
      }
    });
  }
});


function limpiarVistaPreviaImagen() {
    $("#imagen").val(""); // limpia el input file
    $("#imagePreview").attr("src", "#"); // limpia la imagen
    $("#previewContainer").hide(); // oculta el contenedor
    $("#imagen_base64").val(""); // limpia el base64 si lo usas
    
  
}


function limpiarSeleccionComboCategoria() {
    const $combo = $("#codigo_categoria");

    // Verificar si ya existe la opción "Seleccione una categoría"
    const existeOpcion = $combo.find('option[value="0"]').length > 0;

    if (!existeOpcion) {
        $combo.prepend('<option disabled selected value="0">Seleccione una categoría</option>');
    } else {
        // Asegurar que esté marcada como seleccionada
        $combo.find('option[value="0"]').prop("selected", true);
    }

    // Forzar selección visual
    $combo.val("0").trigger("change");
}


$(document).ready(function () {
  const $imagenInput = $("#imagenE");

  if ($imagenInput.length) {
    $imagenInput.on("change", function () {
      const archivo = this.files[0];
      const $preview = $("#imagePreviewE");
      const $container = $("#previewContainerE");

      if (archivo && archivo.type.startsWith("image/")) {
        const lector = new FileReader();
        lector.onload = function (e) {
          $preview.attr("src", e.target.result);
          $container.show();
          $("#imagen_base64E").val(e.target.result.split(',')[1]);
        };
        lector.readAsDataURL(archivo);
      } else {
        $preview.attr("src", "#");
        $container.hide();
      }
    });
  }
});



function cargarCombo(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ProductosController",
        data: datos
    }).done(function (json) {
        const esEdicion = datos.codigo_categoria && datos.codigo_categoria !== "0";
        const combo = esEdicion ? $("#codigo_categoriaE") : $("#codigo_categoria");

        combo.empty().append('<option disabled selected value="0">Seleccione una categoría</option>');

        if (json[0].resultado === "exito") {
            combo.append(json[0].categoria);

            if (esEdicion) {
                const valor = datos.codigo_categoria.toString().trim();
                const existe = combo.find("option[value='" + valor + "']").length > 0;
                if (existe) {
                    combo.val(valor);
                }
            } else {
                combo.val("0").trigger("change"); // ✅ fuerza selección por defecto en inserción
            }
        } else {
            mostrarError("No se pudo cargar el combo de categoría");
        }
    });
}

});

