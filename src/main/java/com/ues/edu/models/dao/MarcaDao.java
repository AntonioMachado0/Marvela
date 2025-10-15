
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Marca;
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
public class MarcaDao {
    
  Conexion conexion = null;
  private ArrayList<Marca> listMarca;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
//
    private static final String SELECT_ALL = "SELECT * FROM marca";

    private static final String INSERTAR = "insert into marca(nombre_marca) values (?)";

    private static final String ACTUALIZAR = "UPDATE marca SET nombre_marca = ? WHERE  id_marca= ?";

    private static final String CARGAR = "SELECT * FROM marca WHERE  id_marca = ?";
    private static final String DELETE = "DELETE FROM marca a WHERE a.id_marca = ?";

    public MarcaDao() {
        this.conexion = new Conexion();
    }
    
    
    //Mostrar
public ArrayList<Marca> selectALL(Integer estado, String quien) throws SQLException {
        listMarca = new ArrayList();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_ALL);
            this.rs = this.ps.executeQuery();

            Marca rg = null;
            while (this.rs.next()) {
                rg = new Marca();

                rg.setCodigoMarca(rs.getInt("id_marca"));
                rg.setMarca(rs.getString("nombre_marca"));

                listMarca.add(rg);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return listMarca;
    }
    public boolean existeMarcaConOtroId(String nombreMarca, int idActual) throws SQLException {
    String sql = "SELECT COUNT(*) FROM marca WHERE LOWER(nombre_marca) = LOWER(?) AND id_marca != ?";
    try (Connection conn = conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombreMarca.trim());
        ps.setInt(2, idActual);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}
    public boolean existeMarca(String nombreMarca) throws SQLException {
    String sql = "SELECT COUNT(*) FROM marca WHERE LOWER(nombre_marca) = LOWER(?)";
    try (Connection conn = conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombreMarca.trim());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            System.out.println("Cantidad de coincidencias: " + count);
            return count > 0;
        } else {
            System.out.println("No se obtuvo resultado del ResultSet.");
            return false;
        }

    } catch (SQLException e) {
        System.out.println("Error en existeMarca: " + e.getMessage());
        throw e;
    }
}
    //insertar
    public String insert(Marca marca) throws SQLException {

        String resultado;
        int resultado_insertar;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERTAR);
            ps.setString(1, marca.getMarca());
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
    public Marca cargarDatos(Marca marca) throws SQLException {
        Marca listaMarca = new Marca();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, marca.getCodigoMarca());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaMarca.setCodigoMarca(rs.getInt("id_marca"));
                listaMarca.setMarca(rs.getString("nombre_marca"));

            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaMarca;
    }

    //Actualizar
    public String update(Marca marca) throws SQLException {
        String resultado;
        int resultado_update;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(ACTUALIZAR);
            ps.setString(1, marca.getMarca());
            ps.setInt(2, marca.getCodigoMarca());
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

    public boolean delete(Marca marca) throws SQLException {

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(DELETE);
            ps.setInt(1, marca.getCodigoMarca());

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
    
    ///PARA COMBO MARCA
     private ArrayList<Marca> listProveedor;
     private ArrayList<Marca> listarCombo;
    
     private static final String CARGAR_COMBO_MARCA = "SELECT id_marca, nombre_marca FROM marca;";
    public ArrayList<Marca> cargarComboMarca() throws SQLException {
        listarCombo = new ArrayList();
 System.out.println("ENTRO AL METODO DAO MOSTRAR");
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CARGAR_COMBO_MARCA);
            this.rs = this.ps.executeQuery();
            Marca c = null;
            while (this.rs.next()) {
                c = new Marca();
                c.setCodigoMarca(rs.getInt("id_marca"));
                c.setMarca(rs.getString("nombre_marca"));
                listarCombo.add(c);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return listarCombo;
    }
    }
    
    
