<%-- 
    Document   : usuario_crud
    Created on : 17 oct. 2025, 21:57:35
    Author     : Maris
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.ues.edu.models.Usuario" %>

<%
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("Usuario") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Usuario usuario = (Usuario) sesion.getAttribute("Usuario");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Marvela | Datos del Usuarios</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="categoria.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    </head>

    <body>
        <aside class="sidebar activo" id="sidebar">
            <div class="menu-header" id="colapsarMenu">
                <i class="fas fa-bars"></i> <span>Menú</span>
            </div>
            <nav class="menu">
               <a href="index.jsp" id="inicio"><i class="fas fa-home"></i> <span>Inicio</span></a>
                <a href="frmRol.jsp" id="roles"><i class="fas fa-user-tag"></i> <span>Roles</span></a>
                <a href="frmEmpleado.jsp" id="empleados"><i class="fas fa-user-tie"></i> <span>Empleados</span></a>
                <a href="usuario_crud.jsp" id="usuario"><i class="fas fa-user-shield"></i> <span>Usuarios</span></a>
                <a href="proveedores_crud.jsp" id="proveedores"><i class="fas fa-handshake"></i> <span>Proveedores</span></a>
                <a href="categoria_crud.jsp" id="categoria"><i class="fas fa-tags"></i> <span>Categoría</span></a>
                <a href="Unidad_Medida_crud.jsp" id="unidadMedida"><i class="fas fa-ruler-combined"></i> <span>Unidad de Medida</span></a>
                <a href="marca_crud.jsp" id="marca"><i class="fas fa-stamp"></i> <span>Marca</span></a>
                <a href="productos_crud.jsp" id="productos"><i class="fas fa-cubes"></i> <span>Productos</span></a>
                <a href="compras_crud.jsp" id="compras"><i class="fas fa-shopping-cart"></i> <span>Compras</span></a>
                <a href="frmInventario.jsp" id="inventario"><i class="fas fa-warehouse"></i> <span>Inventario</span></a>              
                <a href="vistaEscaneos.jsp" id="ventas"><i class="fas fa-shopping-cart"></i> <span>Ventas</span></a>
                <a href="Backup.jsp" id="backup"><i class="fas fa-database"></i> <span>Backup</span></a>
                
            </nav>
        </aside>

        <main>
            <header class="barra-superior">
                <div class="marca">Marvela</div>
                 <div class="derecha">
                    <i class="fas fa-user-circle campana">
                        <span class="badge-notificacion">1</span>
                    </i>
                    <div class="usuario-dropdown">
                        <div class="usuario-dropdown-toggle" id="usuarioToggle">
                            <span id="nombreUsuario"><%= usuario.getEmpleado().getNombreCompleto()%></span>
                            <i class="fas fa-caret-down"></i>
                        </div>
                        <div class="usuario-dropdown-menu" id="usuarioMenu">
                            <a href="#">Mi perfil</a>
                            <a href="acceso.jsp">Cerrar sesión</a>
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

            <!-- Agrega este script en tu <head> o antes del cierre de </body> -->
            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>

            <!-- Modal: Nuevo Producto -->

            <!-- Modal: Nuevo Producto -->

            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">

                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO USUARIO
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form id="formulario_insert" data-parsley-validate >
                            <div class="modal-body">
                                <label for="nombre">Nombre Empleado<span class="text-danger">*</span></label></label>
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
                                <label for="correo">Correo <span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-3 position-relative">
                                    <input type="email" class="form-control" id="correo" name="correo"
                                           placeholder="Ej. contacto@elsol.com"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese un correo válido. Ej. contacto@gmail.com')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="correo">Correo electrónico</label>
                                    <div class="invalid-feedback">
                                        Ingrese un correo válido. Ej. contacto@gmail.com
                                    </div>
                                </div>
                                <label for="correo">Clave <span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-2">
                                    <input type="password" class="form-control" name="clave" id="clave" required placeholder="Ingrese la clave">
                                    <label for="claveE">Clave</label>
                                </div>
                                <small id="contadorClave" class="form-text text-muted">0 caracteres</small>


                                <input type="hidden" id="estado" name="estado" value="true">
                                <div class="required-legend alert alert-light border mt-3 mb-0">
                                    <div class="d-flex align-items-center">
                                        <span class="text-danger me-2"><i class="fas fa-exclamation-circle"></i></span>
                                        <small class="fw-medium">Los campos marcados con <span class="text-danger">*</span> son obligatorios</small>
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
            <!-- Modal: Editar Producto -->
            <!-- MODAL EDITAR USUARIO -->
            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">

                        <!-- ENCABEZADO -->
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR USUARIO</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                        </div>

                        <!-- FORMULARIO -->
                        <form name="formulario_editar" id="formulario_editar">

                            <!-- CUERPO DEL MODAL -->
                            <div class="modal-body" style="background: #eeeeef">

                                <!-- Campo oculto: ID de usuario -->
                                <div class="form-floating mb-3" style="display: none;">
                                    <input type="text" class="form-control" name="codigo_usuario" id="codigo_usuario" readonly style="background: #eeeeef">
                                    <label for="codigo_usuario">ID</label>
                                </div>

                                <!-- Selección de empleado -->
                                <div class="mb-3">
                                    <label for="codigo_empleadoE" class="form-label">Empleado</label>
                                    <select class="form-select" id="codigo_empleadoE" name="codigo_empleadoE" required>
                                        <option value="">Seleccione un empleado</option>
                                        <!-- Opciones dinámicas aquí -->
                                    </select>
                                    <div class="invalid-feedback">El campo debe estar lleno.</div>
                                </div>

                                <!-- Correo electrónico -->
                                <div class="mb-3">
                                    <label for="correoE" class="form-label">Correo</label>
                                    <div class="form-floating position-relative">
                                        <input type="email" class="form-control" id="correoE" name="correoE"
                                               placeholder="Ej. contacto@elsol.com"
                                               required
                                               oninvalid="this.setCustomValidity('Ingrese un correo válido. Ej. contacto@gmail.com')"
                                               oninput="this.setCustomValidity('')">
                                        <label for="correoE">Correo electrónico</label>
                                        <div class="invalid-feedback">Ingrese un correo válido. Ej. contacto@gmail.com</div>
                                    </div>
                                </div>

                                <!-- Clave -->
                                <div class="mb-3">
                                    <label for="claveE" class="form-label">Clave <span class="text-danger">*</span></label>
                                    <div class="form-floating">
                                        <input type="password" class="form-control" name="claveE" id="claveE" required placeholder="Ingrese la clave" autocomplete="off">
                                        <label for="claveE">Clave</label>
                                    </div>
                                    <small id="contadorClave" class="form-text text-muted">0 caracteres</small>
                                </div>

                                <!-- Estado del usuario -->
                                <fieldset class="mb-3">
                                    <div class="text-center">
                                        <label class="form-label fs-5">Estado</label>
                                    </div>
                                    <div class="d-flex justify-content-center gap-3">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="estadoE" id="estado_activo" value="true" required>
                                            <label class="form-check-label" for="estado_activo">ACTIVO</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="estadoE" id="estado_inactivo" value="false" required>
                                            <label class="form-check-label" for="estado_inactivo">INACTIVO</label>
                                        </div>
                                    </div>
                                </fieldset>

                            </div>

                            <!-- PIE DEL MODAL -->
                            <div class="modal-footer" style="background: #eeeeef">
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Guardar</button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
            <section class="table-responsive">
                <h2 style="text-align: center;">USUARIOS</h2>
                <div id="categoriaA" class="table-responsive">   
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
            <%@include file="footer.jsp" %>
            <script src="js/usuario.js"></script>

        </main>
     
        
        <script>
                                            document.addEventListener('DOMContentLoaded', function () {
                                                const usuario = {
                                                    nombre: "<%= usuario.getEmpleado().getNombreCompleto()%>",
                                                    rol: "<%= usuario.getEmpleado().getRol().getNombreRol()%>"
                                                };

                                                // Mostrar nombre y rol
                                                $("#nombreUsuario").text(usuario.nombre);
                                                $("#rolUsuario").text(usuario.rol);

                                                // Ocultar módulos si es Empleado
                                                if (usuario.rol === "Empleado") {
                                                    $("#empleados,  #roles, #usuario,   #backup").hide();
                                                }

                                                // Menú interactivo
                                                document.getElementById('colapsarMenu')?.addEventListener('click', () => {
                                                    document.getElementById('sidebar')?.classList.toggle('colapsado');
                                                });

                                                const usuarioToggle = document.getElementById('usuarioToggle');
                                                const usuarioMenu = document.getElementById('usuarioMenu');

                                                usuarioToggle.addEventListener('click', () => {
                                                    usuarioMenu.style.display = usuarioMenu.style.display === 'flex' ? 'none' : 'flex';
                                                });

                                                document.addEventListener('click', (e) => {
                                                    if (!usuarioToggle.contains(e.target) && !usuarioMenu.contains(e.target)) {
                                                        usuarioMenu.style.display = 'none';
                                                    }
                                                });

                                                document.querySelector('.fa-bell')?.addEventListener('click', () => {
                                                    Swal.fire({
                                                        title: 'Notificaciones',
                                                        text: 'Tienes 3 nuevas notificaciones.',
                                                        icon: 'info',
                                                        confirmButtonText: 'Ver'
                                                    });
                                                });
                                            });
        </script>
    </body>
    <div id="toast" class="toast">Guardado correctamente ?</div>
</html>