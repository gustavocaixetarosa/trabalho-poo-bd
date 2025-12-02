package dev.gustavorosa.controller;

import dev.gustavorosa.dao.ClienteDAO;
import dev.gustavorosa.dao.ContratoDAO;
import dev.gustavorosa.dao.PagamentoDAO;
import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.models.Pagamento;
import dev.gustavorosa.models.SituacaoPagamento;
import dev.gustavorosa.util.Formatador;
import dev.gustavorosa.util.Menu;

import java.util.List;

public class RelatorioController {
    private final ClienteDAO clienteDAO;
    private final ContratoDAO contratoDAO;
    private final PagamentoDAO pagamentoDAO;

    public RelatorioController() {
        this.clienteDAO = new ClienteDAO();
        this.contratoDAO = new ContratoDAO();
        this.pagamentoDAO = new PagamentoDAO();
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

