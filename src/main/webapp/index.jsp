<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="com.ues.edu.models.Productos" %>

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

  <style>
    .card-alert {
      border-radius: 12px;
      overflow: hidden;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      border: none;
      height: 100%;
    }

    .card-alert:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important;
    }

    .card-header-alert {
      display: flex;
      align-items: center;
      font-weight: 600;
      padding: 10px 15px;
      border-bottom: none;
    }

    .card-header-alert i { margin-right: 8px; font-size: 1.1rem; }

    .card-body-alert {
      padding: 15px;
      display: flex;
      flex-direction: column;
    }

    .product-name {
      font-weight: 600;
      margin-bottom: 8px;
      font-size: 1rem;
    }

    .alert-description {
      font-size: 0.85rem;
      color: #6c757d;
      margin-bottom: 10px;
      flex-grow: 1;
    }

    .card-danger { border-left: 4px solid #dc3545; }
    .card-warning { border-left: 4px solid #ffc107; }
    .card-info { border-left: 4px solid #0dcaf0; }
    .card-primary { border-left: 4px solid #0d6efd; }

    .section-title {
      position: relative;
      padding-bottom: 10px;
      margin-bottom: 20px;
      font-weight: 600;
    }
  </style>
</head>

<body>
  <aside class="sidebar activo" id="sidebar">
    <div class="menu-header" id="colapsarMenu">
      <i class="fas fa-bars"></i> <span>Menú</span>
    </div>
    <nav class="menu">
      <a href="index.jsp" id="inicio"><i class="fas fa-home"></i> <span>Inicio</span></a>
      <a href="ventas.jsp" id="ventas"><i class="fas fa-shopping-cart"></i> <span>Ventas</span></a>
      <a href="frmEmpleado.jsp" id="empleados"><i class="fas fa-user-tie"></i> <span>Empleados</span></a>
      <a href="proveedores_crud.jsp" id="proveedores"><i class="fas fa-handshake"></i> <span>Proveedores</span></a>
      <a href="categoria_crud.jsp" id="categoria"><i class="fas fa-tags"></i> <span>Categoría</span></a>
      <a href="frmRol.jsp" id="roles"><i class="fas fa-user-tag"></i> <span>Roles</span></a>
      <a href="Unidad_Medida_crud.jsp" id="unidadMedida"><i class="fas fa-ruler-combined"></i> <span>Unidad de Medida</span></a>
      <a href="marca_crud.jsp" id="marca"><i class="fas fa-stamp"></i> <span>Marca</span></a>
      <a href="compras_crud.jsp" id="compras"><i class="fas fa-shopping-cart"></i> <span>Compras</span></a>
      <a href="frmInventario.jsp" id="inventario"><i class="fas fa-warehouse"></i> <span>Inventario</span></a>
      <a href="productos_crud.jsp" id="productos"><i class="fas fa-cubes"></i> <span>Productos</span></a>
      <a href="vistaEscaneos.jsp" id="escaneos"><i class="fas fa-barcode"></i> <span>Productos Escaneados</span></a>
      <a href="Backup.jsp" id="backup"><i class="fas fa-database"></i> <span>Backup</span></a>
    </nav>
  </aside>

  <main>
    <header class="barra-superior">
      <div class="marca">Marvela</div>
      <div class="derecha">
        <i class="fas fa-bell campana">
          <span class="badge-notificacion">3</span>
        </i>
      </div>
    </header>

    <%
      List<Productos> bajaExistencia = (List<Productos>) request.getAttribute("bajaExistencia");
      List<Productos> agotados = (List<Productos>) request.getAttribute("agotados");
      List<Productos> proximosVencer = (List<Productos>) request.getAttribute("proximosVencer");
      List<Map<String, Object>> comprasSinDetalles = (List<Map<String, Object>>) request.getAttribute("comprasSinDetalles");
    %>

    <div class="container mt-4">

      <!-- 🔴 Productos Agotados -->
      <% if (agotados != null && !agotados.isEmpty()) { %>
        <h4 class="section-title text-black">Productos Agotados</h4>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3 mb-4">
          <% for (Productos p : agotados) { %>
            <div class="col">
              <div class="card card-alert card-danger shadow-sm">
                <div class="card-header-alert bg-danger text-white">
                  <i class="fas fa-times-circle"></i>
                  <span>Agotado</span>
                </div>
                <div class="card-body-alert">
                  <h6 class="product-name">
                    <%= p.getNombre() %> - <span class="text-muted"><%= p.getMarca().getMarca() %></span>
                  </h6>
                  <p class="alert-description">Este producto no tiene existencia disponible (0 unidades).</p>
                </div>
              </div>
            </div>
          <% } %>
        </div>
      <% } %>

      <!-- 🟠 Productos con Baja Existencia -->
      <% if (bajaExistencia != null && !bajaExistencia.isEmpty()) { %>
        <h4 class="section-title text-black">Productos con Baja Existencia</h4>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3 mb-4">
          <% for (Productos p : bajaExistencia) { %>
            <div class="col">
              <div class="card card-alert card-warning shadow-sm">
                <div class="card-header-alert bg-warning text-white">
                  <i class="fas fa-exclamation-triangle"></i>
                  <span>Baja Existencia</span>
                </div>
                <div class="card-body-alert">
                  <h6 class="product-name">
                    <%= p.getNombre() %> - <span class="text-muted"><%= p.getMarca().getMarca() %></span>
                  </h6>
                  <p class="alert-description">
                    Este producto tiene <strong><%= (int)p.getPorcentaje() %></strong> unidad<%= (p.getPorcentaje() == 1) ? "" : "es" %> en inventario.
                  </p>
                </div>
              </div>
            </div>
          <% } %>
        </div>
      <% } %>

      <!-- 🔵 Productos Próximos a Vencer -->
      <% if (proximosVencer != null && !proximosVencer.isEmpty()) { %>
        <h4 class="section-title text-black">Productos Próximos a Vencer</h4>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3 mb-4">
          <% for (Productos p : proximosVencer) { %>
            <div class="col">
              <div class="card card-alert card-info shadow-sm">
                <div class="card-header-alert bg-info text-white">
                  <i class="fas fa-calendar-alt"></i>
                  <span>Próximo a Vencer</span>
                </div>
                <div class="card-body-alert">
                  <h6 class="product-name">
                    <%= p.getNombre() %> - <span class="text-muted"><%= p.getMarca().getMarca() %></span>
                  </h6>
                  <p class="alert-description">Este producto vence en los próximos 30 días.</p>
                </div>
              </div>
            </div>
          <% } %>
        </div>
      <% } %>

      <!-- 🟣 Compras sin Detalle -->
      <% if (comprasSinDetalles != null && !comprasSinDetalles.isEmpty()) { %>
        <h4 class="section-title text-black">Compras pendientes de registrar detalle</h4>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3 mb-4">
          <% for (Map<String, Object> c : comprasSinDetalles) {
               java.sql.Date fechaOrden = (java.sql.Date) c.get("fecha_de_orden");
               java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");
               String fechaFormateada = formato.format(fechaOrden);
               java.time.LocalDate fechaCompra = fechaOrden.toLocalDate();
               java.time.LocalDate hoy = java.time.LocalDate.now();
               long diasPasados = java.time.temporal.ChronoUnit.DAYS.between(fechaCompra, hoy);
               long diasRestantes = 5 - diasPasados;
               if (diasRestantes < 0) diasRestantes = 0;
          %>
            <div class="col">
              <div class="card card-alert card-primary shadow-sm">
                <div class="card-header-alert bg-primary text-white">
                  <i class="fas fa-clock"></i>
                  <span>Compra sin Detalle</span>
                </div>
                <div class="card-body-alert">
                  <h6 class="product-name">Orden: <%= c.get("numero_de_orden") %></h6>
                    <h6 class="product-name">Fecha: <%= fechaFormateada %> </h6>
                  <p class="alert-description">
                  
                    <br>Le quedan <strong><%= diasRestantes %> día<%= (diasRestantes != 1) ? "s" : "" %></strong> para completarla.
                  </p>
                </div>
              </div>
            </div>
          <% } %>
        </div>
      <% } %>
    </div>
  </main>
</body>
</html>
