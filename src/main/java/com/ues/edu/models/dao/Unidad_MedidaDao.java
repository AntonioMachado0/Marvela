/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Marca;
import com.ues.edu.models.Medida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author thebe
 */
public class Unidad_MedidaDao {
    
  Conexion conexion = null;
  private ArrayList<Medida> listMedida;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
//
    private static final String SELECT_ALL = "SELECT * FROM unidad_medida";

    private static final String INSERTAR = "insert into unidad_medida(nombre_medida,abreviacion) values (?,?)";

    private static final String ACTUALIZAR = "UPDATE unidad_medida SET nombre_medida = ?, abreviacion = ? WHERE id_medida = ?";

    private static final String CARGAR = "SELECT * FROM unidad_medida WHERE  id_medida = ?";
    private static final String DELETE = "DELETE FROM unidad_medida a WHERE a.id_medida = ?";

    public Unidad_MedidaDao() {
        this.conexion = new Conexion();
    }
    
    
    //Mostrar
public ArrayList<Medida> selectALL(Integer estado, String quien) throws SQLException {
        listMedida = new ArrayList();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_ALL);
            this.rs = this.ps.executeQuery();

            Medida rg = null;
            while (this.rs.next()) {
                rg = new Medida();

                rg.setId_medida(rs.getInt("id_medida"));
                rg.setMedida(rs.getString("nombre_medida"));
rg.setAbreviacion(rs.getString("abreviacion"));
                listMedida.add(rg);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return listMedida;
    }
    
    //insertar
    public String insert(Medida medida) throws SQLException {

        String resultado;
        int resultado_insertar;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERTAR);
            ps.setString(1, medida.getMedida());
            ps.setString(2, medida.getAbreviacion());
            resultado_insertar = this.ps.executeUpdate();

            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_medida";
            }
        } catch (SQLException e) {
            resultado = "error_exepcion";
            System.out.println("falló insertar" + e.getErrorCode());
        }
        return resultado;
    }
    
    public boolean existeMedida(String nombre, String abreviacion) throws SQLException {
    String sql = "SELECT COUNT(*) FROM unidad_medida WHERE LOWER(nombre_medida) = LOWER(?) OR LOWER(abreviacion) = LOWER(?)";
    try (Connection conn = conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombre.trim());
        ps.setString(2, abreviacion.trim());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}
    public boolean existeMedidaConOtroId(String nombre, String abreviacion, int idActual) throws SQLException {
    String sql = "SELECT COUNT(*) FROM unidad_medida WHERE (LOWER(nombre_medida) = LOWER(?) OR LOWER(abreviacion) = LOWER(?)) AND id_medida != ?";
    try (Connection conn = conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombre.trim());
        ps.setString(2, abreviacion.trim());
        ps.setInt(3, idActual);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}

    //cargar datos 
    public Medida cargarDatos(Medida medida) throws SQLException {
        Medida listaMedida = new Medida();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, medida.getId_medida());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaMedida.setId_medida(rs.getInt("id_medida"));
                listaMedida.setMedida(rs.getString("nombre_medida"));
listaMedida.setAbreviacion(rs.getString("abreviacion"));
            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaMedida;
    }

    //Actualizar
    public String update(Medida medida) throws SQLException {
    String resultado;
    int resultado_update;

    try {
        this.accesoDB = this.conexion.getConexion();
        this.ps = this.accesoDB.prepareStatement(ACTUALIZAR);
        ps.setString(1, medida.getMedida());
        ps.setString(2, medida.getAbreviacion());
        ps.setInt(3, medida.getId_medida());
        System.out.println("UPDATE unidad_medida SET medida = " + medida.getMedida() +
                   ", abreviacion = " + medida.getAbreviacion() +
                   " WHERE id_medida = " + medida.getId_medida());
        resultado_update = ps.executeUpdate();

        if (resultado_update > 0) {
            resultado = "exito";
        } else {
            resultado = "error_update";
        }
    } catch (SQLException e) {
        resultado = "error_exepcion";
        System.out.println("falló editar" + e.getErrorCode());
    }
    return resultado;
}

    public boolean delete(Medida medida) throws SQLException {

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(DELETE);
            ps.setInt(1, medida.getId_medida());

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return false;
    }
    
     
//    public boolean existeNombreCategoria(String nombre) throws SQLException {
//    String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre) = LOWER(?)";
//    
//    try (Connection connection = conexion.getConexion();
//         PreparedStatement ps = connection.prepareStatement(sql)) {
//        
//        ps.setString(1, nombre);
//        try (ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        }
//    }
//    return false;
//}
    
//    public boolean existeNombreEx(String nombre, int codigo_categoria) throws SQLException {
//    String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre) = LOWER(?) AND codigo_categoria != ?";
//    
//    try (Connection connection = conexion.getConexion();
//         PreparedStatement ps = connection.prepareStatement(sql)) {
//        
//        ps.setString(1, nombre);
//        ps.setInt(2, codigo_categoria);
//        
//        try (ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        }
//    }
//    return false;
//}
}