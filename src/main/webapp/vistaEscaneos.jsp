<%@page import="com.ues.edu.models.Usuario"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Map" %>

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
<!-- jsPDF -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
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
    <a href="index.html"><i class="fas fa-home"></i> <span>Inicio</span></a>
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
    <style>
        .contenido { padding: 15px; }
        .form-label { font-weight: bold; }
        
    </style>

<body>
    <style>
    .venta-destacada {
    font-size: 1.5rem;
    font-weight: 600;
    color: #2e7d32;
    background-color: #f4fbf5;
    border-left: 4px solid #2e7d32;
    padding: 4px 10px; /* ← esta línea define el alto y ancho del fondo */
    margin-bottom: 16px;
    border-radius: 6px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
</style>

    <div class="contenido">
        <h2>VENTAS</h2>
        <!-- Contenedor para mostrar el número de venta y la fecha -->
<div id="venta-info" class="venta-destacada"></div>
    <!-- El contenido se insertará dinámicamente desde JavaScript -->
</div>

        <!-- Controles -->
        <div class="row mb-4">
            <div class="col-md-3">
                <label for="codigoManual" class="form-label">Código de barras</label>
                <input type="text" id="codigoManual" class="form-control" placeholder="Ingresa código manualmente">
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <button class="btn btn-success w-100" onclick="agregarManual()">
                    <i class="fas fa-plus-circle"></i> Añadir
                </button>
            </div>
            <div class="col-md-3">
                <label for="totalVenta" class="form-label">Total a pagar</label>
                <input type="text" id="totalVenta" class="form-control text-end fw-bold" value="$0.00" readonly>
            </div>
            <div class="col-md-3 d-flex align-items-end justify-content-between">
<button class="btn btn-primary" onclick="finalizarVenta()">
  <i class="fas fa-check-circle"></i> Finalizar venta
</button>
</div>
        </div>

        <!-- Tabla escaneos -->
        <div id="tabla-container">
            <table class="table table-striped table-bordered">
                <thead style="background-color: #328266; color: white;">
                    <tr>
                        <th>CÓDIGO</th>
                        <th>NOMBRE</th>
                        <th>MARCA</th>
                        <th>CANTIDAD</th>
                        <th>PRECIO VENTA</th>
                        <th>ACCIONES</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Map<String, String>> escaneos = (List<Map<String, String>>) request.getAttribute("escaneos");
                        if (escaneos == null || escaneos.isEmpty()) {
                    %>
                    <tr>
                        <td colspan="6" class="text-center text-muted">
                            <i class="fas fa-box-open"></i> Aún no se ha escaneado ningún producto.
                        </td>
                    </tr>
                    <%
                        } else {
                            for (Map<String, String> producto : escaneos) {
                                String codigo = producto.get("codigo");
                                String nombre = producto.get("nombre");
                                String marca = producto.get("marca");
                                String cantidad = producto.get("cantidad");
                                String precio = producto.get("precio de venta unitario");
                    %>
                    <tr data-codigo="<%= codigo %>">
                        <td><%= codigo %></td>
                        <td><%= nombre %></td>
                        <td><%= marca %></td>
                        <td>
                            <input type="number" class="form-control cantidad-input" value="<%= cantidad %>" min="1"
       max="<%= producto.get("stock") %>"
       onchange="actualizarCantidad('<%= codigo %>', this.value)">
                        </td>
                        <td>$<%= precio %></td>
                        <td>
                            <button class="btn btn-sm btn-danger" onclick="quitarProducto('<%= codigo %>')">
                                <i class="fas fa-times"></i>
                            </button>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
  
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
      <script>
  function recargarTabla() {
    fetch('/Marvela/scan')
      .then(res => res.text())
      .then(html => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const nuevaTabla = doc.querySelector('#tabla-container');
        if (nuevaTabla) {
          document.getElementById('tabla-container').innerHTML = nuevaTabla.innerHTML;
        }
      });
  }

  // Recargar cada 3 segundos
  setInterval(recargarTabla, 3000);
</script>
<!-- Al final del JSP -->
<script src="/Marvela/js/scan.js"></script>
</body>
</html>