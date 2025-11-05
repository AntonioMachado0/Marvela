/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import java.util.HashMap;
import java.util.Map;

public class BloqueoDao {
    private static Map<String, Integer> intentosFallidos = new HashMap<>();
    private static Map<String, Long> bloqueos = new HashMap<>();
    private static final int MAX_INTENTOS = 3;
    private static final long DURACION_BLOQUEO_MS = 5 * 60 * 1000;

    public static boolean estaBloqueado(String correo) {
        if (!bloqueos.containsKey(correo)) return false;

        long inicio = bloqueos.get(correo);
        long ahora = System.currentTimeMillis();
        long restante = DURACION_BLOQUEO_MS - (ahora - inicio);

        if (restante <= 0) {
            bloqueos.remove(correo);
            intentosFallidos.remove(correo);
            return false;
        }

        return true;
    }

    public static long obtenerTiempoRestante(String correo) {
        if (!bloqueos.containsKey(correo)) return 0;
        long inicio = bloqueos.get(correo);
        long ahora = System.currentTimeMillis();
        return Math.max(DURACION_BLOQUEO_MS - (ahora - inicio), 0);
    }

    public static String obtenerEstadoBloqueo(String correo) {
        int intentos = intentosFallidos.getOrDefault(correo, 0);
        if (bloqueos.containsKey(correo)) {
            long restante = obtenerTiempoRestante(correo);
            int segundos = (int) (restante / 1000);
            int minutos = segundos / 60;
            int segRestantes = segundos % 60;
            return "⏳ Bloqueado. Intenta en " + minutos + "m " + segRestantes + "s.";
        } else {
            int restantes = MAX_INTENTOS - intentos;
            return "Te faltan " + restantes + " intento" + (restantes == 1 ? "" : "s") + " antes del bloqueo.";
        }
    }

    public static void registrarIntentoFallido(String correo) {
        int intentos = intentosFallidos.getOrDefault(correo, 0) + 1;
        intentosFallidos.put(correo, intentos);
        if (intentos >= MAX_INTENTOS) {
            bloqueos.put(correo, System.currentTimeMillis());
        }
    }

    public static void reiniciarIntentos(String correo) {
        intentosFallidos.remove(correo);
        bloqueos.remove(correo);
    }
}