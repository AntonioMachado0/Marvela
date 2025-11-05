
<%@page import="java.util.List"%>
<%@page import="com.ues.edu.models.Roles"%>
<%@page import="com.ues.edu.models.dao.RolDao"%>
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
        <title>Marvela | EMPLEADO</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="Empleado.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script src="https://cdn.jsdelivr.net/npm/parsleyjs@2.9.2/dist/parsley.min.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
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
                    <button type="button" id="registrar_rol" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#md_registrar">
                        NUEVO
                    </button>
                </div>
            </header>

            <!-- Agrega este script en tu <head> o antes del cierre de </body> -->
            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>

            <!-- Modal: Nuevo EMPLEADO -->
            <div class="modal fade" id="md_registrar" tabindex="-1" aria-labelledby="nuevoProductoLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="background-color:#276b55;">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO EMPLEADO
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                        </div>

                        <div class="modal-body">
                            <form id="formulario_registro">
                                <div class="mb-3">
                                    <label for="nombre_completo" class="form-label fw-semibold">Nombre Completo<span class="text-danger">*</span></label>
                                    <input type="text" class="form-control mb-1" id="nombre_completo" name="nombre_completo"
                                           placeholder="Digite el nombre del empleado"
                                           required
                                           oninput="validarNombre(this)">
                                    <div id="error-nombre_completo" class="invalid-feedback" style="display: none; color: #dc3545; font-size: 0.875em;">
                                        ❌ Solo se permiten letras y espacios
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="numero_telefono" class="form-label fw-semibold">Teléfono <span class="text-danger">*</span></label>
                                    <input type="text" autocomplete="off" name="numero_telefono" maxlength="9" 
                                           data-parsley-pattern="^\d{4}-\d{4}$" 
                                           data-parsley-error-message="Ingrese un número de teléfono válido (####-####)" 
                                           id="numero_telefono" class="form-control" required 
                                           placeholder="Ej: 1234-5678">
                                </div>


                                <div class="mb-3">
                                    <label for="fecha_nacimiento" class="form-label fw-semibold">Fecha de Nacimiento <span class="text-danger">*</span></label>
                                    <input type="date" id="fecha_nacimiento" name="fecha_nacimiento" class="form-control" required>
                                </div>                      

                                <div class="mb-4">
                                    <label for="codigo_roles" class="form-label fw-semibold">Rol <span class="text-danger">*</span></label>
                                    <select name="codigo_roles" id="codigo_roles" class="form-select" required data-parsley-required-message="Campo requerido">
                                        <option value="">Seleccione un rol</option>
                                        <%
                                            RolDao dao = new RolDao();
                                            List<Roles> rolesDisponibles = dao.mostrarRolesDisponibles();
                                            for (Roles rol : rolesDisponibles) {%>
                                        <option value="<%= rol.getCodigoRoles()%>"><%= rol.getNombreRol()%></option>
                                        <% }%>
                                    </select>
                                </div>

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
            <!-- Incluir Font Awesome para los iconos -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

            <style>
                .required-legend {
                    background-color: #f8f9fa !important;
                    border-color: #dee2e6 !important;
                    padding: 10px 15px;
                    border-radius: 6px;
                }
                .modal-content {
                    border-radius: 10px;
                    overflow: hidden;
                    box-shadow: 0 5px 25px rgba(0,0,0,0.15);
                }
                .form-control:focus, .form-select:focus {
                    border-color: #276b55;
                    box-shadow: 0 0 0 0.25rem rgba(39, 107, 85, 0.25);
                }
                .btn-success {
                    background-color: #276b55;
                    border-color: #276b55;
                }
                .btn-success:hover {
                    background-color: #1e4c3d;
                    border-color: #1e4c3d;
                }
            </style>



            <!-- Modal: Editar Empleado -->
            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">

                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR EMPLEADO</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>                  
                        <form name="formulario_editar" id="formulario_editar">
                            <div style="background: #eeeeef" class="modal-body">
                                <div class="form-floating mb-3" style="display: none;">
                                    <input style="background: #eeeeef" type="text" class="form-control" name="codigo_empleadoE" id="codigo_empleadoE" readonly>
                                    <label for="floatingInput">ID</label>
                                </div>
                                <div class="mb-3">
                                    <input type="text" class="form-control" name="nombre_completoE" id="nombre_completoE" 
                                           required placeholder="Ingrese el nombre completo" 
                                           oninput="validarNombre(this)">
                                    <div id="error-nombre_completoE" class="invalid-feedback" style="display: none; color: #dc3545; font-size: 0.875em;">
                                        ❌ Solo se permiten letras y espacios
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <input type="text" autocomplete="off" name="numero_telefonoE" maxlength="9" 
                                           data-parsley-pattern="^\d{4}-\d{4}$" 
                                           data-parsley-error-message="Ingrese un número de teléfono válido (####-####)" 
                                           id="numero_telefonoE" class="form-control" required 
                                           placeholder="Ej: 1234-5678">
                                </div>
                                <input type="date" id="fecha_nacimientoE" name="fecha_nacimientoE" class="form-control mb-2" required>                          <select name="codigo_rolesE" id="codigo_rolesE" class="form-control" required data-parsley-required-message="Campo requerido">
                                    <option value="">Seleccione un rol</option>
                                    <%
                                        RolDao daoE = new RolDao();
                                        List<Roles> rolesDisponiblesE = daoE.mostrarCombo();
                                        for (Roles rolE : rolesDisponiblesE) {%>
                                    <option value="<%= rolE.getCodigoRoles()%>"><%= rolE.getNombreRol()%></option>
                                    <% }%>

                                </select>


                            </div>
                            <div style="background: #eeeeef" class="modal-footer">
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Guardar</button>
                            </div>
                        </form>
                    </div> 
                </div>
            </div>
            <section class="table-responsive">
                <h2 style="text-align: center;">EMPLEADOS</h2>
                <div id="empleadoA" class="table-responsive">   
                </div>

            </section>

            <!-- Script para establecer la fecha por defecto (hace 18 años) -->
            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    // Calcular fecha hace 18 años
                    const calcularFechaHace18Anios = () => {
                        const today = new Date();
                        const defaultDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());

                        // Formatear a YYYY-MM-DD para input type="date"
                        const year = defaultDate.getFullYear();
                        const month = String(defaultDate.getMonth() + 1).padStart(2, '0');
                        const day = String(defaultDate.getDate()).padStart(2, '0');
                        return `${year}-${month}-${day}`;
                                };

                                // Establecer el valor por defecto en el campo de fecha
                                const fechaInput = document.getElementById('fecha_nacimiento');
                                if (fechaInput) {
                                    fechaInput.value = calcularFechaHace18Anios();
                                    fechaInput.classList.remove('date-loading');
                                }

                                // También para el campo de edición si existe
                                const fechaEdicion = document.getElementById('fecha_nacimientoE');
                                if (fechaEdicion) {
                                    fechaEdicion.value = calcularFechaHace18Anios();
                                }

                                console.log("Fecha establecida correctamente:", calcularFechaHace18Anios());
                            });
            </script>
            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>

            <script src="js/funciones_empleado.js"></script>
            <div id="toast" class="toast">Guardado correctamente ✅</div>
            <!-- Flatpickr CSS -->
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

            <!-- Flatpickr JS -->
            <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

            <!-- Flatpickr Spanish Locale -->
            <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/es.js"></script>
            <%@include file="footer.jsp" %>


            <script>
                            // FUNCIÓN MEJORADA PARA VALIDACIÓN EN TIEMPO REAL
                            function validarNombre(input) {
                                const errorElement = document.getElementById(`error-${input.id}`);
                                const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]*$/; // Solo letras y espacios

                                // Remover espacios en blanco al inicio y final
                                const valor = input.value.trim();

                                if (valor === "") {
                                    // Campo vacío - quitar estilos de error
                                    input.classList.remove('is-invalid', 'is-valid');
                                    input.style.borderColor = '';
                                    if (errorElement)
                                        errorElement.style.display = 'none';
                                    return true;
                                }

                                if (!regex.test(valor)) {
                                    // ❌ Caracteres inválidos - poner en ROJO inmediatamente
                                    input.classList.add('is-invalid');
                                    input.classList.remove('is-valid');
                                    input.style.borderColor = '#dc3545';
                                    input.style.boxShadow = '0 0 0 0.2rem rgba(220, 53, 69, 0.25)';

                                    if (errorElement) {
                                        errorElement.textContent = '❌ Solo se permiten letras y espacios';
                                        errorElement.style.display = 'block';
                                    }
                                    return false;
                                } else {
                                    // ✅ Válido - poner en VERDE
                                    input.classList.remove('is-invalid');
                                    input.classList.add('is-valid');
                                    input.style.borderColor = '#28a745';
                                    input.style.boxShadow = '0 0 0 0.2rem rgba(40, 167, 69, 0.25)';

                                    if (errorElement)
                                        errorElement.style.display = 'none';
                                    return true;
                                }
                            }

                            // FUNCIÓN PARA VALIDAR AL ENVIAR EL FORMULARIO
                            function validarNombreSubmit(input) {
                                const errorElement = document.getElementById(`error-${input.id}`);
                                const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/; // Solo letras y espacios (al menos 1)

                                if (input.value.trim() === "") {
                                    input.classList.add('is-invalid');
                                    input.style.borderColor = '#dc3545';
                                    if (errorElement) {
                                        errorElement.textContent = '❌ Este campo es obligatorio';
                                        errorElement.style.display = 'block';
                                    }
                                    return false;
                                }

                                if (!regex.test(input.value.trim())) {
                                    input.classList.add('is-invalid');
                                    input.style.borderColor = '#dc3545';
                                    if (errorElement) {
                                        errorElement.textContent = '❌ Solo se permiten letras y espacios';
                                        errorElement.style.display = 'block';
                                    }
                                    return false;
                                }

                                return true;
                            }

                            // Limpiar validación al cerrar modales
                            document.querySelectorAll('.modal').forEach(modal => {
                                modal.addEventListener('hidden.bs.modal', function () {
                                    const inputs = this.querySelectorAll('input');
                                    inputs.forEach(input => {
                                        input.classList.remove('is-invalid', 'is-valid');
                                        const errorElement = document.getElementById(`error-${input.id}`);
                                        if (errorElement)
                                            errorElement.style.display = 'none';
                                    });
                                });
                            });
            </script>
            <script>
                function validarNombre(input) {
                    const valor = input.value.trim();
                    const patron = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;

                    if (valor === "") {
                        input.setCustomValidity("Este campo no puede estar vacío.");
                    } else if (!patron.test(valor)) {
                        input.setCustomValidity("Solo se permiten letras y espacios.");
                    } else {
                        input.setCustomValidity(""); // válido
                    }
                }
            </script>



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
                                                    $("#empleados,  #roles, #usuario,  #inventario, #backup").hide();
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