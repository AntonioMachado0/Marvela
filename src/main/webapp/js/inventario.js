/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

$(document).ready(function () {
    cargarInventario();
});

function cargarInventario() {
    $.ajax({
        url: 'InventarioServlet',
        type: 'GET',
        dataType: 'json',
        beforeSend: function () {
            $('#inventarioA').html(`
                <div class="text-center">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Cargando...</span>
                    </div>
                    <p class="mt-2">Cargando inventario...</p>
                </div>
            `);
        },
        success: function (data) {
            console.log(data);
            if (data.error) {
                Swal.fire('Error', data.error, 'error');
                return;
            }
            mostrarInventario(data);
        },
        error: function (xhr, status, error) {
            console.error('Error al cargar inventario:', error);
            Swal.fire('Error', 'No se pudo cargar el inventario: ' + error, 'error');
        }
    });
}

function mostrarInventario(datosPorFecha) {
    const contenedor = $('#inventarioA');
    contenedor.empty();

    // Ordenar fechas cronológicamente (más antigua primero)
    const fechas = Object.keys(datosPorFecha).sort((a, b) => {
        const fechaA = new Date(a);
        const fechaB = new Date(b);
        return fechaA - fechaB;
    });

    if (fechas.length === 0) {
        contenedor.html(`
            <div class="alert alert-info text-center">
                <i class="fas fa-box-open fa-2x mb-3"></i>
                <h4>No hay productos en el inventario</h4>
                <p class="mb-0">No se encontraron productos registrados en el sistema.</p>
            </div>
        `);
        return;
    }

    // Agregar el modal para imágenes ampliadas (solo una vez)
    contenedor.append(`
        <!-- Modal para ver imagen en grande -->
        <div class="modal fade" id="modalImagen" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalImagenTitle">Imagen del Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-center">
                        <img id="imagenAmpliada" src="" alt="" class="img-fluid" style="max-height: 70vh;">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
    `);

    fechas.forEach(fechaCompra => {
        const productos = datosPorFecha[fechaCompra];
        const fechaId = fechaCompra.replace(/-/g, '_');

        const seccionFecha = $(`
            <div class="card mb-4 shadow-sm">
                <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">
                        <i class="fas fa-calendar-alt me-2"></i>
                        Fecha de Compra: ${formatearFecha(fechaCompra)}
                    </h5>
                    <span class="badge bg-light text-dark">${productos.length} producto(s)</span>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover mb-0" id="tabla-${fechaId}">
                            <thead class="encabezado-verde">
                                <tr>
                                    <th style="font-size:12px;"><i class="fas fa-image"></i> IMAGEN</th>
                                   <th style="font-size:12px;"><i class="fas fa-barcode"></i> CÓDIGO</th>
                                   <th style="font-size:12px;"><i class="fas fa-cube"></i> PRODUCTO</th>
                                   <th style="font-size:12px;"><i class="fas fa-tag"></i> MARCA</th>
                                   <th style="font-size:12px;"><i class="fas fa-balance-scale"></i> CANTIDAD</th>
                                   <th style="font-size:12px;"><i class="fas fa-ruler"></i> MEDIDA</th>
                                   <th style="font-size:12px;"><i class="fas fa-dollar-sign"></i> PRECIO VENTA</th>
                                    <th style="font-size:12px;"><i class="fas fa-calendar-times"></i> VENCIMIENTO</th>
<th style="width: 65px; text-align: center; font-size:12px; padding-left:0; padding-right:0;">
  IMPRIMIR
</th>
        
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>
        `);

        const tbody = seccionFecha.find('tbody');

        productos.forEach(producto => {
            const estadoVencimiento = obtenerEstadoVencimiento(producto.fechaVencimiento);
            
            let claseCantidad = '';
            let iconoAlerta = '';
            
            // Solo aplicar estilo a la cantidad si es menor a 5
            if (producto.cantidadProducto <= 5) {
                claseCantidad = 'cantidad-baja';
                iconoAlerta = '<i class="fas fa-exclamation-triangle text-danger ms-1" title="Stock bajo"></i>';
            }
            
            let iconoVencimiento = '';
            let infoDias = '';
            
            if (estadoVencimiento === 'vencido') {
                iconoVencimiento = '<i class="fas fa-exclamation-circle text-danger ms-1" title="Producto vencido"></i>';
                const diasRestantes = calcularDiasRestantes(producto.fechaVencimiento);
                infoDias = ` <small class="text-danger">(Vencido hace ${Math.abs(diasRestantes)} días)</small>`;
            } else if (estadoVencimiento === 'proximo-vencer') {
                iconoVencimiento = '<i class="fas fa-exclamation-triangle text-warning ms-1" title="Próximo a vencer"></i>';
                const diasRestantes = calcularDiasRestantes(producto.fechaVencimiento);
                infoDias = ` <small class="text-warning">(${diasRestantes} días restantes)</small>`;
            }
            
            // Manejar la imagen
            let imagenHTML = '';
            if (producto.imagen) {
                const tipoImagen = producto.tipoImagen || 'image/jpeg';
                imagenHTML = `<img src="data:${tipoImagen};base64,${producto.imagen}" 
                                  alt="${producto.nombreProducto || 'Producto'}" 
                                  class="imagen-producto"
                                  title="Click para ampliar"
                                  onclick="ampliarImagen(this.src, '${(producto.nombreProducto || 'Producto').replace(/'/g, "\\'")}')">`;
            } else {
                imagenHTML = `<div class="sin-imagen" title="Sin imagen disponible">
                                 <i class="fas fa-image"></i>
                             </div>`;
            }
            const productoCodificado = btoa(unescape(encodeURIComponent(JSON.stringify(producto))));

const botonDetalle = `<button class="btn btn-sm btn-outline-primary ver-detalle" data-producto="${productoCodificado}" title="Imprimir etiqueta">
  <i class="fas fa-print"></i>
</button>`;

            
            const fila = $(`
                <tr>
                    <td>${imagenHTML}</td>
                    <td class="fw-bold">${producto.codigoProducto || 'N/A'}</td>
                    <td>${producto.nombreProducto || 'N/A'}</td>
                    <td>${producto.nombreMarca || 'N/A'}</td>
                    <td class="${claseCantidad}">
                        ${producto.cantidadProducto || 0}
                        ${iconoAlerta}
                    </td>
                    <td>
                        ${producto.cantidadMedida || 0} ${producto.nombreMedida || ''}
                    </td>
                    <td>
                        <span class="fw-bold text-success">$${producto.precioVenta ? producto.precioVenta.toFixed(2) : '0.00'}</span>
                    </td>
                    <td>
                        ${producto.fechaVencimiento ? formatearFecha(producto.fechaVencimiento) : 'No aplica'}
                        ${infoDias}
                        ${iconoVencimiento}
                    </td>
            
            
            <td class="text-center">${botonDetalle}</td>

                </tr>
            
            `);
            tbody.append(fila);
            
        });

        contenedor.append(seccionFecha);

        // Inicializar DataTable para esta tabla
        $(`#tabla-${fechaId}`).DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
            },
            "pageLength": 10,
            "responsive": true,
            "dom": '<"top"lf>rt<"bottom"ip><"clear">',
            "order": [],
            "columnDefs": [
                { 
                    "orderable": false, 
                    "targets": [0] // No ordenar la columna de imagen
                },
                { 
                    "orderable": true, 
                    "targets": [1, 2, 3, 4, 5, 6, 7] 
                }
            ]
        });
    });

    // Inicializar tooltips de Bootstrap
    $('[title]').tooltip();
}

$(document).on('click', '.ver-detalle', function () {
  const codificado = $(this).data('producto');
  const producto = JSON.parse(decodeURIComponent(escape(atob(codificado))));
  mostrarDetalleProducto(producto);
});
function mostrarDetalleProducto(producto) {
  $('#detalleImagen').attr('src', `data:${producto.tipoImagen || 'image/jpeg'};base64,${producto.imagen || ''}`);
 $('#detalleNombreProducto').text(producto.nombreProducto || 'N/A');
$('#detalleMarca').text(producto.nombreMarca || 'N/A');
$('#detalleCantidadTexto').text(producto.cantidadProducto || 0);
$('#detalleCantidadInput').val(1);
$('#detalleCantidadInput').attr('max', producto.cantidadProducto || 1);
$('#detallePrecio').text(producto.precioVenta ? producto.precioVenta.toFixed(2) : '0.00');
$('#detalleCodigo').text(producto.codigoProducto || 'N/A');

  // Generar código de barras
  JsBarcode("#detalleBarcode", producto.codigoProducto || "?", {
    format: "CODE128",
    lineColor: "#000",
    width: 1.5,
    height: 40,
    displayValue: true,
    fontSize: 12,
    textMargin: 0
  });

  // Mostrar modal
  const modal = new bootstrap.Modal(document.getElementById('modalDetalleProducto'));
  modal.show();
}


function imprimirEtiquetaDesdeModal() {
  const { jsPDF } = window.jspdf;
  const doc = new jsPDF();

  const codigo = document.getElementById("detalleCodigo").textContent.trim();
  const cantidad = parseInt(document.getElementById("detalleCantidadInput").value) || 1;
  const cantidadDisponible = parseInt(document.getElementById("detalleCantidadTexto").textContent) || 0;

  if (!codigo) {
    alert("No hay código disponible para imprimir.");
    return;
  }

  if (cantidad > cantidadDisponible) {
    alert(`Solo hay ${cantidadDisponible} unidades disponibles. No puedes imprimir más etiquetas que el stock.`);
    return;
  }

  const etiquetasPorFila = 4;
  const espacioX = 50;
  const espacioY = 35;
  const margenIzquierdo = 10;
  const margenSuperior = 10;

  const pageHeight = doc.internal.pageSize.height;
  const etiquetasPorColumna = Math.floor((pageHeight - margenSuperior) / espacioY);
  const etiquetasPorPagina = etiquetasPorFila * etiquetasPorColumna;

  for (let i = 0; i < cantidad; i++) {
    const paginaActual = Math.floor(i / etiquetasPorPagina);
    const posicionEnPagina = i % etiquetasPorPagina;

    const fila = Math.floor(posicionEnPagina / etiquetasPorFila);
    const columna = posicionEnPagina % etiquetasPorFila;

    const x = margenIzquierdo + columna * espacioX;
    const y = margenSuperior + fila * espacioY;

    if (i > 0 && posicionEnPagina === 0) {
      doc.addPage();
    }

    const canvas = document.createElement("canvas");
    canvas.width = 400;
    canvas.height = 120;
    const ctx = canvas.getContext("2d");
    ctx.scale(2, 2);

    JsBarcode(canvas, codigo, {
      format: "CODE128",
      lineColor: "#000",
      width: 2,
      height: 50,
      displayValue: true,
      fontSize: 12,
      textMargin: 0
    });

    const imgData = canvas.toDataURL("image/png");
    doc.addImage(imgData, "PNG", x, y, 45, 25);
  }

  doc.save(`etiquetas_${codigo}.pdf`);
}
// Función para ampliar imagen
function ampliarImagen(src, nombre) {
    $('#imagenAmpliada').attr('src', src).attr('alt', nombre);
    $('#modalImagenTitle').text('Imagen: ' + nombre);
    const modal = new bootstrap.Modal(document.getElementById('modalImagen'));
    modal.show();
}

// Función para obtener el estado de vencimiento
function obtenerEstadoVencimiento(fechaVencimiento) {
    if (!fechaVencimiento)
        return 'no-aplica';

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    
    const vencimiento = new Date(fechaVencimiento);
    vencimiento.setHours(0, 0, 0, 0);
    
    const diferenciaDias = Math.ceil((vencimiento - hoy) / (1000 * 60 * 60 * 24));

    if (diferenciaDias < 0) {
        return 'vencido';
    } else if (diferenciaDias <= 30) {
        return 'proximo-vencer';
    } else {
        return 'vigente';
    }
}

// Función para calcular días restantes
function calcularDiasRestantes(fechaVencimiento) {
    if (!fechaVencimiento) return null;

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    
    const vencimiento = new Date(fechaVencimiento);
    vencimiento.setHours(0, 0, 0, 0);
    
    return Math.ceil((vencimiento - hoy) / (1000 * 60 * 60 * 24));
}

// Función formatearFecha
function formatearFecha(fechaString) {
    if (!fechaString || fechaString === 'null' || fechaString === 'undefined')
        return 'N/A';

    // Si ya está en formato DD-MM-YYYY, devolverlo tal cual
    if (typeof fechaString === 'string' && fechaString.match(/\d{1,2}-\d{1,2}-\d{4}/)) {
        return fechaString;
    }

    // Para formato YYYY-MM-DD (desde la base de datos)
    if (fechaString.match(/^\d{4}-\d{1,2}-\d{1,2}$/)) {
        const [año, mes, dia] = fechaString.split('-');
        return `${dia.padStart(2, '0')}-${mes.padStart(2, '0')}-${año}`;
    }

    // Para otros formatos de fecha (como los de vencimiento)
    try {
        const fecha = new Date(fechaString);
        if (isNaN(fecha.getTime())) {
            return fechaString;
        }
        const dia = fecha.getDate().toString().padStart(2, '0');
        const mes = (fecha.getMonth() + 1).toString().padStart(2, '0');
        const año = fecha.getFullYear();
        return `${dia}-${mes}-${año}`;
    } catch (error) {
        return fechaString;
    }
}

function recargarInventario() {
    Swal.fire({
        title: 'Actualizando inventario',
        text: 'Por favor espere...',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    cargarInventario();
    setTimeout(() => {
        Swal.close();
    }, 1500);
}