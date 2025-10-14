/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.models.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Categoria;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Medida;

/**
 *
 * @author Maris
 */
public class ProductoDao {

    private Conexion conexion;

    private static final String SELECT_ALL = "	SELECT\n"
            + "               p.id_producto AS id_producto,\n"
            + "\n"
            + "               p.nombre_producto AS nombre,\n"
            + "			   p.descripcion,\n"
            + "               p.imagen,\n"
            + "               c.codigo_categoria AS codigo_categoria,\n"
            + "               c.nombre AS categoria\n"
            + "           FROM productos p\n"
            + "          JOIN categoria c ON p.codigo_categoria = c.codigo_categoria;";

    private static final String UPDATE = "UPDATE productos SET nombre_producto=?, descripcion=?, imagen=?, codigo_categoria=? WHERE id_producto = ?";

    public ProductoDao() {
        this.conexion = new Conexion(); // Asegura que no sea null
    }

    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    public ArrayList<Productos> selectAllProductos(Integer estado, String quien) {
        ArrayList<Productos> comprasList = new ArrayList<>();
        System.out.println("ENTRÓ AL MÉTODO DAO: selectAllProductos");

        try (
                 Connection connection = conexion.getConexion();  PreparedStatement ps = connection.prepareStatement(SELECT_ALL);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Productos compra = new Productos();

                compra.setIdProducto(rs.getInt("id_producto")); // ✅ necesario para ImagenServlet

                compra.setNombre(rs.getString("nombre"));
                compra.setDescripcion(rs.getString("descripcion"));
                compra.setImagen(rs.getBytes("imagen"));
                Categoria ca = new Categoria();
                ca.setCodigoCategoria(rs.getInt("codigo_categoria"));
                ca.setNombre(rs.getString("categoria"));
                compra.setCategoria(ca);

                System.out.println("🧾 Producto cargado: " + compra.getNombre() + " | ID: " + compra.getIdProducto());
                comprasList.add(compra);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar compras: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return comprasList;
    }

    public Productos obtenerProductoPorId(int idProducto) {
        Productos producto = null;

        String sql = "SELECT id_producto,  nombre_producto AS nombre, imagen, \n"
                + "                codigo_categoria,'' AS categoria\n"
                + "                FROM productos WHERE id_producto = ?";

        try ( Connection con = conexion.getConexion();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                producto = new Productos();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setImagen(rs.getBytes("imagen"));
                Categoria cat = new Categoria();
                cat.setCodigoCategoria(rs.getInt("codigo_categoria"));

                // Puedes omitir categoría, marca y medida si no son necesarias aquí
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en obtenerProductoPorId: " + e.getMessage());
        }

        return producto;
    }
    private static final String INSERT = "INSERT INTO productos (\n"
            + "    nombre_producto,\n"
            + "    descripcion,\n"
            + "    imagen,\n"
            + "    codigo_categoria\n"
            + ") VALUES (?,?,?,?);";

    public String insert(Productos com) throws SQLException, ClassCastException {
        String resultado;
        int resultado_insertar;
        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(INSERT);
        try {
            System.out.println("entro1");
            //codigo_compra, numero_de_orden ,fecha_de_orden, codigo_empleado,codigo_proveedor)
            ps.setString(1, com.getNombre());
            ps.setString(2, com.getDescripcion());
            ps.setBytes(3, com.getImagen());
            ps.setInt(4, com.getCategoria().getCodigoCategoria());
            resultado_insertar = ps.executeUpdate();
            System.out.println("datos" + resultado_insertar);
            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_empleado";
            }

        } catch (SQLException e) {
            resultado = "error_exception";
            System.out.println("fallo insertar" + e.getErrorCode());
            e.printStackTrace();
        }
        return resultado;
    }

    private static final String CARGAR = "SELECT \n"
            + "    p.id_producto,\n"
            + "    p.nombre_producto ,\n"
            + "    p.descripcion ,\n"
            + "    p.imagen,\n"
            + "    c.codigo_categoria,\n"
            + "    c.nombre \n"
            + "FROM productos p\n"
            + "LEFT JOIN categoria c ON p.codigo_categoria = c.codigo_categoria\n"
            + "WHERE p.id_producto = ?;";

    public Productos cargarDatos(Productos productos) throws SQLException {
        Productos listaC = new Productos();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, productos.getIdProducto());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaC.setIdProducto(rs.getInt("id_producto"));
                listaC.setNombre(rs.getString("nombre_producto"));
                listaC.setDescripcion(rs.getString("descripcion"));
                listaC.setImagen(rs.getBytes("imagen"));

                Categoria em = new Categoria();
                em.setCodigoCategoria(rs.getInt("codigo_categoria"));
                em.setNombre(rs.getString("nombre"));
                listaC.setCategoria(em);

            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaC;
    }

    public String update(Productos producto) throws SQLException {
        String resultado;
        int resultado_update;

        try {
            // Obtener conexión
            this.accesoDB = this.conexion.getConexion();

            // Preparar sentencia
            this.ps = this.accesoDB.prepareStatement(UPDATE);

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBytes(3, producto.getImagen());
            ps.setInt(4, producto.getCategoria().getCodigoCategoria()); // ← corregido nombre del método

            ps.setInt(5, producto.getIdProducto());

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

    public boolean existeProducto(String nombreProducto) throws SQLException {
        String sql = "SELECT COUNT(*) FROM productos WHERE LOWER(nombre_producto) = LOWER(?)";
        try ( Connection conn = conexion.getConexion();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreProducto.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean existeProductoConOtroId(String nombreProducto, int idActual) throws SQLException {
        String sql = "SELECT COUNT(*) FROM productos WHERE LOWER(nombre_producto) = LOWER(?) AND id_producto != ?";
        try ( Connection conn = conexion.getConexion();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreProducto.trim());
            ps.setInt(2, idActual);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private ArrayList<Productos> listProveedor;
    private ArrayList<Productos> listarCombo;

    private static final String CARGAR_COMBO_MEDIDA = "SELECT \n"
            + "    a.id_producto, \n"
            + "    a.nombre_producto\n"
            + "FROM \n"
            + "    productos a\n"
            + "ORDER BY \n"
            + "    a.nombre_producto ASC;";

    public ArrayList<Productos> cargarComboP() throws SQLException {
        listarCombo = new ArrayList();
        System.out.println("ENTRO AL METODO DAO MOSTRAR");
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CARGAR_COMBO_MEDIDA);
            this.rs = this.ps.executeQuery();
            Productos c = null;
            while (this.rs.next()) {
                c = new Productos();
                c.setIdProducto(rs.getInt("id_producto"));
                c.setNombre(rs.getString("nombre_producto"));
                listarCombo.add(c);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return listarCombo;
    }

}
