<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <title>Marvela | Panel de Gestión</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="index.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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

            <section class="hero-panel text-center">
                <h1>FERRETERÍA MARVELA</h1>
                <img src="img/Flogo.png" alt="Logo Marvela" class="logo-hero mt-4" />
                <p class="mt-3">Rol: <span id="rolUsuario"><%= usuario.getEmpleado().getRol().getNombreRol()%></span></p>
            </section>
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
</html>