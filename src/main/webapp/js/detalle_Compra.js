$(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const codigoCompra = urlParams.get("codigo_compra");
    cargarTabla(codigoCompra);
    cargarComboB({consultar_datos: "cargarComboB", id_producto: "0"});
    cargarCombo({consultar_datos: "cargarCombo", id_marca: "0"});
    cargarComboE({consultar_datos: "cargarComboE", id_medida: "0"});


$(document).on("submit", "#formulario_insert", function (e) {
    e.preventDefault();

    console.log("Formulario enviado");

    // Capturar valores del formulario
    var codigo_producto = $("#codigo_producto").val();
    var fecha_vencimiento = $("#fecha_vencimiento").val();
    var cantidad_producto = $("#cantidad_producto").val();
    var precio_compra = $("#precio_compra").val();
    var porcentaje = $("#porcentaje").val();
    var medida_producto = $("#medida_producto").val();
    var numero_de_orden = $("#numero_de_orden").val(); //  Este campo se preserva
    var id_producto = $("#id_producto").val();
    var id_marca = $("#id_marca").val();
    var id_medida = $("#id_medida").val();

if (fecha_vencimiento && fecha_vencimiento.trim() !== "") {
  // Si está en formato YYYY-MM-DD, lo transformamos
  if (/^\d{4}-\d{2}-\d{2}$/.test(fecha_vencimiento)) {
    const partes = fecha_vencimiento.split("-");
    fecha_vencimiento = `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  // Validar que la fecha sea futura (formato DD/MM/YYYY)
  if (/^\d{2}\/\d{2}\/\d{4}$/.test(fecha_vencimiento)) {
    const partes = fecha_vencimiento.split("/");
    const fechaIngresada = new Date(partes[2], partes[1] - 1, partes[0]);

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    if (fechaIngresada <= hoy) {
      Swal.fire({
        title: 'Fecha incorrecta',
        text: 'Solo se permiten fechas futuras',
        icon: 'warning',
        confirmButtonText: 'Entendido'
      });
      return;
    }
  } else {
    Swal.fire({
      title: 'Formato incorrecto',
      text: 'La fecha debe tener el formato DD/MM/YYYY',
      icon: 'error',
      confirmButtonText: 'Entendido'
    });
    return;
  }
}
// Si no hay fecha, no se valida nada y se continúa

    var datos = {
        "consultar_datos": "insertar",
        "codigo_producto": codigo_producto,
        "fecha_vencimiento": fecha_vencimiento,
        "cantidad_producto": cantidad_producto,
        "precio_compra": precio_compra,
        "porcentaje": porcentaje,
        "medida_producto": medida_producto,
        "numero_de_orden": numero_de_orden,
        "id_producto": id_producto,
        "id_marca": id_marca,
        "id_medida": id_medida
    };

    console.log("Datos enviados:", datos);

    // ✅ Confirmación antes de guardar
    Swal.fire({
        title:'¿Continuara con el registro?',
        html: `
            <strong>Producto:</strong> ${$("#id_producto option:selected").text()}<br>
            <strong>Código producto:</strong> ${codigo_producto}<br>
            <strong>Precio unitario:</strong> $${parseFloat(precio_compra).toFixed(2)}<br><br>
            <em>Estos campos no  editables .</em>
        `,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Guardar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                dataType: "json",
                method: "POST",
                url: "Detalle_Producto_Controller",
                data: datos
            }).done(function (json) {
                if (json[0].resultado === "exito") {
                    limpiarFormularioProveedorPreservandoOrden(); // ✅ Limpieza controlada
                    $("#myModal").modal("hide");

                    refrescarTabla(); // ✅ Solo actualiza la tabla

                    cargarComboB({consultar_datos: "cargarComboB", id_producto: "0"});
                    cargarCombo({consultar_datos: "cargarCombo", id_marca: "0"});
                    cargarComboE({consultar_datos: "cargarComboE", id_medida: "0"});

                    mensaje("Excelente", "El Detalle de Producto fue agregado", "success");
                } else {
                    Swal.fire({
                        title: 'Acción no completada',
                        text: 'Código existente',
                        icon: 'error',
                        confirmButtonText: 'Entendido'
                    });
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Error AJAX:", textStatus, errorThrown);
            });
        }
    });
});
// ✅ Refresca solo la tabla sin tocar el resto de la página
    function refrescarTabla() {
        const codigoCompra = new URLSearchParams(window.location.search).get("codigo_compra");
        cargarTabla(codigoCompra);
    }

// ✅ Limpia el formulario pero preserva el campo "Número de Orden"
    function limpiarFormularioProveedorPreservandoOrden() {
        // Preservar el número de orden
        const numeroOrdenActual = $("#numero_de_orden").val();

        // Limpiar manualmente los campos que deben reiniciarse
        $("#codigo_producto").val("");
        $("#fecha_vencimiento").val("");
        $("#cantidad_producto").val("");
        $("#precio_compra").val("");
        $("#porcentaje").val("");
        $("#medida_producto").val("");
        $("#id_producto").val("0");
        $("#id_marca").val("0");
        $("#id_medida").val("0");

        // Restaurar el número de orden explícitamente
        $("#numero_de_orden").val(numeroOrdenActual);

        // Asegurar que todos los campos estén habilitados
        $("#formulario_insert input, #formulario_insert select").prop("disabled", false);
    }
    // 🧾 Cargar combos al iniciar
    cargarCombo({consultar_datos: "cargarCombo", id_marca: "0"});
    cargarComboE({consultar_datos: "cargarComboE", id_medida: "0"});
    cargarComboB({consultar_datos: "cargarComboB", id_producto: "0"});
});

// ✅ Mostrar éxito con imagen
function mostrarExito(json) {
    const rutaRelativa = json[0].imagen; // ejemplo: "imagenes_productos/prod_123.png"
    const rutaCompleta = window.location.origin + "/Marvela/img/" + rutaRelativa;

    $("#preview_imagen").attr("src", rutaCompleta).show();
    $("#formulario_insert")[0].reset();
    mensaje("Producto guardado", "Se guardo correctamente", "success");
    $("#myModal").modal("hide");
    const urlParams = new URLSearchParams(window.location.search);
    const codigoCompra = urlParams.get("codigo_compra");
    cargarTabla(codigoCompra);
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






// ✅ Limpieza modular del formulario proveedor
function limpiarFormularioProveedor() {
    $("#codigo_producto").val("");
    $("#fecha_vencimiento").val("");
    $("#cantidad_producto").val("");
    $("#precio_compra").val("");
    $("#porcentaje").val("");
    $("#medida_producto").val("");
    $("#numero_de_orden").val("");
    $("#id_producto").val("0");
    $("#id_marca").val("0");
    $("#id_medida").val("0");
    $("#precio").val("");
}

// ✅ Cargar tabla de proveedores
function cargarTabla() {
    const urlParams = new URLSearchParams(window.location.search);
    const codigoCompra = urlParams.get("codigo_compra");

    if (!codigoCompra || !codigoCompra.trim()) {
        console.warn("⚠️ No se recibió el parámetro 'codigo_compra'. Usando fallback ID = 1");
        return; // O puedes usar: const datos = {consultar_datos: "mostrar", estado: 1};
    }

    const datos = {
        consultar_datos: "mostrar",
        estado: codigoCompra // ← ahora sí usa el ID dinámico
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "Detalle_Producto_Controller",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#proveedorA").empty().html(json[0].tabla);
            $("#tabla_listCom").DataTable({
                language: {
                    url: "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
                },
                lengthMenu: [[5, 10, 15, -1], [5, 10, 15, "Todos"]],
                pageLength: 5,
                columnDefs: [
                    {responsivePriority: 1, targets: 0},
                    {responsivePriority: 2, targets: -1}
                ]
            });
        } else {
            Swal.fire('Acción no completada', 'No se pudo obtener los datos', 'error');
        }
    });
}




$(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const codigoCompra = urlParams.get("codigo_compra");

    if (codigoCompra && codigoCompra.trim()) {
        $("#numero_de_orden").val(codigoCompra);
    } else {
        console.warn("No se recibió el parámetro código_compra");
        $("#numero_de_orden").val("No disponible"); // Fallback opcional
    }
});


function cargarComboB(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "Detalle_Producto_Controller",
        data: datos
    }).done(function (json) {
        const comboId = datos.id_producto !== "0" ? "#id_productoE" : "#id_producto";
        const $combo = $(comboId);
        $combo.empty().append('<option disabled selected value="0">Seleccione un producto</option>');

        if (json[0]?.resultado === "exito") {
            const productos = json[0].producto;

            if (typeof productos === "string") {
                $combo.append(productos);
            } else if (Array.isArray(productos)) {
                productos.forEach(item => {
                    if (item.id && item.nombre) {
                        $combo.append(`<option value="${item.id}">${item.nombre}</option>`);
                    }
                });
            }

            const valor = datos.id_producto?.toString().trim();
            const existe = $combo.find(`option[value="${valor}"]`).length > 0;
            if (existe)
                $combo.val(valor);

            // 🔁 Disparar carga de marca y medida si corresponde
            if (valor && valor !== "0") {
                cargarComboMarca({consultar_datos: "cargarComboMarca", id_producto: valor});
                cargarComboMedida({consultar_datos: "cargarComboMedida", id_producto: valor});
            }
        } else {
            mostrarError("No se pudo cargar el combo de producto");
        }
    });
}




function cargarCombo(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "Detalle_Producto_Controller",
        data: datos
    }).done(function (json) {
        const comboId = datos.id_marca !== "0" ? "#id_marcaE" : "#id_marca";
        const $combo = $(comboId);
        $combo.empty().append('<option disabled selected value="0">Seleccione una marca</option>');

        if (json[0]?.resultado === "exito") {
            const marca = json[0].marca;

            if (typeof marca === "string") {
                $combo.append(marca);
            } else if (Array.isArray(marca)) {
                marca.forEach(item => {
                    if (item.id && item.nombre) {
                        $combo.append(`<option value="${item.id}">${item.nombre}</option>`);
                    }
                });
            }

            const valor = datos.id_marca?.toString().trim();
            const existe = $combo.find(`option[value="${valor}"]`).length > 0;
            if (existe)
                $combo.val(valor);
        } else {
            mostrarError("No se pudo cargar el combo de marca");
        }
    });
}

function cargarComboE(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "Detalle_Producto_Controller",
        data: datos
    }).done(function (json) {
        console.log("Respuesta recibida:", json);

        const comboId = datos.id_medida !== "0" ? "#id_medidaE" : "#id_medida";
        const $combo = $(comboId);
        $combo.empty().append('<option disabled selected value="0">Seleccione una medida</option>');

        if (json[0]?.resultado === "exito") {
            const medida = json[0].medida;
            console.log("Medidas recibidas:", medida);

            if (typeof medida === "string") {
                $combo.append(medida);
            } else if (Array.isArray(medida)) {
                medida.forEach(item => {
                    if (item.id && item.nombre) {
                        $combo.append(`<option value="${item.id}">${item.nombre}</option>`);
                    }
                });
            }

            const valor = datos.id_medida?.toString().trim();
            console.log("ID de medida a seleccionar:", valor);

            const existe = $combo.find(`option[value="${valor}"]`).length > 0;
            if (existe) {
                $combo.val(valor);
                console.log("Medida seleccionada:", valor);
            } else {
                console.warn("⚠️ Medida no encontrada en el combo:", valor);
            }
        } else {
            mostrarError("No se pudo cargar el combo de medida");
        }
    });
}

$(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const numeroOrden = urlParams.get("numero_de_orden"); // ← Este es el número de orden

    if (numeroOrden) {
        $("#codigoOrden").val(numeroOrden);         // ← Muestra el número en el campo
        $("#codigoOrden").prop("readonly", true);   // ← Lo bloquea para edición
    } else {
        console.warn("No se recibió el parámetro 'numero_de_orden'");
    }
});

function calcularTotalVenta() {
    const cantidad = parseFloat(document.getElementById("cantidad_producto").value) || 0;
    const costo = parseFloat(document.getElementById("precio_compra").value) || 0;
    const porcentaje = parseFloat(document.getElementById("porcentaje").value) || 0;

    const total = cantidad * costo * (1 + porcentaje / 100);
    document.getElementById("precio").value = total.toFixed(2);
}

// Ejecutar al cambiar cualquier campo
["cantidad_producto", "precio_compra", "porcentaje"].forEach(id => {
    document.getElementById(id).addEventListener("input", calcularTotalVenta);
});


function precioUnitario() {
  const cantidad = parseFloat(document.getElementById("cantidad_producto").value) || 0;
  const costo = parseFloat(document.getElementById("precio_compra").value) || 0;
  const porcentaje = parseFloat(document.getElementById("porcentaje").value) || 0;

  // Calcular precio unitario con porcentaje
  const precioConPorcentaje = costo * (1 + porcentaje / 100);
  document.getElementById("precioU").value = precioConPorcentaje.toFixed(2);

  // Calcular total
  const total = cantidad * precioConPorcentaje;
  document.getElementById("precio").value = total.toFixed(2);
}

// Ejecutar al cambiar cualquier campo
["cantidad_producto", "precio_compra", "porcentaje"].forEach(id => {
  document.getElementById(id).addEventListener("input", precioUnitario);
});
function cargarDatosModal(codigo_detalle_compra) {
    var datos = {
        consultar_datos: "cargarDatos",
        codigo_detalle_compra: codigo_detalle_compra
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "Detalle_Producto_Controller",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            console.log("Código de compra enviado:", $("#codigo_detalle_compra").val());
            $('#codigo_detalle_compra').val(json[0].codigo_detalle_compra);
            $('#codigo_productoE').val(json[0].codigo_producto);
            $('#fecha_vencimientoE').val(json[0].fecha_vencimiento);
            $('#cantidad_productoE').val(json[0].cantidad_producto);
            $('#precio_compraE').val(json[0].precio_compra);
            $('#porcentajeE').val(json[0].porcentaje);
            $('#medida_productoE').val(json[0].medida_producto);
            $('#codigo_compraE').val(json[0].codigo_compra);
            $('#numero_de_ordenE').val(json[0].numero_de_orden);
            $('#id_productoE').val(json[0].id_producto);
            $('#id_marcaE').val(json[0].id_marca);
            $('#id_medidaE').val(json[0].id_medida);

            // Cargar combo de empleado con selección automática
            var datosProducto = {
                consultar_datos: "cargarComboB",
                id_producto: json[0].id_producto
            };
            cargarComboB(datosProducto);

            // Cargar combo de proveedor con selección automática
            var datosMarca = {
                consultar_datos: "cargarCombo",
                id_marca: json[0].id_marca
            };
            cargarCombo(datosMarca);
            // Cargar combo de proveedor con selección automática
            var datosMedida = {
                consultar_datos: "cargarComboE",
                id_medida: json[0].id_medida
            };
            cargarComboE(datosMedida);




            $('#myModalE').modal('show');
        } else {
            Swal.fire('Error', 'No existe el Identificador en la Base de Datos', 'error');
        }
    });
}


$(document).on("submit", "#formulario_editar", function (e) {
    e.preventDefault();

    // ✅ Obtener y convertir fecha
    var fecha_original = $("#fecha_vencimientoE").val();
    var fecha_convertida = fecha_original;
    if (fecha_original && fecha_original.includes("-")) {
        var partes = fecha_original.split("-");
        if (partes.length === 3) {
            fecha_convertida = partes[2] + "/" + partes[1] + "/" + partes[0];
        }
    }

    const datos = {
        consultar_datos: "editar",
        codigo_detalle_compra: $("#codigo_detalle_compra").val(),
        codigo_producto: $("#codigo_productoE").val(),
        fecha_vencimiento: fecha_convertida,
        cantidad_producto: $("#cantidad_productoE").val(),
        precio_compra: $("#precio_compraE").val(),
        porcentaje: $("#porcentajeE").val(),
        medida_producto: $("#medida_productoE").val(),
        codigo_compra: $("#codigo_compraE").val(),
        id_producto: $("#id_productoE").val(),
        id_marca: $("#id_marcaE").val(),
        id_medida: $("#id_medidaE").val()
    };

    console.log(datos);

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "Detalle_Producto_Controller",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $("#formulario_editar")[0].reset();
            $("#myModalE").modal("hide");
            const urlParams = new URLSearchParams(window.location.search);
            const codigoCompra = urlParams.get("codigo_compra");
            cargarTabla(codigoCompra);
            mensaje("Excelente", "El Proveedor fue modificado", "success");
        } else {
            Swal.fire('Acción no completada', "No se puede modificar el proveedor", "error");
        }
    }).fail(function () {
        Swal.fire('Error de conexión', "No se pudo contactar al servidor", "error");
    });
});
$(document).on("click", ".btn_editar", function () {
    const codigo = $(this).data("id");
    console.log("Código capturado:", $(this).data("id"));
    cargarDatosModal(codigo);
});

function refrescarTabla() {
    const codigoCompra = new URLSearchParams(window.location.search).get("codigo_compra");
    cargarTabla(codigoCompra); // Esta función ya actualiza solo la tabla
}

document.getElementById("porcentaje").addEventListener("keydown", function (e) {
  if (e.key === "." || e.key === ",") {
    e.preventDefault(); // ❌ Bloquea punto y coma decimal
  }
});

