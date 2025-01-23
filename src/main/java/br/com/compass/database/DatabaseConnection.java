package br.com.compass.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Atualizando a URL de conexão para usar o Session Pooler corretamente
    private static final String url = "jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres";
    private static final String user = "postgres.plqjqfwdghxonmhbfveu"; // Usuário correto
    private static final String password = "bancodacompass";  // Substitua pela sua senha real

    private static Connection conn;

    public static Connection getConnection() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(url, user, password);
                return conn;
            } else {
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
