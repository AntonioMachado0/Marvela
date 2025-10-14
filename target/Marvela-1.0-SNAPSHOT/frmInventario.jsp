<%@page import="java.util.List"%>
<%@page import="com.ues.edu.models.Categoria"%>
<%@page import="com.ues.edu.models.dao.InventarioDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Marvela | INVENTARIO</title>
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
        <style>
            .inventario-header {
                position: relative;
                display: flex;
                justify-content: center;
                align-items: center;
                margin-bottom: 1rem;
                padding: 0 15px;
            }
           
    .encabezado-verde {
        background-color: #198754;
        color: white;
    }
    .cantidad-baja {
        background-color: #f8d7da !important;
        color: #721c24 !important;
        font-weight: bold;
    }
    .imagen-producto {
        width: 50px;
        height: 50px;
        object-fit: cover;
        border-radius: 4px;
        border: 1px solid #dee2e6;
        cursor: pointer;
        transition: transform 0.2s;
    }
    .imagen-producto:hover {
        transform: scale(1.8);
        z-index: 1000;
        position: relative;
        box-shadow: 0 4px 8px rgba(0,0,0,0.3);
    }
    .sin-imagen {
        width: 50px;
        height: 50px;
        background-color: #f8f9fa;
        border: 1px dashed #dee2e6;
        border-radius: 4px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #6c757d;
    }

            .titulo-centrado {
                text-align: center;
                margin: 0;
            }
            
            .categoria-selector {
                position: absolute;
                right: 15px;
                top: 50%;
                transform: translateY(-50%);
                min-width: 250px;
                max-width: 300px;
            }
            
            @media (max-width: 768px) {
                .inventario-header {
                    flex-direction: column;
                    align-items: flex-start;
                    padding: 0;
                }
                
                .titulo-centrado {
                    width: 100%;
                    margin-bottom: 1rem;
                }
                
                .categoria-selector {
                    position: static;
                    transform: none;
                    width: 100%;
                    max-width: 100%;
                }
            }
        </style>
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
            <section class="table-responsive">
                <div class="inventario-header">
                    <h2 class="titulo-centrado">INVENTARIO</h2>
                   
                </div>
                <div id="inventarioA" class="table-responsive">   
                </div>


            </section>
            <script src="js/inventario.js"></script>
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

</html>