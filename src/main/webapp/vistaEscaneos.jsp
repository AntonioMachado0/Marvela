<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Marvela | Productos Escaneados</title>

    <!-- Estilos y librerías -->
    <link rel="stylesheet" href="estilos.css">
    <link rel="stylesheet" href="categoria.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
    <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        .contenido {
            padding: 30px;
        }
        .empty {
            margin-top: 20px;
            color: #777;
        }
    </style>

    <script>
        function actualizarTabla() {
            fetch('/Marvela/scan')
                .then(response => response.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const nuevaTabla = doc.getElementById('tabla-container').innerHTML;
                    document.getElementById('tabla-container').innerHTML = nuevaTabla;
                })
                .catch(error => console.error('Error al actualizar tabla:', error));
        }

        setInterval(actualizarTabla, 3000);
    </script>
</head>
<body>
    <!-- Menú lateral -->
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

    <!-- Contenido principal -->
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

        <div class="contenido">
            <h2>📦 Productos Escaneados</h2>
            <div id="tabla-container">
                <%
                    List<Map<String, String>> escaneos = (List<Map<String, String>>) request.getAttribute("escaneos");
                    if (escaneos == null || escaneos.isEmpty()) {
                %>
                    <p class="empty">Aún no se ha escaneado ningún producto.</p>
                <%
                    } else {
                %>
                    <table class="table table-striped table-bordered">
                        <thead style="background-color: #328266; color: white;">
                            <tr>
                                
                                <th>Nombre</th>
                                <th>Marca</th>
                                <th>Precio Venta</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Map<String, String> producto : escaneos) {
                        %>
                            <tr>
                                
                                <td><%= producto.get("nombre") %></td>
                                <td><%= producto.get("marca") %></td>
                                <td>$<%= producto.get("precio") %></td>
                            </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                <%
                    }
                %>
            </div>
        </div>
    </main>

    <script>
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
    </script>
</body>
</html>