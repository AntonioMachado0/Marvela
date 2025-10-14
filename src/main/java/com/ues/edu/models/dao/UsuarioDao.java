package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author Erick
 */
public class UsuarioDao {

    Conexion conexion = null; //para acceder al metodo de conexion de bd
   
    private ArrayList<Usuario> usuarioList;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;


    private static final String SI_EXISTE_USUARIO = "	 SELECT u.correo, u.clave, ru.nombre_completo\n" +
"FROM usuario u\n" +
"INNER JOIN empleado ru ON u.codigo_empleado = ru.codigo_empleado\n" +
"WHERE u.correo = ? AND u.clave = ?";

 
    public UsuarioDao() {
        this.conexion = new Conexion(); //Esto es para cuando llamemos el metodo se cree una instancia
    }

    

    public Usuario buscarUsuario(Usuario usuario) throws SQLException {

        Usuario u = null;

        this.accesoDB = this.conexion.getConexion();
        this.ps = this.accesoDB.prepareStatement(SI_EXISTE_USUARIO);
        ps.setString(1, usuario.getCorreo());
        ps.setString(2, usuario.getContraseña());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            u = new Usuario();
            u.setCorreo(rs.getString("correo"));
            u.setContraseña(rs.getString("clave"));

            Empleado ru = new Empleado();
            ru.setNombreCompleto(rs.getString("nombre_empleado"));
            u.setEmpleado(ru);
        }
        conexion.cerrarConexiones();
        return u;
    }

  
}
