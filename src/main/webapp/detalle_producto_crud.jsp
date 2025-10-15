<%-- 
    Document   : producto
    Created on : 1 oct 2025, 22:06:07
    Author     : thebe
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Marvela | Datos de Producto</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="proveedores.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <!-- CSS de Flatpickr -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

        <!-- JS de Flatpickr -->
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

    </head>
    <style>
        .custom-file-wrapper {
            display: flex;
            align-items: center;
            gap: 10px;
        }
    </style>
    <body>
        <aside class="sidebar activo" id="sidebar">
            <div class="menu-header" id="colapsarMenu">
                <i class="fas fa-bars"></i> <span>Menú</span>
            </div>
            <nav class="menu">
                <a href="index.html"><i class="fas fa-home"></i> <span>Inicio</span></a>
                <a href="ventas.jsp"><i class="fas fa-shopping-cart"></i> <span>Ventas</span></a>
                <a href="frmEmpleado.jsp"><i class="fas fa-user-tie"></i> <span>Empleados</span></a>
                <a href="proveedores_crud.jsp"><i class="fas fa-handshake"></i> <span>Proveedores</span></a>
                <a href="categoria_crud.jsp"><i class="fas fa-tags"></i> <span>Categoría</span></a>
                <a href="frmRol.jsp"><i class="fas fa-user-tag"></i> <span>Roles</span></a>
                <a href="Unidad_Medida_crud.jsp"><i class="fas fa-ruler-combined"></i> <span>Unidad de Medida</span></a>
                <a href="marca_crud.jsp"><i class="fas fa-stamp"></i> <span>Marca</span></a>
                <a href="compras_crud.jsp"><i class="fas fa-shopping-cart"></i> <span>Compras</span></a>
                
                <a href="frmInventario.jsp"><i class="fas fa-warehouse"></i> <span>Inventario</span></a>
                <a href="productos_crud.jsp"><i class="fas fa-cubes"></i> <span>Productos</span></a>
                <a href="vistaEscaneos.jsp"><i class="fas fa-barcode"></i> <span>Productos Escaneados</span></a>
                <a href="Backup.jsp"><i class="fas fa-database"></i> <span>Backup</span></a>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
            </nav>
        </aside>

        <main>
            <header class="barra-superior">
                <div class="marca">Marvela</div>
                <div class="derecha">
                    <i class="fas fa-bell campana">
                        <span class="badge-notificacion">3</span>
                    </i>
                    <div class="usuario-dropdown">
                        <div class="usuario-dropdown-toggle" id="usuarioToggle">
                            <span>Katherine</span>
                            <i class="fas fa-caret-down"></i>
                        </div>
                        <div class="usuario-dropdown-menu" id="usuarioMenu">
                            <a href="#">Mi perfil</a>
                            <a href="#">Cerrar sesión</a>
                        </div>
                    </div>

                </div>


            </header>

            <header class="d-flex align-items-center px-3" style="height: 60px;">
                <div>
                    <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal">
                        NUEVO
                    </button>
                    <button type="button" class="btn-marvela" onclick="window.location.href = 'compras_crud.jsp'">
                        ORDEN DE COMPRA
                    </button>
                </div>

            </header>

            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO DETALLE DE PRODUCTO
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form id="formulario_insert">
                            <div class="modal-body">
                                <!-- Nombre Proveedor -->

                                <div class="row g-2">
                                    <div style="display: none;">
                                        <label for="numero_de_orden" style="font-size: 0.8rem; display: block; margin-bottom: 4px;">
                                            ID Orden de compra
                                        </label>
                                        <input type="text" class="form-control" id="numero_de_orden" name="numero_de_orden"
                                               value="12345" disabled style="font-size: 0.55rem;">
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                        </div>
                                    </div>

                                    <div style="max-width: 150px; margin-top: 16px;">
                                        <label for="codigo_producto" style="font-size: 0.8rem; display: block; margin-bottom: 4px;">
                                            Número de Orden
                                        </label>
                                        <input type="text" class="form-control" id="codigoOrden" name="codigoOrden"
                                               placeholder="Ej: PROD-001" style="font-size: 0.85rem;">
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese un código válido de producto.
                                        </div>
                                    </div>
                                    <div class="row g-2">


                                        <div style="display: flex; align-items: center; gap: 16px; max-width: 680px; margin-top: 16px; flex-wrap: wrap;">

                                            <!-- Campo: Código de producto -->
                                            <div style="flex: 1 1 160px;">
                                                <label for="codigo_producto" class="form-label" style="font-size: 0.8rem;">Código Producto <span class="text-danger">*</span></label>
                                                <input type="text"
                                                       class="form-control form-control-sm"
                                                       id="codigo_producto"
                                                       name="codigo_producto"
                                                       placeholder="Ej. HJK1234567895"
                                                       required
                                                       maxlength="13"
                                                       pattern="^[A-Z]{3}[0-9]{10}$"
                                                       data-parsley-pattern="^[A-Z]{3}[0-9]{10}$"
                                                       data-parsley-pattern-message="Debe contener exactamente 3 letras seguidas de 10 números"
                                                       style="font-size: 0.75rem; height: 40px;"
                                                       oninput="validarCodigoProducto(this); generarCodigoBarra();" />
                                                <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                    Ingrese un código con 3 letras seguidas de 10 números (total 13 caracteres).
                                                </div>
                                            </div>

                                            <!-- Código de barras -->
                                            <div style="flex: 0 0 160px; text-align: center;">
                                                <svg id="barcode" style="width: 150px; height: 40px; margin: 0 auto; display: block;"></svg>
                                            </div>

                                            <!-- Spinner de cantidad -->
                                            <div style="flex: 0 0 80px; text-align: center;">
                                                <label for="cantidad" style="font-size: 0.8rem;">Cantidad Cod</label>
                                                <input type="number"
                                                       id="cantidad"
                                                       name="cantidad"
                                                       min="1"
                                                       max="100"
                                                       value="1"
                                                       class="form-control form-control-sm"
                                                       style="width: 60px; height: 40px;" />
                                            </div>
                                        </div>

                                        <script>
                                            function validarCodigoProducto(input) {
                                                let valor = input.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
                                                let letras = '';
                                                let numeros = '';

                                                for (let i = 0; i < valor.length && (letras.length + numeros.length) < 13; i++) {
                                                    const char = valor[i];
                                                    if (letras.length < 3 && /[A-Z]/.test(char)) {
                                                        letras += char;
                                                    } else if (letras.length === 3 && /[0-9]/.test(char)) {
                                                        numeros += char;
                                                    }
                                                }

                                                input.value = letras + numeros;
                                            }
                                        </script>

                                        <!-- JsBarcode -->
                                        <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>

                                        <script>
                                            function generarCodigoBarra() {
                                                const valor = document.getElementById("codigo_producto").value;
                                                JsBarcode("#barcode", valor, {
                                                    format: "CODE128",
                                                    lineColor: "#000",
                                                    width: 1,
                                                    height: 40,
                                                    displayValue: true,
                                                    textMargin: 0,
                                                    fontSize: 12
                                                });
                                            }

                                            // Mostrar el código al abrir
                                            window.addEventListener("DOMContentLoaded", generarCodigoBarra);
                                        </script>

                                    </div>

                                </div>

                                <div class="col">
                                    <label for="id_producto" class="form-label">Productos <span class="text-danger">*</span></label>
                                    <select name="id_producto" id="id_producto" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione un  nombre de producto</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>
                                </div>


                                <div class="row">


                                    <div class="col-md-6 mb-2">
                                        <label for="medida_producto" class="form-label" style="font-size: 0.85rem;">
                                            Medida de producto <span class="text-danger">*</span>
                                        </label>
                                        <input type="text"
                                               class="form-control form-control-sm"
                                               id="medida_producto"
                                               name="medida_producto"
                                               required
                                               placeholder="Ej. 100."
                                               pattern="^\d+(\.\d+)?$"
                                               data-parsley-pattern="^\d+(\.\d+)?$"
                                               data-parsley-pattern-message="Solo se permiten números enteros o decimales"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese solo números enteros o decimales.
                                        </div>
                                    </div>

                                    <!-- Campo 1: Modalidad de medida -->
                                    <div class="col-md-6 mb-2">
                                        <label for="id_medida" class="form-label" style="font-size: 0.85rem;">
                                            Modalidad de medida <span class="text-danger">*</span>
                                        </label>
                                        <select name="id_medida" id="id_medida" class="form-select form-select-sm empleado-select"
                                                required data-parsley-required-message="Campo requerido"
                                                style="font-size: 0.85rem; height: 32px;">
                                            <option value="">Seleccione una medida</option>
                                            <!-- Opciones dinámicas aquí -->
                                        </select>
                                    </div>

                                    <!-- Campo 2: Medida de producto (solo números enteros) -->

                                </div>
                                <!-- Fecha de vencimiento -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="fecha_vencimiento" class="form-label" style="font-size: 0.85rem;">
                                        Fecha de vencimiento <span class="text-muted">(opcional)</span>
                                    </label>
                                    <input type="date"
                                           class="form-control form-control-sm"
                                           id="fecha_vencimiento"
                                           name="fecha_vencimiento"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Seleccione una fecha válida en formato día/mes/año.
                                    </div>
                                </div>

                                <div class="row mb-2 align-items-end">
                                    <!-- Cantidad de unidades -->
                                    <div class="col" style="max-width: 95px;">
                                        <label for="cantidad_producto" class="form-label" style="font-size: 0.85rem;">Cantidad <span class="text-danger">*</span></label>
                                        <input type="number" id="cantidad_producto" name="cantidad_producto"
                                               class="form-control form-control-sm"
                                               min="1" step="1" required
                                               placeholder="Ej. 10"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese una cantidad válida (mínimo 1).
                                        </div>
                                    </div>

                                    <!-- Precio -->
                                    <div class="col" style="max-width: 100px;">
                                        <label for="precio_compra" class="form-label" style="font-size: 0.85rem;">Costo <span class="text-danger">*</span></label>
                                        <input type="number" id="precio_compra" name="precio_compra"
                                               class="form-control form-control-sm"
                                               min="0.01" step="0.01" required
                                               placeholder="Ej. 19.99"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            El costo debe ser mayor a 0.
                                        </div>
                                    </div>

                                    <!-- Porcentaje -->
                                    <div class="col" style="max-width: 100px;">
                                        <label for="porcentaje" class="form-label" style="font-size: 0.85rem;">
                                            Porcentaje <span class="text-danger">*</span>
                                        </label>
                                        <input type="number"
                                               id="porcentaje"
                                               name="porcentaje"
                                               class="form-control form-control-sm"
                                               required
                                               min="1"
                                               step="1"
                                               placeholder="Ej. 25"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese solo números enteros sin punto.
                                        </div>
                                    </div>

                                    <!-- Precio unitario -->
                                    <div class="col" style="max-width: 95px;">
                                        <label for="precioU" class="form-label" style="font-size: 0.85rem;">Precio <span class="text-danger">*</span></label>
                                        <input type="number" id="precioU" name="precioU"
                                               class="form-control form-control-sm"
                                               min="0" max="100" step="0.1" readonly
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Porcentaje entre 0 y 100.
                                        </div>
                                    </div>

                                    <!-- Total -->
                                    <div class="col" style="max-width: 95px;">
                                        <label for="precio" class="form-label" style="font-size: 0.85rem;">Total <span class="text-danger">*</span></label>
                                        <input type="number" id="precio" name="precio"
                                               class="form-control form-control-sm"
                                               min="0" max="100" step="0.1" readonly
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Porcentaje entre 0 y 100.
                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <label for="id_marca" class="form-label">Marca <span class="text-danger">*</span></label>
                                    <select name="id_marca" id="id_marca" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione un Marca</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>
                                </div>

                                <!-- Campo: Modalidad de Medida -->




                                <div class="mb-4">
                                    <div class="required-legend alert alert-light border mt-3 mb-0">
                                        <div class="d-flex align-items-center">
                                            <span class="text-danger me-2"><i class="fas fa-exclamation-circle"></i></span>
                                            <small class="fw-medium">Los campos marcados con <span class="text-danger">*</span> son obligatorios</small>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                                    <i class="fas fa-times me-1"></i> Cancelar
                                </button>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-1"></i> Guardar
                                </button>
                                <button type="button" class="btn btn-success" onclick="imprimirPDF()">
                                    <i class="fas fa-save me-1"></i> Imprimir
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR DETALLE PRODUCTO</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form name="formulario_editar" id="formulario_editar">
                            <div class="modal-body" style="background: #eeeeef">
                                <input type="hidden" id="codigo_detalle_compra" name="codigo_detalle_compra">



                                <input type="hidden" id="codigo_compraE" name="codigo_compraE" value="12345" />

                                <div style="display: flex; align-items: flex-start; gap: 12px; max-width: 680px; margin-top: 16px;">
                                    <!-- Campo flotante: Orden de compra (deshabilitado) -->
                                    <div class="form-floating mb-2" style="max-width: 280px;">
                                        <input type="text"
                                               class="form-control form-control-sm"
                                               id="numero_de_ordenE"
                                               name="numero_de_ordenE"
                                               value="12345"
                                               disabled
                                               style="font-size: 0.85rem; height: 55px; background-color: #fff;" />
                                        <label for="numero_de_ordenE" style="font-size: 0.8rem;">Orden de compra</label>
                                    </div>

                                    <!-- Campo flotante: Código de producto -->
                                    <div class="form-floating mb-2" style="max-width: 280px;">
                                        <input type="text"
                                               class="form-control form-control-sm"
                                               id="codigo_productoE"
                                               name="codigo_productoE"
                                               placeholder="Ej. Compra 001"
                                               value="000001"
                                               readonly
                                               style="font-size: 0.85rem; height: 55px; background-color: #fff;" />
                                        <label for="codigo_productoE" style="font-size: 0.8rem;">Código de producto</label>
                                    </div>
                                </div>

                                <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>
                                <script>
                                    function generarCodigoBarrax() {
                                        const valor = document.getElementById("codigo_productoE").value;
                                        JsBarcode("#barcode", valor, {
                                            format: "CODE128",
                                            lineColor: "#000",
                                            width: 1.2,
                                            height: 40,
                                            displayValue: true,
                                            textMargin: 0,
                                            fontSize: 12
                                        });
                                    }
                                    window.addEventListener("DOMContentLoaded", generarCodigoBarrax);
                                </script>

                                <div class="col mt-3">
                                    <label for="id_productoE" class="form-label">Productos <span class="text-danger"></span></label>
                                    <select name="id_productoE"
                                            id="id_productoE"
                                            class="form-select empleado-select"
                                            required
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione un nombre de producto</option>
                                    </select>
                                </div>
                                <div class="row">


                                    <div class="col-md-6 mb-2">
                                        <label for="medida_producto" class="form-label" style="font-size: 0.85rem;">
                                            Medida de producto <span class="text-danger">*</span>
                                        </label>
                                        <input type="text"
                                               class="form-control form-control-sm"
                                               id="medida_productoE"
                                               name="medida_productoE"
                                               required
                                               placeholder="Ej. 100.25"
                                               pattern="^\d+(\.\d+)?$"
                                               data-parsley-pattern="^\d+(\.\d+)?$"
                                               data-parsley-pattern-message="Solo se permiten números enteros o decimales"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese solo números enteros o decimales.
                                        </div>
                                    </div>

                                    <!-- Campo 1: Modalidad de medida -->
                                    <div class="col-md-6 mb-2">
                                        <label for="id_medidaE" class="form-label" style="font-size: 0.85rem;">
                                            Modalidad de medida <span class="text-danger">*</span>
                                        </label>
                                        <select name="id_medidaE" id="id_medidaE" class="form-select form-select-sm empleado-select"
                                                required data-parsley-required-message="Campo requerido"
                                                style="font-size: 0.85rem; height: 32px;">
                                            <option value="">Seleccione una medida</option>
                                            <!-- Opciones dinámicas aquí -->
                                        </select>
                                    </div>

                                    <!-- Campo 2: Medida de producto (solo números enteros) -->

                                </div>


                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="fecha_vencimientoE" class="form-label">Fecha de vencimiento <span class="text-danger">*</span></label>
                                    <input type="date" class="form-control form-control-sm" id="fecha_vencimientoE" name="fecha_vencimientoE" required style="font-size: 0.85rem; height: 32px;" />
                                </div>

                                <div class="row mb-2 align-items-end">
                                    <div class="col-auto" style="max-width: 110px;">
                                        <label for="cantidad_productoE" class="form-label">Cantidad <span class="text-danger">*</span></label>
                                        <input type="number" id="cantidad_productoE" name="cantidad_productoE" class="form-control form-control-sm"
                                               min="1" step="1" required
                                               placeholder="Ej. 10"
                                               style="font-size: 0.85rem; height: 32px;" />
                                    </div>

                                    <div class="col-auto" style="max-width: 110px;">
                                        <label for="precio_compraE" class="form-label">Costo <span class="text-danger"></span></label>
                                        <input type="number"
                                               id="precio_compraE"
                                               name="precio_compraE"
                                               class="form-control form-control-sm"
                                               min="0.01"
                                               step="0.01"
                                               required
                                               value="19.99"
                                               readonly
                                               style="font-size: 0.85rem; height: 32px;" />
                                    </div>

                                    <div class="col-auto" style="max-width: 110px;">
                                        <label for="porcentajeE" class="form-label">Porcentaje <span class="text-danger"></span></label>
                                        <input type="number"
                                               id="porcentajeE"
                                               name="porcentajeE"
                                               class="form-control form-control-sm"
                                               min="0"
                                               max="100"
                                               step="0.1"
                                               required
                                               value="25"
                                               readonly
                                               style="font-size: 0.85rem; height: 32px;" />
                                    </div>


                                </div>

                                <div class="col">
                                    <label for="id_marcaE" class="form-label">Marca <span class="text-danger">*</span></label>
                                    <select name="id_marcaE" id="id_marcaE" class="form-select empleado-select" required style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione una marca</option>
                                    </select>
                                </div>
                            </div> <!-- cierre modal-body -->

                            <div class="required-legend alert alert-light border mt-3 mb-0">
                                <div class="d-flex align-items-center">
                                    <span class="text-danger me-2"><i class="fas fa-exclamation-circle"></i></span>
                                    <small class="fw-medium">Los campos marcados con <span class="text-danger">*</span> son editables</small>
                                </div>
                            </div>
                            <!-- Footer -->
                            <div class="modal-footer" style="background: #eeeeef">
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Guardar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Imprimir</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <section class="tabla_listCategoria">
                <h2 style="text-align: center;">DETALLE DE PRODUCTOS</h2>
                <div id="proveedorA" class="tabla_listCategoria">   
                </div>

            </section>

            <!-- SweetAlert2 CDN -->
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script src="js/detalle_Compra.js"></script>
            <%@include file="footer.jsp" %>
        </main>
        <script>

                                    document.getElementById('colapsarMenu')?.addEventListener('click', () => {
                                        document.getElementById('sidebar')?.classList.toggle('colapsado');
                                    });

                                    document.getElementById('toggleMenu')?.addEventListener('click', () => {
                                        document.getElementById('sidebar')?.classList.toggle('activo');
                                    });

                                    // Mostrar/ocultar menú de usuario
                                    const usuarioToggle = document.getElementById('usuarioToggle');
                                    const usuarioMenu = document.getElementById('usuarioMenu');

                                    usuarioToggle.addEventListener('click', () => {
                                        usuarioMenu.style.display = usuarioMenu.style.display === 'flex' ? 'none' : 'flex';
                                    });

                                    // Cerrar el menú si se hace clic fuera
                                    document.addEventListener('click', (e) => {
                                        if (!usuarioToggle.contains(e.target) && !usuarioMenu.contains(e.target)) {
                                            usuarioMenu.style.display = 'none';
                                        }
                                    });
        </script>
        <script>
            document.getElementById("imageFile").addEventListener("change", function (event) {
                const file = event.target.files[0];
                const preview = document.getElementById("imagePreview");
                const container = document.getElementById("previewContainer");

                if (!file || !file.type.startsWith("image/")) {
                    container.style.display = "none";
                    preview.src = "#";
                    return;
                }

                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                    container.style.display = "block";
                };
                reader.readAsDataURL(file);
            });
        </script>


        <script>
            async function imprimirPDF() {
                const {jsPDF} = window.jspdf;
                const doc = new jsPDF();

                const valor = document.getElementById("codigo_producto").value || "?";
                const cantidad = parseInt(document.getElementById("cantidad").value) || 1;

                const etiquetasPorFila = 4;
                const etiquetaAncho = 45;
                const etiquetaAlto = 30;
                const espacioX = 10; // espacio entre etiquetas
                const espacioY = 40;
                const margenSuperior = 15;

                const paginaAncho = doc.internal.pageSize.getWidth();
                const filaAnchoTotal = etiquetasPorFila * etiquetaAncho + (etiquetasPorFila - 1) * espacioX;
                const margenIzquierdo = (paginaAncho - filaAnchoTotal) / 2;

                let etiquetaGenerada = 0;

                while (etiquetaGenerada < cantidad) {
                    for (let fila = 0; fila < Math.ceil(cantidad / etiquetasPorFila); fila++) {
                        for (let columna = 0; columna < etiquetasPorFila; columna++) {
                            if (etiquetaGenerada >= cantidad)
                                break;

                            const x = margenIzquierdo + columna * (etiquetaAncho + espacioX);
                            const y = margenSuperior + fila * espacioY;

                            const canvas = document.createElement("canvas");
                            canvas.width = 400;
                            canvas.height = 120;
                            const ctx = canvas.getContext("2d");
                            ctx.scale(2, 2);

                            JsBarcode(canvas, valor, {
                                format: "CODE128",
                                lineColor: "#000",
                                width: 2.5,
                                height: 60,
                                displayValue: true,
                                fontSize: 14,
                                textMargin: 2
                            });

                            const imgData = canvas.toDataURL("image/png");

                            if (y + etiquetaAlto > doc.internal.pageSize.height) {
                                doc.addPage();
                                fila = -1;
                                break;
                            }

                            doc.addImage(imgData, "PNG", x, y, etiquetaAncho, etiquetaAlto);
                            etiquetaGenerada++;
                        }
                    }
                }

                doc.save("codigos.pdf");
            }
        </script>
        <script>
            const codigoCompra = "<%= request.getParameter("codigo_compra")%>";
            console.log("Código de compra recibido:", codigoCompra);
            cargarTabla(codigoCompra); // ahora sí usa el ID dinámico
        </script>
        <script>
            document.getElementById('medida_producto').addEventListener('keypress', function (e) {
                const char = String.fromCharCode(e.which);
                const isValid = /^[0-9.]$/.test(char);

                if (!isValid) {
                    e.preventDefault(); // bloquea letras y símbolos no numéricos
                }
            });
        </script>
        <script>
            document.getElementById('id_productoE').addEventListener('mousedown', function (e) {
                e.preventDefault(); // bloquea el clic para abrir el menú
            });
        </script>

        <script>
            document.getElementById("porcentaje").addEventListener("keydown", function (e) {
                if (e.key === "." || e.key === "," || e.key === "Decimal") {
                    e.preventDefault(); //  Bloquea punto y coma decimal
                }
            });
        </script>
    </body>
    <div id="toast" class="toast">Guardado correctamente ?</div>
</html>