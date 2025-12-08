package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gustavorosa.models.MeusGanhos;
import dev.gustavorosa.util.ConexaoBD;

public class MeusGanhosDAO {

    public void salvar(MeusGanhos ganhos) {
        String sql = "INSERT INTO meusGanhos (mes, ano, totalRecebido) VALUES (?, ?, ?);";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ganhos.getMes());
            ps.setInt(2, ganhos.getAno());
            ps.setDouble(3, ganhos.getTotalRecebido());

            ps.executeUpdate();
            System.out.println("Ganhos mensais salvos com sucesso!");
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao salvar ganhos: " + ex.getMessage(), ex);
        }
    }

    public List<MeusGanhos> buscarGanhos() {
        List<MeusGanhos> ganhos = new ArrayList<>();
        String sql = "SELECT * FROM meusGanhos ORDER BY ano DESC, mes DESC;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MeusGanhos ganho = mapResultSetToGanhos(rs);
                ganhos.add(ganho);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar ganhos: " + ex.getMessage(), ex);
        }
        return ganhos;
    }

    public MeusGanhos buscarPorId(Long id) {
        String sql = "SELECT * FROM meusGanhos WHERE id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToGanhos(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar ganhos por id: " + ex.getMessage(), ex);
        }
        return null;
    }

    public MeusGanhos buscarPorMesAno(int mes, int ano) {
        String sql = "SELECT * FROM meusGanhos WHERE mes = ? AND ano = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, ano);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToGanhos(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar ganhos por mes e ano: " + ex.getMessage(), ex);
        }
        return null;
    }

    public void calcularGanhosMensais(int mes, int ano) {
        MeusGanhos existente = buscarPorMesAno(mes, ano);
        
        PagamentoDAO pagamentoDAO = new PagamentoDAO();
        List<dev.gustavorosa.models.Pagamento> todosPagamentos = pagamentoDAO.buscarPagamentos();
        
        double totalRecebido = 0.0;
        
        for (dev.gustavorosa.models.Pagamento pagamento : todosPagamentos) {
            if (pagamento.getDataPagamento() != null &&
                pagamento.getDataPagamento().getMonthValue() == mes &&
                pagamento.getDataPagamento().getYear() == ano &&
                (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.PAGO ||
                 pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.PAGO_ATRASADO)) {
                totalRecebido += pagamento.getValor();
            }
        }

        if (existente != null) {
            atualizar(existente.getId(), totalRecebido);
        } else {
            MeusGanhos ganhos = new MeusGanhos(mes, ano, totalRecebido);
            salvar(ganhos);
        }
    }

    public void atualizar(Long id, Double totalRecebido) {
        String sql = "UPDATE meusGanhos SET totalRecebido = ? WHERE id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, totalRecebido);
            ps.setLong(2, id);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Ganhos mensais atualizados com sucesso!");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao atualizar ganhos: " + ex.getMessage(), ex);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM meusGanhos WHERE id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Ganhos removidos com sucesso!");
            } else {
                System.out.println("Nenhum registro encontrado com ID: " + id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao deletar ganhos: " + ex.getMessage(), ex);
        }
    }

    private MeusGanhos mapResultSetToGanhos(ResultSet rs) throws SQLException {
        MeusGanhos ganhos = new MeusGanhos();
        ganhos.setId(rs.getLong("id"));
        ganhos.setMes(rs.getInt("mes"));
        ganhos.setAno(rs.getInt("ano"));
        ganhos.setTotalRecebido(rs.getDouble("totalRecebido"));
        return ganhos;
    }
}

