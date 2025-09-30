/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ues.edu.controllers;

import com.ues.edu.models.dao.EmpleadoDao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mayel
 */
@WebServlet(name = "BackupServlet", urlPatterns = {"/BackupServlet"})

@MultipartConfig(
        maxFileSize = 1024 * 1024 * 100, // 100MB máximo
        maxRequestSize = 1024 * 1024 * 110, // 110MB máximo
        fileSizeThreshold = 1024 * 1024 // 1MB threshold
)

public class BackupServlet extends HttpServlet {

    // Configuración de la base de datos (deberían ser variables de entorno)
    private static final String HOST = "localhost";
    private static final String PORT = "5434";
    private static final String USER = "postgres";
    private static final String PASSWORD = "dd22005";
    private static final String DB_NAME = "FerreteriaBD";
    private static final String BACKUP_FOLDER = "C:\\Backup";
    private static final String PG_BIN_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\";
    private EmpleadoDao dao;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BackupServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BackupServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            JSONObject result = new JSONObject();

            switch (action) {
                case "generate":
                    result = generateBackup(request);
                    break;
                case "restore":
                    result = restoreBackup(request);
                    break;
                case "list":
                    result = listBackups();
                    break;
                case "delete":
                    result = deleteBackup(request);
                    break;
                default:
                    result.put("resultado", "error");
                    result.put("mensaje", "Acción no válida");
                    break;
            }

            response.getWriter().write(result.toString());

        } catch (Exception ex) {
            JSONObject error = new JSONObject();
            error.put("resultado", "error");
            error.put("mensaje", "Error interno: " + ex.getMessage());
            response.getWriter().write(error.toString());
        }
    }

    // PARA GENERAR EL BACKUPO
   /* private JSONObject generateBackup(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        try {
            // Verificar que el directorio de PostgreSQL existe
            File pgBinDir = new File(PG_BIN_PATH);
            if (!pgBinDir.exists()) {
                result.put("resultado", "error");
                result.put("mensaje", "Directorio de PostgreSQL no encontrado: " + PG_BIN_PATH);
                return result;
            }

            // Verificar que pg_dump.exe existe
            File pgDump = new File(PG_BIN_PATH + "pg_dump.exe");
            if (!pgDump.exists()) {
                result.put("resultado", "error");
                result.put("mensaje", "pg_dump.exe no encontrado en: " + pgDump.getAbsolutePath());
                return result;
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupFileName = DB_NAME + "_backup_" + timestamp + ".sql";
            String path = BACKUP_FOLDER + "\\" + backupFileName;

            // Crear directorio si no existe
            File directory = new File(BACKUP_FOLDER);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    result.put("resultado", "error");
                    result.put("mensaje", "No se pudo crear el directorio de backup: " + BACKUP_FOLDER);
                    return result;
                }
            }

            // Configurar proceso pg_dump
            ProcessBuilder pb = new ProcessBuilder(
                    PG_BIN_PATH + "pg_dump.exe",
                    "--host", HOST,
                    "--port", PORT,
                    "--username", USER,
                    "--no-password",
                    "--format", "custom",
                    "--blobs",
                    "--verbose",
                    "--file", path,
                    DB_NAME
            );

            // Redirigir error output
            pb.redirectErrorStream(true);

            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", PASSWORD);

            Process p = pb.start();

            // Capturar output del proceso
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            int exitCode = p.waitFor();

            if (exitCode == 0) {
                // Verificar que el archivo se creó correctamente
                File backupFile = new File(path);
                if (backupFile.exists() && backupFile.length() > 0) {
                    result.put("resultado", "exito");
                    result.put("mensaje", "Backup generado exitosamente");
                    result.put("archivo", backupFileName);
                    result.put("ruta", path);
                    result.put("tamaño", backupFile.length());
                } else {
                    result.put("resultado", "error");
                    result.put("mensaje", "El archivo de backup no se creó correctamente");
                }
            } else {
                result.put("resultado", "error");
                result.put("mensaje", "Error al generar backup. Código de salida: " + exitCode + "\nDetalles: " + output.toString());
            }

        } catch (IOException | InterruptedException ex) {
            result.put("resultado", "error");
            result.put("mensaje", "Excepción: " + ex.getMessage());
        }

        return result;
    }*/
    
    private JSONObject generateBackup(HttpServletRequest request) {
    JSONObject result = new JSONObject();
    String backupFilePath = null;
    File backupFile = null;

    try {
        // 1. VERIFICAR DIRECTORIO POSTGRESQL
        File pgBinDir = new File(PG_BIN_PATH);
        if (!pgBinDir.exists()) {
            result.put("resultado", "error");
            result.put("mensaje", "Directorio de PostgreSQL no encontrado: " + PG_BIN_PATH);
            return result;
        }

        // 2. VERIFICAR PG_DUMP.EXE
        File pgDump = new File(PG_BIN_PATH + "pg_dump.exe");
        if (!pgDump.exists()) {
            result.put("resultado", "error");
            result.put("mensaje", "pg_dump.exe no encontrado en: " + pgDump.getAbsolutePath());
            return result;
        }

        // 3. CREAR NOMBRE DE ARCHIVO CON TIMESTAMP
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFileName = DB_NAME + "_backup_" + timestamp + ".sql";
        backupFilePath = BACKUP_FOLDER + "\\" + backupFileName;

        // 4. CREAR DIRECTORIO DE BACKUP SI NO EXISTE
        File directory = new File(BACKUP_FOLDER);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                result.put("resultado", "error");
                result.put("mensaje", "No se pudo crear el directorio de backup: " + BACKUP_FOLDER);
                return result;
            }
        }

        // 5. CONFIGURAR PROCESO PG_DUMP
        ProcessBuilder pb = new ProcessBuilder(
                PG_BIN_PATH + "pg_dump.exe",
                "--host", HOST,
                "--port", PORT,
                "--username", USER,
                "--no-password",
                "--format", "custom",
                "--blobs",
                "--verbose",
                "--file", backupFilePath,
                DB_NAME
        );

        // Redirigir error output
        pb.redirectErrorStream(true);

        // 6. CONFIGURAR VARIABLE DE ENTORNO PGPASSWORD
        Map<String, String> env = pb.environment();
        env.put("PGPASSWORD", PASSWORD);

        // 7. EJECUTAR PROCESO
        Process p = pb.start();

        // 8. CAPTURAR OUTPUT DEL PROCESO
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        reader.close();

        // 9. ESPERAR POR LA TERMINACIÓN DEL PROCESO
        int exitCode = p.waitFor();

        // 10. VERIFICAR RESULTADO DEL BACKUP
        if (exitCode == 0) {
            backupFile = new File(backupFilePath);
            if (backupFile.exists() && backupFile.length() > 0) {
                
                // ✅ 11. NUEVO: INTENTAR ENVÍO POR CORREO
                JSONObject reporteEmail = enviarBackupPorCorreo(backupFile, backupFileName);
                boolean emailEnviado = reporteEmail.getBoolean("enviado");
                String mensajeEmail = reporteEmail.getString("mensaje");
                
                // 12. CONSTRUIR RESPUESTA CON INFORMACIÓN DEL EMAIL
                result.put("resultado", "exito");
                result.put("mensaje", "Backup generado exitosamente. " + mensajeEmail);
                result.put("archivo", backupFileName);
                result.put("ruta", backupFilePath);
                result.put("tamaño", backupFile.length());
                result.put("email_enviado", emailEnviado);
                result.put("detalles_email", mensajeEmail);
                
                // 13. LOG DEL RESULTADO
                System.out.println("✅ Backup generado: " + backupFilePath);
                System.out.println("📧 Estado email: " + mensajeEmail);
                
            } else {
                result.put("resultado", "error");
                result.put("mensaje", "El archivo de backup no se creó correctamente o está vacío");
            }
        } else {
            result.put("resultado", "error");
            result.put("mensaje", "Error al generar backup. Código de salida: " + exitCode + "\nDetalles: " + output.toString());
        }

    } catch (IOException | InterruptedException ex) {
        result.put("resultado", "error");
        result.put("mensaje", "Excepción durante la generación del backup: " + ex.getMessage());
        
        // Limpiar archivo temporal en caso de error
        if (backupFile != null && backupFile.exists()) {
            backupFile.delete();
        }
    }

    return result;
}

// ✅ MÉTODO NUEVO: ENVÍO DE BACKUP POR CORREO
private JSONObject enviarBackupPorCorreo(File backupFile, String backupFileName) {
    JSONObject reporte = new JSONObject();
    dao = new EmpleadoDao();
    
    try {
        // 1. CONFIGURACIÓN DE CORREO (AJUSTAR ESTOS VALORES)
        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String emailUsername = "ferreteriamarvela@gmail.com"; // ← CORREO REMITENTE
        String emailPassword = "svgu isrf bwzi viwr"; // ← CONTRASEÑA DE APLICACIÓN
        String emailDestinatario = dao.obtenerCorreoAdministrador(); // ← CORREO DESTINATARIO
        
        // 2. VALIDAR ARCHIVO
        if (!backupFile.exists()) {
            reporte.put("enviado", false);
            reporte.put("mensaje", "El archivo de backup no existe para enviar por correo");
            return reporte;
        }
        
        // 3. VALIDAR TAMAÑO (Gmail límite ~25MB)
        long tamañoMB = backupFile.length() / (1024 * 1024);
        if (tamañoMB > 20) {
            reporte.put("enviado", false);
            reporte.put("mensaje", "Archivo demasiado grande (" + tamañoMB + "MB) para enviar por correo");
            return reporte;
        }
        
        // 4. CONFIGURAR PROPIEDADES SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", smtpHost);
        
        // 5. CREAR SESIÓN
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });
        
        // 6. CREAR MENSAJE
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailUsername, "Sistema de Backup - Ferretería Marvela"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
        
        // 7. ASUNTO DEL CORREO
        String asunto = "Backup de Base de Datos - " + DB_NAME + " - " + 
                       new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        message.setSubject(asunto);
        
        // 8. CUERPO DEL MENSAJE (HTML)
        String mensajeHTML = construirMensajeBackup(backupFileName, tamañoMB);
        
        // 9. PARTE DE TEXTO
        MimeBodyPart textoPart = new MimeBodyPart();
        textoPart.setContent(mensajeHTML, "text/html; charset=utf-8");
        
        // 10. PARTE DEL ADJUNTO
        MimeBodyPart adjuntoPart = new MimeBodyPart();
        adjuntoPart.attachFile(backupFile);
        adjuntoPart.setFileName(backupFileName);
        
        // 11. COMBINAR PARTES
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textoPart);
        multipart.addBodyPart(adjuntoPart);
        message.setContent(multipart);
        
        // 12. ENVIAR CORREO
        Transport.send(message);
        
        reporte.put("enviado", true);
        reporte.put("mensaje", "Backup enviado por correo a " + emailDestinatario);
        System.out.println("✅ Correo enviado exitosamente a: " + emailDestinatario);
        
    } catch (Exception ex) {
        reporte.put("enviado", false);
        reporte.put("mensaje", "Error al enviar por correo: " + ex.getMessage());
        System.err.println("❌ Error enviando correo: " + ex.getMessage());
        ex.printStackTrace();
    }
    
    return reporte;
}

// ✅ MÉTODO AUXILIAR: CONSTRUIR MENSAJE HTML
private String construirMensajeBackup(String nombreArchivo, long tamañoMB) {
    return "<!DOCTYPE html>" +
           "<html>" +
           "<head><meta charset='UTF-8'></head>" +
           "<body style='font-family: Arial, sans-serif; color: #333;'>" +
           "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>" +
           "<h2 style='color: #328266; text-align: center;'>📦 Backup de Base de Datos</h2>" +
           "<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
           "<p><strong>🏢 Sistema:</strong> Ferretería Marvela</p>" +
           "<p><strong>🗃️ Base de Datos:</strong> " + DB_NAME + "</p>" +
           "<p><strong>📁 Archivo:</strong> " + nombreArchivo + "</p>" +
          // "<p><strong>📊 Tamaño:</strong> " + tamañoMB + " MB</p>" +
           "<p><strong>📅 Fecha:</strong> " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</p>" +
           //"<p><strong>🌐 Servidor:</strong> " + HOST + ":" + PORT + "</p>" +
           "</div>" +
           "<p style='text-align: center; color: #666;'>" +
           "<em>Este es un mensaje automático generado por el sistema de backup.</em>" +
           "</p>" +
           "<hr style='margin: 20px 0;'>" +
           "<footer style='text-align: center; color: #999; font-size: 12px;'>" +
           "<p>Ferretería Marvela - Sistema de Gestión</p>" +
           "</footer>" +
           "</div>" +
           "</body></html>";
}

    // Restaurar backup
    private JSONObject restoreBackup(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        try {
            // Para restauración por archivo subido
            Part filePart = request.getPart("archivoBackup");

            if (filePart == null || filePart.getSize() == 0) {
                result.put("resultado", "error");
                result.put("mensaje", "No se ha seleccionado ningún archivo de backup");
                return result;
            }

            // Validar tipo de archivo
            String fileName = filePart.getSubmittedFileName();
            if (!fileName.toLowerCase().endsWith(".backup")
                    && !fileName.toLowerCase().endsWith(".sql")
                    && !fileName.toLowerCase().endsWith(".bak")) {
                result.put("resultado", "error");
                result.put("mensaje", "Tipo de archivo no válido. Use .backup, .sql o .bak");
                return result;
            }

            // Crear directorio temporal si no existe
            String tempDir = System.getProperty("java.io.tmpdir") + "backup_restore/";
            File tempDirectory = new File(tempDir);
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            // Guardar archivo subido en temporal
            String tempFilePath = tempDir + fileName;
            try ( InputStream fileContent = filePart.getInputStream();  FileOutputStream out = new FileOutputStream(tempFilePath)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileContent.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            File backupFile = new File(tempFilePath);
            if (!backupFile.exists() || backupFile.length() == 0) {
                result.put("resultado", "error");
                result.put("mensaje", "El archivo de backup está vacío o no se pudo guardar");
                return result;
            }

            // Verificar que pg_restore.exe existe
            File pgRestore = new File(PG_BIN_PATH + "pg_restore.exe");
            if (!pgRestore.exists()) {
                result.put("resultado", "error");
                result.put("mensaje", "pg_restore.exe no encontrado en: " + pgRestore.getAbsolutePath());
                backupFile.delete(); // Limpiar archivo temporal
                return result;
            }

            // Configurar proceso pg_restore
            ProcessBuilder pb = new ProcessBuilder(
                    PG_BIN_PATH + "pg_restore.exe",
                    "--host", HOST,
                    "--port", PORT,
                    "--username", USER,
                    "--no-password",
                    "--dbname", DB_NAME,
                    "--clean",
                    "--if-exists",
                    "--verbose",
                    tempFilePath
            );

            pb.redirectErrorStream(true);

            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", PASSWORD);

            Process p = pb.start();

            // Capturar output del proceso
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            int exitCode = p.waitFor();

            // Limpiar archivo temporal
            backupFile.delete();

            if (exitCode == 0) {
                result.put("resultado", "exito");
                result.put("mensaje", "Backup restaurado exitosamente");
                result.put("archivo", fileName);
                result.put("detalles", output.toString());
            } else {
                result.put("resultado", "error");
                result.put("mensaje", "Error al restaurar backup. Código: " + exitCode);
                result.put("detalles", output.toString());
            }

        } catch (Exception ex) {
            result.put("resultado", "error");
            result.put("mensaje", "Excepción al restaurar: " + ex.getMessage());
        }

        return result;
    }

    // Listar backups disponibles
    private JSONObject listBackups() {
        JSONObject result = new JSONObject();
        JSONArray backupsArray = new JSONArray();

        try {
            File backupDir = new File(BACKUP_FOLDER);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            File[] backupFiles = backupDir.listFiles((dir, name)
                    -> name.toLowerCase().endsWith(".backup")
            );

            if (backupFiles != null) {
                for (File file : backupFiles) {
                    JSONObject backupInfo = new JSONObject();
                    backupInfo.put("nombre", file.getName());
                    backupInfo.put("tamaño", file.length());
                    backupInfo.put("fechaModificacion", file.lastModified());
                    backupInfo.put("ruta", file.getAbsolutePath());
                    backupsArray.put(backupInfo);
                }
            }

            result.put("resultado", "exito");
            result.put("backups", backupsArray);
            result.put("total", backupsArray.length());

        } catch (Exception ex) {
            result.put("resultado", "error");
            result.put("mensaje", "Error al listar backups: " + ex.getMessage());
        }

        return result;
    }

    // Eliminar backup
    private JSONObject deleteBackup(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        try {
            String fileName = request.getParameter("fileName");
            if (fileName == null || fileName.trim().isEmpty()) {
                result.put("resultado", "error");
                result.put("mensaje", "Nombre de archivo no especificado");
                return result;
            }

            String backupPath = BACKUP_FOLDER + "\\" + fileName;
            File backupFile = new File(backupPath);

            if (!backupFile.exists()) {
                result.put("resultado", "error");
                result.put("mensaje", "El archivo de backup no existe");
                return result;
            }

            if (backupFile.delete()) {
                result.put("resultado", "exito");
                result.put("mensaje", "Backup eliminado exitosamente");
                result.put("archivo", fileName);
            } else {
                result.put("resultado", "error");
                result.put("mensaje", "No se pudo eliminar el archivo");
            }

        } catch (Exception ex) {
            result.put("resultado", "error");
            result.put("mensaje", "Error al eliminar backup: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Returns a short description of the servlet.
     *
     * 
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}




