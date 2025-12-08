package dev.gustavorosa.controller;

import dev.gustavorosa.dao.ClienteDAO;
import dev.gustavorosa.dao.ContratoDAO;
import dev.gustavorosa.dao.MeusGanhosDAO;
import dev.gustavorosa.dao.PagamentoDAO;
import dev.gustavorosa.dao.RelatorioMensalDAO;
import dev.gustavorosa.dao.ViewDAO;
import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.models.MeusGanhos;
import dev.gustavorosa.models.Pagamento;
import dev.gustavorosa.models.RelatorioMensal;
import dev.gustavorosa.models.SituacaoPagamento;
import dev.gustavorosa.util.Formatador;
import dev.gustavorosa.util.Menu;

import java.util.List;
import java.util.Map;

public class RelatorioController {
    private final ClienteDAO clienteDAO;
    private final ContratoDAO contratoDAO;
    private final PagamentoDAO pagamentoDAO;
    private final RelatorioMensalDAO relatorioMensalDAO;
    private final MeusGanhosDAO meusGanhosDAO;
    private final ViewDAO viewDAO;

    public RelatorioController() {
        this.clienteDAO = new ClienteDAO();
        this.contratoDAO = new ContratoDAO();
        this.pagamentoDAO = new PagamentoDAO();
        this.relatorioMensalDAO = new RelatorioMensalDAO();
        this.meusGanhosDAO = new MeusGanhosDAO();
        this.viewDAO = new ViewDAO();
    }

    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuRelatorios();
            switch (opcao) {
                case 1:
                    listarContratosPorCliente();
                    break;
                case 2:
                    listarPagamentosEmAberto();
                    break;
                case 3:
                    listarPagamentosPagos();
                    break;
                case 4:
                    listarPagamentosAtrasados();
                    break;
                case 5:
                    gerarRelatorioMensalCliente();
                    break;
                case 6:
                    listarTodosRelatoriosMensais();
                    break;
                case 7:
                    calcularGanhosMensais();
                    break;
                case 8:
                    listarTodosGanhosMensais();
                    break;
                case 9:
                    mostrarResumoConsolidadoClientes();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    Menu.mensagemErro("Opcao invalida!");
                    break;
            }
        }
    }

    private void listarContratosPorCliente() {
        System.out.println();
        mostrarIdsClientesDisponiveis();
        Long idCliente = Menu.lerLong("Digite o ID do cliente: ");
        Cliente cliente = clienteDAO.buscarClientePorId(idCliente);

        if (cliente == null || cliente.getId() == null) {
            Menu.mensagemErro("Cliente nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        List<Contrato> contratos = contratoDAO.buscarPorCliente(idCliente);
        System.out.println();
        Menu.imprimeTitulo("Contratos do Cliente: " + cliente.getNome());

        if (contratos.isEmpty()) {
            Menu.mensagemInfo("Nenhum contrato encontrado para este cliente.");
        } else {
            System.out.printf("%-5s | %-25s | %-15s | %-12s | %-8s | %-12s%n",
                    "ID", "Nome", "Registro", "Data Inicio", "Meses", "Valor");
            Menu.imprimeSeparador();
            for (Contrato c : contratos) {
                String dataInicio = Formatador.formatarData(c.getDataInicio());
                System.out.printf("%-5d | %-25s | %-15s | %-12s | %-8d | R$%-10.2f%n",
                        c.getId(), c.getNome(), c.getRegistro(),
                        dataInicio, c.getDuracaoEmMeses(), c.getValorContrato());
            }
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void listarPagamentosEmAberto() {
        System.out.println();
        Menu.imprimeTitulo("Pagamentos em Aberto");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(SituacaoPagamento.ABERTO);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private void listarPagamentosPagos() {
        System.out.println();
        Menu.imprimeTitulo("Pagamentos Pagos");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(SituacaoPagamento.PAGO);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private void listarPagamentosAtrasados() {
        System.out.println();
        Menu.imprimeTitulo("Pagamentos Atrasados");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(SituacaoPagamento.PAGO_ATRASADO);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private void imprimirListaPagamentos(List<Pagamento> pagamentos) {
        if (pagamentos.isEmpty()) {
            Menu.mensagemInfo("Nenhum pagamento encontrado.");
        } else {
            System.out.printf("%-5s | %-20s | %-10s | %-8s | %-12s | %-12s | %-12s | %-12s%n",
                    "ID", "Cliente", "Contrato", "Parcela", "Valor", "Vencimento", "Pagamento", "Situacao");
            Menu.imprimeSeparador();
            for (Pagamento p : pagamentos) {
                String dataVenc = Formatador.formatarData(p.getDataVencimento());
                String dataPag = Formatador.formatarData(p.getDataPagamento());
                
                String nomeCliente = obterNomeCliente(p.getIdContrato());
                
                System.out.printf("%-5d | %-20s | %-10d | %-8d | R$%-10.2f | %-12s | %-12s | %-12s%n",
                        p.getId(), nomeCliente, p.getIdContrato(), p.getNumParcela(), p.getValor(),
                        dataVenc, dataPag, p.getSituacao());
            }
        }
    }

    private void mostrarIdsClientesDisponiveis() {
        List<Cliente> clientes = clienteDAO.buscarClientes();
        if (clientes.isEmpty()) {
            Menu.mensagemInfo("Nenhum cliente cadastrado.");
        } else {
            System.out.println("Clientes disponiveis:");
            System.out.printf("%-5s | %-30s%n", "ID", "Nome");
            Menu.imprimeSeparador();
            for (Cliente c : clientes) {
                System.out.printf("%-5d | %-30s%n", c.getId(), c.getNome());
            }
            System.out.println();
        }
    }

    private void gerarRelatorioMensalCliente() {
        System.out.println();
        mostrarIdsClientesDisponiveis();
        Long idCliente = Menu.lerLong("Digite o ID do cliente: ");
        
        Cliente cliente = clienteDAO.buscarClientePorId(idCliente);
        if (cliente == null || cliente.getId() == null) {
            Menu.mensagemErro("Cliente nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        int mes = Menu.lerInt("Digite o mes (1-12): ");
        if (mes < 1 || mes > 12) {
            Menu.mensagemErro("Mes invalido! Use valores de 1 a 12.");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        int ano = Menu.lerInt("Digite o ano (ex: 2025): ");

        System.out.println();
        Menu.mensagemInfo("Gerando relatorio mensal...");
        relatorioMensalDAO.gerarRelatorioMensal(idCliente, mes, ano);

        RelatorioMensal relatorio = relatorioMensalDAO.buscarPorClienteMesAno(idCliente, mes, ano);
        if (relatorio != null) {
            System.out.println();
            Menu.imprimeTitulo("Relatorio Mensal - " + cliente.getNome() + " (" + mes + "/" + ano + ")");
            System.out.println("Total Pago: R$ " + String.format("%.2f", relatorio.getTotalPago()));
            System.out.println("Total Pendente: R$ " + String.format("%.2f", relatorio.getTotalPendente()));
            System.out.println("Total Pago com Atraso: R$ " + String.format("%.2f", relatorio.getTotalPagoComAtraso()));
            System.out.println("Total Geral: R$ " + String.format("%.2f", 
                relatorio.getTotalPago() + relatorio.getTotalPendente() + relatorio.getTotalPagoComAtraso()));
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void listarTodosRelatoriosMensais() {
        System.out.println();
        Menu.imprimeTitulo("Todos os Relatorios Mensais");
        List<RelatorioMensal> relatorios = relatorioMensalDAO.buscarRelatorios();

        if (relatorios.isEmpty()) {
            Menu.mensagemInfo("Nenhum relatorio mensal cadastrado.");
        } else {
            System.out.printf("%-5s | %-10s | %-30s | %-8s | %-12s | %-12s | %-12s | %-12s%n",
                    "ID", "ID Cliente", "Cliente", "Mes/Ano", "Total Pago", "Pendente", "Atraso", "Total Geral");
            Menu.imprimeSeparador();
            for (RelatorioMensal r : relatorios) {
                Cliente cliente = clienteDAO.buscarClientePorId(r.getIdCliente());
                String nomeCliente = cliente != null ? Formatador.truncarTexto(cliente.getNome(), 30) : "N/A";
                String mesAno = r.getMes() + "/" + r.getAno();
                double totalGeral = r.getTotalPago() + r.getTotalPendente() + r.getTotalPagoComAtraso();
                
                System.out.printf("%-5d | %-10d | %-30s | %-8s | R$%-10.2f | R$%-10.2f | R$%-10.2f | R$%-10.2f%n",
                        r.getId(), r.getIdCliente(), nomeCliente, mesAno,
                        r.getTotalPago(), r.getTotalPendente(), r.getTotalPagoComAtraso(), totalGeral);
            }
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void calcularGanhosMensais() {
        System.out.println();
        int mes = Menu.lerInt("Digite o mes (1-12): ");
        if (mes < 1 || mes > 12) {
            Menu.mensagemErro("Mes invalido! Use valores de 1 a 12.");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        int ano = Menu.lerInt("Digite o ano (ex: 2025): ");

        System.out.println();
        Menu.mensagemInfo("Calculando ganhos mensais...");
        meusGanhosDAO.calcularGanhosMensais(mes, ano);

        MeusGanhos ganhos = meusGanhosDAO.buscarPorMesAno(mes, ano);
        if (ganhos != null) {
            System.out.println();
            Menu.imprimeTitulo("Meus Ganhos - " + mes + "/" + ano);
            System.out.println("Total Recebido: R$ " + String.format("%.2f", ganhos.getTotalRecebido()));
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void listarTodosGanhosMensais() {
        System.out.println();
        Menu.imprimeTitulo("Todos os Ganhos Mensais");
        List<MeusGanhos> ganhos = meusGanhosDAO.buscarGanhos();

        if (ganhos.isEmpty()) {
            Menu.mensagemInfo("Nenhum registro de ganhos encontrado.");
        } else {
            System.out.printf("%-5s | %-8s | %-12s%n", "ID", "Mes/Ano", "Total Recebido");
            Menu.imprimeSeparador();
            double totalGeral = 0.0;
            for (MeusGanhos g : ganhos) {
                String mesAno = g.getMes() + "/" + g.getAno();
                totalGeral += g.getTotalRecebido();
                System.out.printf("%-5d | %-8s | R$%-10.2f%n",
                        g.getId(), mesAno, g.getTotalRecebido());
            }
            Menu.imprimeSeparador();
            System.out.printf("%-5s | %-8s | R$%-10.2f%n", "", "TOTAL", totalGeral);
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void mostrarResumoConsolidadoClientes() {
        System.out.println();
        Menu.imprimeTitulo("Resumo Consolidado de Clientes (VIEW)");
        List<Map<String, Object>> resumos = viewDAO.buscarResumoClientes();

        if (resumos.isEmpty()) {
            Menu.mensagemInfo("Nenhum cliente encontrado.");
        } else {
            System.out.printf("%-5s | %-25s | %-15s | %-8s | %-15s | %-8s | %-8s | %-8s | %-8s | %-12s | %-12s%n",
                    "ID", "Nome", "Registro", "Contratos", "Valor Contratos", "Pagamentos", "Pagos", "Pendentes", 
                    "Atrasados", "Total Recebido", "Total Pendente");
            Menu.imprimeSeparador();
            
            double totalGeralRecebido = 0.0;
            double totalGeralPendente = 0.0;
            
            for (Map<String, Object> resumo : resumos) {
                String nome = Formatador.truncarTexto((String) resumo.get("cliente_nome"), 25);
                System.out.printf("%-5d | %-25s | %-15s | %-8d | R$%-13.2f | %-8d | %-8d | %-8d | %-8d | R$%-10.2f | R$%-10.2f%n",
                        resumo.get("cliente_id"),
                        nome,
                        resumo.get("cliente_registro"),
                        resumo.get("total_contratos"),
                        resumo.get("valor_total_contratos"),
                        resumo.get("total_pagamentos"),
                        resumo.get("pagamentos_pagos"),
                        resumo.get("pagamentos_pendentes"),
                        resumo.get("pagamentos_atrasados"),
                        resumo.get("total_recebido"),
                        resumo.get("total_pendente"));
                
                totalGeralRecebido += (Double) resumo.get("total_recebido");
                totalGeralPendente += (Double) resumo.get("total_pendente");
            }
            
            Menu.imprimeSeparador();
            System.out.printf("%-5s | %-25s | %-15s | %-8s | %-15s | %-8s | %-8s | %-8s | %-8s | R$%-10.2f | R$%-10.2f%n",
                    "", "", "", "", "", "", "", "", "TOTAL:", totalGeralRecebido, totalGeralPendente);
        }
        Menu.pressioneEnterParaContinuar();
    }

    private String obterNomeCliente(Long idContrato) {
        Contrato contrato = contratoDAO.buscarPorId(idContrato);
        if (contrato != null) {
            Cliente cliente = clienteDAO.buscarClientePorId(contrato.getIdCliente());
            if (cliente != null && cliente.getNome() != null) {
                return Formatador.truncarTexto(cliente.getNome(), 20);
            }
        }
        return "N/A";
    }
}

