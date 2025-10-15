<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Marvela | Datos del Orden Compras</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="compras.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        
    </head>

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
            <header class="boton-superior">
                <div class="boton-nuevo">
                    <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal">
                        NUEVO
                    </button>
                </div>
            </header>


            <!-- Modal: Nuevo Producto -->

            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVA COMPRA
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form id="formulario_insert">
                            <div class="modal-body">
                                <!-- Nombre Proveedor -->
                                <div
                                    <label for="nombreproveedor">Número De Orden <span class="text-danger">*</span></label></label>
                                    <div class="form-floating mb-3 position-relative">
  <input type="text"
         class="form-control"
         id="numero_de_orden"
         name="numero_de_orden"
         required
         pattern="^(?=(?:.*\d){3,})[0-9\-]+$"
         title="Debe contener solo numeros y guiones, y al menos tres digitos.">
  <label for="numero_de_orden">Numero de compra</label>

  <!-- Mensaje si está vacío -->
  <div class="invalid-feedback" id="error_vacio">
    El campo no puede estar vacio.
  </div>

  <!-- Mensaje si el formato es incorrecto -->
  <div class="invalid-feedback" id="error_formato" style="display: none;">
    se requieren minimo tres dígitos y solo se permiten números y guiones.
  </div>
</div>

                                    <!-- Fecha de compra -->
                                    <label for="nombreproveedor">Fecha De Compra <span class="text-danger">*</span></label></label>
                                    <div class="form-floating mb-3">

                                        <input type="date" class="form-control" id="fecha_de_orden" name="fecha_de_orden" pattern="\d{2}-\d{2}-\d{4}" required>
                                        <label for="fecha_compra">Fecha de compra </label>
                                        <div class="invalid-feedback">
                                            Seleccione una fecha válida en formato día/mes/año.
                                        </div>
                                    </div>
             <div class="form-floating mb-3">
  <select class="form-select" id="codigo_empleado" name="codigo_empleado" required>
    <option value="">Seleccione un empleado</option>
    <!-- Opciones dinámicas aquí -->
  </select>
  <label for="codigo_empleado">Empleado</label>
  <div class="invalid-feedback">
    El campo debe estar lleno.
  </div>
</div>
<div class="form-floating mb-3">
  <select class="form-select" id="codigo_proveedor" name="codigo_proveedor" required>
    <option value="">Seleccione un proveedor</option>
    <!-- Opciones dinámicas aquí -->
  </select>
  <label for="codigo_proveedor">Proveedor</label>
  <div class="invalid-feedback">
    El campo debe estar lleno.
  </div>
</div>
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
                            <h5 class="modal-title">EDITAR PROVEEDOR</h5>
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
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <section class="tabla_listCom">
                <h2 style="text-align: center;">ORDEN DE COMPRA</h2>
                <div id="tablaCompraA" class="tabla_listCom">   
                </div>

            </section>
            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>
            <script>


                // Código de barras en la tabla


                // Vista previa dinámica
                const inputCodigo = document.getElementById("codigo");
                const barcode = document.getElementById("barcode");


            </script>
            <!-- SweetAlert2 CDN -->
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script src="js/compras.js"></script>
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
    </body>
    <div id="toast" class="toast">Guardado correctamente ?</div>
</html>