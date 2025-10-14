/* 
 * Sistema de Backup - Ferretería Marvela
 * Funcionalidades: Generar, restaurar y gestionar backups
 */

// Función para confirmar la generación de backup
function confirmarBackup() {
    Swal.fire({
        text: '¿Desea generar una copia de seguridad de la base de datos?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#808080',
        confirmButtonText: 'Generar',
        cancelButtonText: 'Cancelar',
        backdrop: false,
        allowOutsideClick: false
    }).then((result) => {
        if (result.isConfirmed) {
            generarBackup();
        }
    });
}
// Función para cargar la lista de backups en el modal
function cargarListaBackups() {
    console.log("🔄 Cargando lista de backups...");
    
    // Mostrar loading
    $('#cuerpoTablaBackups').html(`
        <tr>
            <td colspan="3" class="text-center">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Cargando...</span>
                </div>
                <p class="mt-2">Cargando lista de backups...</p>
            </td>
        </tr>
    `);

    $.ajax({
        url: 'BackupServlet',
        method: 'POST',
        data: { action: 'list' },
        dataType: 'json',
        timeout: 30000
    }).done(function(response) {
        console.log("📨 Respuesta del servidor:", response);
        
        if (response.resultado === 'exito') {
            if (response.backups && response.backups.length > 0) {
                console.log(`✅ Se encontraron ${response.backups.length} backups`);
                actualizarTablaBackups(response.backups);
            } else {
                console.log("ℹ️ No se encontraron backups");
                $('#cuerpoTablaBackups').html(`
                    <tr>
                        <td colspan="3" class="no-backups">
                            <i class="fas fa-inbox fa-3x mb-3"></i>
                            <h5>No hay backups generados</h5>
                            <p class="text-muted">No se encontraron archivos de backup en el sistema.</p>
                            <small class="text-info">Genera un nuevo backup para verlo aquí.</small>
                        </td>
                    </tr>
                `);
            }
        } else {
            console.error("❌ Error del servidor:", response.mensaje);
            $('#cuerpoTablaBackups').html(`
                <tr>
                    <td colspan="3" class="text-center text-danger">
                        <i class="fas fa-exclamation-triangle fa-2x mb-2"></i>
                        <p><strong>Error al cargar los backups:</strong></p>
                        <p>${response.mensaje || 'Error desconocido'}</p>
                    </td>
                </tr>
            `);
        }
    }).fail(function(xhr, status, error) {
        console.error("❌ Error de conexión:", error);
        $('#cuerpoTablaBackups').html(`
            <tr>
                <td colspan="3" class="text-center text-danger">
                    <i class="fas fa-exclamation-triangle fa-2x mb-2"></i>
                    <p><strong>Error de conexión:</strong></p>
                    <p>${error}</p>
                    <p class="text-muted">Verifique que el servidor esté funcionando.</p>
                </td>
            </tr>
        `);
    });
}

function actualizarTablaBackups(backups) {
    const tbody = document.getElementById('cuerpoTablaBackups');
    
    if (!backups || backups.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="3" class="no-backups">
                    <i class="fas fa-inbox fa-3x mb-3"></i>
                    <h5>No hay backups generados</h5>
                    <p class="text-muted">No se encontraron archivos de backup en el sistema.</p>
                </td>
            </tr>
        `;
        return;
    }

    let html = '';
    backups.forEach((backup, index) => {
        // Fecha
        const fecha = new Date(backup.fechaModificacion);
        const fechaFormateada = fecha.toLocaleString('es-ES', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        // Tamaño
        const tamanoMB = (backup.tamano / (1024 * 1024)).toFixed(2);
        const tamanoFormateado = `<span class="file-size">${tamanoMB} MB</span>`;

        // Ícono según tipo
        let icono = 'fa-file-alt';
        if (backup.nombre.toLowerCase().endsWith('.sql')) {
            icono = 'fa-database';
        } else if (backup.nombre.toLowerCase().endsWith('.backup')) {
            icono = 'fa-file-archive';
        }

        html += `
            <tr>
                <td>
                    <i class="fas ${icono} text-primary me-2"></i>
                    <strong>${backup.nombre}</strong>
                    ${index === 0 ? '<span class="badge bg-success ms-2">Más reciente</span>' : ''}
                </td>
                <td>
                    <i class="fas fa-clock text-muted me-2"></i>
                    ${fechaFormateada}
                </td>
                <td>${tamanoFormateado}</td>
            </tr>
        `;
    });

    tbody.innerHTML = html;
    console.log("✅ Tabla actualizada con " + backups.length + " backups");
}



// Función principal para generar backup
function generarBackup() {
    // Mostrar loading
    Swal.fire({
        title: 'Generando Backup...',
        text: 'Por favor espere, esto puede tomar unos minutos',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    $.ajax({
        url: "BackupServlet",
        method: "POST",
        data: {
            action: "generate"
        },
        dataType: "json",
        timeout: 300000 // 5 minutos timeout
    }).done(function(response) {
        Swal.close();
        
        if (response.resultado === "exito") {
            // Construir mensaje de éxito sin información de email
            let mensajeExito = `
                <div style="text-align: left;">
                    <p>✅ <strong>Backup generado correctamente</strong></p>
                    <p><strong>Archivo:</strong> ${response.archivo}</p>
                    <p><strong>Ruta:</strong> ${response.ruta}</p>
                    <p><strong>Tamaño:</strong> ${(response.tamaño / (1024 * 1024)).toFixed(2)} MB</p>
                    <div style="background: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0;">
                        <p>📁 <strong>Backup guardado localmente en el servidor</strong></p>
                        <p style="margin: 5px 0; font-size: 14px;">El archivo se ha almacenado en la ruta especificada del servidor.</p>
                    </div>
                </div>
            `;
            
            Swal.fire({
                title: 'Éxito',
                html: mensajeExito,
                icon: 'success',
                confirmButtonColor: '#328266',
                confirmButtonText: 'Aceptar',
                width: '600px'
            });
            
            // Actualizar lista si el modal está abierto
            actualizarListaDespuesDeBackup();
        } else {
            Swal.fire({
                title: 'Error',
                text: response.mensaje || 'No se pudo generar el backup',
                icon: 'error',
                confirmButtonColor: '#328266'
            });
        }
    }).fail(function(xhr, status, error) {
        Swal.close();
        
        let mensajeError = 'No se pudo conectar con el servidor: ' + error;
        
        if (status === 'timeout') {
            mensajeError = 'El tiempo de espera se agotó. El backup puede estar generándose, verifique más tarde.';
        }
        
        Swal.fire({
            title: 'Error de conexión',
            text: mensajeError,
            icon: 'error',
            confirmButtonColor: '#328266'
        });
    });
}

// Mostrar nombre de archivo seleccionado y habilitar botón
document.getElementById('archivoBackup').addEventListener('change', function(e) {
    const fileNameSpan = document.getElementById('fileName');
    const fileLabelText = document.getElementById('fileLabelText');
    const restoreBtn = document.getElementById('restoreBtn');
    
    if (e.target.files[0]) {
        const file = e.target.files[0];
        const fileName = file.name;
        const fileSize = (file.size / (1024 * 1024)).toFixed(2); // MB
        
        fileNameSpan.textContent = `Archivo: ${fileName} (${fileSize} MB)`;
        fileNameSpan.style.color = '#328266';
        fileLabelText.textContent = 'Cambiar archivo';
        restoreBtn.disabled = false;
        restoreBtn.style.opacity = '1';
        
        // Validar tamaño máximo (100MB)
        if (file.size > 100 * 1024 * 1024) {
            Swal.fire({
                title: 'Error',
                text: 'El archivo es demasiado grande. Máximo 100MB permitidos.',
                icon: 'error',
                confirmButtonColor: '#328266'
            });
            restoreBtn.disabled = true;
            restoreBtn.style.opacity = '0.6';
            e.target.value = '';
            fileNameSpan.textContent = 'Archivo demasiado grande. Máximo 100MB.';
            fileNameSpan.style.color = '#dc3545';
            return;
        }
        
        // Validar tipo de archivo
        const validExtensions = ['.backup', '.sql', '.bak'];
        const fileExtension = fileName.toLowerCase().substring(fileName.lastIndexOf('.'));
        if (!validExtensions.includes(fileExtension)) {
            Swal.fire({
                title: 'Error',
                text: 'Tipo de archivo no válido. Use .backup, .sql o .bak',
                icon: 'error',
                confirmButtonColor: '#328266'
            });
            restoreBtn.disabled = true;
            restoreBtn.style.opacity = '0.6';
            e.target.value = '';
            fileNameSpan.textContent = 'Tipo de archivo no válido. Use .backup, .sql o .bak';
            fileNameSpan.style.color = '#dc3545';
            return;
        }
        
        // Validación exitosa
        fileNameSpan.style.color = '#28a745';
        
    } else {
        fileNameSpan.textContent = 'No se eligió ningún archivo';
        fileNameSpan.style.color = '#6c757d';
        fileLabelText.textContent = 'Seleccionar archivo de backup';
        restoreBtn.disabled = true;
        restoreBtn.style.opacity = '0.6';
    }
});

// Manejar la restauración de backup
document.getElementById('restoreBtn').addEventListener('click', function(e) {
    e.preventDefault();
    
    const fileInput = document.getElementById('archivoBackup');
    if (!fileInput.files[0]) {
        Swal.fire({
            title: 'Error',
            text: 'Por favor seleccione un archivo de backup',
            icon: 'error',
            confirmButtonColor: '#328266'
        });
        return;
    }

    Swal.fire({
        title: '¿Está completamente seguro?',
        html: `<div style="text-align: left;">
               <p><strong style="color: #dc3545;">ADVERTENCIA CRÍTICA</strong></p>
               <ul style="text-align: left; padding-left: 20px;">
                 <li>Todos los datos actuales serán reemplazados permanentemente</li>
                 <li>Esta acción no se puede deshacer</li>
                 <li>El sistema puede quedar inaccesible temporalmente</li>
               </ul>
               <p style="margin-top: 20px;">Escriba <strong>RESTAURAR</strong> para confirmar:</p>
               <input type="text" id="confirmText" class="swal2-input" placeholder="RESTAURAR" style="text-align: center; font-weight: bold; text-transform: uppercase;">
               </div>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ef476f',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Confirmar Restauración',
        cancelButtonText: 'Cancelar',
        backdrop: true,
        allowOutsideClick: false,
        preConfirm: () => {
            const confirmValue = document.getElementById('confirmText').value;
            if (confirmValue.toUpperCase() !== 'RESTAURAR') {
                Swal.showValidationMessage('Debe escribir exactamente: RESTAURAR');
                return false;
            }
            return true;
        },
        didOpen: () => {
            document.getElementById('confirmText').focus();
        }
    }).then((result) => {
        if (result.isConfirmed) {
            restaurarBackup(fileInput.files[0]);
        }
    });
});

// Función para restaurar backup
function restaurarBackup(file) {
    // Mostrar progreso con más detalles
    Swal.fire({
        title: 'Restaurando Backup...',
        html: `<div style="text-align: center;">
               <p><strong>Este proceso puede tomar varios minutos</strong></p>
               <p style="color: #6c757d; font-size: 14px;">
                   <i class="fas fa-exclamation-triangle"></i> 
                   No cierre el navegador durante la restauración
               </p>
               <div class="spinner-border text-primary mt-3" style="width: 3rem; height: 3rem;" role="status">
                 <span class="visually-hidden">Cargando...</span>
               </div>
               <p style="margin-top: 15px; font-size: 12px; color: #6c757d;">
                   Archivo: ${file.name}<br>
                   Tamaño: ${(file.size / (1024 * 1024)).toFixed(2)} MB
               </p>
               </div>`,
        allowOutsideClick: false,
        showConfirmButton: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    const formData = new FormData();
    formData.append('archivoBackup', file);
    formData.append('action', 'restore');

    $.ajax({
        url: 'BackupServlet',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        dataType: 'json',
        timeout: 300000 // 5 minutos timeout
    }).done(function(response) {
        Swal.close();
        
        if (response.resultado === 'exito') {
            Swal.fire({
                title: '¡Restauración Exitosa!',
                html: `<div style="text-align: left;">
                       <p>✅ El backup se restauró correctamente.</p>
                       <p><strong>Archivo:</strong> ${response.archivo}</p>
                       <p><strong>Detalles:</strong>Proceso completado</p>
                       <div style="background: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0;">
                           <p style="margin: 0;">El sistema se recargará automáticamente en 5 segundos.</p>
                       </div>
                       </div>`,
                icon: 'success',
                confirmButtonColor: '#328266',
                confirmButtonText: 'Recargar Ahora',
                showCancelButton: true,
                cancelButtonText: 'Esperar',
                willClose: () => {
                    // Recargar la página después de cerrar
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.reload();
                } else {
                    // Si el usuario elige esperar, mostrar contador
                    let seconds = 5;
                    const timerInterval = setInterval(() => {
                        Swal.update({
                            html: `<div style="text-align: left;">
                                   <p>✅ El backup se restauró correctamente.</p>
                                   <p><strong>Archivo:</strong> ${response.archivo}</p>
                                   <div style="background: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0;">
                                       <p style="margin: 0;">El sistema se recargará automáticamente en ${seconds} segundos.</p>
                                   </div>
                                   </div>`
                        });
                        seconds--;
                        
                        if (seconds < 0) {
                            clearInterval(timerInterval);
                            window.location.reload();
                        }
                    }, 1000);
                }
            });
        } else {
            Swal.fire({
                title: 'Error en la Restauración',
                html: `<div style="text-align: left;">
                       <p>❌ No se pudo restaurar el backup.</p>
                       <p><strong>Error:</strong> ${response.mensaje}</p>
                       ${response.detalles ? `
                       <details style="margin-top: 10px;">
                           <summary style="cursor: pointer; font-weight: bold;">Detalles técnicos</summary>
                           <div style="background: #f8f9fa; padding: 10px; border-radius: 5px; margin-top: 5px; font-family: monospace; font-size: 12px;">
                               ${response.detalles}
                           </div>
                       </details>
                       ` : ''}
                       </div>`,
                icon: 'error',
                confirmButtonColor: '#ef476f',
                width: '600px'
            });
        }
    }).fail(function(xhr, status, error) {
        Swal.close();
        
        let mensajeError = '';
        if (status === 'timeout') {
            mensajeError = `
                <p>⏰ El tiempo de espera se agotó (5 minutos).</p>
                <p>La restauración puede estar en proceso. Verifique el servidor y espere antes de intentar nuevamente.</p>
            `;
        } else {
            mensajeError = `
                <p>❌ No se pudo completar la restauración.</p>
                <p><strong>Error:</strong> ${error}</p>
                <p>Verifique la conexión al servidor e intente nuevamente.</p>
            `;
        }
        
        Swal.fire({
            title: 'Error de Conexión',
            html: `<div style="text-align: left;">${mensajeError}</div>`,
            icon: 'error',
            confirmButtonColor: '#ef476f',
            width: '500px'
        });
    });
}

// Función para eliminar backup desde la tabla
function eliminarBackup(nombreArchivo) {
    Swal.fire({
        title: '¿Eliminar Backup?',
        html: `¿Está seguro de eliminar el backup <strong>"${nombreArchivo}"</strong>?<br><br>
              <small class="text-danger">Esta acción no se puede deshacer.</small>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ef476f',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        backdrop: true
    }).then((result) => {
        if (result.isConfirmed) {
            // Mostrar loading
            Swal.fire({
                title: 'Eliminando...',
                text: 'Eliminando archivo de backup',
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });

            $.ajax({
                url: 'BackupServlet',
                method: 'POST',
                data: { 
                    action: 'delete',
                    fileName: nombreArchivo
                },
                dataType: 'json'
            }).done(function(response) {
                Swal.close();
                if (response.resultado === 'exito') {
                    Swal.fire({
                        title: '¡Eliminado!',
                        text: 'Backup eliminado correctamente',
                        icon: 'success',
                        confirmButtonColor: '#328266'
                    });
                    // Recargar la lista de backups
                    cargarListaBackups();
                } else {
                    Swal.fire({
                        title: 'Error',
                        text: response.mensaje || 'No se pudo eliminar el backup',
                        icon: 'error',
                        confirmButtonColor: '#ef476f'
                    });
                }
            }).fail(function(xhr, status, error) {
                Swal.close();
                Swal.fire({
                    title: 'Error de conexión',
                    text: 'No se pudo eliminar el backup: ' + error,
                    icon: 'error',
                    confirmButtonColor: '#ef476f'
                });
            });
        }
    });
}

// Función para actualizar automáticamente la lista después de generar un backup
function actualizarListaDespuesDeBackup() {
    // Si el modal está abierto, actualizar la lista
    const modal = document.getElementById('backupsModal');
    if (modal && modal.classList.contains('show')) {
        cargarListaBackups();
    }
}

// Función para listar backups existentes
function listarBackups() {
    $.ajax({
        url: 'BackupServlet',
        method: 'POST',
        data: { action: 'list' },
        dataType: 'json',
        timeout: 30000
    }).done(function(response) {
        if (response.resultado === 'exito') {
            console.log('Backups disponibles:', response.backups);
        } else {
            console.error('Error al listar backups:', response.mensaje);
        }
    }).fail(function(xhr, status, error) {
        console.error('Error de conexión al listar backups:', error);
    });
}

// Drag and drop para archivos (mejora UX)
function inicializarDragAndDrop() {
    const fileInput = document.getElementById('archivoBackup');
    const fileLabel = document.querySelector('.file-input-label');
    
    // Prevenir comportamiento por defecto
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        fileLabel.addEventListener(eventName, preventDefaults, false);
    });
    
    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }
    
    // Efectos visuales al arrastrar
    ['dragenter', 'dragover'].forEach(eventName => {
        fileLabel.addEventListener(eventName, () => {
            fileLabel.classList.add('dragover');
        }, false);
    });
    
    ['dragleave', 'drop'].forEach(eventName => {
        fileLabel.addEventListener(eventName, () => {
            fileLabel.classList.remove('dragover');
        }, false);
    });
    
    // Manejar drop de archivos
    fileLabel.addEventListener('drop', (e) => {
        const dt = e.dataTransfer;
        const files = dt.files;
        
        if (files.length > 0) {
            fileInput.files = files;
            const event = new Event('change');
            fileInput.dispatchEvent(event);
        }
    }, false);
}

// Cargar lista de backups al iniciar la página
$(document).ready(function() {
    listarBackups();
    inicializarDragAndDrop();
    
    // Mejorar accesibilidad del file input
    document.querySelector('.file-input-label').addEventListener('keypress', function(e) {
        if (e.key === 'Enter' || e.key === ' ') {
            document.getElementById('archivoBackup').click();
        }
    });
});

// Función para probar conexión con el servlet (debug)
function probarConexionBackup() {
    Swal.fire({
        title: 'Probando conexión...',
        text: 'Verificando comunicación con el servidor',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    $.ajax({
        url: "BackupServlet",
        method: "POST",
        data: { action: "list" },
        dataType: "json",
        timeout: 10000
    }).done(function(response) {
        Swal.close();
        Swal.fire({
            title: 'Conexión Exitosa',
            text: 'El servidor responde correctamente',
            icon: 'success',
            confirmButtonColor: '#328266'
        });
    }).fail(function(xhr, status, error) {
        Swal.close();
        Swal.fire({
            title: 'Error de Conexión',
            text: 'No se puede conectar con el servidor: ' + error,
            icon: 'error',
            confirmButtonColor: '#ef476f'
        });
    });
}