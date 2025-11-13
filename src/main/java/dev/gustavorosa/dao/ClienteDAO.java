package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.util.ConexaoBD;

public class ClienteDAO {

  public void salvar(Cliente cliente) {
    String sql = "INSERT INTO cliente (nome, registro, endereco) VALUES (?, ?, ?);";

    try (
        Connection connection = ConexaoBD.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, cliente.getNome());
      statement.setString(2, cliente.getRegistro());
      statement.setString(3, cliente.getEndereco());

      statement.executeUpdate();
      System.out.println("Cliente " + cliente.getNome() + " salvo com sucesso!");
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao salvar cliente: " + ex.getMessage(), ex);
    }
  }

  public List<Cliente> buscarClientes() {
    List<Cliente> todosClientes = new ArrayList<>();
    String sql = "SELECT * FROM cliente;";

    try (
        Connection connection = ConexaoBD.connect();
        PreparedStatement statement = connection.prepareStatement(sql);) {

      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setRegistro(rs.getString("registro"));
        todosClientes.add(cliente);
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao conectar com banco de dados");
    }
    return todosClientes;
  }

  public Cliente buscarClientePorId(Long id) {
    String sql = "SELECT * FROM cliente WHERE id = ?;";

    Cliente clienteBuscado = new Cliente();
    try (
        Connection connection = ConexaoBD.connect();
        PreparedStatement statement = connection.prepareStatement(sql);) {

      statement.setLong(1, id);

      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        clienteBuscado = new Cliente();
        clienteBuscado.setId(rs.getLong("id"));
        clienteBuscado.setNome(rs.getString("nome"));
        clienteBuscado.setEndereco(rs.getString("endereco"));
        clienteBuscado.setRegistro(rs.getString("registro"));
      }
    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao conectar com banco de dados: " + ex.getMessage());
    }
    return clienteBuscado;
  }

  public void atualizar(Cliente cliente, Long id) {
    String sql = "UPDATE cliente SET nome = ?, registro = ?, endereco = ? WHERE id = ?;";

    try (
        Connection connection = ConexaoBD.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, cliente.getNome());
      statement.setString(2, cliente.getRegistro());
      statement.setString(3, cliente.getEndereco());
      statement.setLong(4, id);

      int linhasAfetadas = statement.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Cliente atualizado com sucesso!");
      } else {
        System.out.println("Nenhum cliente encontrado com ID: " + cliente.getId());
      }

    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao atualizar cliente: " + ex.getMessage(), ex);
    }
  }

  public void deletar(Long id) {
    String sql = "DELETE FROM cliente WHERE id = ?;";

    try (
        Connection connection = ConexaoBD.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);

      int linhasAfetadas = statement.executeUpdate();
      if (linhasAfetadas > 0) {
        System.out.println("Cliente removido com sucesso!");
      } else {
        System.out.println("Nenhum cliente encontrado com ID: " + id);
      }

    } catch (SQLException ex) {
      throw new RuntimeException("Falha ao deletar cliente: " + ex.getMessage(), ex);
    }
  }
}
