package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventarioDao {

    Conexion conexion;
    private ArrayList<ProductoCompra> lista;
    private ArrayList<Categoria> listaCat;
    private ResultSet rs = null;

    public InventarioDao() {
        this.conexion = new Conexion();
    }

    // Método para obtener el inventario completo con imágenes
    public ArrayList<ProductoCompra> mostrarProductosAgrupados() throws SQLException {
        System.out.println("ENTRO AL METODO DAO MOSTRAR PRODUCTOS AGRUPADOS");
        this.lista = new ArrayList<>();

        String SQL = "SELECT \n"
                + "    p.nombre_producto,\n"
                + "    dc.cantidad_producto,\n"
                + "    ROUND(CAST(dc.precio_compra * (1 + dc.porcentaje/ 100.0) AS numeric), 2) AS precio_venta,\n"
                + "    c.fecha_de_orden AS fecha_compra,\n"
                + "    m.nombre_marca AS marca,\n"
                + "    dc.medida_producto,\n"
                + "    dc.fecha_vencimiento,\n"
                + "    dc.codigo_producto,\n"
                + "    um.nombre_medida,\n"
                + "    p.imagen\n"
                + "FROM detalle_compra dc\n"
                + "JOIN productos p ON dc.id_producto = p.id_producto\n"
                + "JOIN compra c ON dc.codigo_compra = c.codigo_compra\n"
                + "JOIN marca m ON dc.id_marca = m.id_marca\n"
                + "JOIN unidad_medida um ON dc.id_medida = um.id_medida\n"
                + "WHERE dc.cantidad_producto > 0\n"
                + "ORDER BY fecha_compra ASC;";

        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        ProductoCompra productoCompra = null;
        while (rs.next()) {
            productoCompra = new ProductoCompra();

            // Datos básicos del producto
            productoCompra.setNombreProducto(rs.getString("nombre_producto"));
            productoCompra.setNombreMarca(rs.getString("marca"));
            productoCompra.setCantidadProducto(rs.getInt("cantidad_producto"));
            productoCompra.setCodigoProducto(rs.getString("codigo_producto"));
            productoCompra.setPrecioVenta(rs.getDouble("precio_venta"));
            productoCompra.setFechaCompra(rs.getDate("fecha_compra"));
            productoCompra.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
            productoCompra.setNombreMedida(rs.getString("nombre_medida"));
            productoCompra.setCantidadMedida(rs.getString("medida_producto"));

            // Obtener y procesar la imagen
            byte[] imagenBytes = rs.getBytes("imagen");
            if (imagenBytes != null && imagenBytes.length > 0) {
                String imagenBase64 = java.util.Base64.getEncoder().encodeToString(imagenBytes);
                productoCompra.setImagen(imagenBase64);
                
                // Detectar automáticamente el tipo de imagen
                String tipoDetectado = detectarTipoImagen(imagenBytes);
                productoCompra.setTipoImagen(tipoDetectado);
            }

            this.lista.add(productoCompra);
            System.out.println("Producto agregado: " + productoCompra.getNombreProducto() + " - Imagen: " + (imagenBytes != null ? "Sí" : "No"));
        }

        this.conexion.cerrarConexiones();
        return this.lista;
    }

    // Método para detectar automáticamente el tipo de imagen
    private String detectarTipoImagen(byte[] imagenBytes) {
        if (imagenBytes.length < 4) {
            return "image/jpeg"; // Por defecto
        }
        
        // Detectar PNG
        if (imagenBytes[0] == (byte) 0x89 && imagenBytes[1] == (byte) 0x50 && 
            imagenBytes[2] == (byte) 0x4E && imagenBytes[3] == (byte) 0x47) {
            return "image/png";
        }
        
        // Detectar JPEG
        if (imagenBytes[0] == (byte) 0xFF && imagenBytes[1] == (byte) 0xD8) {
            return "image/jpeg";
        }
        
        // Detectar GIF
        if (imagenBytes[0] == (byte) 0x47 && imagenBytes[1] == (byte) 0x49 && 
            imagenBytes[2] == (byte) 0x46) {
            return "image/gif";
        }
        
        // Detectar WebP
        if (imagenBytes[0] == (byte) 0x52 && imagenBytes[1] == (byte) 0x49 && 
            imagenBytes[2] == (byte) 0x46 && imagenBytes[3] == (byte) 0x46) {
            return "image/webp";
        }
        
        // Detectar BMP
        if (imagenBytes[0] == (byte) 0x42 && imagenBytes[1] == (byte) 0x4D) {
            return "image/bmp";
        }
        
        // Por defecto, asumir JPEG
        return "image/jpeg";
    }

    // Método para obtener las categorías (sin cambios)
    public ArrayList<Categoria> mostrarCategorias() throws SQLException {
        System.out.println("ENTRO AL METODO DAO MOSTRAR CATEGORIAS");
        this.listaCat = new ArrayList<>();

        String SQL = "SELECT\n"
                + "	ct.nombre, \n"
                + "	ct.codigo_categoria\n"
                + "FROM\n"
                + "	categoria ct";
        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        Categoria cat = null;
        while (rs.next()) {
            cat = new Categoria();
            cat.setCodigoCategoria(rs.getInt("codigo_categoria"));
            cat.setNombre(rs.getString("nombre"));
           
            this.listaCat.add(cat);
        }

        this.conexion.cerrarConexiones();
        return this.listaCat;
    }
}