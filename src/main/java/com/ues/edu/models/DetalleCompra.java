/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author mayel
 */
public class DetalleCompra {
    
    private int codigoDetalleCompra;
    private float precioCompra;
    private Compras compra;
    private int cantidad;
    private Productos productos;
    private float precioVentaUnitario;
    private float totalVenta;


    public DetalleCompra() {
    }

    public DetalleCompra(int codigoDetalleCompra, float precioCompra, Compras compra, int cantidad, Productos productos, float precioVentaUnitario, float totalVenta) {
        this.codigoDetalleCompra = codigoDetalleCompra;
        this.precioCompra = precioCompra;
        this.compra = compra;
        this.cantidad = cantidad;
        this.productos = productos;
        this.precioVentaUnitario = precioVentaUnitario;
        this.totalVenta = totalVenta;
    }

    public DetalleCompra(float precioCompra, Compras compra, int cantidad, Productos productos, float precioVentaUnitario, float totalVenta) {
        this.precioCompra = precioCompra;
        this.compra = compra;
        this.cantidad = cantidad;
        this.productos = productos;
        this.precioVentaUnitario = precioVentaUnitario;
        this.totalVenta = totalVenta;
    }

   

 
    public int getCodigoDetalleCompra() {
        return codigoDetalleCompra;
    }

    public void setCodigoDetalleCompra(int codigoDetalleCompra) {
        this.codigoDetalleCompra = codigoDetalleCompra;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Compras getCompra() {
        return compra;
    }

    public void setCompra(Compras compra) {
        this.compra = compra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(float precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public float getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(float totalVenta) {
        this.totalVenta = totalVenta;
    }
    

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }
    
    
    
    
}

