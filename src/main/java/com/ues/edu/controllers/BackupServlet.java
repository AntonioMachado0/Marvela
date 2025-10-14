package com.ues.edu.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "BackupServlet", urlPatterns = {"/BackupServlet"})
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 100,      // 100MB máximo
    maxRequestSize = 1024 * 1024 * 110,   // 110MB máximo
    fileSizeThreshold = 1024 * 1024       // 1MB threshold
)
public class BackupServlet extends HttpServlet {

    // Configuración de la base de datos
    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";
    private static final String DB_NAME = "FerreteriaBD";
    private static final String BACKUP_FOLDER = "C:\\Backup";
    private static final String PG_BIN_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        System.out.println("🔍 Acción recibida en servlet: " + action);

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
                    result.put("mensaje", "Acción no válida: " + action);
                    break;
            }

            response.getWriter().write(result.toString());

        } catch (Exception ex) {
            System.err.println("❌ Error en servlet: " + ex.getMessage());
            ex.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("resultado", "error");
            error.put("mensaje", "Error interno: " + ex.getMessage());
            response.getWriter().write(error.toString());
        }
    }

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
                boolean created = directory.mkdirs();
                System.out.println("📁 Directorio de backup creado: " + created + " - " + BACKUP_FOLDER);
                if (!created) {
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

            System.out.println("🚀 Ejecutando pg_dump...");
            System.out.println("📂 Ruta de backup: " + backupFilePath);

            // 7. EJECUTAR PROCESO
            Process p = pb.start();

            // 8. CAPTURAR OUTPUT DEL PROCESO
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println("pg_dump: " + line);
            }
            reader.close();

            // 9. ESPERAR POR LA TERMINACIÓN DEL PROCESO
            int exitCode = p.waitFor();
            System.out.println("📋 Código de salida pg_dump: " + exitCode);

            // 10. VERIFICAR RESULTADO DEL BACKUP
            if (exitCode == 0) {
                backupFile = new File(backupFilePath);
                if (backupFile.exists() && backupFile.length() > 0) {
                    System.out.println("✅ Backup generado exitosamente: " + backupFilePath);
                    System.out.println("📊 Tamaño del archivo: " + backupFile.length() + " bytes");
                    
                    result.put("resultado", "exito");
                    result.put("mensaje", "Backup generado exitosamente y guardado localmente.");
                    result.put("archivo", backupFileName);
                    result.put("ruta", backupFilePath);
                    result.put("tamaño", backupFile.length());
                    
                } else {
                    System.err.println("❌ El archivo de backup no se creó correctamente");
                    result.put("resultado", "error");
                    result.put("mensaje", "El archivo de backup no se creó correctamente o está vacío");
                }
            } else {
                System.err.println("❌ Error en pg_dump. Código: " + exitCode);
                result.put("resultado", "error");
                result.put("mensaje", "Error al generar backup. Código de salida: " + exitCode + "\nDetalles: " + output.toString());
            }

        } catch (IOException | InterruptedException ex) {
            System.err.println("❌ Excepción durante backup: " + ex.getMessage());
            ex.printStackTrace();
            result.put("resultado", "error");
            result.put("mensaje", "Excepción durante la generación del backup: " + ex.getMessage());
            
            // Limpiar archivo temporal en caso de error
            if (backupFile != null && backupFile.exists()) {
                backupFile.delete();
            }
        }

        return result;
    }

    private JSONObject listBackups() {
        JSONObject result = new JSONObject();
        JSONArray backupsArray = new JSONArray();

        try {
            File backupDir = new File(BACKUP_FOLDER);
            System.out.println("🔍 Buscando backups en: " + BACKUP_FOLDER);
            System.out.println("📁 El directorio existe: " + backupDir.exists());

            if (!backupDir.exists()) {
                boolean created = backupDir.mkdirs();
                System.out.println("📁 Directorio creado: " + created);
                result.put("resultado", "exito");
                result.put("backups", backupsArray);
                result.put("total", 0);
                result.put("mensaje", "Directorio de backups creado. No hay backups generados aún.");
                return result;
            }

            // Listar todos los archivos de backup con diferentes extensiones
            File[] backupFiles = backupDir.listFiles((dir, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".backup") || lower.endsWith(".sql") || lower.endsWith(".bak");
            });

            System.out.println("📊 Archivos encontrados: " + (backupFiles != null ? backupFiles.length : 0));

            if (backupFiles != null && backupFiles.length > 0) {
                // Ordenar por fecha de modificación (más reciente primero)
                Arrays.sort(backupFiles, Comparator.comparingLong(File::lastModified).reversed());
                
                for (File file : backupFiles) {
                    JSONObject backupInfo = new JSONObject();
                    backupInfo.put("nombre", file.getName());
                    backupInfo.put("tamano", file.length());
                    backupInfo.put("fechaModificacion", file.lastModified());
                    backupInfo.put("ruta", file.getAbsolutePath());
                    
                    System.out.println("📄 Backup encontrado: " + file.getName() + 
                                     " - Tamano: " + file.length() + 
                                     " - Fecha: " + new Date(file.lastModified()));
                    
                    backupsArray.put(backupInfo);
                }
                
                result.put("resultado", "exito");
                result.put("backups", backupsArray);
                result.put("total", backupsArray.length());
                result.put("mensaje", "Se encontraron " + backupsArray.length() + " backups");
            } else {
                System.out.println("ℹ️ No se encontraron archivos de backup");
                result.put("resultado", "exito");
                result.put("backups", backupsArray);
                result.put("total", 0);
                result.put("mensaje", "No se encontraron backups en el sistema");
            }

        } catch (Exception ex) {
            System.err.println("❌ Error al listar backups: " + ex.getMessage());
            ex.printStackTrace();
            result.put("resultado", "error");
            result.put("mensaje", "Error al listar backups: " + ex.getMessage());
        }

        return result;
    }

    // ... (restoreBackup y deleteBackup methods remain the same)
    private JSONObject restoreBackup(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        try {
            Part filePart = request.getPart("archivoBackup");

            if (filePart == null || filePart.getSize() == 0) {
                result.put("resultado", "error");
                result.put("mensaje", "No se ha seleccionado ningún archivo de backup");
                return result;
            }

            String fileName = filePart.getSubmittedFileName();
            if (!fileName.toLowerCase().endsWith(".backup")
                    && !fileName.toLowerCase().endsWith(".sql")
                    && !fileName.toLowerCase().endsWith(".bak")) {
                result.put("resultado", "error");
                result.put("mensaje", "Tipo de archivo no válido. Use .backup, .sql o .bak");
                return result;
            }

            String tempDir = System.getProperty("java.io.tmpdir") + "backup_restore/";
            File tempDirectory = new File(tempDir);
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            String tempFilePath = tempDir + fileName;
            try ( InputStream fileContent = filePart.getInputStream();  
                  FileOutputStream out = new FileOutputStream(tempFilePath)) {

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

            File pgRestore = new File(PG_BIN_PATH + "pg_restore.exe");
            if (!pgRestore.exists()) {
                result.put("resultado", "error");
                result.put("mensaje", "pg_restore.exe no encontrado en: " + pgRestore.getAbsolutePath());
                backupFile.delete();
                return result;
            }

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

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            int exitCode = p.waitFor();

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

    private JSONObject deleteBackup(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        try {
            String fileName = request.getParameter("fileName");
            System.out.println("🗑️ Intentando eliminar: " + fileName);

            if (fileName == null || fileName.trim().isEmpty()) {
                result.put("resultado", "error");
                result.put("mensaje", "Nombre de archivo no especificado");
                return result;
            }

            // Prevenir path traversal
            if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
                result.put("resultado", "error");
                result.put("mensaje", "Nombre de archivo no válido");
                return result;
            }

            String backupPath = BACKUP_FOLDER + "\\" + fileName;
            File backupFile = new File(backupPath);

            if (!backupFile.exists()) {
                System.err.println("❌ Archivo no existe: " + backupPath);
                result.put("resultado", "error");
                result.put("mensaje", "El archivo de backup no existe: " + fileName);
                return result;
            }

            if (backupFile.delete()) {
                System.out.println("✅ Archivo eliminado: " + fileName);
                result.put("resultado", "exito");
                result.put("mensaje", "Backup eliminado exitosamente");
                result.put("archivo", fileName);
            } else {
                System.err.println("❌ No se pudo eliminar: " + fileName);
                result.put("resultado", "error");
                result.put("mensaje", "No se pudo eliminar el archivo");
            }

        } catch (Exception ex) {
            System.err.println("❌ Error al eliminar backup: " + ex.getMessage());
            result.put("resultado", "error");
            result.put("mensaje", "Error al eliminar backup: " + ex.getMessage());
        }

        return result;
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestión de backups";
    }
}
