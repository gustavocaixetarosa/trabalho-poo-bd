package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.util.ConexaoBD;

public class ContratoDAO {

  public void salvar(Contrato contrato) {
    String sql = "INSERT INTO contrato (idCliente, nome, registro, dataInicio, duracaoEmMeses, valorContrato)" +
        "VALUES ?, ?, ?, ?, ?, ?;";

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql);) {

      ps.setLong(1, contrato.getIdCliente());
      ps.setString(2, contrato.getNome());
      ps.setString(3, contrato.getRegistro());
      ps.setDate(4, java.sql.Date.valueOf(contrato.getDataInicio()));
      ps.setInt(5, contrato.getDuracaoEmMeses());
      ps.setDouble(6, contrato.getValorContrato());
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao salvar contrato: " + ex.getMessage());
    }
  }

  public Contrato buscarPorId(Long id) {
    String sql = "SELECT * FROM contrato WHERE id = ?;";
    Contrato contratoEncontrado = new Contrato();

    try (
        Connection conn = ConexaoBD.connect();
        PreparedStatement ps = conn.prepareStatement(sql);) {
      ps.setLong(1, id);

      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Date dateSql = rs.getDate("dataInicio");
        contratoEncontrado.setDataInicio(dateSql.toLocalDate());
        contratoEncontrado.setDuracaoEmMeses(rs.getInt("duracaoEmMeses"));
        contratoEncontrado.setId(rs.getLong("id"));
        contratoEncontrado.setIdCliente(rs.getLong("idCliente"));
        contratoEncontrado.setRegistro(rs.getString(arg0));
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao recuperar contrato por id: " + ex.getMessage());
    }

  }
}
