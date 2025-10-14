<%-- 
    Document   : productos_crud
    Created on : 30 sep. 2025, 20:18:21
    Author     : Maris
--%>


<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.ues.edu.models.dao.ProductoDao"%>
<%@page import="com.ues.edu.models.Productos"%>
<%@page import="com.ues.edu.models.Compras"%>
<%@page import="com.ues.edu.models.dao.ComprasDao"%>

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
                <a href="index.html"><i class="fas fa-home"></i> <span>Inicio</span></a>
                <a href="ventas.jsp"><i class="fas fa-shopping-cart"></i> <span>Ventas</span></a>
                <a href="frmEmpleado.jsp"><i class="fas fa-user-tie"></i> <span>Empleados</span></a>
                <a href="proveedores_crud.jsp"><i class="fas fa-handshake"></i> <span>Proveedores</span></a>
                <a href="categoria_crud.jsp"><i class="fas fa-tags"></i> <span>Categoría</span></a>
                <a href="frmRol.jsp"><i class="fas fa-user-shield"></i> <span>Roles</span></a>
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


                </div>
            </header>



            <!-- Modal Principal -->
            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">

                        <!-- Encabezado del Modal -->
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO PRODUCTO
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <!-- Formulario -->

                        <form id="formulario_insert" enctype="multipart/form-data">
                            <div class="modal-body">

                                <!-- Campo: Nombre de Producto -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="nombre_producto" class="form-label" style="font-size: 0.85rem;">
                                        Nombre de Producto <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control form-control-sm" id="nombre_producto" name="nombre_producto"
                                           required placeholder="Ej. Producto A"
                                           oninput="this.value = this.value.toUpperCase();"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <!-- Campo: Descripción de Producto -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="descripcion" class="form-label" style="font-size: 0.85rem;">
                                        Descripción de producto <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control form-control-sm" id="descripcion" name="descripcion"
                                           required placeholder="Ej. Detalle técnico"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <!-- Campo: Categoría -->
                                <div class="col">
                                    <label for="codigo_categoria" class="form-label">Categoria <span class="text-danger">*</span></label>
                                    <select name="codigo_categoria" id="codigo_categoria" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione una categoria</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>
                                </div>

                                <!-- Campo: Marca -->


                                <!-- Campo: Imagen de Producto -->
                                <div class="mb-3">
                                    <label for="imageFile">Imagen de producto <span class="text-danger">*</span></label>
                                    <div class="form-floating position-relative" style="max-width: 350px;">
                                        <input type="file" class="form-control" id="imagen" name="imagen" accept="image/*" required
                                               style="padding: 6px 12px; font-size: 0.9rem; height: auto;" />
                                    </div>
                                </div>

                                <!-- Vista Previa de Imagen -->
                                <div class="form-floating mb-3" style="max-width: 300px;">
                                    <div id="previewContainer"
                                         style="display:none; border: 1px solid #ccc; border-radius: 6px; padding: 10px; background-color: #f8f9fa; text-align: center;">
                                        <img id="imagePreview" src="#" alt="Vista previa"
                                             class="img-fluid rounded"
                                             style="max-width: 100px; max-height: 100px; margin-top: 0px;" />
                                    </div>
                                </div>

                                <!-- Leyenda de Campos Requeridos -->
                                <div class="mb-4">
                                    <div class="required-legend alert alert-light border mt-3 mb-0">
                                        <div class="d-flex align-items-center">
                                            <span class="text-danger me-2"><i class="fas fa-exclamation-circle"></i></span>
                                            <small class="fw-medium">Los campos marcados con <span class="text-danger">*</span> son obligatorios</small>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Pie del Modal -->
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


            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR PRODUCTO</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form name="formulario_editar" id="formulario_editar">
                            <div class="modal-body" style="background: #eeeeef">

                                <!-- ID oculto -->
                                <input type="hidden" id="id_producto" name="id_producto">
                                <input type="hidden" id="imagen_base64E" name="imagen_base64E">

                                <!-- Nombre de Producto -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="nombre_productoE" class="form-label" style="font-size: 0.85rem;">
                                        Nombre de Producto <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control form-control-sm" id="nombre_productoE" name="nombre_productoE"
                                           required placeholder="Ej. Producto A"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <!-- Descripción -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="descripcionE" class="form-label" style="font-size: 0.85rem;">
                                        Descripción de producto <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control form-control-sm" id="descripcionE" name="descripcionE"
                                           required placeholder="Ej. Detalle técnico"
                                           style="font-size: 0.85rem; height: 32px;" />
                                    <div class="invalid-feedback" style="font-size: 0.75rem;">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <!-- Categoría -->
                                <div class="mb-2" style="max-width: 470px;">
                                    <label for="codigo_categoriaE" class="form-label">Categoría <span class="text-danger">*</span></label>
                                    <select name="codigo_categoriaE" id="codigo_categoriaE" class="form-select empleado-select"
                                            required data-parsley-required-message="Campo requerido"
                                            style="font-size: 0.85rem; height: 32px;">
                                        <option value="">Seleccione una categoría</option>
                                        <!-- Opciones dinámicas -->
                                    </select>
                                </div>

                                <!-- Imagen -->
                                <div class="mb-3">
                                    <label for="imagenE">Imagen de producto <span class="text-danger">*</span></label>
                                    <div class="form-floating position-relative" style="max-width: 350px;">
                                        <input type="file" class="form-control" id="imagenE" name="imagenE" accept="image/*"
                                               style="padding: 6px 12px; font-size: 0.9rem; height: auto;" />
                                        <input type="hidden" id="imagen_base64E" name="imagen_base64E" />
                                    </div>
                                </div>

                                <!-- Vista actual de imagen desde BD -->
                                <div class="form-floating mb-3" style="max-width: 300px;">
                                    <label class="form-label" style="font-size: 0.85rem;">Vista actual</label>
                                    <div id="previewContainerE"
                                         style="display:none; border: 1px solid #ccc; border-radius: 6px; padding: 10px; background-color: #f8f9fa; text-align: center;">
                                        <img id="imagePreviewE" src="#" alt="Vista actual"
                                             class="img-fluid rounded"
                                             style="max-width: 100px; max-height: 100px; margin-top: 0px;" />
                                    </div>
                                </div>

                                <!-- Footer -->
                                <div class="modal-footer" style="background: #eeeeef">
                                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                    <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Guardar</button>
                                    <button type="submit" class="btn btn-outline-success" name="accion" value="Imprimir">Imprimir</button>
                                </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>



        <section class="tabla_listCom">
            <h2 style="text-align: center;">PRODUCTOS</h2>

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
        <script src="js/productos.js"></script>
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