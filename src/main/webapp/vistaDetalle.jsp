<%-- 
    Document   : vistaDetalle
    Created on : 4 oct 2025, 21:14:03
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
                <a href="ventas.jsp" id="ventas"><i class="fas fa-shopping-cart"></i> <span>Ventas</span></a>
                <a href="Backup.jsp" id="backup"><i class="fas fa-database"></i> <span>Backup</span></a>
  </nav>
</aside>

  <main>
    <header class="barra-superior">
      <div class="medida">Marvela</div>
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