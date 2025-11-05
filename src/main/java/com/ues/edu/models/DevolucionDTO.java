package com.ues.edu.models;

import java.util.List;
import java.util.Map;

public class DevolucionDTO {
    private String numeroVenta;
    private String fechaVenta;
    private List<Map<String, Object>> productos; // Lista de productos en esta venta

    // Getters y Setters
    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public List<Map<String, Object>> getProductos() {
        return productos;
    }

    public void setProductos(List<Map<String, Object>> productos) {
        this.productos = productos;
    }
}