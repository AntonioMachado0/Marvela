// Funciones JavaScript actualizadas para devoluciones con manejo de cantidades
function realizarDevolucion(numeroVenta) {
    const ventaCard = document.querySelector(`[data-numero-venta="${numeroVenta}"]`);
    const productosVenta = [];

    // Obtener productos de la venta con todos sus datos
    ventaCard.querySelectorAll('.producto-item').forEach(item => {
        const nombre = item.querySelector('.producto-nombre').textContent;
        const cantidad = parseInt(item.querySelector('.producto-cantidad').textContent);
        const idProducto = item.dataset.productoId;
        const codigoDetalleVenta = item.dataset.codigoDetalleVenta;
        const codigoDetalleCompra = item.dataset.codigoDetalleCompra;
        const cantidadOriginal = parseInt(item.dataset.cantidadOriginal);

        productosVenta.push({
            nombre,
            cantidad: cantidadOriginal,
            idProducto,
            codigoDetalleVenta,
            codigoDetalleCompra
        });
    });

    // Cargar productos disponibles y mostrar modal
    cargarProductosDisponibles().then(productosDisponibles => {
        mostrarModalDevolucion(numeroVenta, productosVenta, productosDisponibles);
    }).catch(error => {
        Swal.fire('Error', 'No se pudieron cargar los productos disponibles', 'error');
        console.error('Error:', error);
    });
}

function cargarProductosDisponibles() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: 'ObtenerProductosDisponiblesServlet',
            type: 'GET',
            dataType: 'json',
            success: function (productos) {
                resolve(productos);
            },
            error: function (xhr, status, error) {
                reject(error);
            }
        });
    });
}

function mostrarModalDevolucion(numeroVenta, productosVenta, productosDisponibles) {
    let htmlProductosVenta = '';

    productosVenta.forEach((producto, index) => {
        htmlProductosVenta += `
        <div class="form-check mb-3 p-3 border rounded shadow-sm">
            <div class="d-flex justify-content-between align-items-center">
                <div class="form-check">
                    <input class="form-check-input producto-radio" type="radio" name="productoDevuelto"
                        id="prodDevuelto${index}"
                        value="${producto.idProducto}"
                        data-codigo-detalle-venta="${producto.codigoDetalleVenta}"
                        data-codigo-detalle-compra="${producto.codigoDetalleCompra}"
                        data-cantidad-original="${producto.cantidad}">
                    <label class="form-check-label fw-bold" for="prodDevuelto${index}">
                        ${producto.nombre}
                    </label>
                </div>
                <div>
                    <label for="cantidadVendida${index}" class="form-label mb-0">Cantidad vendida:</label>
                    <input type="number" class="form-control form-control-sm d-inline-block"
                        id="cantidadVendida${index}"
                        value="${producto.cantidad}"
                        min="1" max="${producto.cantidad}" readonly
                        style="width: 80px;">
                </div>
            </div>
        </div>
        `;
    });

    const modalHTML = `
    <div class="modal fade" id="modalDevolucion" tabindex="-1" aria-labelledby="modalDevolucionLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title" id="modalDevolucionLabel">DEVOLUCIÓN DE PRODUCTO</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    <p class="mb-3 text-muted">Selecciona el producto que deseas devolver de la venta <strong>#${numeroVenta}</strong>.</p>
                    ${htmlProductosVenta}
                    <div class="mb-3">
                        <label class="form-label fw-bold" for="codigoProducto">Nuevo producto:</label>
                        <input type="text" class="form-control" id="codigoProducto" name="codigoProducto" placeholder="Código del nuevo producto">
                        <div id="infoProductoNuevo" class="mt-2"></div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Cantidad a intercambiar:</label>
                        <div class="input-group">
                            <input type="number" class="form-control" id="cantidadIntercambio" min="1" value="1" max="0">
                            <span class="input-group-text" id="cantidadMaxima">Máx: 0</span>
                        </div>
                        <div class="form-text">Cantidad máxima disponible: <span id="cantidadDisponibleText">0</span></div>
                    </div>
                    <div class="alert alert-info">
                        <small>
                            <i class="fas fa-info-circle"></i>
                            El producto devuelto se reingresará al inventario y se realizará el intercambio con el nuevo producto ingresado.
                        </small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-success" id="btnConfirmarDevolucion">Confirmar devolución</button>
                </div>
            </div>
        </div>
    </div>
    `;

    // Eliminar cualquier modal anterior para evitar duplicados
    const modalExistente = document.getElementById('modalDevolucion');
    if (modalExistente) modalExistente.remove();

    document.body.insertAdjacentHTML('beforeend', modalHTML);
    const modal = new bootstrap.Modal(document.getElementById('modalDevolucion'));
    modal.show();

    // Listeners para actualizar cantidad máxima y mostrar info del nuevo producto
    const radios = document.querySelectorAll('.producto-radio');
    const cantidadInput = document.getElementById('cantidadIntercambio');
    const cantidadMaxSpan = document.getElementById('cantidadMaxima');
    const cantidadDisponibleSpan = document.getElementById('cantidadDisponibleText');
    const codigoInput = document.getElementById('codigoProducto');
    const infoContainer = document.getElementById('infoProductoNuevo');

    radios.forEach(radio => {
        radio.addEventListener('change', function () {
            const cantidadOriginal = parseInt(this.getAttribute('data-cantidad-original'));
            cantidadInput.max = cantidadOriginal;
            cantidadInput.value = Math.min(1, cantidadOriginal);
            cantidadMaxSpan.textContent = `Máx: ${cantidadOriginal}`;
            cantidadDisponibleSpan.textContent = cantidadOriginal;
        });
    });

    // 🔹 Actualiza cantidad máxima según el stock disponible del nuevo producto
    codigoInput.addEventListener('input', function () {
        const codigoIngresado = this.value.trim();
        const producto = productosDisponibles.find(p => p.codigo_producto === codigoIngresado);

        if (producto) {
            infoContainer.innerHTML = `
                <div class="alert alert-success">
                    <strong>${producto.nombre_producto}</strong><br>
                    Marca: ${producto.marca}<br>
                    Medida: ${producto.medida_producto} ${producto.medida}<br>
                    Stock disponible: ${producto.stock_total}<br>
                    Precio de venta: $${producto.precio_venta}
                </div>
            `;

            if (producto.stock_total > 0) {
                cantidadInput.max = producto.stock_total;
                cantidadInput.value = 1;
                cantidadDisponibleSpan.textContent = producto.stock_total;
                cantidadMaxSpan.textContent = `Máx: ${producto.stock_total}`;
            } else {
                cantidadInput.max = 0;
                cantidadInput.value = 0;
                cantidadDisponibleSpan.textContent = 0;
                cantidadMaxSpan.textContent = 'Máx: 0';
            }
        } else {
            infoContainer.innerHTML = `<div class="alert alert-warning">Código no válido o sin stock disponible</div>`;
            cantidadInput.max = 0;
            cantidadDisponibleSpan.textContent = 0;
            cantidadMaxSpan.textContent = 'Máx: 0';
        }
    });

    // Confirmar devolución
    document.getElementById('btnConfirmarDevolucion').addEventListener('click', () => {
        const productoDevueltoRadio = document.querySelector('input[name="productoDevuelto"]:checked');
        const codigoProductoNuevo = codigoInput.value.trim();
        const cantidad = parseInt(cantidadInput.value);
        const cantidadMaxima = parseInt(cantidadInput.max);

        if (!productoDevueltoRadio) {
            Swal.fire('Error', 'Debe seleccionar un producto a devolver', 'error');
            return;
        }
        if (!codigoProductoNuevo) {
            Swal.fire('Error', 'Debe ingresar el código del nuevo producto', 'error');
            return;
        }

        const productoEncontrado = productosDisponibles.find(p => p.codigo_producto === codigoProductoNuevo);
        if (!productoEncontrado) {
            Swal.fire('Error', 'El código ingresado no corresponde a ningún producto disponible', 'error');
            return;
        }

        if (!cantidad || cantidad <= 0 || cantidad > cantidadMaxima) {
            Swal.fire('Error', `La cantidad debe estar entre 1 y ${cantidadMaxima}`, 'error');
            return;
        }

        const datos = {
            productoDevueltoId: productoDevueltoRadio.value,
            productoNuevoId: productoEncontrado.id_producto,
            cantidad: cantidad,
            codigoDetalleVenta: productoDevueltoRadio.dataset.codigoDetalleVenta,
            codigoDetalleCompra: productoDevueltoRadio.dataset.codigoDetalleCompra,
            cantidadOriginal: parseInt(productoDevueltoRadio.dataset.cantidadOriginal)
        };

        console.log("Datos enviados para devolución:", datos);
        procesarDevolucionCompleta(numeroVenta, datos);
    });
}

function procesarDevolucionCompleta(numeroVenta, datos) {
    Swal.fire({
        title: 'Procesando Intercambio...',
        html: `
            <div class="text-center">
                <div class="spinner-border text-primary mb-3" role="status">
                    <span class="visually-hidden">Procesando...</span>
                </div>
                <p>Realizando devolución e intercambio de productos</p>
                <p><strong>Venta:</strong> ${numeroVenta}</p>
                <p><strong>Cantidad:</strong> ${datos.cantidad} de ${datos.cantidadOriginal}</p>
            </div>
        `,
        allowOutsideClick: false,
        showConfirmButton: false,
        didOpen: () => {
            $.ajax({
                url: 'ProcesarDevolucionServlet',
                type: 'POST',
                data: {
                    numeroVenta: numeroVenta,
                    productoDevueltoId: datos.productoDevueltoId,
                    productoNuevoId: datos.productoNuevoId,
                    cantidad: datos.cantidad,
                    codigoDetalleVenta: datos.codigoDetalleVenta,
                    codigoDetalleCompra: datos.codigoDetalleCompra,
                    cantidadOriginal: datos.cantidadOriginal
                },
                success: function (response) {
                    Swal.close();

                    let result;
                    try {
                        result = typeof response === 'string' ? JSON.parse(response) : response;
                    } catch (e) {
                        console.error('Error parsing response:', e);
                        result = { success: false, message: 'Error en formato de respuesta' };
                    }

                    if (result.success) {
                        Swal.fire({
                            title: '¡Intercambio Exitoso!',
                            html: `
                                <div class="text-center">
                                    <div class="mb-3">
                                        <i class="fas fa-check-circle text-success" style="font-size: 4rem;"></i>
                                    </div>
                                    <h4 class="text-success">Devolución Procesada Correctamente</h4>
                                    <div class="alert alert-success mt-3">
                                        <p class="mb-1"><strong>Venta:</strong> ${numeroVenta}</p>
                                        <p class="mb-1"><strong>Cantidad intercambiada:</strong> ${datos.cantidad} de ${datos.cantidadOriginal}</p>
                                        <p class="mb-0"><strong>Productos actualizados correctamente</strong></p>
                                    </div>
                                    <p class="text-muted mt-3">
                                        <i class="fas fa-info-circle"></i>
                                        El inventario ha sido actualizado correctamente.
                                    </p>
                                </div>
                            `,
                            icon: 'success',
                            confirmButtonColor: '#328266',
                            confirmButtonText: 'Aceptar'
                        }).then(() => {
                            window.location.reload();
                        });
                    } else {
                        Swal.fire({
                            title: 'Error',
                            text: result.message || 'Error al procesar la devolución',
                            icon: 'error',
                            confirmButtonColor: '#d33'
                        });
                    }
                },
                error: function (xhr, status, error) {
                    Swal.close();
                    Swal.fire({
                        title: 'Error de Comunicación',
                        text: 'No se pudo conectar con el servidor: ' + error,
                        icon: 'error',
                        confirmButtonColor: '#d33'
                    });
                }
            });
        }
    });
}

// Funciones auxiliares existentes
function filtrarVentas() {
    const filtro = $('#filtroVentas').val().toLowerCase().trim();
    let ventasVisibles = 0;
    const totalVentas = $('.venta-card').length;

    $('.no-resultados').remove();

    if (filtro === '') {
        $('.venta-card').removeClass('hidden');
        ventasVisibles = totalVentas;
    } else {
        $('.venta-card').each(function () {
            const numeroVenta = $(this).data('numero-venta').toString().toLowerCase();

            if (numeroVenta.includes(filtro)) {
                $(this).removeClass('hidden');
                ventasVisibles++;
            } else {
                $(this).addClass('hidden');
            }
        });

        if (ventasVisibles === 0 && filtro !== '') {
            mostrarMensajeNoResultados(filtro);
        }
    }

    $('#ventasVisibles').text(ventasVisibles);
    $('#ventasTotales').text(totalVentas);
}

function actualizarContadorVentas() {
    const totalVentas = $('.venta-card').length;
    const ventasVisibles = $('.venta-card:not(.hidden)').length;

    $('#ventasVisibles').text(ventasVisibles);
    $('#ventasTotales').text(totalVentas);
}

function limpiarFiltro() {
    $('#filtroVentas').val('');
    $('.no-resultados').remove();
    filtrarVentas();
    $('#filtroVentas').focus();
}

function recargarVentas() {
    Swal.fire({
        title: 'Recargando ventas...',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    setTimeout(() => {
        window.location.reload();
    }, 1000);
}

function mostrarMensajeNoResultados(filtro) {
    $('.no-resultados').remove();

    const mensaje = `
        <div class="no-ventas no-resultados">
            <i class="fas fa-search"></i>
            <h3>No se encontraron ventas</h3>
            <p>No hay ventas que coincidan con "<strong>${filtro}</strong>"</p>
            <button class="btn-filtro btn-limpiar" onclick="limpiarFiltro()" style="margin-top: 15px;">
                <i class="fas fa-times"></i> Limpiar búsqueda
            </button>
        </div>
    `;

    $('#ventasCards').append(mensaje);
}
