package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gustavorosa.models.RelatorioMensal;
import dev.gustavorosa.util.ConexaoBD;

public class RelatorioMensalDAO {

    public void salvar(RelatorioMensal relatorio) {
        String sql = "INSERT INTO relatorioMensal (idCliente, mes, ano, totalPago, totalPendente, totalPagoComAtraso) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, relatorio.getIdCliente());
            ps.setInt(2, relatorio.getMes());
            ps.setInt(3, relatorio.getAno());
            ps.setDouble(4, relatorio.getTotalPago());
            ps.setDouble(5, relatorio.getTotalPendente());
            ps.setDouble(6, relatorio.getTotalPagoComAtraso());

            ps.executeUpdate();
            System.out.println("Relatorio mensal salvo com sucesso!");
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao salvar relatorio mensal: " + ex.getMessage(), ex);
        }
    }

    public List<RelatorioMensal> buscarRelatorios() {
        List<RelatorioMensal> relatorios = new ArrayList<>();
        String sql = "SELECT * FROM relatorioMensal ORDER BY ano DESC, mes DESC;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RelatorioMensal relatorio = mapResultSetToRelatorio(rs);
                relatorios.add(relatorio);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar relatorios mensais: " + ex.getMessage(), ex);
        }
        return relatorios;
    }

    public RelatorioMensal buscarPorId(Long id) {
        String sql = "SELECT * FROM relatorioMensal WHERE id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToRelatorio(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar relatorio por id: " + ex.getMessage(), ex);
        }
        return null;
    }

    public List<RelatorioMensal> buscarPorCliente(Long idCliente) {
        List<RelatorioMensal> relatorios = new ArrayList<>();
        String sql = "SELECT * FROM relatorioMensal WHERE idCliente = ? ORDER BY ano DESC, mes DESC;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RelatorioMensal relatorio = mapResultSetToRelatorio(rs);
                relatorios.add(relatorio);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar relatorios por cliente: " + ex.getMessage(), ex);
        }
        return relatorios;
    }

    public RelatorioMensal buscarPorClienteMesAno(Long idCliente, int mes, int ano) {
        String sql = "SELECT * FROM relatorioMensal WHERE idCliente = ? AND mes = ? AND ano = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            ps.setInt(2, mes);
            ps.setInt(3, ano);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToRelatorio(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar relatorio por cliente, mes e ano: " + ex.getMessage(), ex);
        }
        return null;
    }

    public void gerarRelatorioMensal(Long idCliente, int mes, int ano) {
        RelatorioMensal existente = buscarPorClienteMesAno(idCliente, mes, ano);
        if (existente != null) {
            System.out.println("Relatorio para este cliente/mes/ano ja existe. Atualizando...");
            atualizar(existente.getId(), idCliente, mes, ano);
            return;
        }

        PagamentoDAO pagamentoDAO = new PagamentoDAO();
        ContratoDAO contratoDAO = new ContratoDAO();
        
        List<dev.gustavorosa.models.Contrato> contratos = contratoDAO.buscarPorCliente(idCliente);
        
        double totalPago = 0.0;
        double totalPendente = 0.0;
        double totalPagoComAtraso = 0.0;

        for (dev.gustavorosa.models.Contrato contrato : contratos) {
            List<dev.gustavorosa.models.Pagamento> pagamentos = pagamentoDAO.buscarPorContrato(contrato.getId());
            
            for (dev.gustavorosa.models.Pagamento pagamento : pagamentos) {
                if (pagamento.getDataVencimento().getMonthValue() == mes && 
                    pagamento.getDataVencimento().getYear() == ano) {
                    
                    if (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.PAGO) {
                        totalPago += pagamento.getValor();
                    } else if (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.ABERTO) {
                        totalPendente += pagamento.getValor();
                    } else if (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.PAGO_ATRASADO) {
                        totalPagoComAtraso += pagamento.getValor();
                    }
                }
            }
        }

        RelatorioMensal relatorio = new RelatorioMensal(idCliente, mes, ano, totalPago, totalPendente, totalPagoComAtraso);
        salvar(relatorio);
    }

    public void atualizar(Long id, Long idCliente, int mes, int ano) {
        PagamentoDAO pagamentoDAO = new PagamentoDAO();
        ContratoDAO contratoDAO = new ContratoDAO();
        
        List<dev.gustavorosa.models.Contrato> contratos = contratoDAO.buscarPorCliente(idCliente);
        
        double totalPago = 0.0;
        double totalPendente = 0.0;
        double totalPagoComAtraso = 0.0;

        for (dev.gustavorosa.models.Contrato contrato : contratos) {
            List<dev.gustavorosa.models.Pagamento> pagamentos = pagamentoDAO.buscarPorContrato(contrato.getId());
            
            for (dev.gustavorosa.models.Pagamento pagamento : pagamentos) {
                if (pagamento.getDataVencimento().getMonthValue() == mes && 
                    pagamento.getDataVencimento().getYear() == ano) {
                    
                    if (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.PAGO) {
                        totalPago += pagamento.getValor();
                    } else if (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.ABERTO) {
                        totalPendente += pagamento.getValor();
                    } else if (pagamento.getSituacao() == dev.gustavorosa.models.SituacaoPagamento.PAGO_ATRASADO) {
                        totalPagoComAtraso += pagamento.getValor();
                    }
                }
            }
        }

        String sql = "UPDATE relatorioMensal SET totalPago = ?, totalPendente = ?, totalPagoComAtraso = ? WHERE id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, totalPago);
            ps.setDouble(2, totalPendente);
            ps.setDouble(3, totalPagoComAtraso);
            ps.setLong(4, id);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Relatorio mensal atualizado com sucesso!");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao atualizar relatorio mensal: " + ex.getMessage(), ex);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM relatorioMensal WHERE id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Relatorio mensal removido com sucesso!");
            } else {
                System.out.println("Nenhum relatorio encontrado com ID: " + id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao deletar relatorio mensal: " + ex.getMessage(), ex);
        }
    }

    private RelatorioMensal mapResultSetToRelatorio(ResultSet rs) throws SQLException {
        RelatorioMensal relatorio = new RelatorioMensal();
        relatorio.setId(rs.getLong("id"));
        relatorio.setIdCliente(rs.getLong("idCliente"));
        relatorio.setMes(rs.getInt("mes"));
        relatorio.setAno(rs.getInt("ano"));
        relatorio.setTotalPago(rs.getDouble("totalPago"));
        relatorio.setTotalPendente(rs.getDouble("totalPendente"));
        relatorio.setTotalPagoComAtraso(rs.getDouble("totalPagoComAtraso"));
        return relatorio;
    }
}

