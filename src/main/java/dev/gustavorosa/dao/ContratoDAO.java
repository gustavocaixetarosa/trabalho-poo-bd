package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
