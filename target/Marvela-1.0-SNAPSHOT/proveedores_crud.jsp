<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Marvela | Datos del Proveedor</title>
        <link rel="stylesheet" href="estilos.css">
        <link rel="stylesheet" href="proveedores.css">
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
            <header class="boton-superior">
                <div class="boton-nuevo">
                    <button type="button" class="btn-marvela" data-bs-toggle="modal" data-bs-target="#myModal">
                        NUEVO
                    </button>
                </div>
            </header>


            <!-- Modal: Nuevo Producto -->

            <div class="modal fade" id="myModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="nuevoProductoLabel">
                                <i class="fas fa-user-plus me-2"></i>NUEVO PROVEEEDOR
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form id="formulario_insert">
                            <div class="modal-body">
                                <!-- Nombre Proveedor -->
                                <label for="nombreproveedor">Nombre Proveedor <span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="nombre_proveedor" name="nombre_proveedor"
                                           placeholder="Ej. Distribuidora El Sol"
                                           maxlength="100"
                                           pattern="^[A-Za-zÁÉÍÓÚáéíóúÑñ.\s]{5,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese solo letras y espacios, entre 5 y 100 caracteres.')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="nombre_proveedor">Nombre completo proveedor</label>
                                    <div class="invalid-feedback">
                                        Ingrese solo letras y espacios, entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <label for="correo">Correo <span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-3 position-relative">
                                    <input type="email" class="form-control" id="correo" name="correo"
                                           placeholder="Ej. contacto@elsol.com"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese un correo válido. Ej. contacto@gmail.com')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="correo">Correo electrónico</label>
                                    <div class="invalid-feedback">
                                        Ingrese un correo válido. Ej. contacto@gmail.com
                                    </div>
                                </div>



                                <input type="hidden" id="estado" name="estado" value="true">

                                <!-- Dirección -->
                                <label for="direccion">Dirección<span class="text-danger">*</span></label></label>
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="direccion" name="direccion"
                                           placeholder="Ej. Calle El Progreso #123, Col. Las Rosas"
                                           maxlength="100"
                                           pattern="^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9#.,\- ]{5,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Formato inválido. Ingrese una dirección entre 5 y 100 caracteres, usando letras, números y símbolos comunes.')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="direccion">Dirección</label>
                                    <div class="invalid-feedback">
                                        Formato inválido. Ingrese una dirección entre 5 y 100 caracteres, usando letras, números y símbolos comunes.
                                    </div>
                                </div>

                                <!-- Teléfono -->

                                <!-- ? Campo Teléfono con validación visual y lógica -->
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="numero_telefono">Teléfono 1 <span class="text-danger">*</span></label>
                                        <div class="form-floating mb-3 position-relative">
                                            <input type="text"
                                                   class="form-control"
                                                   id="numero_telefono"
                                                   name="numero_telefono"
                                                   placeholder="Ej. 7123-4567"
                                                   maxlength="9"
                                                   pattern="^\d{4}-\d{4}$"
                                                   required
                                                   oninvalid="this.setCustomValidity('Ingresar el formato solicitado ####-####.')"
                                                   oninput="this.setCustomValidity(''); formatearTelefono(this)">
                                            <label for="numero_telefono">Teléfono</label>
                                            <div class="invalid-feedback">
                                                Ingresar el formato solicitado ####-####.
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="numero_telefono1">Teléfono 2</label>
                                        <div class="form-floating mb-3 position-relative">
                                            <input type="text"
                                                   class="form-control"
                                                   id="numero_telefono1"
                                                   name="numero_telefono1"
                                                   placeholder="Ej. 7123-4567"
                                                   maxlength="9"
                                                   pattern="^\d{4}-\d{4}$"
                                                   oninvalid="this.setCustomValidity('Ingresar el formato solicitado ####-####.')"
                                                   oninput="this.setCustomValidity(''); formatearTelefono(this)">
                                            <label for="numero_telefono1">Teléfono</label>
                                            <div class="invalid-feedback">
                                                Ingresar el formato solicitado ####-####.
                                            </div>
                                        </div>
                                    </div>
                                </div>



                                <script>
                                    function formatearTelefono(input) {
                                        let valor = input.value.replace(/\D/g, ""); // Elimina todo lo que no sea número
                                        if (valor.length > 4) {
                                            valor = valor.slice(0, 4) + "-" + valor.slice(4, 8);
                                        } else {
                                            valor = valor.slice(0, 4); // Evita guion si no hay suficientes dígitos
                                        }
                                        input.value = valor;
                                    }
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

            <!-- Modal: Editar Proveedor -->
            <div class="modal fade" id="myModalE" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content shadow-lg">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">EDITAR PROVEEDOR</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <form name="formulario_editar" id="formulario_editar">
                            <div class="modal-body" style="background: #eeeeef">

                                <!-- ID oculto -->
                                <input type="hidden" name="codigo_proveedor" id="codigo_proveedor">

                                <!-- Nombre del proveedor -->
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="nombre_proveedorE" name="nombre_proveedor"
                                           placeholder="Ej. Distribuidora El Sol"
                                           maxlength="100"
                                           pattern=".{5,100}"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese un nombre entre 5 y 100 caracteres.')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="nombre_proveedorE">Nombre Proveedor</label>
                                    <div class="invalid-feedback">
                                        Ingrese un nombre entre 5 y 100 caracteres.
                                    </div>
                                </div>

                                <div class="form-floating mb-3 position-relative">
                                    <input type="email" class="form-control" id="correoE" name="correoE"
                                           placeholder="Ej. contacto@elsol.com"
                                           required
                                           oninvalid="this.setCustomValidity('Ingrese un correo válido. Ej. contacto@elsol.com')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="correo">Correo</label>
                                    <div class="invalid-feedback">
                                        Ingrese un correo válido. Ej. contacto@elsol.com
                                    </div>
                                </div>
                                <!-- Dirección -->
                                <div class="form-floating mb-3 position-relative">
                                    <input type="text" class="form-control" id="direccionE" name="direccion"
                                           placeholder="Ej. Calle El Progreso #123, Col. Las Rosas"
                                           maxlength="100"
                                           pattern="^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9#.,\- ]{5,100}$"
                                           required
                                           oninvalid="this.setCustomValidity('Formato inválido. Ingrese una dirección entre 5 y 100 caracteres, usando letras, números y símbolos comunes.')"
                                           oninput="this.setCustomValidity('')">
                                    <label for="direccionE">Dirección</label>
                                    <div class="invalid-feedback">
                                        Formato inválido. Ingrese una dirección entre 5 y 100 caracteres, usando letras, números y símbolos comunes.
                                    </div>
                                </div>

                                <!-- Teléfono -->

                                <div class="row">
                                    <!-- Teléfono principal -->
                                    <div class="col-md-6">
                                        <div class="form-floating mb-3 position-relative">
                                            <input type="text"
                                                   class="form-control"
                                                   id="numero_telefonoE"
                                                   name="numero_telefonoE"
                                                   placeholder="Ej. 7123-4567"
                                                   maxlength="9"
                                                   pattern="^\d{4}-\d{4}$"
                                                   required
                                                   oninvalid="this.setCustomValidity('Ingresar el formato solicitado ####-####.')"
                                                   oninput="this.setCustomValidity(''); formatearTelefono(this)">
                                            <label for="numero_telefonoE">Teléfono </label>
                                            <div class="invalid-feedback">
                                                Ingresar el formato solicitado ####-####.
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Teléfono secundario -->
                                    <div class="col-md-6">
                                        <div class="form-floating mb-3 position-relative">
                                            <input type="text"
                                                   class="form-control"
                                                   id="numero_telefono1E"
                                                   name="numero_telefono1E"
                                                   placeholder="Ej. 7123-4567"
                                                   maxlength="9"
                                                   pattern="^\d{4}-\d{4}$"
                                                   oninput="validarTelefonoOpcional(this)">


                                            <label for="numero_telefono1E">Teléfono 2 </label>
                                            <div class="invalid-feedback">
                                                Ingresar el formato solicitado ####-####.
                                            </div>
                                        </div>
                                        <script>
                                            function validarTelefonoOpcional(campo) {
                                                const valor = campo.value.trim();

                                                if (valor === "") {
                                                    campo.setCustomValidity(""); // Campo vacío: válido
                                                    campo.classList.remove("is-invalid");
                                                } else if (!/^\d{4}-\d{4}$/.test(valor)) {
                                                    campo.setCustomValidity("Ingresar el formato solicitado ####-####.");
                                                    campo.classList.add("is-invalid");
                                                } else {
                                                    campo.setCustomValidity(""); // Formato correcto
                                                    campo.classList.remove("is-invalid");
                                                }
                                            }
                                        </script>
                                    </div>
                                </div>
                                <!-- Estado -->
                                <div>
                                    <fieldset class="mb-2">
                                        <div class="text-center">
                                            <label class="form-label fs-5">Estado</label>
                                        </div>

                                        <div class="d-flex justify-content-center gap-3">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="estadoE" id="estado_activo" value="true" required>
                                                <label class="form-check-label" for="estado_activo">ACTIVO</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="estadoE" id="estado_inactivo" value="false" required>
                                                <label class="form-check-label" for="estado_inactivo">INACTIVO</label>
                                            </div>
                                        </div>

                                    </fieldset>
                                </div>
                            </div>

                            <!-- Footer -->
                            <div class="modal-footer" style="background: #eeeeef">
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-outline-success" name="accion" value="Guardar">Guardar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <section class="tabla_listCategoria">
                <h2 style="text-align: center;">PROVEEDORES</h2>
                <div id="proveedorA" class="tabla_listCategoria">   
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
            <script src="js/proveedor.js"></script>
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
    </body>
    <div id="toast" class="toast">Guardado correctamente ?</div>
</html>