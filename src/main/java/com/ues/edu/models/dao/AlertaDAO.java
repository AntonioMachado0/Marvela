package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Productos;
import com.ues.edu.models.Marca;

import java.sql.*;
import java.util.*;

public class AlertaDAO {

    // 🔸 Productos con baja existencia (<=5 unidades)
    public List<Productos> getProductosBajaExistencia() {
        String sql = "SELECT p.id_producto, p.nombre_producto, m.id_marca, m.nombre_marca, " +
                     "SUM(dc.cantidad_producto) AS total " +
                     "FROM productos p " +
                     "JOIN detalle_compra dc ON p.id_producto = dc.id_producto " +
                     "JOIN marca m ON dc.id_marca = m.id_marca " +
                     "GROUP BY p.id_producto, p.nombre_producto, m.id_marca, m.nombre_marca " +
                     "HAVING SUM(dc.cantidad_producto) > 0 AND SUM(dc.cantidad_producto) <= 5";
        return ejecutarConsulta(sql);
    }

    // 🔸 Productos agotados (0 unidades)
    public List<Productos> getProductosAgotados() {
        String sql = "SELECT p.id_producto, p.nombre_producto, m.id_marca, m.nombre_marca, " +
                     "SUM(dc.cantidad_producto) AS total " +
                     "FROM productos p " +
                     "JOIN detalle_compra dc ON p.id_producto = dc.id_producto " +
                     "JOIN marca m ON dc.id_marca = m.id_marca " +
                     "GROUP BY p.id_producto, p.nombre_producto, m.id_marca, m.nombre_marca " +
                     "HAVING SUM(dc.cantidad_producto) = 0";
        return ejecutarConsulta(sql);
    }

    // 🔸 Productos próximos a vencer (en los próximos 30 días)
    public List<Productos> getProductosProximosAVencer() {
        String sql = "SELECT DISTINCT p.id_producto, p.nombre_producto, m.id_marca, m.nombre_marca, " +
                     "SUM(dc.cantidad_producto) AS total " +
                     "FROM productos p " +
                     "JOIN detalle_compra dc ON p.id_producto = dc.id_producto " +
                     "JOIN marca m ON dc.id_marca = m.id_marca " +
                     "WHERE dc.fecha_vencimiento <= CURRENT_DATE + INTERVAL '30 day' " +
                     "AND dc.cantidad_producto > 0 " +
                     "GROUP BY p.id_producto, p.nombre_producto, m.id_marca, m.nombre_marca";
        return ejecutarConsulta(sql);
    }

    // 🔸 Compras registradas sin detalles dentro de los 5 días
    public List<Map<String, Object>> getComprasSinDetalles() {
        String sql = "SELECT c.codigo_compra, c.numero_de_orden, c.fecha_de_orden " +
                     "FROM compra c " +
                     "LEFT JOIN detalle_compra dc ON c.codigo_compra = dc.codigo_compra " +
                     "WHERE dc.codigo_compra IS NULL " +
                     "AND CURRENT_DATE <= c.fecha_de_orden + INTERVAL '5 day'";

        List<Map<String, Object>> compras = new ArrayList<>();
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> compra = new HashMap<>();
                compra.put("codigo_compra", rs.getInt("codigo_compra"));
                compra.put("numero_de_orden", rs.getString("numero_de_orden"));
                compra.put("fecha_de_orden", rs.getDate("fecha_de_orden"));
                compras.add(compra);
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta de compras sin detalles: " + e.getMessage());
        }
        return compras;
    }

    // 🔹 Método auxiliar para ejecutar consultas comunes
    private List<Productos> ejecutarConsulta(String sql) {
        List<Productos> productos = new ArrayList<>();
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Productos p = new Productos();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre_producto"));

                // Marca
                Marca marca = new Marca();
                marca.setCodigoMarca(rs.getInt("id_marca"));
                marca.setMarca(rs.getString("nombre_marca"));
                p.setMarca(marca);

                // Guardamos la cantidad total del inventario en el campo porcentaje (float)
                p.setPorcentaje(rs.getFloat("total"));

                productos.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta de alerta: " + e.getMessage());
        }
        return productos;
    }
}
