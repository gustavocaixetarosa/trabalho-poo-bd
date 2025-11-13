package dev.gustavorosa.util;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexaoBD {
    private static Connection conn;

    public static Connection connect() {
        try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          String url = "jdbc:mysql://localhost:3306/cobranca";
          String username = "root";
          String password = "139150";
          conn = java.sql.DriverManager.getConnection(url, username, password);

          return conn;
        } catch (ClassNotFoundException ex) {
          System.out.println("Classe de conexão não encontrada!");
        } catch (SQLException ex) {
          System.out.println("Erro ao conectar ao banco de dados: " + ex.getMessage());
        }
        return null;
    }
}
