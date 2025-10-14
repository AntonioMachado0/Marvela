
<%-- 
    Document   : productos_crud
    Created on : 30 sep. 2025, 20:18:21
    Author     : Maris
--%>


<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.ues.edu.models.dao.ProductoDao"%>
<%@page import="com.ues.edu.models.Productos"%>
<%@page import="com.ues.edu.models.Compras"%>


<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <title>Marvela | Datos de Producto</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="productos.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.12.1/js/dataTables.bootstrap5.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.12.1/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <!-- CSS de Flatpickr -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

        <!-- JS de Flatpickr -->
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

    </head>
    <style>
        .custom-file-wrapper {
            display: flex;
            align-items: center;
            gap: 10px;
        }
    </style>
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

            <header class="d-flex align-items-center px-3" style="height: 60px;">
                <div class="d-flex align-items-center">
                    <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal">
                        NUEVO
                    </button>

                    <div style="margin-left: 0.5cm;">
                        <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal1">
                            EXISTENTES
                        </button>
                    </div>


                </div>
            </header>



     <!-- Modal Principal -->
<div class="modal fade" id="myModal" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <!-- Encabezado del Modal -->
      <div class="modal-header bg-success text-white">
        <h5 class="modal-title text-white" id="nuevoProductoLabel">
          <i class="fas fa-user-plus me-2"></i>NUEVO PRODUCTO
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>

      <!-- Formulario -->
      <form id="formulario_insert" enctype="multipart/form-data">
        <div class="modal-body">

          <!-- Campo: Número de compra + Código de barras + Cantidad -->
          <div style="display: flex; align-items: flex-start; gap: 12px; flex-wrap: wrap; margin-bottom: 16px;">
            <!-- Número de compra -->
            <div class="form-floating" style="flex: 1; min-width: 200px;">
              <input type="text" class="form-control form-control-sm" id="numero_de_orden" name="numero_de_orden"
                placeholder="Ej. Compra 001" oninput="generarCodigoBarra()" />
              <label for="numero_de_orden" style="font-size: 0.8rem;">Número de compra</label>
            </div>

            <!-- Código de barras -->
            <div style="text-align: center; width: 160px;">
              <svg id="barcode" style="
                width: 150px;
                height: 40px;
                margin: 0 auto;
                display: block;
                overflow: hidden;
              "></svg>
              <div style="font-size: 0.7rem; margin-top: 2px; color: #555;">Generado</div>
            </div>

            <!-- Spinner de cantidad -->
            <div style="text-align: center;">
              <label for="cantidad" style="font-size: 0.8rem;">Cantidad</label>
              <input type="number" id="cantidad" name="cantidad" min="1" max="100" value="1"
                class="form-control form-control-sm" style="width: 70px;">
            </div>
          </div>

          <!-- Aquí siguen los demás campos del producto como antes... -->
          <!-- Nombre, descripción, categoría, marca, medida, imagen, etc. -->

          <!-- Pie del Modal -->
          <div class="modal-footer">
            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
              <i class="fas fa-times me-1"></i> Cancelar
            </button>
            <button type="submit" class="btn btn-success">
              <i class="fas fa-save me-1"></i> Guardar
            </button>
            <button type="button" class="btn btn-success" onclick="imprimirPDF()">
              <i class="fas fa-save me-1"></i> Imprimir
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>



            <!-- Modal: Editar Proveedor -->
            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR PRODUCTO</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form name="formulario_editar" id="formulario_editar">
                            <div class="modal-body" style="background: #eeeeef">


                                <input type="hidden" id="codigo_compra" name="codigo_compra">
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="numero_de_ordenE" name="numero_de_orden" required minlength="5" maxlength="100">
                                    <label for="numero_de_ordenE">Número De Orden</label>
                                    <div class="invalid-feedback">
                                        Ingrese un número entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <!-- ? Fecha de compra -->
                                <div class="mb-3">
                                    <label for="fecha_de_ordenE">Fecha De Compra <span class="text-danger">*</span></label>
                                    <div class="form-floating">
                                        <input type="date" class="form-control" id="fecha_de_ordenE" name="fecha_de_orden" required>
                                        <label for="fecha_de_ordenE">Fecha de compra</label>
                                        <div class="invalid-feedback">
                                            Seleccione una fecha válida en formato día/mes/año.
                                        </div>
                                    </div>
                                </div>

                                <!-- ? Combo empleado -->
                                <div class="mb-4">
                                    <label for="codigo_empleadoE">Empleado <span class="text-danger">*</span></label>
                                    <div class="form-floating mb-2">
                                        <select name="codigo_empleadoE" id="codigo_empleadoE" class="form-select" required data-parsley-required-message="Campo requerido">
                                            <option value="">Seleccione un empleado</option>
                                        </select>
                                        <label for="codigo_empleadoE">Empleado</label>
                                    </div>
                                </div>

                                <!-- ? Combo proveedor -->
                                <div class="form-floating mb-2">
                                    <select class="form-select" id="codigo_proveedorE" name="codigo_proveedorE" required>
                                        <option value="">Seleccione un proveedor</option>
                                    </select>
                                    <label for="codigo_proveedorE">Proveedor</label>
                                </div>

                            </div>

                            <!-- Footer -->
                            <div class="modal-footer" style="background: #eeeeef">
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Guardar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Imprimir</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>
<script>
  function generarCodigoBarra() {
    const valor = document.getElementById("numero_de_orden").value || "?";
    JsBarcode("#barcode", valor, {
      format: "CODE128",
      lineColor: "#000",
      width: 1.5,
      height: 40,
      displayValue: true,
      fontSize: 12,
      textMargin: 0
    });
  }

  window.addEventListener("DOMContentLoaded", generarCodigoBarra);
</script>

<!-- Scripts al final del documento -->
<script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>

<script>
  // Aquí va tu función imprimirPDF()
  async function imprimirPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const valor = document.getElementById("numero_de_orden").value || "?";
    const cantidad = parseInt(document.getElementById("cantidad").value) || 1;

    const etiquetasPorFila = 5;
    const espacioX = 40;
    const espacioY = 30;
    const margenIzquierdo = 10;
    const margenSuperior = 10;

    let etiquetaGenerada = 0;

    while (etiquetaGenerada < cantidad) {
      for (let fila = 0; fila < Math.ceil(cantidad / etiquetasPorFila); fila++) {
        for (let columna = 0; columna < etiquetasPorFila; columna++) {
          if (etiquetaGenerada >= cantidad) break;

          const x = margenIzquierdo + columna * espacioX;
          const y = margenSuperior + fila * espacioY;

          const canvas = document.createElement("canvas");
          canvas.width = 300;
          canvas.height = 100;
          const ctx = canvas.getContext("2d");
          ctx.scale(2, 2);

          JsBarcode(canvas, valor, {
            format: "CODE128",
            lineColor: "#000",
            width: 1.5,
            height: 40,
            displayValue: true,
            fontSize: 10,
            textMargin: 0
          });

          const imgData = canvas.toDataURL("image/png");

          if (y + 20 > doc.internal.pageSize.height) {
            doc.addPage();
            fila = -1;
            break;
          }

          doc.addImage(imgData, "PNG", x, y, 35, 20);
          etiquetaGenerada++;
        }
      }
    }

    doc.save("codigos.pdf");
  }
</script>
</body>



            <section class="tabla_listCom">
                <h2 style="text-align: center;">PRODUCTOS</h2>

                <div id="proveedorA" class="tabla_listCom">   
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
            <!-- SweetAlert2 CDN -->
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script src="js/productos.js"></script>
            <%@include file="footer.jsp" %>
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
            document.getElementById("imageFile").addEventListener("change", function (event) {
                const file = event.target.files[0];
                const preview = document.getElementById("imagePreview");
                const container = document.getElementById("previewContainer");

                if (!file || !file.type.startsWith("image/")) {
                    container.style.display = "none";
                    preview.src = "#";
                    return;
                }

                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                    container.style.display = "block";
                };
                reader.readAsDataURL(file);
            });
        </script>


    </body>
    <div id="toast" class="toast">Guardado correctamente ?</div>
</html>