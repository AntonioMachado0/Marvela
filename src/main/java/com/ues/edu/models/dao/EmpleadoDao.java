/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Roles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mayel
 */
public class EmpleadoDao {

    Conexion conexion;
    private ArrayList<Empleado> lista;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
    public EmpleadoDao() {
        this.conexion = new Conexion();
    }

    private static final String INSERT_EMPLEADO = "INSERT INTO empleado(nombre_completo, fecha_nacimiento, numero_telefono, codigo_roles) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_EMPLEADO = "UPDATE empleado SET nombre_completo=?, fecha_nacimiento=?, numero_telefono=?, codigo_roles=? WHERE codigo_empleado=?";
    private static final String CARGAR = "SELECT * FROM empleado WHERE  codigo_empleado = ?";
    private static final String SELECT_ALL_EMPLEADOS = "SELECT e.codigo_empleado, e.nombre_completo,e.fecha_nacimiento, e.numero_telefono, r.nombre_rol as nombre_rol FROM empleado e INNER JOIN  rol r ON e.codigo_roles = r.codigo_roles";
    private static final String SELECT_EMPLEADO_BY_ID = 
        "SELECT e.codigo_empleado, e.nombre_completo, e.fecha_nacimiento, e.numero_telefono, r.codigo_roles, r.nombre_rol " +
        "FROM empleado e " +
        "INNER JOIN rol r ON e.codigo_roles = r.codigo_roles " +
        "WHERE e.codigo_empleado = ?";
    public ArrayList<Empleado> mostrar(Integer estado, String quien) throws SQLException {
        System.out.println("ENTRO AL METODO DAO MOSTRAR");
        this.lista = new ArrayList<>();

        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLEADOS);
        ResultSet rs = ps.executeQuery();
        Empleado empleado = null;
        while (rs.next()) {
            Roles rol = new Roles();
            empleado = new Empleado();
            empleado.setCodigoEmpleado(rs.getInt("codigo_empleado"));
            empleado.setNombreCompleto(rs.getString("nombre_completo"));
// Si fecha_nacimiento en la DB es DATE
            empleado.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
            empleado.setNumeroTelefono(rs.getString("numero_telefono"));

            rol.setNombreRol(rs.getString("nombre_rol"));
            empleado.setRol(rol);
            this.lista.add(empleado);
        }
        this.conexion.cerrarConexiones();//Cerramos la conexion
        return this.lista;
    }

    public String insertEmp(Empleado emp) throws SQLException, ClassCastException {
        String resultado;
        int resultado_insertar;
        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(INSERT_EMPLEADO);
        try {
            //nombre_completo, fecha_nacimiento, numero_telefono, codigo_roles, estado
            ps.setString(1, emp.getNombreCompleto());
            ps.setDate(2, new java.sql.Date(emp.getFechaNacimiento().getTime()));
            ps.setString(3, emp.getNumeroTelefono());
            ps.setInt(4, emp.getRol().getCodigoRoles());

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

    public String updateEmpleado(Empleado emp) throws SQLException {
        //nombre_completo=?, fecha_nacimiento=?, numero_telefono=?, codigo_roles
        System.out.println(emp.getCodigoEmpleado());
        String resultado;
        int res_actualizar;
        try {
            Connection connection = conexion.getConexion();
            PreparedStatement ps = connection.prepareStatement(UPDATE_EMPLEADO);
            System.out.println("empleado a editar" + emp);
            ps.setString(1, emp.getNombreCompleto());
            ps.setDate(2, emp.getFechaNacimiento());
            ps.setString(3, emp.getNumeroTelefono());
            ps.setInt(4, emp.getRol().getCodigoRoles());
            ps.setInt(5, emp.getCodigoEmpleado());
            res_actualizar = ps.executeUpdate();
            if (res_actualizar > 0) {
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
    
    public Empleado obtenerEmpleadoPorId(int codigoEmpleado) throws SQLException {
        Empleado empleado = null;

        try (Connection connection = conexion.getConexion();
             PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADO_BY_ID)) {

            ps.setInt(1, codigoEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empleado = new Empleado();
                    empleado.setCodigoEmpleado(rs.getInt("codigo_empleado"));
                    empleado.setNombreCompleto(rs.getString("nombre_completo"));
                    empleado.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                    empleado.setNumeroTelefono(rs.getString("numero_telefono"));

                    Roles rol = new Roles();
                    rol.setCodigoRoles(rs.getInt("codigo_roles"));
                    rol.setNombreRol(rs.getString("nombre_rol"));

                    empleado.setRol(rol);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtenerEmpleadoPorId: " + e.getMessage());
            throw e;
        }

        return empleado;
    }
    public ArrayList<Empleado>listarCombo;
    private static final String CARGAR_COMBO_EMPLEADO = "SELECT a.codigo_empleado, a.nombre_completo\n" +
"            FROM empleado a";

    public ArrayList<Empleado> cargarComboEmpleado() throws SQLException {
        listarCombo = new ArrayList();
 System.out.println("ENTRO AL METODO DAO MOSTRAR");
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CARGAR_COMBO_EMPLEADO);
            this.rs = this.ps.executeQuery();
System.out.println("ENTRO AL METODO DAO MOSTRAR 22");
            Empleado c = null;
            while (this.rs.next()) {
                c = new     Empleado();
                c.setCodigoEmpleado(rs.getInt("codigo_empleado"));
                c.setNombreCompleto(rs.getString("nombre_completo"));
                listarCombo.add(c);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return listarCombo;
    }

}
