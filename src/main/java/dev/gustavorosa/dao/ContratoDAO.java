package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.models.Pagamento;
import dev.gustavorosa.models.SituacaoPagamento;
import dev.gustavorosa.util.ConexaoBD;

public class ContratoDAO {

  private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

  public void salvar(Contrato contrato) {
    String sql = "INSERT INTO contrato (idCliente, nome, registro, dataInicio, duracaoEmMeses, valorContrato) " +
        "VALUES (?, ?, ?, ?, ?, ?);";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setLong(1, contrato.getIdCliente());
      ps.setString(2, contrato.getNome());
      ps.setString(3, contrato.getRegistro());
      ps.setDate(4, java.sql.Date.valueOf(contrato.getDataInicio()));
      ps.setInt(5, contrato.getDuracaoEmMeses());
      ps.setDouble(6, contrato.getValorContrato());

      ps.executeUpdate();

      ResultSet generatedKeys = ps.getGeneratedKeys();
      Long idContrato = null;
      if (generatedKeys.next()) {
        idContrato = generatedKeys.getLong(1);
        contrato.setId(idContrato);
      }

      System.out.println("Contrato " + contrato.getNome() + " salvo com sucesso!");

      if (idContrato != null) {
        gerarPagamentosDoContrato(contrato);
      }

    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao salvar contrato: " + ex.getMessage(), ex);
    }
  }

  private void gerarPagamentosDoContrato(Contrato contrato) {
    int numParcelas = contrato.getDuracaoEmMeses();
    double valorParcela = contrato.getValorContrato() / numParcelas;
    LocalDate dataInicio = contrato.getDataInicio();

    System.out.println("Gerando " + numParcelas + " parcelas de R$ " + String.format("%.2f", valorParcela) + "...");

    for (int i = 1; i <= numParcelas; i++) {
      LocalDate dataVencimento = dataInicio.plusMonths(i);

      Pagamento pagamento = new Pagamento(
          contrato.getId(),
          valorParcela,
          i,
          null,
          dataVencimento,
          SituacaoPagamento.ABERTO
      );

      pagamentoDAO.salvarSemMensagem(pagamento);
    }

    System.out.println(numParcelas + " pagamentos gerados com sucesso!");
  }

  public List<Contrato> buscarContratos() {
    List<Contrato> todosContratos = new ArrayList<>();
    String sql = "SELECT * FROM contrato;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Contrato contrato = new Contrato();
        contrato.setId(rs.getLong("id"));
        contrato.setIdCliente(rs.getLong("idCliente"));
        contrato.setNome(rs.getString("nome"));
        contrato.setRegistro(rs.getString("registro"));
        Date dateSql = rs.getDate("dataInicio");
        if (dateSql != null) {
          contrato.setDataInicio(dateSql.toLocalDate());
        }
        contrato.setDuracaoEmMeses(rs.getInt("duracaoEmMeses"));
        contrato.setValorContrato(rs.getDouble("valorContrato"));
        todosContratos.add(contrato);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao buscar contratos: " + ex.getMessage(), ex);
    }
    return todosContratos;
  }

  public Contrato buscarPorId(Long id) {
    String sql = "SELECT * FROM contrato WHERE id = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);

      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Contrato contratoEncontrado = new Contrato();
        contratoEncontrado.setId(rs.getLong("id"));
        contratoEncontrado.setIdCliente(rs.getLong("idCliente"));
        contratoEncontrado.setNome(rs.getString("nome"));
        contratoEncontrado.setRegistro(rs.getString("registro"));
        Date dateSql = rs.getDate("dataInicio");
        if (dateSql != null) {
          contratoEncontrado.setDataInicio(dateSql.toLocalDate());
        }
        contratoEncontrado.setDuracaoEmMeses(rs.getInt("duracaoEmMeses"));
        contratoEncontrado.setValorContrato(rs.getDouble("valorContrato"));
        return contratoEncontrado;
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao recuperar contrato por id: " + ex.getMessage(), ex);
    }
    return null;
  }

  public List<Contrato> buscarPorCliente(Long idCliente) {
    List<Contrato> contratos = new ArrayList<>();
    String sql = "SELECT * FROM contrato WHERE idCliente = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, idCliente);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Contrato contrato = new Contrato();
        contrato.setId(rs.getLong("id"));
        contrato.setIdCliente(rs.getLong("idCliente"));
        contrato.setNome(rs.getString("nome"));
        contrato.setRegistro(rs.getString("registro"));
        Date dateSql = rs.getDate("dataInicio");
        if (dateSql != null) {
          contrato.setDataInicio(dateSql.toLocalDate());
        }
        contrato.setDuracaoEmMeses(rs.getInt("duracaoEmMeses"));
        contrato.setValorContrato(rs.getDouble("valorContrato"));
        contratos.add(contrato);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao buscar contratos por cliente: " + ex.getMessage(), ex);
    }
    return contratos;
  }

  public void atualizar(Contrato contrato, Long id) {
    String sql = "UPDATE contrato SET idCliente = ?, nome = ?, registro = ?, dataInicio = ?, " +
        "duracaoEmMeses = ?, valorContrato = ? WHERE id = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setLong(1, contrato.getIdCliente());
      ps.setString(2, contrato.getNome());
      ps.setString(3, contrato.getRegistro());
      ps.setDate(4, java.sql.Date.valueOf(contrato.getDataInicio()));
      ps.setInt(5, contrato.getDuracaoEmMeses());
      ps.setDouble(6, contrato.getValorContrato());
      ps.setLong(7, id);

      int linhasAfetadas = ps.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Contrato atualizado com sucesso!");
      } else {
        System.out.println("Nenhum contrato encontrado com ID: " + id);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao atualizar contrato: " + ex.getMessage(), ex);
    }
  }

  public void deletar(Long id) {
    String sql = "DELETE FROM contrato WHERE id = ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);

      int linhasAfetadas = ps.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Contrato removido com sucesso!");
      } else {
        System.out.println("Nenhum contrato encontrado com ID: " + id);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao deletar contrato: " + ex.getMessage(), ex);
    }
  }
}
