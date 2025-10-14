
$(function () {
  cargarTabla();

  function esFechaDelMesActualOHoy(fechaStr) {
    if (!/^\d{4}-\d{2}-\d{2}$/.test(fechaStr)) return false;
    const fecha = new Date(fechaStr);
    const hoy = new Date();
    return (
      fecha.getFullYear() === hoy.getFullYear() &&
      fecha.getMonth() === hoy.getMonth() &&
      fecha.getDate() <= hoy.getDate()
    );
  }

  function esNumeroDeOrdenValido(valor) {
    const tieneContenido = valor.trim().length > 0;
    const soloNumerosYGuiones = /^[0-9\-]+$/.test(valor);
    const cantidadNumeros = (valor.match(/\d/g) || []).length;
    return tieneContenido && soloNumerosYGuiones && cantidadNumeros >= 3;
  }

  function marcarError(selector) {
    $(selector).addClass("input-error");
  }

  function limpiarError(selector) {
    $(selector).removeClass("input-error");
  }

  $("#numero_de_orden").on("input", function () {
    const valor = $(this).val();
    if (!esNumeroDeOrdenValido(valor)) {
      marcarError(this);
    } else {
      limpiarError(this);
    }
  });

  $(document).on("submit", "#formulario_insert", function (e) {
    e.preventDefault();

    const numero_de_orden = $("#numero_de_orden").val();
    const fecha_de_orden_raw = $("#fecha_de_orden").val();
    const codigo_empleado = $("#codigo_empleado").val();
    const codigo_proveedor = $("#codigo_proveedor").val();

    limpiarError("#numero_de_orden");

    if (!numero_de_orden || !fecha_de_orden_raw || codigo_empleado === "0" || codigo_proveedor === "0") {
      Swal.fire('Campos incompletos', 'Por favor complete todos los campos obligatorios.', 'warning');
      return;
    }

    if (!esNumeroDeOrdenValido(numero_de_orden)) {
      marcarError("#numero_de_orden");
      Swal.fire('Numero de orden incorrecto', 'Debe contener solo numeros y guiones, y al menos tres dígitos.', 'warning');
      return;
    }

    if (!esFechaDelMesActualOHoy(fecha_de_orden_raw)) {
      Swal.fire('Fecha incorrecta', 'Ingrese una fecha correcta del mes actual. No se permiten fechas futuras.', 'warning');
      return;
    }

    let fecha_de_orden = fecha_de_orden_raw;
    if (/^\d{4}-\d{2}-\d{2}$/.test(fecha_de_orden_raw)) {
      const partes = fecha_de_orden_raw.split("-");
      fecha_de_orden = `${partes[2]}/${partes[1]}/${partes[0]}`;
    }

    const datos = {
      "consultar_datos": "insertar",
      "numero_de_orden": numero_de_orden,
      "fecha_de_orden": fecha_de_orden,
      "codigo_empleado": codigo_empleado,
      "codigo_proveedor": codigo_proveedor
    };

    $.ajax({
      dataType: "json",
      method: "POST",
      url: "ComprasController",
      data: datos
    }).done(function (json) {
      console.log("Respuesta de inserción:", json);

      if (json[0]?.resultado === "exito") {
        $("#numero_de_orden").val("").removeClass("input-error");
        $("#fecha_de_orden").val("");
        $("#codigo_empleado").val("0");
        $("#codigo_proveedor").val("0");
        $("#myModal").modal("hide");
        cargarTabla();
        mensaje("Excelente", "El dato fue agregado", "success");
      } else if (json[0]?.resultado === "duplicado") {
        marcarError("#numero_de_orden");
        Swal.fire('Duplicado', 'Ya existe una orden con ese número.', 'error');
      } else {
        Swal.fire('Duplicado', "EL numero de orden ya existe", 'error');
      }
    }).fail(function () {
      Swal.fire('Campo basio', 'No se pudo agregar.', 'error');
    });
  });

//$(document).on("submit", "#formulario_editar", function (e) {
//    e.preventDefault();
//
//    var codigo_compra = $("#codigo_compra").val();
//    console.log("Código de compra enviado:", $("#codigo_compra").val());
//    var numero_de_orden = $("#numero_de_ordenE").val();
//    var fecha_original = $("#fecha_de_ordenE").val(); // YYYY-MM-DD
//    var codigo_empleado = $("#codigo_empleadoE").val();
//    var codigo_proveedor = $("#codigo_proveedorE").val();
//
//    // Convertir fecha a DD/MM/YYYY
//    var fecha_convertida = "";
//    if (fecha_original && fecha_original.includes("-")) {
//        var partes = fecha_original.split("-");
//        fecha_convertida = partes[2] + "/" + partes[1] + "/" + partes[0];
//    }
//
//    var datos = {
//        "consultar_datos": "editar",
//        "codigo_compra": codigo_compra,
//        "numero_de_orden": numero_de_orden,
//        "fecha_de_orden": fecha_convertida,
//        "codigo_empleado": codigo_empleado,
//        "codigo_proveedor": codigo_proveedor
//    };
//
//    $.ajax({
//        dataType: "json",
//        method: "POST",
//        url: "ComprasController",
//        data: datos
//    }).done(function (json) {
//        if (json[0].resultado === "exito") {
//            $("#formulario_editar")[0].reset();
//            $("#myModalE").modal("hide");
//            cargarTabla();
//            mensaje("Excelente", "El dato fue modificado", "success");
//        } else {
//            Swal.fire('Acción no completada', "No se puede modificar la compra", "error");
//        }
//    }).fail(function () {
//        Swal.fire('Error de conexión', "No se pudo contactar al servidor", "error");
//    });
//});

$(document).on("submit", "#formulario_editar", function (e) {
  e.preventDefault();

  var codigo_compra = $("#codigo_compra").val();
  var numero_de_orden = $("#numero_de_ordenE").val();
  var fecha_original = $("#fecha_de_ordenE").val(); // YYYY-MM-DD
  var codigo_empleado = $("#codigo_empleadoE").val();
  var codigo_proveedor = $("#codigo_proveedorE").val();

  var errores = [];

  // 🔍 Validar número de orden
  if (!numero_de_orden || numero_de_orden.trim() === "") {
    errores.push("El campo de numero de orden no puede estar vacio.");
  } else {
    var formatoOrden = /^(?=(?:.*\d){3,})[0-9\-]+$/;
    if (!formatoOrden.test(numero_de_orden)) {
      errores.push("El numero de orden debe contener solo numeros y guiones, y al menos tres digitos.");
    }
  }

  // 🔍 Validar fecha
  var hoy = new Date();
  var fechaIngresada = new Date(fecha_original);

  if (!fecha_original) {
    errores.push("El campo de fecha no puede estar vacio.");
  } else {
    var mismoMes = fechaIngresada.getMonth() === hoy.getMonth() &&
                   fechaIngresada.getFullYear() === hoy.getFullYear();
    var noFutura = fechaIngresada <= hoy;

    if (!mismoMes) {
      errores.push("Solo se permiten fechas del mes actual.");
    }
    if (!noFutura) {
      errores.push("No se permiten fechas posteriores al dia de hoy.");
    }
  }

  // 🔍 Mostrar errores si existen
  if (errores.length > 0) {
    Swal.fire("Validacion de datos", errores.join("<br>"), "warning");
    return;
  }

  // ✅ Convertir fecha a DD/MM/YYYY
  var fecha_convertida = "";
  if (fecha_original.includes("-")) {
    var partes = fecha_original.split("-");
    fecha_convertida = partes[2] + "/" + partes[1] + "/" + partes[0];
  }

  var datos = {
    "consultar_datos": "editar",
    "codigo_compra": codigo_compra,
    "numero_de_orden": numero_de_orden,
    "fecha_de_orden": fecha_convertida,
    "codigo_empleado": codigo_empleado,
    "codigo_proveedor": codigo_proveedor
  };

  $.ajax({
    dataType: "json",
    method: "POST",
    url: "ComprasController",
    data: datos
  }).done(function (json) {
    if (json[0].resultado === "exito") {
      $("#formulario_editar")[0].reset();
      $("#myModalE").modal("hide");
      cargarTabla();
      mensaje("Excelente", "El dato fue modificado", "success");
    } else {
      Swal.fire('Accion no completada', "No se puede modificar la compra", "error");
    }
  }).fail(function () {
    Swal.fire('Error de conexion', "No se pudo contactar al servidor", "error");
  });
});

    $(document).on("click", ".btn_editar", function () {
    var idCompra = $(this).data("id");
    cargarDatosModal(idCompra);
});
//



    $(document).ready(function () {
        $("#fecha_de_orden").flatpickr({
            dateFormat: "d-m-y"
        });
        });
    //Cargar Combo
    var datos = {"consultar_datos": "cargarCombo", "codigo_proveedor": "0"};
    cargarCombo(datos);

    var datos1 = {"consultar_datos": "cargarComboE", "codigo_empleado": "0"};
    cargarComboE(datos1);

});




function cargarDatosModal(codigo_compra) {
    var datos = {
        consultar_datos: "cargarDatos",
        codigo_compra: codigo_compra
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ComprasController",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            $('#codigo_compra').val(json[0].codigo_compra);
            $('#numero_de_ordenE').val(json[0].numero_de_orden);
            $('#fecha_de_ordenE').val(json[0].fecha_de_orden);

            // Cargar combo de empleado con selección automática
            var datosEmpleado = {
                consultar_datos: "cargarComboE",
                codigo_empleado: json[0].codigo_empleado
            };
            cargarComboE(datosEmpleado);

            // Cargar combo de proveedor con selección automática
            var datosProveedor = {
                consultar_datos: "cargarCombo",
                codigo_proveedor: json[0].codigo_proveedor
            };
            cargarCombo(datosProveedor);

            $('#myModalE').modal('show');
        } else {
            Swal.fire('Error', 'No existe el Identificador en la Base de Datos', 'error');
        }
    });
}
function cargarComboE(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ComprasController",
        data: datos
    }).done(function (json) {
        if (datos.codigo_empleado != "0") {
            $("#codigo_empleadoE").empty();
            $('#codigo_empleadoE').append('<option disabled selected value="0">Seleccione un empleado</option>'); // ← corregido

            if (json[0].resultado == "exito") {
                $('#codigo_empleadoE').append(json[0].empleado);

                const valor = datos.codigo_empleado?.toString().trim();
                const existe = $('#codigo_empleadoE').find("option[value='" + valor + "']").length > 0;

                if (existe) {
                    $('#codigo_empleadoE').val(valor);
                    console.log("✅ Empleado seleccionado:", valor);
                } else {
                    console.warn("⚠️ El valor '" + valor + "' no está en las opciones.");
                }
            } else {
                Swal.fire('Acción no completada', 'No pudo obtener los datos', 'error');
            }
        } else {
            $("#codigo_empleado").empty();
            $('#codigo_empleado').append('<option disabled selected value="0">Seleccione un empleado</option>');

            if (json[0].resultado == "exito") {
                $('#codigo_empleado').append(json[0].empleado);
            } else {
                Swal.fire('Acción no completada', 'No pudo obtener los datos', 'error');
            }
        }
    });
}



function cargarTabla(estado = 1) {
    var datos = {"consultar_datos": "mostrar", "estado": estado};
    console.log("datos de compra: ", datos);
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ComprasController",
        data: datos

    }).done(function (json) {
        console.log("datos de json: ", json);
        if (json[0].resultado == "exito") {
            $("#tablaCompraA").empty().html(json[0].tabla);
            $("#tabla_listCom").DataTable({
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

function cargarCombo(datos) {
    $.ajax({
        dataType: "json",
        method: "POST",
        url: "ComprasController",
        data: datos
    }).done(function (json) {
        const esEdicion = datos.codigo_proveedor && datos.codigo_proveedor !== "0";
        const combo = esEdicion ? $("#codigo_proveedorE") : $("#codigo_proveedor");

        if (combo.length === 0) {
            console.error(" Combo no encontrado:", esEdicion ? "#codigo_proveedorE" : "#codigo_proveedor");
            return;
        }

        combo.empty();
        combo.append('<option disabled value="0">Seleccione un proveedor</option>');

        if (json[0].resultado === "exito") {
            combo.append(json[0].proveedor);

            const valor = datos.codigo_proveedor?.toString().trim();
            const existe = combo.find("option[value='" + valor + "']").length > 0;

            if (esEdicion && valor !== "0" && existe) {
                combo.val(valor);
                console.log("✅ Proveedor seleccionado en edición:", valor);
            } else {
                combo.val("0");
                console.log("Modo agregar, sin selección.");
            }
        } else {
            Swal.fire('Acción no completada', 'No pudo obtener los datos del combo', 'error');
        }
    });
}
cargarCombo({
    consultar_datos: "cargarCombo",
    codigo_proveedor: "0"
});
cargarCombo({
    consultar_datos: "cargarCombo",
    codigo_proveedor: json[0].codigo_proveedor
});


$(document).on("click", "#btn_editar", function () {
    const numeroOrden = $("#numero_de_orden").val()?.trim(); // ← Captura el número de orden
    if (!numeroOrden) {
        Swal.fire("Error", "No se encontró el número de orden", "error");
        return;
    }

    // Redirige con el número como parámetro GET
    window.location.href = `detalle_orden.jsp?orden=${numeroOrden}`;
});

$(function () {
  // Validación en tiempo real
  $("#numero_de_orden").on("input", function () {
    const valor = $(this).val();
    const contieneSoloNumerosYGuiones = /^[0-9\-]*$/.test(valor);
    const cantidadNumeros = (valor.match(/\d/g) || []).length;

    if (!contieneSoloNumerosYGuiones || cantidadNumeros < 3) {
      $(this).addClass("is-invalid").removeClass("is-valid");
    } else {
      $(this).removeClass("is-invalid").addClass("is-valid");
    }
  });

  // Validación al enviar el formulario
  $(document).on("submit", "#formulario_insert", function (e) {
    const valor = $("#numero_de_orden").val();
    const contieneSoloNumerosYGuiones = /^[0-9\-]+$/.test(valor);
    const cantidadNumeros = (valor.match(/\d/g) || []).length;

    if (!contieneSoloNumerosYGuiones || cantidadNumeros < 3) {
      $("#numero_de_orden").addClass("is-invalid").removeClass("is-valid");
      e.preventDefault();
      Swal.fire('Número de orden inválido', 'Debe contener solo números y guiones, y al menos tres dígitos.', 'warning');
      return;
    }
  });
});

$(function () {
  $("#formulario_insert").on("submit", function (e) {
    let valido = true;

    if ($("#codigo_empleado").val() === "") {
      $("#error_empleado").show();
      valido = false;
    } else {
      $("#error_empleado").hide();
    }

    if ($("#codigo_proveedor").val() === "") {
      $("#error_proveedor").show();
      valido = false;
    } else {
      $("#error_proveedor").hide();
    }

    if (!valido) {
      e.preventDefault(); // bloquea el envío
    }
  });
});
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formulario_insert");

  form.addEventListener("submit", function (event) {
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    }

    form.classList.add("was-validated");
  });
});