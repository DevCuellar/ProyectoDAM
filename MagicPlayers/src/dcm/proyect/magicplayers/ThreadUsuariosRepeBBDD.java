package dcm.proyect.magicplayers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Hilo que comprueba que el nombre de usuario no existe en la bbdd ya.
public class ThreadUsuariosRepeBBDD extends Thread {
	String nombre = "";
	String passwd = "";
	String email = "";
	String pais = "";
	String provincia = "";
	String cp = "";
	String dci = "";
	boolean bandera = false;
	boolean banderaEmail = false;

	public ThreadUsuariosRepeBBDD(String nombre, String passwd, String email,
			String pais, String provincia, String cp, String dci) {
		this.nombre = nombre;
		this.passwd = passwd;
		this.email = email;
		this.pais = pais;
		this.provincia = provincia;
		this.cp = cp;
		this.dci = dci;
	}

	@Override
	public void run() {
		Connection conn = null;
		try {
			// Conexion con la base de datos.
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(
					"jdbc:mysql://db4free.net:3306/magicplayers", "dcuellar",
					"QAZwsx123");
		} catch (SQLException se) {

		} catch (ClassNotFoundException e) {

		} catch (Exception e) {

		}

		try {
			Statement stat = conn.createStatement();
			// Comprueba si el nombre de usuario ya existe.
			ResultSet rs = stat
					.executeQuery("SELECT nombreU from Usuario where nombreU='"
							+ nombre + "';");
			while (rs.next()) {
				bandera = true;
			}
			// Comprueba si el correo ya existe.
			rs = stat.executeQuery("SELECT email from Usuario where email='"
					+ email + "';");
			while (rs.next()) {
				banderaEmail = true;
			}
			// Si no existen ni el nombre ni el correo, registra al usuario en
			// la base de datos.
			if (!bandera && !banderaEmail) {
				// Encriptamos la contraseņa para aumentar la seguridad.
				passwd = EncriptarPasswd.encriptar(passwd);
				stat.executeUpdate("INSERT INTO Usuario VALUES ('"+nombre+"','"+passwd+"', NULL,'"+dci+"','"+pais+"','"+provincia+"','"+cp+"', NULL, NULL, 1, NULL,'"+email+"')");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {

		}

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean isBandera() {
		return bandera;
	}

	public void setBandera(boolean bandera) {
		this.bandera = bandera;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isBanderaEmail() {
		return banderaEmail;
	}

	public void setBanderaEmail(boolean banderaEmail) {
		this.banderaEmail = banderaEmail;
	}

}
