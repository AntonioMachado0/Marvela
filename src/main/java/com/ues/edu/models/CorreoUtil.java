/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;


public class CorreoUtil {

    private static final String REMITENTE = "ferreteriamarvela@gmail.com";
    private static final String CLAVE = "siyr elpq xlkn mygz"; // Usa clave de aplicación si es Gmail

    public static void enviar(String destinatario, String asunto, String mensajeTexto) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session sesion = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE);
            }
        });

        Message mensaje = new MimeMessage(sesion);
        mensaje.setFrom(new InternetAddress(REMITENTE));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject(asunto);
        mensaje.setText(mensajeTexto);

        Transport.send(mensaje);
    }

    // Variante específica para recuperación
    public static void enviarCodigoRecuperacion(String correo, String codigo) throws MessagingException {
        String asunto = "Recuperación de contraseña - Ferretería Marvela";
        String cuerpo = "Hola, tu contraseña temporal es: " + codigo + "\n\nPor favor cámbiala después de iniciar sesión.";
        enviar(correo, asunto, cuerpo);
    }
}