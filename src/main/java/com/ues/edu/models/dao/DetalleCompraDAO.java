/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Categoria;
import com.ues.edu.models.Compras;
import com.ues.edu.models.DetalleCompra;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Medida;
import com.ues.edu.models.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author thebe
 * @author Maris
 */
public class DetalleCompraDao {

    private Conexion conexion;

    public DetalleCompraDao() {
        this.conexion = new Conexion(); // Asegura que no sea null
    }

    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    private static final String SELECT_ALL = " SELECT\n"
            + "  dc.codigo_detalle_compra,\n"
            + "  p.nombre_producto,\n"
            + "  dc.cantidad_producto,\n"
            + "  dc.precio_compra AS costo_unitario,\n"
            + "  ROUND(CAST(dc.precio_compra * (1 + dc.porcentaje / 100.0) AS numeric), 2) AS precio_venta_unitario,\n"
            + "  ROUND(CAST(dc.cantidad_producto * dc.precio_compra * (1 + dc.porcentaje / 100.0) AS numeric), 2) AS total_venta\n"
            + "FROM\n"
            + "  productos p\n"
            + "JOIN\n"
            + "  detalle_compra dc ON p.id_producto = dc.id_producto\n"
            + "WHERE\n"
            + "  dc.codigo_compra =?";

    public ArrayList<DetalleCompra> selectAllDetalleCompra(Integer codigoCompra) {
        ArrayList<DetalleCompra> comprasList = new ArrayList<>();
        System.out.println("ENTRÓ AL MÉTODO DAO: selectAllDetalleCompra");

        try (
                 Connection connection = conexion.getConexion();  PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ps.setInt(1, codigoCompra); // ← Aquí pasas el filtro
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetalleCompra compra = new DetalleCompra();
                Productos producto = new Productos();

                compra.setCodigoDetalleCompra(rs.getInt("codigo_detalle_compra"));
                producto.setNombre(rs.getString("nombre_producto"));
                compra.setProductos(producto);
                compra.setCantidad(rs.getInt("cantidad_producto"));
                compra.setPrecioCompra(rs.getFloat("costo_unitario"));
                compra.setPrecioVentaUnitario(rs.getFloat("precio_venta_unitario"));
                compra.setTotalVenta(rs.getFloat("total_venta"));

                comprasList.add(compra);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar compras: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return comprasList;
    }
    private static final String INSERT
            = "INSERT INTO detalle_compra ("
            + "codigo_producto, fecha_vencimiento, cantidad_producto, precio_compra, "
            + "porcentaje, medida_producto, codigo_compra, id_producto, id_marca, id_medida"
            + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public String insert(DetalleCompra com) throws SQLException, ClassCastException {
        String resultado;
        int resultado_insertar;

        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(INSERT);

        try {
            // Trazabilidad previa
            System.out.println("🧩 Datos recibidos para insertar:");
            System.out.println("Producto ID: " + com.getProductos().getIdProducto());
            System.out.println("Marca ID: " + com.getMarca().getCodigoMarca());
            System.out.println("Medida ID: " + com.getMedida().getId_medida());
            System.out.println("Compra ID: " + com.getCompra().getIdCompra());

            // Asignación de parámetros
            ps.setString(1, com.getCodigo_producto());
            ps.setDate(2, new java.sql.Date(com.getFecha_vencimiento().getTime()));
            ps.setInt(3, com.getCantidad());
            ps.setFloat(4, com.getPrecioCompra());
            ps.setInt(5, com.getPorcentaje());
            ps.setString(6, com.getMedida_producto());
            ps.setInt(7, com.getCompra().getIdCompra());
            ps.setInt(8, com.getProductos().getIdProducto());
            ps.setInt(9, com.getMarca().getCodigoMarca());
            ps.setInt(10, com.getMedida().getId_medida());

            // Ejecución
            resultado_insertar = ps.executeUpdate();

            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_detalle";
            }

        } catch (SQLException e) {
            resultado = "error_exception";
            System.out.println("❌ Fallo al insertar detalle_compra. Código de error: " + e.getErrorCode());
            e.printStackTrace();
        }

        return resultado;
    }

    private static final String CARGAR = "SELECT c.codigo_detalle_compra, c.codigo_producto, c.fecha_vencimiento, c.cantidad_producto,c.precio_compra,\n"
            + "            c.porcentaje,c.medida_producto ,\n"
              + "            c.codigo_compra,\n"
               + "            c.codigo_compra,m.numero_de_orden,\n"
          
            + "                            c.id_producto, t.nombre_producto, \n"
            + "                             c.id_marca, p.nombre_marca ,\n"
            + "            				 c.id_medida, k.nombre_medida \n"
            + "                        FROM detalle_compra c \n"
            + "           		LEFT JOIN compra m ON c.codigo_compra = m.codigo_compra\n"
            + "                       LEFT JOIN productos t ON c.id_producto = t.id_producto \n"
            + "                        LEFT JOIN marca p ON c.id_marca = p.id_marca \n"
            + "            			LEFT JOIN unidad_medida k ON c.id_medida = k.id_medida \n"
            + "                        WHERE c.codigo_detalle_compra =?";

    public DetalleCompra cargarDatos(DetalleCompra compras) throws SQLException {
        DetalleCompra listaC = new DetalleCompra();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, compras.getCodigoDetalleCompra());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaC.setCodigoDetalleCompra(rs.getInt("codigo_detalle_compra"));
                listaC.setCodigo_producto(rs.getString("codigo_producto"));
                listaC.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                listaC.setCantidad(rs.getInt("cantidad_producto"));
                listaC.setPrecioCompra(rs.getFloat("precio_compra"));
                listaC.setPorcentaje(rs.getInt("porcentaje"));
                listaC.setMedida_producto(rs.getString("medida_producto"));

                Compras em = new Compras();
                em.setIdCompra(rs.getInt("codigo_compra"));
 em.setNumeroOrden(rs.getString("numero_de_orden"));
                listaC.setCompra(em);

                Productos p = new Productos();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre_producto"));
                listaC.setProductos(p);

                Marca ma = new Marca();
                ma.setCodigoMarca(rs.getInt("id_marca"));
                ma.setMarca(rs.getString("nombre_marca"));
                listaC.setMarca(ma);

                Medida mad = new Medida();
                mad.setId_medida(rs.getInt("id_medida"));
                mad.setMedida(rs.getString("nombre_medida"));
                listaC.setMedida(mad);

            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaC;
    }

    private static final String UPDATE = "UPDATE detalle_compra SET codigo_producto=?, fecha_vencimiento=?, cantidad_producto=?, precio_compra=?, porcentaje=?,medida_producto=?,codigo_compra=?,id_producto=?,id_marca=?,id_medida=? WHERE codigo_detalle_compra = ?";

    public String update(DetalleCompra compras) throws SQLException {
        String resultado;
        int resultado_update;

        try {
            // Obtener conexión
            this.accesoDB = this.conexion.getConexion();

            // Preparar sentencia
            this.ps = this.accesoDB.prepareStatement(UPDATE);

            ps.setString(1, compras.getCodigo_producto());
            ps.setDate(2, compras.getFecha_vencimiento());
            ps.setInt(3, compras.getCantidad());
            ps.setFloat(4, compras.getPrecioCompra());
            ps.setInt(5, compras.getPorcentaje());
            ps.setString(6, compras.getMedida_producto());
            ps.setInt(7, compras.getCompra().getIdCompra()); // ← corregido nombre del método
            ps.setInt(8, compras.getProductos().getIdProducto());
            ps.setInt(9, compras.getMarca().getCodigoMarca());
            ps.setInt(10, compras.getMedida().getId_medida());
            ps.setInt(11, compras.getCodigoDetalleCompra());

            // Ejecutar actualización
            resultado_update = ps.executeUpdate();
            // Evaluar resultado
            if (resultado_update > 0) {
                resultado = "exito";
            } else {
                resultado = "error_update";
            }

        } catch (SQLException e) {
            resultado = "error_excepcion";
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
        } finally {
            // Cierre de recursos
            try {
                if (ps != null) {
                    ps.close();
                }
                if (accesoDB != null) {
                    accesoDB.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return resultado;
    }

}
