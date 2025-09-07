/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author mayel
 */
public class DetalleVenta {
    
    private int codigoDetalleVenta;
    private int cantidadProducto;
    private float totalVenta;
    private Ventas venta;
    private Productos producto;

    public DetalleVenta() {
    }

    public DetalleVenta(int codigoDetalleVenta, int cantidadProducto, float totalVenta, Ventas venta, Productos producto) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidadProducto = cantidadProducto;
        this.totalVenta = totalVenta;
        this.venta = venta;
        this.producto = producto;
    }

    public DetalleVenta(int cantidadProducto, float totalVenta, Ventas venta, Productos producto) {
        this.cantidadProducto = cantidadProducto;
        this.totalVenta = totalVenta;
        this.venta = venta;
        this.producto = producto;
    }

    public int getCodigoDetalleVenta() {
        return codigoDetalleVenta;
    }

    public void setCodigoDetalleVenta(int codigoDetalleVenta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public float getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(float totalVenta) {
        this.totalVenta = totalVenta;
    }

    public Ventas getVenta() {
        return venta;
    }

    public void setVenta(Ventas venta) {
        this.venta = venta;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }
    
    
}
