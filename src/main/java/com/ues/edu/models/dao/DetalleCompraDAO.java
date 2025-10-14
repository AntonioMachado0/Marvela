/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.DetalleCompra;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author thebe
 */
public class DetalleCompraDAO {

    public DetalleCompra obtenerPorCodigo(String codigo) {
        DetalleCompra detalle = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexionStatic(); // ✅ Solo asignas, no redeclaras
if (conn != null) {
    System.out.println("✅ Conexión establecida correctamente");
} else {
    System.out.println("❌ Falló la conexión a la base de datos");
}
            String sql
                    = "SELECT p.nombre_producto, m.nombre_marca, "
                    + "ROUND(CAST(dc.precio_compra * (1 + dc.porcentaje / 100.0) AS numeric), 2) AS precio_venta_unitario "
                    + "FROM productos p "
                    + "JOIN marca m ON p.id_marca = m.id_marca "
                    + "JOIN detalle_compra dc ON p.id_producto = dc.id_producto "
                    + "WHERE p.codigo_producto = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            System.out.println("Ejecutando consulta SQL para código: " + codigo);
            if (rs.next()) {
                
                Productos producto = new Productos();
                producto.setNombre(rs.getString("nombre_producto"));
                Marca marca = new Marca();
                marca.setMarca(rs.getString("nombre_marca"));
                producto.setMarca(marca);
                producto.setPrecioVenta(rs.getDouble("precio_venta_unitario"));
System.out.println("✅ Producto encontrado en la base de datos");
                detalle = new DetalleCompra();
                detalle.setProductos(producto);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener producto por código: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.cerrarConexiones(conn, stmt, rs);
        }

        return detalle;
    }
}
