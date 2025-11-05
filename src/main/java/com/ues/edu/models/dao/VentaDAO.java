/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author thebe
 */
public class VentaDAO {
    private Connection conn;

    public VentaDAO() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/FerreteriaBD", "postgres", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int generarNumeroVenta() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(numero_venta), 0) + 1 FROM ventas")) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
    public String generarNumeroVentaFormateado() {
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COALESCE(MAX(CAST(numero_venta AS INT)), 0) + 1 FROM ventas")) {
        if (rs.next()) {
            int numero = rs.getInt(1);
            return String.format("%04d", numero); // ← devuelve "0001", "0028", etc.
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "0001"; // valor por defecto si falla
}

    public int insertarVenta(String numeroVenta, int codigoEmpleado) {
    try (PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO ventas (numero_venta, hora, fecha, codigo_empleado) VALUES (?, CURRENT_TIME, CURRENT_DATE, ?) RETURNING codigo_ventas, numero_venta")) {
        ps.setString(1, numeroVenta); // ← guarda como texto con ceros
        ps.setInt(2, codigoEmpleado);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("codigo_ventas");
        if (rs.next()) return rs.getInt("numero_venta");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
}

    public int obtenerIdProductoPorCodigo(String codigo) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_producto FROM productos WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_producto");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertarDetalleVenta(int cantidad, double total, int codigoVenta, int idProducto) {
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO detalle_ventas (cantidad_producto, total_ventas, codigo_ventas, id_producto) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, cantidad);
            ps.setDouble(2, total);
            ps.setInt(3, codigoVenta);
            ps.setInt(4, idProducto);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

