<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Marvela | BACKUP</title>
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
            /* Estilos para el drag and drop */
            .file-input-label.dragover {
                border-color: #328266 !important;
                background-color: rgba(50, 130, 102, 0.1) !important;
                transform: scale(1.02);
                transition: all 0.3s ease;
            }

            /* Mejoras visuales para los estados de los botones */
            .btn-restore:disabled {
                background-color: #6c757d !important;
                cursor: not-allowed;
                opacity: 0.6;
            }

            .btn-restore:not(:disabled):hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            }

            /* Animaciones para los iconos */
            .card-icon {
                transition: transform 0.3s ease;
            }

            .backup-card:hover .card-icon {
                transform: scale(1.1);
            }

            /* Responsive improvements */
            @media (max-width: 768px) {
                .swal2-popup {
                    width: 90% !important;
                    margin: 20px auto;
                }
            }

            /* Estilos para detalles técnicos */
            details {
                background: #f8f9fa;
                border: 1px solid #dee2e6;
                border-radius: 5px;
                padding: 10px;
                margin-top: 10px;
            }

            summary {
                cursor: pointer;
                font-weight: bold;
                outline: none;
            }

            details[open] summary {
                margin-bottom: 10px;
            }
            :root {
                --primary-color: #328266;
                --secondary-color: #8338ec;
                --success-color: #06d6a0;
                --warning-color: #ffd166;
                --danger-color: #ef476f;
                --light-color: #f8f9fa;
                --dark-color: #212529;
            }

            .backup-container {
                max-width: 1000px;
                margin: 30px auto;
                padding: 20px;
            }

            .backup-header {
                text-align: center;
                margin-bottom: 40px;
            }

            .backup-header h1 {
                color: var(--primary-color);
                font-weight: 700;
                margin-bottom: 10px;
            }

            .backup-header p {
                color: #6c757d;
                font-size: 1.1rem;
            }

            .backup-cards {
                display: flex;
                justify-content: space-between;
                gap: 30px;
                flex-wrap: wrap;
            }

            .backup-card {
                flex: 1;
                min-width: 300px;
                background: white;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.08);
                padding: 30px;
                transition: transform 0.3s, box-shadow 0.3s;
            }

            .backup-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 15px 35px rgba(0,0,0,0.12);
            }

            .card-icon {
                width: 70px;
                height: 70px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 20px;
                font-size: 28px;
            }

            .generate-icon {
                background-color: rgba(58, 134, 255, 0.15);
                color: var(--primary-color);
            }

            .restore-icon {
                background-color: rgba(6, 214, 160, 0.15);
                color: #328266;
            }

            .backup-card h2 {
                font-size: 1.5rem;
                margin-bottom: 15px;
                color: var(--dark-color);
            }

            .backup-card p {
                color: #6c757d;
                margin-bottom: 25px;
                line-height: 1.6;
            }

            .file-input-container {
                position: relative;
                margin-bottom: 25px;
            }

            .file-input-label {
                display: block;
                padding: 12px 20px;
                background: var(--light-color);
                border: 2px dashed #ced4da;
                border-radius: 8px;
                text-align: center;
                cursor: pointer;
                transition: all 0.3s;
            }

            .file-input-label:hover {
                border-color: var(--primary-color);
                background: rgba(58, 134, 255, 0.05);
            }

            .file-input {
                position: absolute;
                left: -9999px;
            }

            .file-name {
                display: block;
                margin-top: 10px;
                font-size: 0.9rem;
                color: #6c757d;
            }

            .btn-backup {
                display: block;
                width: 100%;
                padding: 12px;
                border: none;
                border-radius: 8px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s;
            }

            .btn-generate {
                background: var(--primary-color);
                color: white;
            }

            .btn-generate:hover {
                background: #328266;
                transform: translateY(-2px);
            }

            .btn-restore {
                background: #328266;
                color: white;
            }

            .btn-restore:hover {
                background: #328266;
                transform: translateY(-2px);
            }

            .btn-restore:disabled {
                background: #328266;
                cursor: not-allowed;
                transform: none;
            }

            @media (max-width: 768px) {
                .backup-cards {
                    flex-direction: column;
                }
            }

            /* Agregar en el style del JSP */
            .file-input-label.dragover {
                border-color: #328266;
                background-color: rgba(50, 130, 102, 0.1);
                transform: scale(1.02);
            }

            .swal2-input {
                text-align: center;
                font-weight: bold;
            }

            details {
                background: #f8f9fa;
                padding: 10px;
                border-radius: 5px;
                margin-top: 10px;
                text-align: left;
            }

            summary {
                cursor: pointer;
                font-weight: bold;
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

            <div class="backup-container">
                <div class="backup-header">
                    <h1>Gestión de Datos</h1>
                </div>

                <div class="backup-cards">
                    <div class="backup-card">
                        <div class="card-icon generate-icon">
                            <i class="fas fa-database"></i>
                        </div>
                        <h2>Generar Backup</h2>
                        <p>Se crea una copia de seguridad de toda la base de datos del sistema para prevenir pérdida de información.</p>

                        <!-- Cambia el formulario por un botón que llame a la función JavaScript -->
                        <button type="button" class="btn-backup btn-generate" onclick="confirmarBackup()">
                            <i class="fas fa-download me-2"></i> Generar Backup
                        </button>
                    </div>

                    <div class="backup-card">
                        <div class="card-icon restore-icon">
                            <i class="fas fa-upload"></i>
                        </div>
                        <h2>Restaurar Backup</h2>
                        <p>Se recupera la información del sistema desde un archivo de respaldo previamente generado.</p>

                        <form id="restoreForm" method="post" enctype="multipart/form-data">
                            <div class="file-input-container">
                                <label for="archivoBackup" class="file-input-label">
                                    <i class="fas fa-cloud-upload-alt me-2"></i> 
                                    <span id="fileLabelText">Seleccionar archivo de backup</span>
                                </label>
                                <input type="file" name="archivoBackup" id="archivoBackup" class="file-input" 
                                       accept=".sql,.backup,.bak" required>
                                <span id="fileName" class="file-name">No se eligió ningún archivo</span>
                            </div>

                            <button type="button" class="btn-backup btn-restore" id="restoreBtn" disabled>
                                <i class="fas fa-history me-2"></i> Restaurar Backup
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <%@include file="footer.jsp" %>

            <!--  <script>
                  // Mostrar nombre de archivo seleccionado
                  document.getElementById('archivoBackup').addEventListener('change', function (e) {
                      const fileName = e.target.files[0] ? e.target.files[0].name : 'No se eligió ningún archivo';
                      document.getElementById('fileName').textContent = fileName;
  
                      // Habilitar o deshabilitar botón según si hay archivo
                      document.getElementById('restoreBtn').disabled = !e.target.files[0];
                  });
  
                  // Confirmación antes de restaurar
                  document.getElementById('restoreBtn').addEventListener('click', function (e) {
                      e.preventDefault();
                      const form = document.getElementById('restoreForm');
                      const fileInput = document.getElementById('archivoBackup');
  
                      if (!fileInput.files[0]) {
                          return;
                      }
  
                      Swal.fire({
                          title: '¿Estás seguro?',
                          text: "Esta acción restaurará la base de datos desde el archivo seleccionado. Los datos actuales podrían ser reemplazados.",
                          icon: 'warning',
                          showCancelButton: true,
                          confirmButtonColor: '#328266',
                          cancelButtonColor: '#328266',
                          confirmButtonText: 'Sí, restaurar',
                          cancelButtonText: 'Cancelar'
                      }).then((result) => {
                          if (result.isConfirmed) {
                              form.submit();
                          }
                      });
                  });
              </script> -->
            <script src="js/backup.js"></script>
            <script>
                            // Función para manejar la restauración de backup
                            document.getElementById('restoreForm').addEventListener('submit', function (e) {
                                e.preventDefault();

                                const fileInput = document.getElementById('archivoBackup');
                                if (!fileInput.files[0]) {
                                    Swal.fire('Error', 'Por favor seleccione un archivo de backup', 'error');
                                    return;
                                }

                                Swal.fire({
                                    title: '¿Estás seguro?',
                                    text: "Esta acción restaurará la base de datos desde el archivo seleccionado. Los datos actuales podrían ser reemplazados.",
                                    icon: 'warning',
                                    showCancelButton: true,
                                    confirmButtonColor: '#328266',
                                    cancelButtonColor: '#328266',
                                    confirmButtonText: 'Sí, restaurar',
                                    cancelButtonText: 'Cancelar'
                                }).then((result) => {
                                    if (result.isConfirmed) {
                                        restaurarBackup(fileInput.files[0]);
                                    }
                                });
                            });

                            function restaurarBackup(file) {
                                const formData = new FormData();
                                formData.append('archivoBackup', file);
                                formData.append('action', 'restore');

                                Swal.fire({
                                    title: 'Restaurando Backup...',
                                    text: 'Por favor espere',
                                    allowOutsideClick: false,
                                    didOpen: () => {
                                        Swal.showLoading();
                                    }
                                });

                                $.ajax({
                                    url: 'BackupServlet',
                                    type: 'POST',
                                    data: formData,
                                    processData: false,
                                    contentType: false,
                                    dataType: 'json'
                                }).done(function (response) {
                                    Swal.close();
                                    if (response.resultado === 'exito') {
                                        Swal.fire('Éxito', 'Backup restaurado correctamente', 'success');
                                    } else {
                                        Swal.fire('Error', response.mensaje, 'error');
                                    }
                                }).fail(function (xhr, status, error) {
                                    Swal.close();
                                    Swal.fire('Error', 'Error al restaurar: ' + error, 'error');
                                });
                            }
            </script>

        </main>
    </body>
</html>



