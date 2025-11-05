<%-- 
    Document   : Unidad_Medida_crud
    Created on : 27 sept 2025, 21:07:24
    Author     : thebe
--%>


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
        <title>Marvela | Unidad de Medida</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">

                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVA MEDIDA
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form id="formulario_insert" data-parsley-validate >
                            <div class="modal-body">
                                <label for="nombre">Nombre Medida<span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="nombre" name="nombre"
                                           placeholder="Ej. Distribuidora El Sol"
                                           maxlength="100"
                                           pattern="^[A-Za-zÁÉÍÓÚáéíóúÑñ.\s]{1,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese solo letras y espacios, entre 4 y 100 caracteres.')"
                                           oninput="this.setCustomValidity('')">

                                    <div class="invalid-feedback">
                                        Ingrese solo letras y espacios, entre 1 y 100 caracteres.
                                    </div>
                                </div>
                                <script>
                                    const campoNombre = document.getElementById("nombre");
                                    campoNombre.addEventListener("input", function () {
                                    const valor = this.value;
                                    const valorSinEspacios = valor.replace(/\s/g, "");
                                    if (valorSinEspacios.length < 1) {
                                    this.classList.add("is-invalid");
                                    this.setCustomValidity("Debe ingresar al menos 1 caracteres reales (sin contar espacios).");
                                    } else {
                                    this.classList.remove("is-invalid");
                                    this.setCustomValidity("");
                                    }
                                    });
                                </script>
                                <label for="abreviacion">abreviacion<span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="abreviacion" name="abreviacion"
                                           placeholder="Ej. Distribuidora El Sol"
                                           maxlength="100"
                                           pattern="^[A-Za-zÁÉÍÓÚáéíóúÑñ.\s]{1,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese solo letras y espacios, entre 1 y 100 caracteres.')"
                                           oninput="this.setCustomValidity('')">

                                    <div class="invalid-feedback">
                                        Ingrese solo letras y espacios, entre 1 y 100 caracteres.
                                    </div>
                                </div>
                                <script>
                                    const campoAbreviacion = document.getElementById("abreviacion");
                                    campoAbreviacion.addEventListener("input", function () {
                                    const valor = this.value;
                                    const valorSinEspacios = valor.replace(/\s/g, "");
                                    if (valorSinEspacios.length < 1) {
                                    this.classList.add("is-invalid");
                                    this.setCustomValidity("Debe ingresar al menos 1 caracteres reales (sin contar espacios).");
                                    } else {
                                    this.classList.remove("is-invalid");
                                    this.setCustomValidity("");
                                    }
                                    });
                                </script>
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
            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">

                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR MEDIDA</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>                  
                        <form name="formulario_editar" id="formulario_editar">
                            <div style="background: #eeeeef" class="modal-body">
                                <div class="form-floating mb-3" style="display: none;">
                                    <input style="background: #eeeeef" type="text" class="form-control" name="id_medida" id="id_medida" readonly>
                                    <label for="floatingInput">ID</label>
                                </div>


                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="categoriaE" name="nombre_medida"
                                           placeholder="Ej. Distribuidora El Sol"
                                           maxlength="100"
                                           pattern="^(?!\s*$)[A-Za-zÁÉÍÓÚáéíóúÑñ. ]{1,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese solo letras y espacios, entre 4 y 100 caracteres.')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="nombre_medida">Nombre de Marca</label>
                                    <div class="invalid-feedback">
                                        Ingrese solo letras y espacios, entre 2 y 100 caracteres.
                                    </div>
                                </div>
                                <script>
                                    const campoNombre = document.getElementById("nombre_medida");
                                    campoNombre.addEventListener("input", function () {
                                    const valor = this.value;
                                    const valorSinEspacios = valor.replace(/\s/g, "");
                                    if (valorSinEspacios.length < 1) {
                                    this.classList.add("is-invalid");
                                    this.setCustomValidity("Debe ingresar al menos 1 caracteres reales (sin contar espacios).");
                                    } else {
                                    this.classList.remove("is-invalid");
                                    this.setCustomValidity("");
                                    }
                                    });
                                </script>

                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="categoriaA" name="abreviacion"
                                           placeholder="Ej. Distribuidora El Sol"
                                           maxlength="100"
                                           pattern="^(?!\s*$)[A-Za-zÁÉÍÓÚáéíóúÑñ. ]{1,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese solo letras y espacios, entre 1 y 100 caracteres.')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="abreviacion">abreviacion</label>
                                    <div class="invalid-feedback">
                                        Ingrese solo letras y espacios, entre 1 y 100 caracteres.
                                    </div>
                                </div>
                                <script>
                                    const campoAbreviacion = document.getElementById("abreviacion");
                                    campoAbreviacion.addEventListener("input", function () {
                                    const valor = this.value;
                                    const valorSinEspacios = valor.replace(/\s/g, "");
                                    if (valorSinEspacios.length < 1) {
                                    this.classList.add("is-invalid");
                                    this.setCustomValidity("Debe ingresar al menos 1 caracteres reales (sin contar espacios).");
                                    } else {
                                    this.classList.remove("is-invalid");
                                    this.setCustomValidity("");
                                    }
                                    });
                                </script>
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
                <h2 style="text-align: center;">MEDIDA</h2>
                <div id="nombre_medida" class="table-responsive">   
                </div>
            </section>
            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>


            <%@include file="footer.jsp" %>
            <script src="js/medida.js"></script>

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
                                    $("#empleados,  #roles, #usuario, #backup").hide();
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