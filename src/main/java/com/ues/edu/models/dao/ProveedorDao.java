/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
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
public class ProveedorDao {

    Conexion conexion = null;
    private ArrayList<Proveedores> listProveedor;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    private static final String SELECT_ALL = "SELECT * FROM  proveedores";

    private static final String INSERT = "INSERT INTO proveedores (nombre_proveedor, correo, direccion, numero_telefono, numero_telefono1, estado) VALUES (?, ?, ?, ?,?,?)";
    private static final String UPDATE = "UPDATE proveedores SET nombre_proveedor=?, direccion=?, numero_telefono=?, estado=? WHERE codigo_proveedor = ?";

    private static final String CARGAR = "SELECT * FROM proveedores WHERE codigo_proveedor = ?";

    public ProveedorDao() {
        this.conexion = new Conexion();
    }

    //Mostrar
    public ArrayList<Proveedores> selectALL(Integer estado, String quien) throws SQLException {
        listProveedor = new ArrayList();
        System.out.println("ENTRO dao");
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_ALL);
            this.rs = this.ps.executeQuery();

            Proveedores rg = null;
            while (this.rs.next()) {
                rg = new Proveedores();
                System.out.println("E1215");
                rg.setCodigoProveedor(rs.getInt("codigo_proveedor"));
                rg.setNombreProveedor(rs.getString("nombre_proveedor"));
                rg.setCorreo(rs.getString("correo"));
                rg.setDirrecion(rs.getString("direccion"));
                rg.setNumeroTelefono(rs.getString("numero_telefono"));
                rg.setNumeroTelefono1(rs.getString("numero_telefono1"));
                rg.setEstado(rs.getBoolean("estado"));
                listProveedor.add(rg);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return listProveedor;
    }

    public String insert(Proveedores pro) throws SQLException {
        String resultado;
        int resultado_insertar;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERT);
            ps.setString(1, pro.getNombreProveedor());
            ps.setString(2, pro.getCorreo());
            ps.setString(3, pro.getDirrecion());
            ps.setString(4, pro.getNumeroTelefono());
             ps.setString(5, pro.getNumeroTelefono1());
            ps.setBoolean(6, pro.isEstado());

            resultado_insertar = this.ps.executeUpdate();

            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar";
            }
        } catch (SQLException e) {
            resultado = "error_exepcion";
            System.out.println("falló insertar" + e.getErrorCode());
        }
        return resultado;
    }

    public Proveedores cargarDatos(Proveedores proveedores) throws SQLException {
        Proveedores listaP = new Proveedores();

        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, proveedores.getCodigoProveedor());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaP.setCodigoProveedor(rs.getInt("codigo_proveedor"));
                listaP.setNombreProveedor(rs.getString("nombre_proveedor"));
                listaP.setCorreo(rs.getString("correo"));
                listaP.setDirrecion(rs.getString("direccion"));
                listaP.setNumeroTelefono(rs.getString("numero_telefono"));
                listaP.setNumeroTelefono1(rs.getString("numero_telefono1"));
                listaP.setEstado(rs.getBoolean("estado"));
            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaP;
    }

    public String update(Proveedores proveedores) throws SQLException {
        String resultado;
        int resultado_update;

        final String UPDATE = "UPDATE proveedores SET nombre_proveedor = ?, correo= ?,  direccion = ?, numero_telefono = ?, numero_telefono1 = ?,estado = ? WHERE codigo_proveedor = ?";

        try {
            // Obtener conexión
            this.accesoDB = this.conexion.getConexion();

            // Preparar sentencia
            this.ps = this.accesoDB.prepareStatement(UPDATE);
            ps.setString(1, proveedores.getNombreProveedor());
             ps.setString(2, proveedores.getCorreo());
            ps.setString(3, proveedores.getDirrecion()); // ← corregido nombre del método
            ps.setString(4, proveedores.getNumeroTelefono());
             ps.setString(5, proveedores.getNumeroTelefono1());
            ps.setBoolean(6, proveedores.isEstado());
            ps.setInt(7, proveedores.getCodigoProveedor());

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