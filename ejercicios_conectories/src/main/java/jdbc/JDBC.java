package jdbc;

import java.sql.*;
import java.util.*;

public class JDBC {
    private Connection conexion;
    Scanner sc = new Scanner(System.in);

    private void abrirConexion(String bd, String servidor, String usuario, String password) {
        try {
            String url = String.format("jdbc:mariadb://%s:3306/%s?useServerPrepStmts=true", servidor, bd);
            this.conexion = DriverManager.getConnection(url, usuario, password);
            if (this.conexion != null) {
                System.out.println("Conectado a " + bd + " en " + servidor);
            } else {
                System.out.println("No conectado a " + bd + " en " + servidor);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getLocalizedMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Código error: " + e.getErrorCode());
        }
    }

    private void cerrarConexion() {
        try {
            this.conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getLocalizedMessage());
        }
    }

    // Ejercicio 1.
    public void buscarAlumno(String cadena) throws SQLException {
        int cont = 0;
        Statement sta = this.conexion.createStatement();
        String query = "select nombre from alumnos where nombre like '%" + cadena + "%'";
        ResultSet rs = sta.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("nombre"));
            cont++;
        }
        System.out.println("Se ha(n) encontrado " + cont + " nombre(s) con esa cadena");
    }

    // Ejercicio 2.
    public void anadirAlumno(int codigo, String nombre, String apellido, int altura, int aula) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format("insert into alumnos values (%d, '%s', '%s', %d, %d)", codigo, nombre, apellido,
                altura, aula);
        int filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se ha(n) añadido " + filasAfectadas + " columna(s)");
    }

    // Ejercicio 2.
    public void anadirAsignatura(int cod, String nombre) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format("insert into asignaturas values (%d, '%s')", cod, nombre);
        int filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se ha(n) añadido " + filasAfectadas + " columna(s)");
    }

    // Ejercicio 3.
    public void eliminarAlumno(int codigo) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format("delete from alumnos where codigo=%d", codigo);
        int filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se ha(n) eliminado " + filasAfectadas + " columna(s)");
    }

    // Ejercicio 3.
    public void eliminarAsignatura(int cod) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format("delete from asignaturas where cod=%d", cod);
        int filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se ha(n) eliminado " + filasAfectadas + " columna(s)");
    }

    // Ejercicio 4.
    public void modificarAlumno(int codigo, String nombre, String apellido, int altura, int aula) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format(
                "update alumnos set nombre='%s', apellidos='%s', altura=%d, aula=%d where codigo=%d", nombre, apellido,
                altura, aula, codigo);
        int filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se ha(n) modificado " + filasAfectadas + " columna(s)");
    }

    // Ejercicio 4.
    public void modificarAsignatura(int codigo, String nombre) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format("update asignaturas set nombre='%s' where cod=%d", nombre, codigo);
        int filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se ha(n) modificado " + filasAfectadas + " columna(s)");
    }

    // Ejercicio 5.
    public void nombreAulasConAlumnos() throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = "select nombreaula from aulas where numero in (select aula from alumnos)";
        ResultSet rs = sta.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("nombreaula"));
        }
    }

    // Ejercicio 5.
    public void nombresAlumnosYAsignaturasAprobados() throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = "select alumnos.nombre, asignaturas.nombre, nota from alumnos, asignaturas, notas where notas.nota >= 5 && alumnos.codigo = notas.alumno && asignaturas.cod = notas.asignatura";
        ResultSet rs = sta.executeQuery(query);
        while (rs.next()) {
            System.out.printf("%10s ha sacado un %2d en %30s\n", rs.getString("alumnos.nombre"),
                    rs.getInt("notas.nota"),
                    rs.getString("asignaturas.nombre"));
        }
    }

    // Ejercicio 5.
    public void nombreAsignaturasSinAlumnos() throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = "select nombre from asignaturas where cod not in (select asignatura from notas)";
        ResultSet rs = sta.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("nombre"));
        }
    }

    // Ejercicio 6.
    public void buscarAlumnoPatron(String cadena, int altura) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = "select * from alumnos where nombre like '%" + cadena + "%' and altura > " + altura;
        ResultSet rs = sta.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("nombre"));
        }
    }

    // Ejercicio 6.
    public void buscarAlumnoPatronPreparada(String cadena, int altura) throws SQLException {
        String query = "select * from alumnos where nombre like ? and altura > ?";
        PreparedStatement ps = this.conexion.prepareStatement(query);
        ps.setString(1, cadena);
        ps.setInt(2, altura);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("nombre"));
        }
    }

    // Ejercicio 8.
    public void anadirColumna(String tabla, String campo, String dato, String propiedad) throws SQLException {
        Statement sta = this.conexion.createStatement();
        String query = String.format("alter table %s add %s %s %s", tabla, campo, dato, propiedad);
        sta.executeUpdate(query);
        System.out.println("Columna añadida correctamente");
    }

    public void getInfo(String bd) {
        DatabaseMetaData dbmt;
        ResultSet catalogos, tablas, procedimientos, primarias, foraneas, columnas;
        try {
            dbmt = this.conexion.getMetaData();

            // a)
            System.out.println("Driver: " + dbmt.getDriverName());
            System.out.println("Version del driver: " + dbmt.getDriverVersion());
            System.out.println("URL: " + dbmt.getURL());
            System.out.println("Usuario: " + dbmt.getUserName());
            System.out.println("Nombre de la base de datos: " + dbmt.getDatabaseProductName());
            System.out.println("Version de la base de datos: " + dbmt.getDatabaseProductVersion());
            System.out.println("Palabras clave: " + dbmt.getSQLKeywords());
            System.out.println();

            // b)
            // catalogos = dbmt.getCatalogs();
            // while (catalogos.next()) {
            // System.out.println(catalogos.getString(1));
            // }
            // System.out.println();

            // c)
            // tablas = dbmt.getTables(bd, null, null, null);
            // while (tablas.next()) {
            // System.out.printf("%15s - %6s\n", tablas.getString("TABLE_NAME"),
            // tablas.getString("TABLE_TYPE"));
            // }
            // System.out.println();

            // d)
            tablas = dbmt.getTables(bd, null, null, null);
            while (tablas.next()) {
                if (tablas.getString("TABLE_TYPE").equals("VIEW")) {
                    System.out.printf("%s - %s\n", tablas.getString("TABLE_NAME"), tablas.getString("TABLE_TYPE"));
                }
            }
            System.out.println();

            // e)
            catalogos = dbmt.getCatalogs();
            while (catalogos.next()) {
                String base = catalogos.getString(1);
                System.out.println(base);
                tablas = dbmt.getTables(base, null, null, null);
                while (tablas.next()) {
                    System.out.printf("\t%s - %s\n", tablas.getString("TABLE_NAME"), tablas.getString("TABLE_TYPE"));
                }
            }
            System.out.println();

            // f)
            procedimientos = dbmt.getProcedures(bd, null, null);
            System.out.println("Procedimientos:");
            while (procedimientos.next()) {
                System.out.println("\t" + procedimientos.getString("PROCEDURE_NAME"));
            }
            System.out.println();

            // g)
            tablas = dbmt.getTables(bd, null, null, null);
            System.out.println("Columnas por patrón:");
            while (tablas.next()) {
                columnas = dbmt.getColumns(tablas.getString(1), null, "a%", null);
            }

            // h)
            primarias = dbmt.getPrimaryKeys(bd, null, null);
            System.out.println("Claves primarias:");
            while (primarias.next()) {
                System.out.println(primarias.getString(1));
            }
            foraneas = dbmt.getExportedKeys(bd, null, null);
            System.out.println("Claves foráneas:");
            while (foraneas.next()) {
                System.out.println(foraneas.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("Error obteniendo datos " + e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        JDBC jdbc = new JDBC();
        jdbc.abrirConexion("add", "localhost", "root", "");
        try {
            // CONSULTAS
            System.out.println("Buscando alumnos:");
            jdbc.buscarAlumno("a");
            System.out.println();

            System.out.println("Añadiendo datos a las bases de datos:");
            jdbc.anadirAlumno(10, "Nicolas", "Fernandez", 173, 20);
            jdbc.anadirAsignatura(9, "Fol");
            System.out.println();

            System.out.println("Modificando datos de las bases de datos:");
            jdbc.modificarAlumno(10, "Niocolias", "Fiernandez", 173, 20);
            jdbc.modificarAsignatura(9, "PHP");
            System.out.println();

            System.out.println("Eliminando datos de las bases de datos:");
            jdbc.eliminarAlumno(10);
            jdbc.eliminarAsignatura(9);
            System.out.println();

            System.out.println("Aulas que tienen al menos un alumno:");
            jdbc.nombreAulasConAlumnos();
            System.out.println();

            System.out.println("Alumnos aprobados:");
            jdbc.nombresAlumnosYAsignaturasAprobados();
            System.out.println();

            System.out.println("Asignaturas que no tienen ningún alumno:");
            jdbc.nombreAsignaturasSinAlumnos();
            System.out.println();

            System.out.println("Alumno con cierto patrón:");
            // jdbc.buscarAlumnoPatron("a", 180);
            jdbc.buscarAlumnoPatron("a", 180);
            System.out.println();

            System.out.println("Añadir una columna:");
            // jdbc.anadirColumna("alumnos", "peso", "int", "");
            System.out.println();

            System.out.println("DatabaseMetaData:");
            jdbc.getInfo("add");
        } catch (SQLException e) {
            System.out.println("Se ha producido un error: " + e.getLocalizedMessage());
        }
        jdbc.cerrarConexion();
    }
}