/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Compras;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Proveedores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Maris
 */
public class ComprasDao {

    private Conexion conexion;
    private static final String SELECT_ALL = "SELECT "
            + "a.codigo_compra, a.numero_de_orden, a.fecha_de_orden, "
            + "p.codigo_proveedor, p.nombre_proveedor, "
            + "e.codigo_empleado, e.nombre_completo "
            + "FROM compra a "
            + "INNER JOIN proveedores p ON a.codigo_proveedor = p.codigo_proveedor "
            + "INNER JOIN empleado e ON a.codigo_empleado = e.codigo_empleado "
            + "ORDER BY a.fecha_de_orden DESC";
    
    

   private static final String UPDATE = "UPDATE compra SET numero_de_orden=?, fecha_de_orden=?, codigo_empleado=?, codigo_proveedor=? WHERE codigo_compra = ?";
   private static final String INSERT_COMPRA = "INSERT INTO compra( numero_de_orden ,fecha_de_orden, codigo_empleado,codigo_proveedor) VALUES(?, ?, ?, ?);";
   private static final String INSERT = "INSERT INTO compra (numero_de_orden, fecha_de_orden, codigo_empleado,codigo_proveedor) VALUES (?, ?, ?, ?)";

    private static final String CARGAR = "SELECT c.codigo_compra, c.numero_de_orden, c.fecha_de_orden, "
            + "       c.codigo_empleado, e.nombre_completo, "
            + "       c.codigo_proveedor, p.nombre_proveedor "
            + "FROM compra c "
            + "LEFT JOIN empleado e ON c.codigo_empleado = e.codigo_empleado "
            + "LEFT JOIN proveedores p ON c.codigo_proveedor = p.codigo_proveedor "
            + "WHERE c.codigo_compra = ?";

    
    
      private static final String CODIGO ="SELECT c.codigo_compra,c.numero_de_orden FROM Compra c";
    
    public ComprasDao() {
        this.conexion = new Conexion(); // ✅ inicialización segura
    }

    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    public ArrayList<Compras> selectAllCompra(Integer estado, String quien) {
        ArrayList<Compras> comprasList = new ArrayList<>();
        System.out.println("ENTRO AL METODO DAO MOSTRAR");

        try ( Connection connection = conexion.getConexion();  PreparedStatement ps = connection.prepareStatement(SELECT_ALL);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Compras compra = new Compras();
                compra.setIdCompra(rs.getInt("codigo_compra"));
                compra.setNumeroOrden(rs.getString("numero_de_orden"));
                compra.setFechaCompra(rs.getDate("fecha_de_orden"));

                Proveedores proveedor = new Proveedores();
                proveedor.setCodigoProveedor(rs.getInt("codigo_proveedor"));
                proveedor.setNombreProveedor(rs.getString("nombre_proveedor"));
                compra.setProveedores(proveedor); // ✅ nombre correcto

                Empleado empleado = new Empleado();
                empleado.setCodigoEmpleado(rs.getInt("codigo_empleado"));
                empleado.setNombreCompleto(rs.getString("nombre_completo"));
                compra.setEmpleado(empleado);

                comprasList.add(compra);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar compras: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return comprasList;
    }

    public String insert(Compras com) throws SQLException, ClassCastException {
        String resultado;
        int resultado_insertar;
        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(INSERT_COMPRA);
        try {
            //codigo_compra, numero_de_orden ,fecha_de_orden, codigo_empleado,codigo_proveedor)
            ps.setString(1, com.getNumeroOrden());
            ps.setDate(2, new java.sql.Date(com.getFechaCompra().getTime()));
            ps.setInt(3, com.getEmpleado().getCodigoEmpleado());
            ps.setInt(4, com.getProveedores().getCodigoProveedor());

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

    public Compras cargarDatos(Compras compras) throws SQLException {
        Compras listaC = new Compras();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, compras.getIdCompra());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaC.setIdCompra(rs.getInt("codigo_compra"));
                listaC.setNumeroOrden(rs.getString("numero_de_orden"));
                listaC.setFechaCompra(rs.getDate("fecha_de_orden"));
                Empleado em = new Empleado();
                em.setCodigoEmpleado(rs.getInt("codigo_empleado"));
                em.setNombreCompleto(rs.getString("nombre_completo"));
                listaC.setEmpleado(em);

                Proveedores p = new Proveedores();
                p.setCodigoProveedor(rs.getInt("codigo_proveedor"));
                p.setNombreProveedor(rs.getString("nombre_proveedor"));
                listaC.setProveedores(p);
            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaC;
    }

 
    public String update(Compras compras) throws SQLException {
        String resultado;
        int resultado_update;

        try {
            // Obtener conexión
            this.accesoDB = this.conexion.getConexion();

            // Preparar sentencia
            this.ps = this.accesoDB.prepareStatement(UPDATE);

            ps.setString(1, compras.getNumeroOrden());
            ps.setDate(2, compras.getFechaCompra());

            ps.setInt(3, compras.getEmpleado().getCodigoEmpleado()); // ← corregido nombre del método
            ps.setInt(4, compras.getProveedores().getCodigoProveedor());
            ps.setInt(5, compras.getIdCompra());

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
