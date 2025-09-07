/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Categoria;
import com.ues.edu.models.Roles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MINEDUCYT
 */
public class RolDao {

    Conexion conexion;
    private ArrayList<Roles> lista;
    private ResultSet rs = null;

    public RolDao() {
        this.conexion = new Conexion();
    }
    private static final String SELECT_ALL_ROLES = "SELECT * FROM rol";
    private static final String SELECT_ROLES_BY_ID = "SELECT * FROM rol WHERE codigo_roles= CAST(? AS INTEGER)";
    private static final String INSERT_ROLES = "INSERT INTO rol(nombre_rol)VALUES (?);";
    private static final String UPDATE_ROLES = "UPDATE rol SET nombre_rol=? WHERE codigo_roles= CAST(? AS INTEGER)";
    private static final String CARGAR = "SELECT * FROM rol WHERE  codigo_roles = ?";

    public ArrayList<Roles> mostrar(Integer estado, String quien) throws SQLException, ClassCastException {
        System.out.println("ENTRO AL DAO");
        this.lista = new ArrayList<>();//Instancia del arreglo
        try {
            Connection connection = conexion.getConexion();
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_ROLES);
            ResultSet rs = ps.executeQuery();

            Roles rol = null;
            while (rs.next()) {
                rol = new Roles();
                rol.setCodigoRoles(rs.getInt("codigo_roles"));
                rol.setNombreRol(rs.getString("nombre_rol"));

                this.lista.add(rol);

            }
            this.conexion.cerrarConexiones();//Cerramos la conexion
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.lista;
    }
    
    
    public ArrayList<Roles> mostrarCombo() throws SQLException, ClassCastException {
        System.out.println("ENTRO AL DAO");
        this.lista = new ArrayList<>();//Instancia del arreglo
        try {
            Connection connection = conexion.getConexion();
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_ROLES);
            ResultSet rs = ps.executeQuery();

            Roles rol = null;
            while (rs.next()) {
                rol = new Roles();
                rol.setCodigoRoles(rs.getInt("codigo_roles"));
                rol.setNombreRol(rs.getString("nombre_rol"));

                this.lista.add(rol);

            }
            this.conexion.cerrarConexiones();//Cerramos la conexion
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.lista;
    }

    public String insertRol(Roles rol) throws SQLException, ClassCastException {
        String resultado;
        int resultado_insertar;
        Connection connection = conexion.getConexion();
        PreparedStatement ps = connection.prepareStatement(INSERT_ROLES);

        try {
            ps.setString(1, rol.getNombreRol());

            System.out.println("autor_insertar" + rol);
            resultado_insertar = ps.executeUpdate();
            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_rol";
            }

        } catch (SQLException e) {
            resultado = "error_exception";
            System.out.println("fallo insertar" + e.getErrorCode());
            e.printStackTrace();
        }
        return resultado;
    }

    public String updateRol(Roles rol) throws SQLException, ClassNotFoundException {
        System.out.println(rol.getCodigoRoles());
        String resultado;
        int res_actualizar;
        try {
            Connection connection = conexion.getConexion();
            PreparedStatement ps = connection.prepareStatement(UPDATE_ROLES);
            ps.setString(1, rol.getNombreRol());

            ps.setInt(2, rol.getCodigoRoles());

            System.out.println("autor_insertar" + rol);
            res_actualizar = ps.executeUpdate();
            if (res_actualizar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_rol";
            }

        } catch (SQLException e) {
            resultado = "error_exception";
            System.out.println("fallo insertar" + e.getErrorCode());
            e.printStackTrace();
        }
        return resultado;
    }

    //cargar datos 
    public Roles cargarDatos(Roles rol) throws SQLException {
        Roles listaRoles = new Roles();

        try {
            Connection connection = conexion.getConexion();
            PreparedStatement ps = connection.prepareStatement(CARGAR);
            ps.setInt(1, rol.getCodigoRoles());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaRoles.setCodigoRoles(rs.getInt("codigo_roles"));
                listaRoles.setNombreRol(rs.getString("nombre_rol"));

            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaRoles;
    }

    public ResultSet findById(String quien) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        try {
            Connection connection = conexion.getConexion();
            PreparedStatement ps = connection.prepareStatement(SELECT_ROLES_BY_ID);
            System.out.println("sql" + quien + SELECT_ROLES_BY_ID);
            ps.setString(1, quien);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public List<Roles> mostrarRolesDisponibles() throws SQLException {
        List<Roles> roles = new ArrayList<>();
        String sql = "SELECT *\n"
                + "FROM rol r\n"
                + "WHERE r.nombre_rol != 'Administrador'\n"
                + "   OR (r.nombre_rol = 'Administrador' \n"
                + "       AND NOT EXISTS (\n"
                + "           SELECT 1 \n"
                + "           FROM empleado e\n"
                + "           WHERE e.codigo_roles = r.codigo_roles\n"
                + "       )\n"
                + "   );";

        try ( Connection connection = conexion.getConexion();  PreparedStatement ps = connection.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Roles rol = new Roles();
                rol.setCodigoRoles(rs.getInt("codigo_roles"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                roles.add(rol);
            }
        }
        return roles;
    }

    
    public boolean existeNombreRol(String nombreRol) throws SQLException {
    String sql = "SELECT COUNT(*) FROM rol WHERE LOWER(nombre_rol) = LOWER(?)";
    
    try (Connection connection = conexion.getConexion();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        
        ps.setString(1, nombreRol);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
    
    public boolean existeNombreRolExcluyendoActual(String nombreRol, int codigoRolActual) throws SQLException {
    String sql = "SELECT COUNT(*) FROM rol WHERE LOWER(nombre_rol) = LOWER(?) AND codigo_roles != ?";
    
    try (Connection connection = conexion.getConexion();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        
        ps.setString(1, nombreRol);
        ps.setInt(2, codigoRolActual);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
}
