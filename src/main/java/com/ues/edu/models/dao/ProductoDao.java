package com.ues.edu.models.dao;


import com.ues.edu.connection.Conexion;
import com.ues.edu.models.DetalleCompra;
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
import java.io.Console;
import java.io.InputStream;
import java.sql.Statement;

/**
 *
 * @author Maris
 */
public class ProductoDao {

    private Conexion conexion;
//    private static final String SELECT_ALL = "SELECT \n" +
//"  p.nombre_producto,\n" +
//"  dc.cantidad_producto,\n" +
//"  dc.precio_compra AS costo_unitario,\n" +
//"  ROUND(CAST(dc.precio_compra * (1 + p.porcentaje / 100.0) AS numeric), 2) AS precio_venta_unitario,\n" +
//"  ROUND(CAST(dc.cantidad_producto * dc.precio_compra * (1 + p.porcentaje / 100.0) AS numeric), 2) AS total_venta\n" +
//"FROM \n" +
//"  productos p\n" +
//"JOIN \n" +
//" detalle_compra dc ON p.id_producto = dc.id_producto";

    private static final String SELECT_ALL = "SELECT\n" +
"    p.id_producto AS id_producto,\n" +
"    p.codigo_producto AS codigo,\n" +
"    p.nombre_producto AS nombre,\n" +
"    p.imagen,\n" +
"    c.codigo_categoria AS codigo_categoria,\n" +
"    c.nombre AS categoria,\n" +
"    m.id_marca AS id_marca,\n" +
"    m.nombre_marca AS marca,\n" +
"    u.id_medida AS id_medida,\n" +
"    u.nombre_medida AS unidad_medida\n" +
"FROM productos p\n" +
"JOIN categoria c ON p.codigo_categoria = c.codigo_categoria\n" +
"JOIN marca m ON p.id_marca = m.id_marca\n" +
"JOIN unidad_medida u ON p.id_medida = u.id_medida;";


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
        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
        ResultSet rs = ps.executeQuery()
    ) {
        while (rs.next()) {
            Productos compra = new Productos();

            compra.setIdProducto(rs.getInt("id_producto")); // ✅ necesario para ImagenServlet
            compra.setCodigoProducto(rs.getString("codigo"));
            compra.setNombre(rs.getString("nombre"));
            compra.setImagen(rs.getBytes("imagen"));

            Categoria ca = new Categoria();
            ca.setCodigoCategoria(rs.getInt("codigo_categoria"));
            ca.setNombre(rs.getString("categoria"));
            compra.setCategoria(ca);

            Marca empleado = new Marca();
            empleado.setCodigoMarca(rs.getInt("id_marca"));
            empleado.setMarca(rs.getString("marca"));
            compra.setMarca(empleado);

            Medida me = new Medida();
            me.setId_medida(rs.getInt("id_medida"));
            me.setMedida(rs.getString("unidad_medida"));
            compra.setMedida(me);

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

    String sql = "SELECT id_producto, codigo_producto AS codigo, nombre_producto AS nombre, imagen, " +
                 "codigo_categoria, '' AS categoria, id_marca, '' AS marca, id_medida, '' AS unidad_medida " +
                 "FROM productos WHERE id_producto = ?";

    try (Connection con = conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idProducto);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            producto = new Productos();
            producto.setIdProducto(rs.getInt("id_producto"));
            producto.setCodigoProducto(rs.getString("codigo"));
            producto.setNombre(rs.getString("nombre"));
            producto.setImagen(rs.getBytes("imagen"));
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
            + "    codigo_categoria,\n"
            + "    id_marca,\n"
            + "    id_medida\n"
            + ") VALUES (?,?,?,?,?,?);";

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
            ps.setInt(5, com.getMarca().getCodigoMarca());
            ps.setInt(6, com.getMedida().getId_medida());

            resultado_insertar = ps.executeUpdate();

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

    
    
    
}