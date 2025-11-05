package com.ues.edu.models.dao;

import com.ues.edu.connection.Conexion;
import com.ues.edu.models.Empleado;
import com.ues.edu.models.Roles;
import com.ues.edu.models.Usuario;
import java.io.IOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import jdk.jshell.execution.Util;

/**
 *
 * @author Mary
 */
public class UsuarioDao {

    Conexion conexion = null; //para acceder al metodo de conexion de bd

    private ArrayList<Usuario> usuarioList;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    private static final String SELLECT_ALL = "SELECT  r.nombre_completo, u.codigo_usuario, u.correo,  u.clave, u.estado\n"
            + "         FROM usuario u \n"
            + "            INNER JOIN empleado r ON u.codigo_empleado = r.codigo_empleado";

    private static final String INSERT = "INSERT INTO usuario(correo, clave, estado, codigo_empleado)\n"
            + "	VALUES (?, ?, ?, ?);";

    public UsuarioDao() {
        this.conexion = new Conexion(); //Esto es para cuando llamemos el metodo se cree una instancia
    }

    private static final String SI_EXISTE_USUARIO = "SELECT \n"
            + "    u.correo, \n"
            + "    u.clave, \n"
            + "    e.nombre_completo, \n"
            + "    r.codigo_roles, \n"
            + "    r.nombre_rol\n"
            + "FROM usuario u\n"
            + "INNER JOIN empleado e ON u.codigo_empleado = e.codigo_empleado\n"
            + "LEFT JOIN rol r ON e.codigo_roles = r.codigo_roles\n"
            + "WHERE u.correo = ? AND u.clave = ?";

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

            Empleado empleado = new Empleado();
            empleado.setNombreCompleto(rs.getString("nombre_completo"));

            Roles rol = new Roles();
            rol.setCodigoRoles(rs.getInt("codigo_roles"));
            rol.setNombreRol(rs.getString("nombre_rol"));

            empleado.setRol(rol);
            u.setEmpleado(empleado);
        }

        conexion.cerrarConexiones();
        return u;
    }

    public ArrayList<Usuario> selectAllUsuario(Integer estado, String quien) throws SQLException {
        this.usuarioList = new ArrayList<>();
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELLECT_ALL);
            this.rs = ps.executeQuery();
            Usuario usuario;
            while (this.rs.next()) {
                usuario = new Usuario();
                Empleado rlUsuario = new Empleado();
                rlUsuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setEmpleado(rlUsuario);
                usuario.setCodigoUsuario(rs.getInt("codigo_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContraseña(rs.getString("clave"));
                usuario.setEstado(rs.getBoolean("estado"));

                this.usuarioList.add(usuario);
            }
            conexion.cerrarConexiones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return this.usuarioList;
    }

    public String insert(Usuario usuario) throws SQLException {
        String resultado;
        int resultado_insertar;
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERT);
            ps.setString(1, usuario.getCorreo());
            ps.setString(2, usuario.getContraseña());
            ps.setBoolean(3, usuario.isEstado());
            ps.setInt(4, usuario.getEmpleado().getCodigoEmpleado());
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

    public boolean correoExiste(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try ( Connection conn = conexion.getConexion();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    private static final String CARGAR = "	SELECT \n"
            + "    a.codigo_usuario, \n"
            + "    a.correo, \n"
            + "    a.clave, \n"
            + "    a.estado, \n"
            + "    c.codigo_empleado, \n"
            + "    c.nombre_completo\n"
            + "FROM usuario a\n"
            + "INNER JOIN empleado c ON a.codigo_empleado = c.codigo_empleado\n"
            + "WHERE a.codigo_usuario = ?;";

    public Usuario cargarDatos(Usuario usuario) throws SQLException {
        Usuario listaActividad = new Usuario();
        Empleado c = new Empleado();
        try {
            accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(CARGAR);
            ps.setInt(1, usuario.getCodigoUsuario());
            rs = ps.executeQuery();
            while (rs.next()) {
                listaActividad.setCodigoUsuario(rs.getInt("codigo_usuario"));
                listaActividad.setCorreo(rs.getString("correo"));
                listaActividad.setContraseña(rs.getString("clave"));
                listaActividad.setEstado(rs.getBoolean("estado"));
                c.setCodigoEmpleado(rs.getInt("codigo_empleado"));
                c.setNombreCompleto(rs.getString("nombre_completo"));
                listaActividad.setEmpleado(c);

            }
            this.conexion.cerrarConexiones();
        } catch (SQLException ex) {
            System.out.println("falló insertar" + ex.getErrorCode());
        }
        return listaActividad;
    }

    List<Integer> administradoresPermitidos = Arrays.asList(1, 2, 3); // agrega los IDs reales

    private static final String UPDATE = "UPDATE usuario SET correo=?,  clave=?, estado=?, codigo_empleado=? WHERE codigo_usuario = ?";

    private static final String COUNT = "SELECT COUNT(*) \n"
            + "FROM usuario u\n"
            + "JOIN empleado e ON u.codigo_empleado = e.codigo_empleado\n"
            + "WHERE u.estado = true AND e.codigo_roles = 1;";

    public String update(Usuario usuario) throws SQLException {
        String resultado;
        int resultado_update;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(COUNT);
            this.rs = ps.executeQuery();
            int count = 0;

            if (rs.next()) {
                count = rs.getInt("count");
            }

            this.ps = this.accesoDB.prepareStatement(UPDATE);
            ps.setString(1, usuario.getCorreo());
            ps.setString(2, usuario.getContraseña());

            ps.setBoolean(3, usuario.isEstado());
            ps.setInt(4, usuario.getEmpleado().getCodigoEmpleado());
            ps.setInt(5, usuario.getCodigoUsuario());

            if (count > 1) {
                resultado_update = this.ps.executeUpdate();

                if (resultado_update > 0) {
                    resultado = "exito";
                } else {
                    resultado = "error_update";
                }
            } //         else if (count < 2 && usuario.isEstado() && usuario.getEmpleado().getCodigoEmpleado() == 1) 
            else if (count < 2 && usuario.isEstado() && administradoresPermitidos.contains(usuario.getEmpleado().getCodigoEmpleado())) {
                resultado_update = this.ps.executeUpdate();
                if (resultado_update > 0) {
                    resultado = "exito";
                } else {
                    resultado = "error_update";
                }
            } else {
                resultado = "admin_error";
            }
        } catch (SQLException e) {
            resultado = "error_exepcion";
            System.out.println("falló editar" + e.getErrorCode());
            System.out.println("Error general al actualizar usuario: " + e.getMessage());
        }
        return resultado;
    }

    public static String generarCodigo() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

  public static void guardarCodigoRecuperacion(String correo, String codigo) throws SQLException {
    Conexion conexion = new Conexion();
    Connection con = conexion.getConexion();

    String sql = "UPDATE usuario SET codigo_recuperacion = ? WHERE correo = ?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, codigo);
    ps.setString(2, correo);
    ps.executeUpdate();

    ps.close();
    con.close();
}
public static boolean validarCodigo(String correo, String codigoIngresado) throws SQLException {
    Conexion conexion = new Conexion();
    Connection con = conexion.getConexion();

    String sql = "SELECT codigo_recuperacion FROM usuario WHERE correo = ?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, correo);
    ResultSet rs = ps.executeQuery();

    boolean valido = false;
    if (rs.next()) {
        String codigoGuardado = rs.getString("codigo_recuperacion");
        valido = codigoGuardado != null && codigoGuardado.equals(codigoIngresado);
    }

    rs.close();
    ps.close();
    con.close();
    return valido;
}
public static boolean validarAcceso(String correo, String clave) {
    boolean accesoValido = false;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = Conexion.getConnection();
        String sql = "SELECT * FROM usuario WHERE correo = ? AND clave = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, correo);
        ps.setString(2, clave); // texto plano

        rs = ps.executeQuery();
        accesoValido = rs.next(); // Si hay resultado, el acceso es válido
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return accesoValido;
}

public static boolean actualizarClave(String correo, String nuevaClave) {
    boolean actualizado = false;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        // Verificar si el correo existe
        String verificarSql = "SELECT 1 FROM usuario WHERE correo = ?";
        PreparedStatement verificarPs = con.prepareStatement(verificarSql);
        verificarPs.setString(1, correo);
        ResultSet rs = verificarPs.executeQuery();

        if (!rs.next()) {
            // El correo no existe, no se actualiza
            rs.close();
            verificarPs.close();
            con.close();
            return false;
        }

        rs.close();
        verificarPs.close();

        // 🔐 Cifrar la nueva contraseña antes de guardarla
        String claveHash = com.ues.edu.models.Util.encriptarSHA256(nuevaClave);

        String sql = "UPDATE usuario SET clave = ?, codigo_recuperacion = NULL WHERE correo = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, claveHash);
        ps.setString(2, correo);

        int filas = ps.executeUpdate();
        actualizado = (filas > 0);

        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return actualizado;
}
public static boolean existeCorreo(String correo) {
    boolean existe = false;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        String sql = "SELECT 1 FROM usuario WHERE correo = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, correo);

        ResultSet rs = ps.executeQuery();
        existe = rs.next(); // ✅ Si hay resultado, el correo existe

        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return existe;
}

public static int crearEmpleadoBase(String nombreCompleto, Date fechaNacimiento, String telefono, int codigoRol) {
    int codigoEmpleado = -1;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        String sql = "INSERT INTO empleado (nombre_completo, fecha_nacimiento, numero_telefono, codigo_roles) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, nombreCompleto);
        ps.setDate(2, fechaNacimiento);
        ps.setString(3, telefono);
        ps.setInt(4, codigoRol);

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            codigoEmpleado = rs.getInt(1);
        }

        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return codigoEmpleado;
}

public static int obtenerOCrearRol(String nombreRol) {
    int codigoRol = -1;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        // Verificar si el rol ya existe
        String consulta = "SELECT codigo_roles FROM rol WHERE nombre_rol = ?";
        PreparedStatement psBuscar = con.prepareStatement(consulta);
        psBuscar.setString(1, nombreRol);
        ResultSet rsBuscar = psBuscar.executeQuery();

        if (rsBuscar.next()) {
            codigoRol = rsBuscar.getInt("codigo_roles");
        } else {
            // Insertar nuevo rol
            String insercion = "INSERT INTO rol (nombre_rol) VALUES (?)";
            PreparedStatement psInsertar = con.prepareStatement(insercion, Statement.RETURN_GENERATED_KEYS);
            psInsertar.setString(1, nombreRol);
            psInsertar.executeUpdate();

            ResultSet rsInsertar = psInsertar.getGeneratedKeys();
            if (rsInsertar.next()) {
                codigoRol = rsInsertar.getInt(1);
            }

            rsInsertar.close();
            psInsertar.close();
        }

        rsBuscar.close();
        psBuscar.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return codigoRol;
}

public static boolean registrarUsuario(String correo, String claveHash, int codigoEmpleado) {
    boolean exito = false;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        // Verificar si ya existe un usuario para ese empleado
        String verificarSql = "SELECT 1 FROM usuario WHERE codigo_empleado = ?";
        PreparedStatement verificarPs = con.prepareStatement(verificarSql);
        verificarPs.setInt(1, codigoEmpleado);
        ResultSet rs = verificarPs.executeQuery();

        if (rs.next()) {
            // Ya existe usuario para ese empleado
            rs.close();
            verificarPs.close();
            con.close();
            return false;
        }

        rs.close();
        verificarPs.close();

        // Si no existe, registrar
        String sql = "INSERT INTO usuario (correo, clave, estado, codigo_empleado) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, correo);
        ps.setString(2, claveHash);
        ps.setBoolean(3, true); // Estado activo por defecto
        ps.setInt(4, codigoEmpleado);

        exito = ps.executeUpdate() > 0;

        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return exito;
}
public static String obtenerNombrePorCodigo(int codigoEmpleado) {
    String nombre = "";

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        String sql = "SELECT nombre_completo FROM empleado WHERE codigo_empleado = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, codigoEmpleado);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            nombre = rs.getString("nombre_completo");
        }

        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nombre;
}
public static boolean empleadoYaTieneUsuario(int codigoEmpleado) {
    boolean existe = false;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        String sql = "SELECT 1 FROM usuario WHERE codigo_empleado = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, codigoEmpleado);
        ResultSet rs = ps.executeQuery();

        existe = rs.next(); // Si hay resultado, ya existe usuario

        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return existe;
}
private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensajeError, Object codigoEmpleadoObj) throws ServletException, IOException {
    request.setAttribute("error", mensajeError);
    request.setAttribute("codigo_empleado", codigoEmpleadoObj);

    try {
        int codigoEmpleado = Integer.parseInt(codigoEmpleadoObj.toString());
        String nombreEmpleado = UsuarioDao.obtenerNombrePorCodigo(codigoEmpleado);
        request.setAttribute("nombre_empleado", nombreEmpleado);
    } catch (NumberFormatException e) {
        request.setAttribute("nombre_empleado", "");
    }

    request.getRequestDispatcher("registrar_usuario.jsp").forward(request, response);
}

public static int contarEmpleados() {
    int cantidad = 0;

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.getConexion();

        String sql = "SELECT COUNT(*) FROM empleado";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            cantidad = rs.getInt(1);
        }

        rs.close();
        ps.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return cantidad;
}

public static Usuario buscarPorCorreo(String correo) {
    Usuario usuario = null;

    Conexion conexionBD = new Conexion();
    Connection conn = conexionBD.getConexion();
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, correo);
        rs = ps.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setCodigoUsuario(rs.getInt("codigo_usuario"));
            usuario.setCorreo(rs.getString("correo"));
            usuario.setContraseña(rs.getString("clave")); // clave encriptada
            // Agrega más campos si los necesitas
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException e) {}
        try { if (ps != null) ps.close(); } catch (SQLException e) {}
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }

    return usuario;
}

public static Usuario ingresar(String correo, String claveEncriptada) {
    Usuario usuario = null;

    Conexion conexionBD = new Conexion();
    Connection conn = conexionBD.getConexion();
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        String sql = "SELECT u.codigo_usuario, u.correo, u.clave, " +
                     "e.codigo_empleado, e.nombre_completo, " +
                     "r.codigo_roles, r.nombre_rol " +
                     "FROM usuario u " +
                     "JOIN empleado e ON u.codigo_empleado = e.codigo_empleado " +
                     "JOIN rol r ON e.codigo_roles = r.codigo_roles " +
                     "WHERE u.correo = ? AND u.clave = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, correo);
        ps.setString(2, claveEncriptada);
        rs = ps.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setCodigoUsuario(rs.getInt("codigo_usuario"));
            usuario.setCorreo(rs.getString("correo"));
            usuario.setContraseña(rs.getString("clave"));

            Empleado empleado = new Empleado();
            empleado.setCodigoEmpleado(rs.getInt("codigo_empleado"));
            empleado.setNombreCompleto(rs.getString("nombre_completo"));

            Roles rol = new Roles();
            rol.setCodigoRoles(rs.getInt("codigo_roles"));
            rol.setNombreRol(rs.getString("nombre_rol"));

            empleado.setRol(rol);
            usuario.setEmpleado(empleado);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException e) {}
        try { if (ps != null) ps.close(); } catch (SQLException e) {}
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }

    return usuario;
}

}
