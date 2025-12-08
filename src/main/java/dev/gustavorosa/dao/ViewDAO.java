package dev.gustavorosa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.gustavorosa.util.ConexaoBD;

public class ViewDAO {

    public List<Map<String, Object>> buscarResumoClientes() {
        List<Map<String, Object>> resumos = new ArrayList<>();
        String sql = "SELECT * FROM vw_resumo_clientes ORDER BY cliente_nome;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> resumo = new HashMap<>();
                resumo.put("cliente_id", rs.getLong("cliente_id"));
                resumo.put("cliente_nome", rs.getString("cliente_nome"));
                resumo.put("cliente_registro", rs.getString("cliente_registro"));
                resumo.put("total_contratos", rs.getInt("total_contratos"));
                resumo.put("valor_total_contratos", rs.getDouble("valor_total_contratos"));
                resumo.put("total_pagamentos", rs.getInt("total_pagamentos"));
                resumo.put("pagamentos_pagos", rs.getInt("pagamentos_pagos"));
                resumo.put("pagamentos_pendentes", rs.getInt("pagamentos_pendentes"));
                resumo.put("pagamentos_atrasados", rs.getInt("pagamentos_atrasados"));
                resumo.put("total_recebido", rs.getDouble("total_recebido"));
                resumo.put("total_pendente", rs.getDouble("total_pendente"));
                resumos.add(resumo);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar resumo de clientes: " + ex.getMessage(), ex);
        }
        return resumos;
    }

    public Map<String, Object> buscarResumoCliente(Long idCliente) {
        String sql = "SELECT * FROM vw_resumo_clientes WHERE cliente_id = ?;";

        try (
                Connection conn = ConexaoBD.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idCliente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> resumo = new HashMap<>();
                resumo.put("cliente_id", rs.getLong("cliente_id"));
                resumo.put("cliente_nome", rs.getString("cliente_nome"));
                resumo.put("cliente_registro", rs.getString("cliente_registro"));
                resumo.put("total_contratos", rs.getInt("total_contratos"));
                resumo.put("valor_total_contratos", rs.getDouble("valor_total_contratos"));
                resumo.put("total_pagamentos", rs.getInt("total_pagamentos"));
                resumo.put("pagamentos_pagos", rs.getInt("pagamentos_pagos"));
                resumo.put("pagamentos_pendentes", rs.getInt("pagamentos_pendentes"));
                resumo.put("pagamentos_atrasados", rs.getInt("pagamentos_atrasados"));
                resumo.put("total_recebido", rs.getDouble("total_recebido"));
                resumo.put("total_pendente", rs.getDouble("total_pendente"));
                return resumo;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Falha ao buscar resumo do cliente: " + ex.getMessage(), ex);
        }
        return null;
    }
}

