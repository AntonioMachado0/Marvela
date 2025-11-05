<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Marvela | DEVOLUCION</title>
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
        :root {
            --primary-color: #328266;
            --secondary-color: #8338ec;
            --success-color: #06d6a0;
            --warning-color: #ffd166;
            --danger-color: #ef476f;
            --light-color: #f8f9fa;
            --dark-color: #212529;
        }

        .ventas-container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 20px;
        }

        .ventas-header {
            text-align: center;
            margin-bottom: 40px;
        }

        .ventas-header h1 {
            color: var(--primary-color);
            font-weight: 700;
            margin-bottom: 10px;
        }

        .ventas-header p {
            color: #6c757d;
            font-size: 1.1rem;
        }

        .plazo-info {
            background: #e7f3ff;
            border: 1px solid #b3d9ff;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            text-align: center;
        }

        .plazo-info i {
            color: var(--primary-color);
            margin-right: 8px;
        }

        /* Estilos para el filtro */
        .filtro-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 25px;
            border-left: 4px solid var(--primary-color);
        }

        .filtro-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .filtro-title {
            font-size: 1.2rem;
            font-weight: 600;
            color: var(--dark-color);
            margin: 0;
        }

        .contador-ventas {
            background: var(--primary-color);
            color: white;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 600;
        }

        .search-box {
            position: relative;
            margin-bottom: 15px;
        }

        .search-input {
            width: 100%;
            padding: 12px 45px 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s;
        }

        .search-input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(50, 130, 102, 0.1);
        }

        .search-icon {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #6c757d;
            font-size: 1.1rem;
        }

        .filtro-actions {
            display: flex;
            gap: 10px;
        }

        .btn-filtro {
            padding: 8px 16px;
            border: none;
            border-radius: 6px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
            font-size: 0.9rem;
        }

        .btn-limpiar {
            background: #6c757d;
            color: white;
        }

        .btn-limpiar:hover {
            background: #5a6268;
            transform: translateY(-1px);
        }

        .btn-recargar {
            background: var(--primary-color);
            color: white;
        }

        .btn-recargar:hover {
            background: #2a6f54;
            transform: translateY(-1px);
        }

        .ventas-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
        }

        .venta-card {
            flex: 0 0 calc(33.333% - 20px);
            min-width: 350px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            padding: 25px;
            transition: transform 0.3s, box-shadow 0.3s;
            border-left: 4px solid var(--primary-color);
        }

        .venta-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0,0,0,0.12);
        }

        .card-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 15px;
            font-size: 24px;
        }

        .venta-icon {
            background-color: rgba(50, 130, 102, 0.15);
            color: var(--primary-color);
        }

        .venta-card h2 {
            font-size: 1.4rem;
            margin-bottom: 10px;
            color: var(--dark-color);
        }

        .venta-fecha {
            color: #6c757d;
            font-size: 0.9rem;
            margin-bottom: 15px;
            background: #f8f9fa;
            padding: 8px 12px;
            border-radius: 6px;
            display: inline-block;
        }

        .producto-item {
            background: #f8f9fa;
            padding: 12px;
            border-radius: 8px;
            margin: 10px 0;
            border-left: 3px solid var(--primary-color);
        }

        .producto-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .producto-nombre {
            font-weight: 500;
            color: var(--dark-color);
            flex: 1;
        }

        .producto-cantidad {
            background: var(--primary-color);
            color: white;
            padding: 4px 12px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: 600;
            min-width: 60px;
            text-align: center;
        }

        .btn-devolucion, .btn-ticket {
            display: block;
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            background: var(--primary-color);
            color: white;
            margin-top: 20px;
        }

        .btn-devolucion:hover {
            background: #2a6f54;
            transform: translateY(-2px);
        }

        .no-ventas {
            text-align: center;
            padding: 40px;
            color: #6c757d;
            background: #f8f9fa;
            border-radius: 15px;
            margin: 20px 0;
        }

        .no-ventas i {
            font-size: 3rem;
            margin-bottom: 15px;
            color: #dee2e6;
        }

        .venta-card.hidden {
            display: none;
        }

        @media (max-width: 768px) {
            .ventas-cards {
                flex-direction: column;
            }
            
            .venta-card {
                flex: 0 0 100%;
            }

            .filtro-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }

            .filtro-actions {
                width: 100%;
                justify-content: space-between;
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
            <a href="devolucion.jsp" class="active"><i class="fas fa-undo-alt"></i> <span>Devoluciones</span></a>
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

        <div class="ventas-container">
            <div class="ventas-header">
                <h1>Ventas Recientes</h1>
                <p>Listado de ventas realizadas en los últimos 3 días</p>
            </div>

            <div class="plazo-info">
                <i class="fas fa-info-circle"></i>
                <strong>Plazo de devolución:</strong> Solo se muestran las ventas de los últimos 3 días para devolución.
            </div>

            <!-- Filtro de búsqueda -->
            <div class="filtro-container">
                <div class="filtro-header">
                    <h3 class="filtro-title">
                        <i class="fas fa-search"></i> Buscar Venta
                    </h3>
                    <div class="contador-ventas" id="contadorVentas">
                        <span id="ventasVisibles">0</span> / <span id="ventasTotales">0</span> ventas
                    </div>
                </div>
                
                <div class="search-box">
                    <input type="text" 
                           class="search-input" 
                           id="filtroVentas" 
                           placeholder="Buscar por número de venta (ej: 0012, 0015)..."
                           autocomplete="off">
                    <i class="fas fa-search search-icon"></i>
                </div>
                
                <div class="filtro-actions">
                    <button class="btn-filtro btn-limpiar" onclick="limpiarFiltro()">
                        <i class="fas fa-times"></i> Limpiar
                    </button>
                    <button class="btn-filtro btn-recargar" onclick="recargarVentas()">
                        <i class="fas fa-sync-alt"></i> Recargar
                    </button>
                </div>
            </div>

          <div class="ventas-cards" id="ventasCards">
    <c:choose>
        <c:when test="${not empty listaVentas}">
            <c:forEach var="venta" items="${listaVentas}">
                <div class="venta-card" data-numero-venta="${venta.numeroVenta}">
                    <div class="card-icon venta-icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>

                    <h2>Venta #${venta.numeroVenta}</h2>

                    <div class="venta-fecha">
                        <i class="fas fa-calendar-alt"></i> ${venta.fechaVenta}
                    </div>
                    
                    <div class="productos-lista">
                        <c:forEach var="producto" items="${venta.productos}">
                            <div class="producto-item"
                                 data-producto-id="${producto.idProducto}"
                                 data-codigo-detalle-venta="${producto.codigoDetalleVenta}"
                                 data-codigo-detalle-compra="${producto.codigoDetalleCompra}"
                                 data-cantidad-original="${producto.cantidad}">
                                <div class="producto-info">
                                    <span class="producto-nombre">${producto.nombre} ${producto.nombre_marca}</span>
                                    <span class="producto-cantidad">${producto.cantidad}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- 🔄 Botón para realizar devolución -->
                    <button class="btn-devolucion" onclick="realizarDevolucion('${venta.numeroVenta}')">
                        <i class="fas fa-undo-alt"></i> Realizar Devolución
                    </button>

                    <!-- 🧾 Nuevo botón para generar ticket PDF -->
                    <button class="btn-ticket" onclick="generarTicketDevolucion('${venta.numeroVenta}')">
                        <i class="fas fa-file-pdf"></i> Generar Ticket
                    </button>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-ventas">
                <i class="fas fa-shopping-cart"></i>
                <h3>No hay ventas recientes</h3>
                <p>No se encontraron ventas realizadas en los últimos 3 días.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

        </div>
      
        <%@include file="footer.jsp" %>
    </main>
    <script src="js/devolucion.js"></script>
    <script>
    // JavaScript para el funcionamiento de las ventanas de ventas
    $(document).ready(function() {
        console.log("Página de devoluciones cargada correctamente");
        
        // Inicializar contador de ventas
        actualizarContadorVentas();
        
        // Configurar evento de búsqueda en tiempo real
        $('#filtroVentas').on('input', function() {
            filtrarVentas();
        });
        
        // Permitir búsqueda con Enter
        $('#filtroVentas').on('keypress', function(e) {
            if (e.which === 13) { // Enter key
                filtrarVentas();
            }
        });
    });

    function filtrarVentas() {
        const filtro = $('#filtroVentas').val().toLowerCase().trim();
        let ventasVisibles = 0;
        const totalVentas = $('.venta-card').length;

        // Siempre eliminar mensaje de no resultados al empezar a filtrar
        $('.no-resultados').remove();

        if (filtro === '') {
            // Mostrar todas las ventas si no hay filtro
            $('.venta-card').removeClass('hidden');
            ventasVisibles = totalVentas;
        } else {
            // Filtrar ventas por número de venta
            $('.venta-card').each(function() {
                const numeroVenta = $(this).data('numero-venta').toString().toLowerCase();
                
                if (numeroVenta.includes(filtro)) {
                    $(this).removeClass('hidden');
                    ventasVisibles++;
                } else {
                    $(this).addClass('hidden');
                }
            });

            // Mostrar mensaje solo si no hay resultados Y el filtro no está vacío
            if (ventasVisibles === 0 && filtro !== '') {
                mostrarMensajeNoResultados(filtro);
            }
        }

        // Actualizar contador
        $('#ventasVisibles').text(ventasVisibles);
        $('#ventasTotales').text(totalVentas);
    }

    function actualizarContadorVentas() {
        const totalVentas = $('.venta-card').length;
        const ventasVisibles = $('.venta-card:not(.hidden)').length;
        
        $('#ventasVisibles').text(ventasVisibles);
        $('#ventasTotales').text(totalVentas);
    }

    function limpiarFiltro() {
        $('#filtroVentas').val('');
        // Eliminar cualquier mensaje de no resultados
        $('.no-resultados').remove();
        filtrarVentas();
        $('#filtroVentas').focus();
    }

    function mostrarMensajeNoResultados(filtro) {
        // Si ya existe un mensaje de no resultados, removerlo
        $('.no-resultados').remove();
        
        const mensaje = `
            <div class="no-ventas no-resultados">
                <i class="fas fa-search"></i>
                <h3>No se encontraron ventas</h3>
                <p>No hay ventas que coincidan con "<strong>${filtro}</strong>"</p>
                <button class="btn-filtro btn-limpiar" onclick="limpiarFiltro()" style="margin-top: 15px;">
                    <i class="fas fa-times"></i> Limpiar búsqueda
                </button>
            </div>
        `;
        
        $('#ventasCards').append(mensaje);
    }

    function recargarVentas() {
        Swal.fire({
            title: 'Recargando ventas...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        // Recargar la página después de un breve tiempo
        setTimeout(() => {
            window.location.reload();
        }, 1000);
    }
</script>
<script>
function generarTicketDevolucion(numeroVenta) {
    const contextPath = "${pageContext.request.contextPath}";
    if (numeroVenta) {
        const url = contextPath + "/generarTicketDevolucion?numeroVenta=" + numeroVenta;
        window.open(url, '_blank');
    } else {
        alert("No se encontró el número de venta para generar el ticket.");
    }
}
</script>

</body>
</html>