/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author mayel
 */
import com.ues.edu.models.dao.EmpleadoDao;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.sql.SQLException;

public class EmailUtil {
    
    // Configuración del servidor de correo (ajusta estos valores)
    EmpleadoDao dao;
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "ferreteriamarvela@gmail.com";
    private static final String EMAIL_PASSWORD = "svgu isrf bwzi viwr"; // Usar contraseña de aplicación

    public EmailUtil() {
        dao = new EmpleadoDao();
    }
    
    
    
    public static boolean enviarBackupPorCorreo(String destinatario, String asunto, String mensaje, 
                                              File archivoBackup, String nombreArchivo) {
        try {
            // Configurar propiedades
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            
            // Crear sesión
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            // Crear mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            
            // Crear parte del mensaje
            MimeBodyPart textoPart = new MimeBodyPart();
            textoPart.setText(mensaje, "utf-8", "html");
            
            // Crear parte del archivo adjunto
            MimeBodyPart adjuntoPart = new MimeBodyPart();
            adjuntoPart.attachFile(archivoBackup);
            adjuntoPart.setFileName(nombreArchivo);
            
            // Combinar partes
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoPart);
            multipart.addBodyPart(adjuntoPart);
            
            message.setContent(multipart);
            
            // Enviar mensaje
            Transport.send(message);
            return true;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    // Método para obtener el destinatario desde la base de datos o configuración
    public  String obtenerDestinatarioBackup() throws SQLException {
        // Puedes obtener esto de la base de datos, un archivo de configuración, o hardcodearlo
        String correo = dao.obtenerCorreoAdministrador();
        return correo; // Cambia por el correo destino
    }
}
