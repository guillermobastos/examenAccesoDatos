package sqlite;

import java.sql.Connection;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite {
    Connection conexionSqLite;
    Connection conexionMySql;
    String query;
    ResultSet res;
    int filasAfectadas;
    Statement sta;
    Statement sta2;
    PreparedStatement ps;
    Calendar date;

    private void abrirConexionSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:/C:/Users/nico/OneDrive/FP informática/2º CS DAM/Acceso a Datos/2ª evaluación/SQLITE/tablas";
            conexionSqLite = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void abrirConexion(String bd, String servidor, String usuario, String password) {
        try {
            String url = String.format(
                    "jdbc:mariadb://%s:3306/%s?useServerPrepStmts=true&jdbcCompliantTruncation=false&zeroDateTimeBehavior=convertToNull",
                    servidor, bd);
            conexionMySql = DriverManager.getConnection(url, usuario, password);
            if (conexionMySql != null) {
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

    private void cerrarConexion(Connection conexion) {
        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getLocalizedMessage());
        }
    }

    // Ejercicio 4.
    public void getMinimoPuestos(int minimo, boolean preparada) throws SQLException {
        if (preparada) {
            query = "select * from aulas where puestos >= ?";
            ps = conexionSqLite.prepareStatement(query);
            ps.setInt(1, minimo);
            res = ps.executeQuery();
        } else {
            query = String.format("select * from aulas where puestos >= %d", minimo);
            res = sta.executeQuery(query);
        }
        while (res.next()) {
            System.out.println(res.getString("nombreAula"));
        }
    }

    public void insertarAulas(int numero, String nombreAula, int puestos) throws SQLException {
        query = String.format("insert into aulas values (%d, '%s', %d)", numero, nombreAula, puestos);
        filasAfectadas = sta.executeUpdate(query);

        System.out.println("Se han añadido " + filasAfectadas + " aulas nuevas");
    }

    public void insertarAulasPorCodigo(int numero, String nombreAula, int puestos) throws SQLException {
        query = "select numero from aulas";
        res = sta.executeQuery(query);

        while (res.next()) {
            if (numero == res.getInt("numero")) {
                query = String.format("delete from aulas where numero=%d", numero);
                filasAfectadas = sta.executeUpdate(query);
                System.out.println("Se han tenido que eliminar " + filasAfectadas + " filas");
            }
        }

        query = String.format("insert into aulas values (%d, '%s', %d)", numero, nombreAula, puestos);
        filasAfectadas = sta.executeUpdate(query);
        System.out.println("Se han añadido " + filasAfectadas + " aulas nuevas");
    }

    public void insertarAlumnos(String nombre, String apellidos, int altura, int aula, int peso)
            throws SQLException {
        boolean mola = false;
        int numero = 1;
        int codigo = 1;

        query = "select numero from aulas";
        res = sta2.executeQuery(query);
        while (res.next()) {
            if (aula == res.getInt("numero")) {
                mola = true;
                numero = aula;
            }
        }
        query = "select codigo from alumnos";
        res = sta2.executeQuery(query);
        while (res.next()) {
            if (codigo <= res.getInt("codigo")) {
                codigo = res.getInt("codigo") + 1;
            }
        }
        query = String.format("insert into alumnos values (%d, '%s', '%s', %d, %d, %d)", codigo, nombre, apellidos,
                altura > 0 ? altura : null, mola ? aula : null, peso > 0 ? peso : null);
        filasAfectadas = sta2.executeUpdate(query);

        query = "select numero from aulas";
        res = sta.executeQuery(query);
        while (res.next()) {
            if (aula == res.getInt("numero")) {
                mola = true;
                numero = aula;
            }
        }
        query = "select codigo from alumnos";
        codigo = 1;
        res = sta.executeQuery(query);
        while (res.next()) {
            if (codigo <= res.getInt("codigo")) {
                codigo = res.getInt("codigo") + 1;
            }
        }
        query = String.format("insert into alumnos values (%d, '%s', '%s', %d, %d)", codigo, nombre, apellidos,
                altura > 0 ? altura : null, mola ? numero : null);
        filasAfectadas += sta.executeUpdate(query);

        System.out.println("Se han insertado " + filasAfectadas + " filas entre las dos bases de datos");
    }

    public void buscarNombre(String nombre) throws SQLException {
        query = String.format("select nombreAula from aulas where nombreAula like '%s'", nombre);
        res = sta.executeQuery(query);
        while (res.next()) {
            System.out.println("Se ha encontrado el aula en SQLITE: " + res.getString("nombreAula"));
        }

        res = sta2.executeQuery(query);
        while (res.next()) {
            System.out.println("Se ha encontrado el aula en MYSQL: " + res.getString("nombreAula"));
        }
    }

    public void insertarAulasPorDuplicado(int numero, String aula, int puestos) {
        boolean error = false;
        query = String.format("insert into aulas values(%d, '%s', %d)", numero, aula, puestos);
        try {
            filasAfectadas = sta.executeUpdate(query);
            System.out.println("Filas afectadas en SQLite: " + filasAfectadas);
        } catch (SQLException e) {
            System.out.println("Error al añadir un aula");
            error = true;
        }
        if (!error) {
            try {
                filasAfectadas = sta2.executeUpdate(query);
                System.out.println("Filas afectadas en MYSQL: " + filasAfectadas);
            } catch (SQLException e) {
                System.out.println("Error al añadir un aula");
                error = true;
            }
            if (error) {
                try {
                    query = "delete from aulas where numero = " + numero;
                    filasAfectadas = sta.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertarFechas(String nombre, String fecha) throws SQLException {
        query = String.format("insert into fechas values('%s', '%s')", nombre, fecha);
        filasAfectadas = sta.executeUpdate(query);
        System.out.println("Filas añadidas en SQLite: " + filasAfectadas);
        filasAfectadas = sta2.executeUpdate(query);
        System.out.println("Filas añadidas en MYSQL: " + filasAfectadas);
    }

    public void insertarNombre(String nombre) throws SQLException {
        query = String.format("insert into fechas values ('%s', '%s')", nombre, null);
        filasAfectadas = sta.executeUpdate(query);
        System.out.println("Filas añadidas en SQLite: " + filasAfectadas);
        filasAfectadas = sta2.executeUpdate(query);
        System.out.println("Filas añadidas en MYSQL: " + filasAfectadas);
    }

    public static void main(String[] args) {
        SQLite s = new SQLite();
        s.abrirConexionSQLite();
        s.abrirConexion("add", "localhost", "root", "");
        s.date = Calendar.getInstance();
        

        // ejercicio 3. --> select * from aulas order by puestos desc limit 1, 2;
        try {
            s.sta2 = s.conexionMySql.createStatement();
            s.sta = s.conexionSqLite.createStatement();
            s.getMinimoPuestos(31, true); // Ejercicio 4.
            s.getMinimoPuestos(31, false); // Ejercicio 4.
            s.insertarAulas(1, "Historia", 25); // Ejercicio 5.
            s.insertarAulasPorCodigo(1, "Historia", 14); // Ejercicio 6.
            s.insertarAlumnos("Manuel", "Fernandez", 176, 60, 0); // Ejercicio 7.
            s.buscarNombre("Historia"); // Ejercicio 8.
            s.insertarAulasPorDuplicado(0, "Inteligencia Artificial", 1); // Ejercicio 9.
            s.insertarFechas("Now", String.format("%d-%d-%d %d:%d:%d", s.date.get(Calendar.YEAR), s.date.get(Calendar.MONTH) + 1, s.date.get(Calendar.DAY_OF_MONTH), s.date.get(Calendar.HOUR), s.date.get(Calendar.MINUTE), s.date.get(Calendar.SECOND))); // Ejercicio 10.
            s.insertarNombre("Abada Kedavra"); // Ejercicio 11.
            s.insertarFechas(null, s.date.getTime().toString()); // Ejercicio 12.
            s.insertarFechas(null, null); // Ejercicio 13.
        } catch (SQLException e) {
            e.printStackTrace();
        }

        s.cerrarConexion(s.conexionSqLite);
        s.cerrarConexion(s.conexionMySql);
    }
}