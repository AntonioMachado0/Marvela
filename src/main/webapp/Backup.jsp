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

        .list-icon {
            background-color: rgba(58, 134, 255, 0.15);
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

        .file-input-label.dragover {
            border-color: #328266;
            background-color: rgba(50, 130, 102, 0.1);
            transform: scale(1.02);
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
            opacity: 0.6;
        }

        .btn-list {
            background: #328266;
            color: white;
        }

        .btn-list:hover {
            background: #328266;
            transform: translateY(-2px);
        }

        /* Estilos para la tabla de backups */
        .backup-table th {
            background-color: #328266;
            color: white;
        }

        .backup-table .btn-sm {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }

        .file-size {
            font-family: monospace;
            background: #f8f9fa;
            padding: 2px 6px;
            border-radius: 3px;
            font-size: 0.875rem;
        }

        .no-backups {
            text-align: center;
            padding: 40px !important;
            color: #6c757d;
        }

        .no-backups i {
            font-size: 3rem;
            margin-bottom: 15px;
            color: #dee2e6;
        }

        @media (max-width: 768px) {
            .backup-cards {
                flex-direction: column;
            }
            
            .swal2-popup {
                width: 90% !important;
                margin: 20px auto;
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

                <!-- Nueva tarjeta para listar backups -->
                <div class="backup-card">
                    <div class="card-icon list-icon">
                        <i class="fas fa-list"></i>
                    </div>
                    <h2>Backups Generados</h2>
                    <p>Visualice todos los backups generados en el sistema con información detallada de fecha, hora y tamaño.</p>

                    <button type="button" class="btn-backup btn-list" data-bs-toggle="modal" data-bs-target="#backupsModal" onclick="cargarListaBackups()">
                        <i class="fas fa-folder-open me-2"></i> Ver Backups
                    </button>
                </div>
            </div>
        </div>

     <!-- Modal para mostrar lista de backups -->
<div class="modal fade" id="backupsModal" tabindex="-1" aria-labelledby="backupsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="backupsModalLabel">
                    <i class="fas fa-database me-2"></i>Backups Generados
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover backup-table" id="tablaBackups">
                        <thead>
                            <tr>
                                <th><i class="fas fa-file me-1"></i> Nombre del Archivo</th>
                                <th><i class="fas fa-calendar me-1"></i> Fecha de Generación</th>
                                <th><i class="fas fa-weight-hanging me-1"></i> Tamaño</th>
                            </tr>
                        </thead>
                        <tbody id="cuerpoTablaBackups">
                            <!-- Los backups se cargarán aquí dinámicamente -->
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="fas fa-times me-1"></i> Cerrar
                </button>
                
            </div>
        </div>
    </div>
</div>

        <%@include file="footer.jsp" %>

        <script src="js/backup.js"></script>
        <script>
         

         
            // Función para usar un backup en la restauración
            function usarParaRestauracion(nombreArchivo) {
                // Cerrar el modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('backupsModal'));
                modal.hide();

                // Mostrar confirmación para restaurar
                Swal.fire({
                    title: '¿Usar para restauración?',
                    html: `¿Desea usar el archivo <strong>${nombreArchivo}</strong> para restaurar la base de datos?`,
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#328266',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, restaurar',
                    cancelButtonText: 'Cancelar',
                    backdrop: true
                }).then((result) => {
                    if (result.isConfirmed) {
                        Swal.fire({
                            title: 'Backup Seleccionado',
                            html: `El archivo <strong>${nombreArchivo}</strong> ha sido seleccionado para restauración.<br><br>
                                  Por favor, proceda con el proceso de restauración manual.`,
                            icon: 'info',
                            confirmButtonColor: '#328266'
                        });
                    }
                });
            }

            // Función para descargar backup (placeholder)
            function descargarBackup(nombreArchivo) {
                Swal.fire({
                    title: 'Descargar Backup',
                    html: `La descarga del archivo <strong>${nombreArchivo}</strong> se iniciará pronto.<br><br>
                          <small class="text-muted">Nota: Esta funcionalidad requiere configuración adicional en el servidor.</small>`,
                    icon: 'info',
                    confirmButtonColor: '#328266'
                });
            }
            
            
        </script>
    </main>
</body>
</html>
