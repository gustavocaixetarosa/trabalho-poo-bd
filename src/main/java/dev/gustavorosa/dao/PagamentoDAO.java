package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gustavorosa.models.Pagamento;
import dev.gustavorosa.models.SituacaoPagamento;
import dev.gustavorosa.util.ConexaoBD;

public class PagamentoDAO {

  public void salvar(Pagamento pagamento) {
    salvarSemMensagem(pagamento);
    System.out.println("Pagamento salvo com sucesso!");
  }

  public void salvarSemMensagem(Pagamento pagamento) {
    String sql = "INSERT INTO pagamento (idContrato, valor, numParcela, dataVencimento, dataPagamento, situacao) " +
        "VALUES (?, ?, ?, ?, ?, ?);";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setLong(1, pagamento.getIdContrato());
      ps.setDouble(2, pagamento.getValor());
      ps.setInt(3, pagamento.getNumParcela());
      ps.setDate(4, java.sql.Date.valueOf(pagamento.getDataVencimento()));
      if (pagamento.getDataPagamento() != null) {
        ps.setDate(5, java.sql.Date.valueOf(pagamento.getDataPagamento()));
      } else {
        ps.setNull(5, java.sql.Types.DATE);
      }
      ps.setString(6, convertSituacaoToDb(pagamento.getSituacao()));

      ps.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao salvar pagamento: " + ex.getMessage(), ex);
    }
  }

  public List<Pagamento> buscarPagamentos() {
    List<Pagamento> todosPagamentos = new ArrayList<>();
    String sql = "SELECT * FROM pagamento;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Pagamento pagamento = mapResultSetToPagamento(rs);
        todosPagamentos.add(pagamento);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao buscar pagamentos: " + ex.getMessage(), ex);
    }
    return todosPagamentos;
  }

  public Pagamento buscarPorId(Long id) {
    String sql = "SELECT * FROM pagamento WHERE id = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);

      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return mapResultSetToPagamento(rs);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao buscar pagamento por id: " + ex.getMessage(), ex);
    }
    return null;
  }

  public List<Pagamento> buscarPorContrato(Long idContrato) {
    List<Pagamento> pagamentos = new ArrayList<>();
    String sql = "SELECT * FROM pagamento WHERE idContrato = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, idContrato);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Pagamento pagamento = mapResultSetToPagamento(rs);
        pagamentos.add(pagamento);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao buscar pagamentos por contrato: " + ex.getMessage(), ex);
    }
    return pagamentos;
  }

  public List<Pagamento> buscarPorSituacao(SituacaoPagamento situacao) {
    List<Pagamento> pagamentos = new ArrayList<>();
    String sql = "SELECT * FROM pagamento WHERE situacao = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, convertSituacaoToDb(situacao));

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Pagamento pagamento = mapResultSetToPagamento(rs);
        pagamentos.add(pagamento);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao buscar pagamentos por situacao: " + ex.getMessage(), ex);
    }
    return pagamentos;
  }

  public void atualizar(Pagamento pagamento, Long id) {
    String sql = "UPDATE pagamento SET idContrato = ?, valor = ?, numParcela = ?, " +
        "dataVencimento = ?, dataPagamento = ?, situacao = ? WHERE id = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setLong(1, pagamento.getIdContrato());
      ps.setDouble(2, pagamento.getValor());
      ps.setInt(3, pagamento.getNumParcela());
      ps.setDate(4, java.sql.Date.valueOf(pagamento.getDataVencimento()));
      if (pagamento.getDataPagamento() != null) {
        ps.setDate(5, java.sql.Date.valueOf(pagamento.getDataPagamento()));
      } else {
        ps.setNull(5, java.sql.Types.DATE);
      }
      ps.setString(6, convertSituacaoToDb(pagamento.getSituacao()));
      ps.setLong(7, id);

      int linhasAfetadas = ps.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Pagamento atualizado com sucesso!");
      } else {
        System.out.println("Nenhum pagamento encontrado com ID: " + id);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao atualizar pagamento: " + ex.getMessage(), ex);
    }
  }

  public void registrarPagamento(Long id) {
    Pagamento pagamento = buscarPorId(id);
    if (pagamento == null) {
      System.out.println("Pagamento não encontrado!");
      return;
    }

    java.time.LocalDate hoje = java.time.LocalDate.now();
    pagamento.setDataPagamento(hoje);

    if (hoje.isAfter(pagamento.getDataVencimento())) {
      pagamento.setSituacao(SituacaoPagamento.PAGO_ATRASADO);
    } else {
      pagamento.setSituacao(SituacaoPagamento.PAGO);
    }

    atualizar(pagamento, id);
    System.out.println("Pagamento registrado com sucesso!");
  }

  public void deletar(Long id) {
    String sql = "DELETE FROM pagamento WHERE id = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);

      int linhasAfetadas = ps.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Pagamento removido com sucesso!");
      } else {
        System.out.println("Nenhum pagamento encontrado com ID: " + id);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao deletar pagamento: " + ex.getMessage(), ex);
    }
  }

  private Pagamento mapResultSetToPagamento(ResultSet rs) throws SQLException {
    Pagamento pagamento = new Pagamento();
    pagamento.setId(rs.getLong("id"));
    pagamento.setIdContrato(rs.getLong("idContrato"));
    pagamento.setValor(rs.getDouble("valor"));
    pagamento.setNumParcela(rs.getInt("numParcela"));

    Date dataVencimento = rs.getDate("dataVencimento");
    if (dataVencimento != null) {
      pagamento.setDataVencimento(dataVencimento.toLocalDate());
    }

    Date dataPagamento = rs.getDate("dataPagamento");
    if (dataPagamento != null) {
      pagamento.setDataPagamento(dataPagamento.toLocalDate());
    }

    String situacaoStr = rs.getString("situacao");
    pagamento.setSituacao(convertDbToSituacao(situacaoStr));

    return pagamento;
  }

  private String convertSituacaoToDb(SituacaoPagamento situacao) {
    switch (situacao) {
      case ABERTO:
        return "PENDENTE";
      case PAGO:
        return "PAGO";
      case PAGO_ATRASADO:
        return "ATRASADO";
      default:
        return "PENDENTE";
    }
  }

  private SituacaoPagamento convertDbToSituacao(String dbValue) {
    switch (dbValue) {
      case "PENDENTE":
        return SituacaoPagamento.ABERTO;
      case "PAGO":
        return SituacaoPagamento.PAGO;
      case "ATRASADO":
        return SituacaoPagamento.PAGO_ATRASADO;
      default:
        return SituacaoPagamento.ABERTO;
    }
  }
}

