/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Categoria;
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
public class CategoriaDao {

    Conexion conexion = null;
  private ArrayList<Categoria> listRolGrupo;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
//
    private static final String SELECT_ALL = "SELECT * FROM categoria";

    private static final String INSERTAR = "INSERT INTO categoria( nombre ) VALUES(?)";

    private static final String ACTUALIZAR = "UPDATE categoria SET nombre = ? WHERE  codigo_categoria= ?";

    private static final String CARGAR = "SELECT * FROM categoria WHERE  codigo_categoria = ?";
    private static final String DELETE = "DELETE FROM categoria a WHERE a.codigo_categoria = ?";

    public CategoriaDao() {
        this.conexion = new Conexion();
    }
    
    
    //Mostrar
public ArrayList<Categoria> selectALL(Integer estado, String quien) throws SQLException {
        listRolGrupo = new ArrayList();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_ALL);
            this.rs = this.ps.executeQuery();

            Categoria rg = null;
            while (this.rs.next()) {
                rg = new Categoria();

                rg.setCodigoCategoria(rs.getInt("codigo_categoria"));
                rg.setNombre(rs.getString("nombre"));

                listRolGrupo.add(rg);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return listRolGrupo;
    }
    
    //insertar
    public String insert(Categoria categoria) throws SQLException {

        String resultado;
        int resultado_insertar;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERTAR);
            ps.setString(1, categoria.getNombre());
            resultado_insertar = this.ps.executeUpdate();

            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_categoria";
            }
        } catch (SQLException e) {
            resultado = "error_exepcion";
            System.out.println("falló insertar" + e.getErrorCode());
        }
        return resultado;
    }

    //cargar datos 
    public Categoria cargarDatos(Categoria categoria) throws SQLException {
        Categoria listaCategoria = new Categoria();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, categoria.getCodigoCategoria());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaCategoria.setCodigoCategoria(rs.getInt("codigo_categoria"));
                listaCategoria.setNombre(rs.getString("nombre"));

            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaCategoria;
    }

    //Actualizar
    public String update(Categoria categoria) throws SQLException {
        String resultado;
        int resultado_update;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(ACTUALIZAR);
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getCodigoCategoria());
            resultado_update = this.ps.executeUpdate();

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

    public boolean delete(Categoria categoria) throws SQLException {

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(DELETE);
            ps.setInt(1, categoria.getCodigoCategoria());

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return false;
    }
    
     
    public boolean existeNombreCategoria(String nombre) throws SQLException {
    String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre) = LOWER(?)";
    
    try (Connection connection = conexion.getConexion();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        
        ps.setString(1, nombre);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
    
    public boolean existeNombreEx(String nombre, int codigo_categoria) throws SQLException {
    String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre) = LOWER(?) AND codigo_categoria != ?";
    
    try (Connection connection = conexion.getConexion();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        
        ps.setString(1, nombre);
        ps.setInt(2, codigo_categoria);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
}