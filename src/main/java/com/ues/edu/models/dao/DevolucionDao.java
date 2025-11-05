package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.DevolucionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevolucionDao {

    Conexion conexion;
    private ArrayList<DevolucionDTO> lista;

    public DevolucionDao() {
        this.conexion = new Conexion();
    }

    // Método para obtener ventas recientes (últimos 3 días)
    public ArrayList<DevolucionDTO> mostrarVentasRecientes() throws SQLException {
        System.out.println("ENTRO AL METODO DAO MOSTRAR VENTAS RECIENTES (ÚLTIMOS 3 DÍAS)");

        Map<String, DevolucionDTO> ventasMap = new HashMap<>();

       // En DevolucionDao.mostrarVentasRecientes()
String SQL = "SELECT\n"
        + "    v.numero_venta, \n"
        + "    dv.cantidad_producto, \n"
        + "    p.nombre_producto,\n"
        + "    p.id_producto,\n"
        + "    m.nombre_marca,\n"
        + "    v.fecha,\n"
        + "    dv.codigo_detalle_venta,\n"
        + "    dv.codigo_detalle_compra\n"
        + "FROM\n"
        + "    ventas v\n"
        + "    INNER JOIN detalle_ventas dv ON v.codigo_ventas = dv.codigo_ventas\n"
        + "    INNER JOIN productos p ON dv.id_producto = p.id_producto\n"
        + "    INNER JOIN detalle_compra dc ON dv.codigo_detalle_compra = dc.codigo_detalle_compra\n"  // CORREGIDO
        + "    INNER JOIN marca m ON dc.id_marca = m.id_marca\n"
        + "WHERE\n"
        + "    v.fecha >= (CURRENT_DATE - 3)\n"
        + "ORDER BY\n"
        + "    v.fecha DESC,\n"
        + "    p.nombre_producto ASC;";

        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        while (rs.next()) {
            String numeroVenta = rs.getString("numero_venta");
            String nombreProducto = rs.getString("nombre_producto");
            String nombre_marca = rs.getString("nombre_marca");
            int cantidad = rs.getInt("cantidad_producto");
            int idProducto = rs.getInt("id_producto");
            int codigoDetalleVenta = rs.getInt("codigo_detalle_venta");
            int codigoDetalleCompra = rs.getInt("codigo_detalle_compra");

            // Formatear la fecha
            String fechaFormateada = "";
            try {
                java.sql.Date fechaSQL = rs.getDate("fecha");
                if (fechaSQL != null) {
                    java.util.Date fecha = new java.util.Date(fechaSQL.getTime());
                    fechaFormateada = dateFormat.format(fecha);
                }
            } catch (Exception e) {
                System.err.println("Error al formatear fecha: " + e.getMessage());
                String fechaOriginal = rs.getString("fecha");
                if (fechaOriginal != null && fechaOriginal.length() >= 10) {
                    fechaFormateada = fechaOriginal.substring(8, 10) + "-"
                            + fechaOriginal.substring(5, 7) + "-"
                            + fechaOriginal.substring(0, 4);
                }
            }

            if (!ventasMap.containsKey(numeroVenta)) {
                DevolucionDTO objDev = new DevolucionDTO();
                objDev.setNumeroVenta(numeroVenta);
                objDev.setFechaVenta(fechaFormateada);
                objDev.setProductos(new ArrayList<>());
                ventasMap.put(numeroVenta, objDev);
            }

            DevolucionDTO ventaExistente = ventasMap.get(numeroVenta);

            Map<String, Object> producto = new HashMap<>();
            producto.put("nombre", nombreProducto);
            producto.put("nombre_marca", nombre_marca);
            producto.put("cantidad", cantidad);
            producto.put("idProducto", idProducto);
            producto.put("codigoDetalleVenta", codigoDetalleVenta);
            producto.put("codigoDetalleCompra", codigoDetalleCompra);

            ventaExistente.getProductos().add(producto);
        }

        // Convertir el Map a List y ordenar numéricamente
        this.lista = new ArrayList<>(ventasMap.values());

        // Ordenar la lista numéricamente por número de venta
        Collections.sort(this.lista, new Comparator<DevolucionDTO>() {
            @Override
            public int compare(DevolucionDTO v1, DevolucionDTO v2) {
                try {
                    int num1 = Integer.parseInt(v1.getNumeroVenta());
                    int num2 = Integer.parseInt(v2.getNumeroVenta());
                    return Integer.compare(num1, num2);
                } catch (NumberFormatException e) {
                    return v1.getNumeroVenta().compareTo(v2.getNumeroVenta());
                }
            }
        });

        this.conexion.cerrarConexiones();

        System.out.println("VENTAS ENCONTRADAS (últimos 3 días): " + this.lista.size());
        return this.lista;
    }

    public List<Map<String, Object>> obtenerProductosDisponibles() throws SQLException {
        System.out.println("OBTENIENDO PRODUCTOS DISPONIBLES EN INVENTARIO");

        List<Map<String, Object>> productos = new ArrayList<>();

        String SQL = "SELECT\n"
                + "  p.id_producto,\n"
                + "  dc.codigo_producto,\n"
                + "  p.nombre_producto,\n"
                + "  p.descripcion,\n"
                + "  SUM(dc.cantidad_producto) AS stock_total,\n"
                + "  m.nombre_marca,\n"
                + "  dc.medida_producto,\n"
                + "  um.nombre_medida,\n"
                + "  dc.precio_compra,\n"
                + "  dc.porcentaje,\n"
                + "  ROUND(CAST(dc.precio_compra * (1 + dc.porcentaje / 100.0) AS numeric), 2) AS precio_venta\n"
                + "FROM\n"
                + "  productos p\n"
                + "LEFT JOIN detalle_compra dc ON p.id_producto = dc.id_producto\n"
                + "LEFT JOIN marca m ON dc.id_marca = m.id_marca\n"
                + "LEFT JOIN unidad_medida um ON dc.id_medida = um.id_medida\n"
                + "GROUP BY\n"
                + "  p.id_producto,\n"
                + "  dc.codigo_producto,\n"
                + "  p.nombre_producto,\n"
                + "  p.descripcion,\n"
                + "  m.nombre_marca,\n"
                + "  dc.medida_producto,\n"
                + "  um.nombre_medida,\n"
                + "  dc.precio_compra,\n"
                + "  dc.porcentaje\n"
                + "HAVING\n"
                + "  SUM(dc.cantidad_producto) > 0\n"
                + "ORDER BY\n"
                + "  p.nombre_producto ASC;";

        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String, Object> producto = new HashMap<>();
            producto.put("id_producto", rs.getInt("id_producto"));
            producto.put("codigo_producto", rs.getString("codigo_producto"));
            producto.put("nombre_producto", rs.getString("nombre_producto"));
            producto.put("descripcion", rs.getString("descripcion"));
            producto.put("stock_total", rs.getInt("stock_total"));
            producto.put("marca", rs.getString("nombre_marca"));
            producto.put("medida_producto", rs.getString("medida_producto"));
            producto.put("medida", rs.getString("nombre_medida"));
            producto.put("precio_venta", rs.getFloat("precio_venta"));
            productos.add(producto);
        }

        this.conexion.cerrarConexiones();

        System.out.println("PRODUCTOS DISPONIBLES ENCONTRADOS: " + productos.size());
        return productos;
    }

public boolean procesarDevolucionIntercambio(String numeroVenta,
        int productoDevueltoId,
        int productoNuevoId,
        int cantidad,
        int codigoDetalleVenta,
        int codigoDetalleCompra,
        int cantidadOriginal) throws SQLException {

    System.out.println("=== 🔁 INICIANDO PROCESO DE DEVOLUCIÓN/INTERCAMBIO (ACTUALIZACIÓN DIRECTA) ===");
    Connection connection = null;

    try {
        connection = conexion.getConexion();
        connection.setAutoCommit(false);

        // 1️⃣ Obtener código interno de la venta
        String sqlVenta = "SELECT codigo_ventas FROM ventas WHERE numero_venta = ?";
        PreparedStatement psVenta = connection.prepareStatement(sqlVenta);
        psVenta.setString(1, numeroVenta);
        ResultSet rsVenta = psVenta.executeQuery();
        int codigoVentas = 0;
        if (rsVenta.next()) {
            codigoVentas = rsVenta.getInt("codigo_ventas");
        } else {
            throw new SQLException("No se encontró la venta con número: " + numeroVenta);
        }

        // 2️⃣ Reingresar producto devuelto al inventario
        String sqlReingreso = "UPDATE detalle_compra SET cantidad_producto = cantidad_producto + ? WHERE codigo_detalle_compra = ?";
        PreparedStatement psReingreso = connection.prepareStatement(sqlReingreso);
        psReingreso.setInt(1, cantidad);
        psReingreso.setInt(2, codigoDetalleCompra);
        int filasReingreso = psReingreso.executeUpdate();
        if (filasReingreso == 0) {
            throw new SQLException("No se pudo reingresar el producto devuelto al inventario");
        }

        // 3️⃣ Obtener nuevo lote (detalle_compra) del producto nuevo con su precio de venta
        String sqlObtenerNuevo =
            "SELECT dc.codigo_detalle_compra, dc.codigo_producto, dc.precio_compra, dc.porcentaje " +
            "FROM detalle_compra dc " +
            "WHERE dc.id_producto = ? AND dc.cantidad_producto > 0 " +
            "ORDER BY dc.fecha_vencimiento ASC NULLS LAST LIMIT 1";
        PreparedStatement psObtenerNuevo = connection.prepareStatement(sqlObtenerNuevo);
        psObtenerNuevo.setInt(1, productoNuevoId);
        ResultSet rsNuevo = psObtenerNuevo.executeQuery();

        int codigoDetalleCompraNuevo = 0;
        String codigoProductoNuevo = null;
        double precioVenta = 0.0;

        if (rsNuevo.next()) {
            codigoDetalleCompraNuevo = rsNuevo.getInt("codigo_detalle_compra");
            codigoProductoNuevo = rsNuevo.getString("codigo_producto");

            double precioCompra = rsNuevo.getDouble("precio_compra");
            double porcentaje = rsNuevo.getDouble("porcentaje");
            precioVenta = precioCompra * (1 + porcentaje / 100.0);
        } else {
            throw new SQLException("⚠️ No hay stock disponible del nuevo producto");
        }

        // Calcular total
        double totalVenta = precioVenta * cantidad;

        // 4️⃣ Actualizar el mismo registro del detalle de venta
        String sqlActualizarVenta =
            "UPDATE detalle_ventas " +
            "SET id_producto = ?, cantidad_producto = ?, codigo_detalle_compra = ?, total_ventas = ? " +
            "WHERE codigo_detalle_venta = ?";
        PreparedStatement psActualizarVenta = connection.prepareStatement(sqlActualizarVenta);
        psActualizarVenta.setInt(1, productoNuevoId);
        psActualizarVenta.setInt(2, cantidad);
        psActualizarVenta.setInt(3, codigoDetalleCompraNuevo);
        psActualizarVenta.setDouble(4, totalVenta);
        psActualizarVenta.setInt(5, codigoDetalleVenta);
        int filasActualizadas = psActualizarVenta.executeUpdate();

        if (filasActualizadas == 0) {
            throw new SQLException("No se pudo actualizar el detalle de venta");
        }

        // 5️⃣ Restar stock del nuevo producto
        String sqlRestarStock =
            "UPDATE detalle_compra " +
            "SET cantidad_producto = cantidad_producto - ? " +
            "WHERE codigo_detalle_compra = (" +
            "   SELECT codigo_detalle_compra FROM detalle_compra " +
            "   WHERE codigo_producto = ? AND id_producto = ? AND cantidad_producto >= ? " +
            "   ORDER BY fecha_vencimiento ASC NULLS LAST LIMIT 1" +
            ")";
        PreparedStatement psRestarStock = connection.prepareStatement(sqlRestarStock);
        psRestarStock.setInt(1, cantidad);
        psRestarStock.setString(2, codigoProductoNuevo);
        psRestarStock.setInt(3, productoNuevoId);
        psRestarStock.setInt(4, cantidad);
        int filasStock = psRestarStock.executeUpdate();

        if (filasStock == 0) {
            throw new SQLException("⚠️ No se pudo restar stock del nuevo producto (revisa existencia o código)");
        }

        // 6️⃣ Confirmar transacción
        connection.commit();
        System.out.println("✅ DEVOLUCIÓN/INTERCAMBIO ACTUALIZADA CORRECTAMENTE (sin insertar nuevo detalle)");
        return true;

    } catch (SQLException e) {
        System.err.println("❌ ERROR EN DEVOLUCIÓN/INTERCAMBIO: " + e.getMessage());
        if (connection != null) {
            connection.rollback();
            System.err.println("ROLLBACK EJECUTADO");
        }
        throw e;
    } finally {
        if (connection != null) {
            connection.setAutoCommit(true);
            conexion.cerrarConexiones();
        }
    }
}



    // Método para verificar stock disponible de un producto
    public int verificarStockDisponible(int idProducto) throws SQLException {
        String SQL = "SELECT SUM(cantidad_producto) as stock_total FROM detalle_compra WHERE id_producto = ? AND cantidad_producto > 0";

        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setInt(1, idProducto);
        ResultSet rs = ps.executeQuery();

        int stock = 0;
        if (rs.next()) {
            stock = rs.getInt("stock_total");
        }

        this.conexion.cerrarConexiones();
        return stock;
    }
}
