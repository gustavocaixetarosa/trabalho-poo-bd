package dev.gustavorosa.util;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexaoBD {
    private static Connection conn = null;
    private static final String URL = "jdbc:mysql://localhost:3306/cobranca";
    private static final String USERNAME = "cobranca_user";
    private static final String PASSWORD = "139150";

    public static void inicializar() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                conn = java.sql.DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Conexão estabelecida com sucesso!");
            } catch (SQLException ex) {
                System.err.println("Erro ao conectar ao banco de dados: " + ex.getMessage());
                System.err.println("Verifique se:");
                System.err.println("1. O MySQL está rodando");
                System.err.println("2. O banco 'cobranca' existe");
                System.err.println("3. O usuário 'cobranca_user' foi criado corretamente");
                throw ex;
            }
        }
    }

    public static Connection connect() throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                conn = java.sql.DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            return conn;
        } catch (SQLException ex) {
            System.err.println("Erro ao obter conexão: " + ex.getMessage());
            throw ex;
        }
    }

    public static void fechar() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("Conexão com o banco de dados fechada.");
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar conexão: " + ex.getMessage());
            } finally {
                conn = null;
            }
        }
    }

    public static boolean isConectado() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
