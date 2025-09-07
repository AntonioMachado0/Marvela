/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mayel
 */
public class Conexion {

    //AGREGUE ESTO
    private Connection conexion = null;
    private static final ResultSet rs = null;
    private static Statement sentencia = null;
    private static final PreparedStatement ps = null;

    private static final String jdbcURL = "jdbc:postgresql://localhost:5432/FerreteriaBD?useSSL=false";
    private static final String jdbcUsername = "postgres";
    private static final String jdbcPassword = "dd22005";

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            // Obtener la conexion
            con = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException ex) {
        } catch (Exception e) {
        }
        return con;
    }

    public void cerrarConexiones() {
        if (sentencia != null) {
            try {
                sentencia.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar el Statement" + e);
            }
        }
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion a la bd" + e);
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion a la bd" + e);
            }
        }
    }
}