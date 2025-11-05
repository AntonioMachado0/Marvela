<%-- 
    Document   : nuevoProductos_crud
    Created on : 3 oct. 2025, 19:13:10
    Author     : Maris
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="es">
   
    <head>
        <meta charset="UTF-8">
        <title>Marvela | Datos de Producto</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="productos.css">
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
                <a href="index.jsp"><i class="fas fa-home"></i> <span>Inicio</span></a>
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
    <div class="d-flex align-items-center">
        <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal">
            NUEVO
        </button>

        <div style="margin-left: 0.5cm;">
            <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal1">
                EXISTENTES
            </button>
        </div>

        <div style="margin-left: 0.5cm; display: flex; align-items: center;">
            <input type="text" id="numero_de_orden" class="form-control form-control-sm" placeholder="Código interno" style="width: 200px;">
        </div>
    </div>
</header>

            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO PRODUCTO
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form id="formulario_insert">
                            <div class="modal-body">
                                <!-- Nombre Proveedor -->
                               
                                <div class="row g-2">
                                    <div class="row g-2">
                                        
                                        <div class="col-md-5">
                                             <div <label for="nombreproveedor">Codigo de producto <span class="text-danger">*</span></label></label>
                                </div>
                                            <div class="form-floating mb-2" style="max-width: 280px;">
                                                <input type="text" class="form-control form-control-sm" id="numero_de_orden" name="numero_de_orden" placeholder="Ej. Compra 001">
                                                <label for="numero_de_orden" style="font-size: 0.8rem;">Número de compra</label>
                                                <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                    Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="col-md-5">
                                            <div <label for="nombreproveedor">Codigo de producto <span class="text-danger"></span></label></label>
                                </div>
                                            <div class="form-floating mb-2" style="max-width: 280px;">
                                                <input type="text" class="form-control form-control-sm" id="codigo_interno" name="codigo_interno" placeholder="Ej. INT-001">
                                                <label for="codigo_interno" style="font-size: 0.8rem;">Código interno</label>
                                                <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                    Ingrese un código alfanumérico válido.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                             <div <label for="nombreproveedor">N <span class="text-danger">*</span></label></label>
                                </div>
                                            <div class="form-floating mb-2" style="max-width: 50px;">
                                                <input type="number" class="form-control form-control-sm" id="cantidad_unidades" name="cantidad_unidades"
                                                       min="1" step="1" required placeholder="Ej. 10">
                                                <label for="cantidad_unidades" style="font-size: 0.8rem;">Cantidad <span class="text-danger">*</span></label>
                                                <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                    Ingrese una cantidad válida (mínimo 1).
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div><!-- Nombre de producto -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="nombre_producto" class="form-label" style="font-size: 0.85rem;">Nombre de producto <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control form-control-sm" id="nombre_producto" name="nombre_producto"
                                           required placeholder="Ej. Producto A"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>
                                <!-- Descripción de producto -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="descripcion_producto" class="form-label" style="font-size: 0.85rem;">Descripción de producto <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control form-control-sm" id="descripcion_producto" name="descripcion_producto"
                                           required placeholder="Ej. Detalle técnico"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>
                                <!-- Fecha de vencimiento -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="fecha_vencimiento" class="form-label" style="font-size: 0.85rem;">Fecha de vencimiento <span class="text-danger">*</span></label>
                                    <input type="date" class="form-control form-control-sm" id="fecha_vencimiento" name="fecha_vencimiento"
                                           required pattern="\d{2}-\d{2}-\d{4}"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Seleccione una fecha válida en formato día/mes/año.
                                    </div>
                                </div>          <!-- Fila: Porcentaje + Empleado -->
                                <!-- Precio -->
                                <!-- Fila: Precio + Porcentaje -->
                                <!-- Fila: Cantidad + Precio + Porcentaje -->
                                <div class="row mb-2 align-items-end">
                                    <!-- Cantidad de unidades -->
                                    <div class="col-auto" style="max-width: 110px;">
                                        <label for="cantidad_unidades" class="form-label" style="font-size: 0.85rem;">Cantidad <span class="text-danger">*</span></label>
                                        <input type="number" id="cantidad_unidades" name="cantidad_unidades" class="form-control form-control-sm"
                                               min="1" step="1" required
                                               placeholder="Ej. 10"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese una cantidad válida (mínimo 1).
                                        </div>
                                    </div>

                                    <!-- Precio -->
                                    <div class="col-auto" style="max-width: 110px;">
                                        <label for="precio" class="form-label" style="font-size: 0.85rem;">Costo <span class="text-danger">*</span></label>
                                        <input type="number" id="precio" name="precio" class="form-control form-control-sm"
                                               min="0" step="0.01" required
                                               placeholder="Ej. 19.99"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese un precio válido mayor o igual a 0.
                                        </div>
                                    </div>

                                    <!-- Porcentaje -->
                                    <div class="col-auto" style="max-width: 110px;">
                                        <label for="porcentaje" class="form-label" style="font-size: 0.85rem;">Porcentaje <span class="text-danger">*</span></label>
                                        <input type="number" id="porcentaje" name="porcentaje" class="form-control form-control-sm"
                                               min="0" max="100" step="0.1" required
                                               placeholder="Ej. 25"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Porcentaje entre 0 y 100.
                                        </div>
                                    </div>
                                    <div class="col-auto" style="max-width: 110x;">
                                        <label for="porcentaje" class="form-label" style="font-size: 0.85rem;">Precio <span class="text-danger">*</span></label>
                                        <input type="number" id="porcentaje" name="porcentaje" class="form-control form-control-sm"
                                               min="0" max="100" step="0.1" required
                                               placeholder="Ej. 25"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Porcentaje entre 0 y 100.
                                        </div>
                                    </div>
                                </div>
                                <!-- Empleado -->
                                <div class="col">
                                    <label for="codigo_empleado" class="form-label">Empleado <span class="text-danger">*</span></label>
                                    <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione un empleado</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>

                                </div>
                                <div class="col">
                                    <label for="codigo_empleado" class="form-label">Empleado <span class="text-danger">*</span></label>
                                    <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione un empleado</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>

                                </div>
                                <div class="col">
                                    <label for="codigo_empleado" class="form-label">Empleado <span class="text-danger">*</span></label>
                                    <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione un empleado</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>

                                </div>

                                <!-- Imagen relacionada -->
                                <label for="imageFile">Imagen de producto <span class="text-danger">*</span></label>
                                <div class="form-floating mb-3 position-relative" style="max-width: 350px;">
                                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*" required
                                           style="padding: 6px 12px; font-size: 0.9rem; height: auto;" />
                                </div>
                                <!-- Vista previa en recuadro -->
                                <!-- Vista previa en recuadro -->
                                <div class="form-floating mb-3" style="max-width: 300px;">
                                    <div id="previewContainer"
                                         style="display:none; border: 1px solid #ccc; border-radius: 6px; padding: 10px; background-color: #f8f9fa; text-align: center;">

                                        <img id="imagePreview" src="#" alt="Vista previa"
                                             class="img-fluid rounded"
                                             style="max-width: 100px; max-height: 100px; margin-top: 0px;" />
                                    </div>
                                </div>
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
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-1"></i> Imprimir
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>   



            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO PRODUCTO
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form id="formulario_insert">
                            <div class="modal-body">
                                <!-- Nombre Proveedor -->
                                <div <label for="nombreproveedor">Codigo de producto <span class="text-danger">*</span></label></label>
                                    <div class="col">

                                        <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                                required data-parsley-required-message="Campo requerido"
                                                style="font-size: 0.85rem; height: 32px;">
                                            <option value="">Seleccione un empleado</option>
                                            <!-- Opciones dinámicas aquí -->
                                        </select>

                                    </div>

                                    <!-- Nombre de producto -->
                                    <div class="mb-2" style="max-width: 470px;">
                                        <label for="nombre_producto" class="form-label" style="font-size: 0.85rem;">Nombre de producto <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control form-control-sm" id="nombre_producto" name="nombre_producto"
                                               required placeholder="Ej. Producto A"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                        </div>
                                    </div>

                                    <!-- Descripción de producto -->
                                    <div class="mb-2" style="max-width: 470px;">
                                        <label for="descripcion_producto" class="form-label" style="font-size: 0.85rem;">Descripción de producto <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control form-control-sm" id="descripcion_producto" name="descripcion_producto"
                                               required placeholder="Ej. Detalle técnico"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                        </div>
                                    </div>

                                    <!-- Fecha de vencimiento -->
                                    <div class="mb-2" style="max-width: 470px;">
                                        <label for="fecha_vencimiento" class="form-label" style="font-size: 0.85rem;">Fecha de vencimiento <span class="text-danger">*</span></label>
                                        <input type="date" class="form-control form-control-sm" id="fecha_vencimiento" name="fecha_vencimiento"
                                               required pattern="\d{2}-\d{2}-\d{4}"
                                               style="font-size: 0.85rem; height: 32px;" />
                                        <div class="invalid-feedback" style="font-size: 0.75rem;">
                                            Seleccione una fecha válida en formato día/mes/año.
                                        </div>
                                    </div>          <!-- Fila: Porcentaje + Empleado -->
                                    <!-- Precio -->
                                    <!-- Fila: Precio + Porcentaje -->
                                    <!-- Fila: Cantidad + Precio + Porcentaje -->
                                    <div class="row mb-2 align-items-end">
                                        <!-- Cantidad de unidades -->
                                        <div class="col-auto" style="max-width: 140px;">
                                            <label for="cantidad_unidades" class="form-label" style="font-size: 0.85rem;">Cantidad <span class="text-danger">*</span></label>
                                            <input type="number" id="cantidad_unidades" name="cantidad_unidades" class="form-control form-control-sm"
                                                   min="1" step="1" required
                                                   placeholder="Ej. 10"
                                                   style="font-size: 0.85rem; height: 32px;" />
                                            <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                Ingrese una cantidad válida (mínimo 1).
                                            </div>
                                        </div>

                                        <!-- Precio -->
                                        <div class="col-auto" style="max-width: 140px;">
                                            <label for="precio" class="form-label" style="font-size: 0.85rem;">Precio <span class="text-danger">*</span></label>
                                            <input type="number" id="precio" name="precio" class="form-control form-control-sm"
                                                   min="0" step="0.01" required
                                                   placeholder="Ej. 19.99"
                                                   style="font-size: 0.85rem; height: 32px;" />
                                            <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                Ingrese un precio válido mayor o igual a 0.
                                            </div>
                                        </div>

                                        <!-- Porcentaje -->
                                        <div class="col-auto" style="max-width: 140px;">
                                            <label for="porcentaje" class="form-label" style="font-size: 0.85rem;">Porcentaje <span class="text-danger">*</span></label>
                                            <input type="number" id="porcentaje" name="porcentaje" class="form-control form-control-sm"
                                                   min="0" max="100" step="0.1" required
                                                   placeholder="Ej. 25"
                                                   style="font-size: 0.85rem; height: 32px;" />
                                            <div class="invalid-feedback" style="font-size: 0.75rem;">
                                                Porcentaje entre 0 y 100.
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Empleado -->
                                    <div class="col">
                                        <label for="codigo_empleado" class="form-label">Empleado <span class="text-danger">*</span></label>
                                        <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                                required data-parsley-required-message="Campo requerido"
                                                style="font-size: 0.85rem; height: 32px;">
                                            <option value="">Seleccione un empleado</option>
                                            <!-- Opciones dinámicas aquí -->
                                        </select>

                                    </div>
                                    <div class="col">
                                        <label for="codigo_empleado" class="form-label">Empleado <span class="text-danger">*</span></label>
                                        <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                                required data-parsley-required-message="Campo requerido"
                                                style="font-size: 0.85rem; height: 32px;">
                                            <option value="">Seleccione un empleado</option>
                                            <!-- Opciones dinámicas aquí -->
                                        </select>

                                    </div>
                                    <div class="col">
                                        <label for="codigo_empleado" class="form-label">Empleado <span class="text-danger">*</span></label>
                                        <select name="codigo_empleado" id="codigo_empleado" class="form-select empleado-select"
                                                required data-parsley-required-message="Campo requerido"
                                                style="font-size: 0.85rem; height: 32px;">
                                            <option value="">Seleccione un empleado</option>
                                            <!-- Opciones dinámicas aquí -->
                                        </select>

                                    </div>
                                </div>
                                <!-- Imagen relacionada -->
                                <label for="imageFile">Imagen de producto <span class="text-danger">*</span></label>
                                <div class="form-floating mb-3 position-relative" style="max-width: 350px;">
                                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*" required
                                           style="padding: 6px 12px; font-size: 0.9rem; height: auto;" />
                                </div>
                                <!-- Vista previa en recuadro -->
                                <!-- Vista previa en recuadro -->
                                <div class="form-floating mb-3" style="max-width: 300px;">
                                    <div id="previewContainer"
                                         style="display:none; border: 1px solid #ccc; border-radius: 6px; padding: 10px; background-color: #f8f9fa; text-align: center;">

                                        <img id="imagePreview" src="#" alt="Vista previa"
                                             class="img-fluid rounded"
                                             style="max-width: 100px; max-height: 100px; margin-top: 0px;" />
                                    </div>
                                </div>
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
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-1"></i> Imprimir
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>   
























            <!-- Modal: Editar Proveedor -->
            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR PRODUCTO</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form name="formulario_editar" id="formulario_editar">
                            <div class="modal-body" style="background: #eeeeef">


                                <input type="hidden" id="codigo_compra" name="codigo_compra">
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="numero_de_ordenE" name="numero_de_orden" required minlength="5" maxlength="100">
                                    <label for="numero_de_ordenE">Número De Orden</label>
                                    <div class="invalid-feedback">
                                        Ingrese un número entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <!-- ? Fecha de compra -->
                                <div class="mb-3">
                                    <label for="fecha_de_ordenE">Fecha De Compra <span class="text-danger">*</span></label>
                                    <div class="form-floating">
                                        <input type="date" class="form-control" id="fecha_de_ordenE" name="fecha_de_orden" required>
                                        <label for="fecha_de_ordenE">Fecha de compra</label>
                                        <div class="invalid-feedback">
                                            Seleccione una fecha válida en formato día/mes/año.
                                        </div>
                                    </div>
                                </div>

                                <!-- ? Combo empleado -->
                                <div class="mb-4">
                                    <label for="codigo_empleadoE">Empleado <span class="text-danger">*</span></label>
                                    <div class="form-floating mb-2">
                                        <select name="codigo_empleadoE" id="codigo_empleadoE" class="form-select" required data-parsley-required-message="Campo requerido">
                                            <option value="">Seleccione un empleado</option>
                                        </select>
                                        <label for="codigo_empleadoE">Empleado</label>
                                    </div>
                                </div>

                                <!-- ? Combo proveedor -->
                                <div class="form-floating mb-2">
                                    <select class="form-select" id="codigo_proveedorE" name="codigo_proveedorE" required>
                                        <option value="">Seleccione un proveedor</option>
                                    </select>
                                    <label for="codigo_proveedorE">Proveedor</label>
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



            <section class="tabla_listCom">
                <h2 style="text-align: center;">DETALLE PRODUCTO</h2>
                
                <div id="proveedorA" class="tabla_listCom">   
                </div>

            </section>
            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>
            <script>
                const toggle = document.getElementById('modoOscuroToggle');
                toggle.addEventListener('click', () => {
                    document.body.classList.toggle('oscuro');
                });

                // Código de barras en la tabla


                // Vista previa dinámica
                const inputCodigo = document.getElementById("codigo");
                const barcode = document.getElementById("barcode");


            </script>
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
    

    </body>
    <div id="toast" class="toast">Guardado correctamente ?</div>
</html>