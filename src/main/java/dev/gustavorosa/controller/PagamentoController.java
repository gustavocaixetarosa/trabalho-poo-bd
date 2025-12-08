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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PagamentoController {
    private final PagamentoDAO pagamentoDAO;
    private final ContratoDAO contratoDAO;
    private final ClienteDAO clienteDAO;

    public PagamentoController() {
        this.pagamentoDAO = new PagamentoDAO();
        this.contratoDAO = new ContratoDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuPagamentos();
            switch (opcao) {
                case 1:
                    listar();
                    break;
                case 2:
                    buscarPorId();
                    break;
                case 3:
                    buscarPorContrato();
                    break;
                case 4:
                    buscarPorSituacao();
                    break;
                case 5:
                    adicionar();
                    break;
                case 6:
                    registrarPagamento();
                    break;
                case 7:
                    atualizar();
                    break;
                case 8:
                    excluir();
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

    private void listar() {
        System.out.println();
        Menu.imprimeTitulo("Lista de Pagamentos");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPagamentos();
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

    private void buscarPorId() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do pagamento: ");
        Pagamento pagamento = pagamentoDAO.buscarPorId(id);

        if (pagamento != null) {
            System.out.println();
            Menu.imprimeTitulo("Dados do Pagamento");
            System.out.println("ID: " + pagamento.getId());
            System.out.println("ID Contrato: " + pagamento.getIdContrato());
            
            Contrato contrato = contratoDAO.buscarPorId(pagamento.getIdContrato());
            if (contrato != null) {
                System.out.println("Nome do Contrato: " + contrato.getNome());
                Cliente cliente = clienteDAO.buscarClientePorId(contrato.getIdCliente());
                if (cliente != null && cliente.getNome() != null) {
                    System.out.println("Cliente: " + cliente.getNome());
                }
            }
            
            System.out.println("Numero da Parcela: " + pagamento.getNumParcela());
            System.out.println("Valor: R$ " + String.format("%.2f", pagamento.getValor()));
            System.out.println("Data Vencimento: " + Formatador.formatarData(pagamento.getDataVencimento()));
            System.out.println("Data Pagamento: " + Formatador.formatarData(pagamento.getDataPagamento()));
            System.out.println("Situacao: " + pagamento.getSituacao());
        } else {
            Menu.mensagemErro("Pagamento nao encontrado!");
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void buscarPorContrato() {
        System.out.println();
        mostrarIdsContratosDisponiveis();
        Long idContrato = Menu.lerLong("Digite o ID do contrato: ");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorContrato(idContrato);

        System.out.println();
        
        String titulo = "Pagamentos do Contrato " + idContrato;
        Contrato contrato = contratoDAO.buscarPorId(idContrato);
        if (contrato != null) {
            Cliente cliente = clienteDAO.buscarClientePorId(contrato.getIdCliente());
            if (cliente != null && cliente.getNome() != null) {
                titulo = "Pagamentos do Contrato " + idContrato + " (Cliente: " + cliente.getNome() + ")";
            }
        }
        
        Menu.imprimeTitulo(titulo);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private void buscarPorSituacao() {
        System.out.println();
        System.out.println("Situacoes disponiveis:");
        System.out.println("1. ABERTO (Pendente)");
        System.out.println("2. PAGO");
        System.out.println("3. PAGO_ATRASADO");
        int opcao = Menu.lerInt("Escolha a situacao: ");

        SituacaoPagamento situacao;
        switch (opcao) {
            case 1:
                situacao = SituacaoPagamento.ABERTO;
                break;
            case 2:
                situacao = SituacaoPagamento.PAGO;
                break;
            case 3:
                situacao = SituacaoPagamento.PAGO_ATRASADO;
                break;
            default:
                Menu.mensagemErro("Opcao invalida!");
                Menu.pressioneEnterParaContinuar();
                return;
        }

        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(situacao);
        System.out.println();
        Menu.imprimeTitulo("Pagamentos com Situacao: " + situacao);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private void adicionar() {
        System.out.println();
        Menu.imprimeTitulo("Adicionar Novo Pagamento");

        mostrarIdsContratosDisponiveis();
        Long idContrato = Menu.lerLong("ID do Contrato: ");

        Contrato contrato = contratoDAO.buscarPorId(idContrato);
        if (contrato == null) {
            Menu.mensagemErro("Contrato nao encontrado! Cadastre o contrato primeiro.");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        double valor = Menu.lerDouble("Valor da parcela: ");
        int numParcela = Menu.lerInt("Numero da parcela: ");
        String dataVencStr = Menu.lerString("Data de vencimento (dd/MM/yyyy): ");

        LocalDate dataVencimento;
        try {
            dataVencimento = LocalDate.parse(dataVencStr, Formatador.FORMATO_DATA);
        } catch (DateTimeParseException e) {
            Menu.mensagemErro("Data invalida! Use o formato dd/MM/yyyy");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        Pagamento pagamento = new Pagamento(idContrato, valor, numParcela, null,
                dataVencimento, SituacaoPagamento.ABERTO);
        pagamentoDAO.salvar(pagamento);
        Menu.pressioneEnterParaContinuar();
    }

    private void registrarPagamento() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do pagamento a registrar: ");
        pagamentoDAO.registrarPagamento(id);
        Menu.pressioneEnterParaContinuar();
    }

    private void atualizar() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do pagamento a atualizar: ");
        Pagamento pagamentoExistente = pagamentoDAO.buscarPorId(id);

        if (pagamentoExistente == null) {
            Menu.mensagemErro("Pagamento nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        System.out.println("\nDados atuais:");
        System.out.println("ID Contrato: " + pagamentoExistente.getIdContrato());
        System.out.println("Valor: R$ " + String.format("%.2f", pagamentoExistente.getValor()));
        System.out.println("Numero Parcela: " + pagamentoExistente.getNumParcela());
        System.out.println("Data Vencimento: " + Formatador.formatarData(pagamentoExistente.getDataVencimento()));
        System.out.println("Situacao: " + pagamentoExistente.getSituacao());

        System.out.println("\nDigite os novos dados (deixe vazio/0 para manter o atual):");

        String valorStr = Menu.lerString("Novo valor (ou 0 para manter): ");
        if (!valorStr.isEmpty() && !valorStr.equals("0")) {
            pagamentoExistente.setValor(Double.parseDouble(valorStr));
        }

        String parcelaStr = Menu.lerStringDireto("Novo numero da parcela (ou 0 para manter): ");
        if (!parcelaStr.isEmpty() && !parcelaStr.equals("0")) {
            pagamentoExistente.setNumParcela(Integer.parseInt(parcelaStr));
        }

        String dataVencStr = Menu.lerStringDireto("Nova data de vencimento (dd/MM/yyyy): ");
        if (!dataVencStr.isEmpty()) {
            try {
                pagamentoExistente.setDataVencimento(LocalDate.parse(dataVencStr, Formatador.FORMATO_DATA));
            } catch (DateTimeParseException e) {
                Menu.mensagemErro("Data invalida, mantendo a anterior.");
            }
        }

        pagamentoDAO.atualizar(pagamentoExistente, id);
        Menu.pressioneEnterParaContinuar();
    }

    private void excluir() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do pagamento a excluir: ");
        pagamentoDAO.deletar(id);
        Menu.pressioneEnterParaContinuar();
    }

    private void mostrarIdsDisponiveis() {
        List<Pagamento> pagamentos = pagamentoDAO.buscarPagamentos();
        if (pagamentos.isEmpty()) {
            Menu.mensagemInfo("Nenhum pagamento cadastrado.");
        } else {
            System.out.println("Pagamentos disponiveis:");
            System.out.printf("%-5s | %-10s | %-8s | %-12s | %-12s%n", "ID", "Contrato", "Parcela", "Valor", "Situacao");
            Menu.imprimeSeparador();
            for (Pagamento p : pagamentos) {
                System.out.printf("%-5d | %-10d | %-8d | R$%-10.2f | %-12s%n",
                        p.getId(), p.getIdContrato(), p.getNumParcela(), p.getValor(), p.getSituacao());
            }
            System.out.println();
        }
    }

    private void mostrarIdsContratosDisponiveis() {
        List<Contrato> contratos = contratoDAO.buscarContratos();
        if (contratos.isEmpty()) {
            Menu.mensagemInfo("Nenhum contrato cadastrado.");
        } else {
            System.out.println("Contratos disponiveis:");
            System.out.printf("%-5s | %-25s | %-10s%n", "ID", "Nome", "ID Cliente");
            Menu.imprimeSeparador();
            for (Contrato c : contratos) {
                System.out.printf("%-5d | %-25s | %-10d%n", c.getId(), c.getNome(), c.getIdCliente());
            }
            System.out.println();
        }
    }
}

