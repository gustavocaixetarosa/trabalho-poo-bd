package dev.gustavorosa;

import dev.gustavorosa.dao.ClienteDAO;
import dev.gustavorosa.dao.ContratoDAO;
import dev.gustavorosa.dao.PagamentoDAO;
import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.models.Pagamento;
import dev.gustavorosa.models.SituacaoPagamento;
import dev.gustavorosa.util.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ContratoDAO contratoDAO = new ContratoDAO();
    private static final PagamentoDAO pagamentoDAO = new PagamentoDAO();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Scanner scan = Menu.getScanner();

    public static void main(String[] args) {
        Menu.mensagemInicial();

        boolean continuar = true;
        while (continuar) {
            try {
                int opcao = Menu.mostrarMenuPrincipal();
                switch (opcao) {
                    case 1:
                        menuClientes();
                        break;
                    case 2:
                        menuContratos();
                        break;
                    case 3:
                        menuPagamentos();
                        break;
                    case 4:
                        menuRelatorios();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("\nObrigado por usar o sistema. Ate logo!");
                        break;
                    default:
                        Menu.mensagemErro("Opcao invalida. Tente novamente!");
                        break;
                }
            } catch (Exception e) {
                Menu.mensagemErro("Ocorreu um erro: " + e.getMessage());
                scan.nextLine(); // Limpa o buffer em caso de erro
            }
        }
    }

    // ==================== MENU CLIENTES ====================
    private static void menuClientes() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuCrud("Clientes");
            switch (opcao) {
                case 1:
                    listarClientes();
                    break;
                case 2:
                    buscarClientePorId();
                    break;
                case 3:
                    adicionarCliente();
                    break;
                case 4:
                    atualizarCliente();
                    break;
                case 5:
                    excluirCliente();
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

    private static void listarClientes() {
        System.out.println();
        Menu.imprimeTitulo("Lista de Clientes");
        List<Cliente> clientes = clienteDAO.buscarClientes();

        if (clientes.isEmpty()) {
            Menu.mensagemInfo("Nenhum cliente cadastrado.");
        } else {
            System.out.printf("%-5s | %-30s | %-15s | %-40s%n", "ID", "Nome", "Registro", "Endereco");
            Menu.imprimeSeparador();
            for (Cliente c : clientes) {
                System.out.printf("%-5d | %-30s | %-15s | %-40s%n",
                        c.getId(), c.getNome(), c.getRegistro(), c.getEndereco());
            }
        }
        Menu.pressioneEnterParaContinuar();
    }

    private static void buscarClientePorId() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do cliente: ");
        Cliente cliente = clienteDAO.buscarClientePorId(id);

        if (cliente != null && cliente.getId() != null) {
            System.out.println();
            Menu.imprimeTitulo("Dados do Cliente");
            System.out.println("ID: " + cliente.getId());
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("Registro: " + cliente.getRegistro());
            System.out.println("Endereco: " + cliente.getEndereco());
        } else {
            Menu.mensagemErro("Cliente nao encontrado!");
        }
        Menu.pressioneEnterParaContinuar();
    }

    private static void adicionarCliente() {
        System.out.println();
        Menu.imprimeTitulo("Adicionar Novo Cliente");

        String nome = Menu.lerString("Nome: ");
        String registro = Menu.lerStringDireto("Registro (CPF/CNPJ): ");
        String endereco = Menu.lerStringDireto("Endereco: ");

        Cliente cliente = new Cliente(nome, registro, endereco);
        clienteDAO.salvar(cliente);
        Menu.pressioneEnterParaContinuar();
    }

    private static void atualizarCliente() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do cliente a atualizar: ");
        Cliente clienteExistente = clienteDAO.buscarClientePorId(id);

        if (clienteExistente == null || clienteExistente.getId() == null) {
            Menu.mensagemErro("Cliente nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        System.out.println("\nDados atuais:");
        System.out.println("Nome: " + clienteExistente.getNome());
        System.out.println("Registro: " + clienteExistente.getRegistro());
        System.out.println("Endereco: " + clienteExistente.getEndereco());

        System.out.println("\nDigite os novos dados (deixe vazio para manter o atual):");
        String nome = Menu.lerString("Novo nome: ");
        String registro = Menu.lerStringDireto("Novo registro: ");
        String endereco = Menu.lerStringDireto("Novo endereco: ");

        if (!nome.isEmpty()) clienteExistente.setNome(nome);
        if (!registro.isEmpty()) clienteExistente.setRegistro(registro);
        if (!endereco.isEmpty()) clienteExistente.setEndereco(endereco);

        clienteDAO.atualizar(clienteExistente, id);
        Menu.pressioneEnterParaContinuar();
    }

    private static void excluirCliente() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do cliente a excluir: ");
        clienteDAO.deletar(id);
        Menu.pressioneEnterParaContinuar();
    }

    // ==================== MENU CONTRATOS ====================
    private static void menuContratos() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuCrud("Contratos");
            switch (opcao) {
                case 1:
                    listarContratos();
                    break;
                case 2:
                    buscarContratoPorId();
                    break;
                case 3:
                    adicionarContrato();
                    break;
                case 4:
                    atualizarContrato();
                    break;
                case 5:
                    excluirContrato();
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

    private static void listarContratos() {
        System.out.println();
        Menu.imprimeTitulo("Lista de Contratos");
        List<Contrato> contratos = contratoDAO.buscarContratos();

        if (contratos.isEmpty()) {
            Menu.mensagemInfo("Nenhum contrato cadastrado.");
        } else {
            System.out.printf("%-5s | %-10s | %-25s | %-15s | %-12s | %-8s | %-12s%n",
                    "ID", "ID Cliente", "Nome", "Registro", "Data Inicio", "Meses", "Valor");
            Menu.imprimeSeparador();
            for (Contrato c : contratos) {
                String dataInicio = c.getDataInicio() != null ? c.getDataInicio().format(formatter) : "N/A";
                System.out.printf("%-5d | %-10d | %-25s | %-15s | %-12s | %-8d | R$%-10.2f%n",
                        c.getId(), c.getIdCliente(), c.getNome(), c.getRegistro(),
                        dataInicio, c.getDuracaoEmMeses(), c.getValorContrato());
            }
        }
        Menu.pressioneEnterParaContinuar();
    }

    private static void buscarContratoPorId() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do contrato: ");
        Contrato contrato = contratoDAO.buscarPorId(id);

        if (contrato != null) {
            System.out.println();
            Menu.imprimeTitulo("Dados do Contrato");
            System.out.println("ID: " + contrato.getId());
            System.out.println("ID Cliente: " + contrato.getIdCliente());
            System.out.println("Nome: " + contrato.getNome());
            System.out.println("Registro: " + contrato.getRegistro());
            System.out.println("Data Inicio: " + (contrato.getDataInicio() != null ?
                    contrato.getDataInicio().format(formatter) : "N/A"));
            System.out.println("Duracao (meses): " + contrato.getDuracaoEmMeses());
            System.out.println("Valor: R$ " + String.format("%.2f", contrato.getValorContrato()));
        } else {
            Menu.mensagemErro("Contrato nao encontrado!");
        }
        Menu.pressioneEnterParaContinuar();
    }

    private static void adicionarContrato() {
        System.out.println();
        Menu.imprimeTitulo("Adicionar Novo Contrato");

        Long idCliente = Menu.lerLong("ID do Cliente: ");

        // Verifica se o cliente existe
        Cliente cliente = clienteDAO.buscarClientePorId(idCliente);
        if (cliente == null || cliente.getId() == null) {
            Menu.mensagemErro("Cliente nao encontrado! Cadastre o cliente primeiro.");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        String nome = Menu.lerString("Nome do contrato: ");
        String registro = Menu.lerStringDireto("Registro do contrato: ");
        String dataInicioStr = Menu.lerStringDireto("Data de inicio (dd/MM/yyyy): ");

        LocalDate dataInicio;
        try {
            dataInicio = LocalDate.parse(dataInicioStr, formatter);
        } catch (DateTimeParseException e) {
            Menu.mensagemErro("Data invalida! Use o formato dd/MM/yyyy");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        int duracaoMeses = Menu.lerInt("Duracao em meses: ");
        double valorContrato = Menu.lerDouble("Valor do contrato: ");

        Contrato contrato = new Contrato(idCliente, nome, registro, dataInicio, duracaoMeses, valorContrato);
        contratoDAO.salvar(contrato);
        Menu.pressioneEnterParaContinuar();
    }

    private static void atualizarContrato() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do contrato a atualizar: ");
        Contrato contratoExistente = contratoDAO.buscarPorId(id);

        if (contratoExistente == null) {
            Menu.mensagemErro("Contrato nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        System.out.println("\nDados atuais:");
        System.out.println("ID Cliente: " + contratoExistente.getIdCliente());
        System.out.println("Nome: " + contratoExistente.getNome());
        System.out.println("Registro: " + contratoExistente.getRegistro());
        System.out.println("Data Inicio: " + (contratoExistente.getDataInicio() != null ?
                contratoExistente.getDataInicio().format(formatter) : "N/A"));
        System.out.println("Duracao (meses): " + contratoExistente.getDuracaoEmMeses());
        System.out.println("Valor: R$ " + String.format("%.2f", contratoExistente.getValorContrato()));

        System.out.println("\nDigite os novos dados (deixe vazio/0 para manter o atual):");

        String idClienteStr = Menu.lerString("Novo ID Cliente (ou 0 para manter): ");
        if (!idClienteStr.isEmpty() && !idClienteStr.equals("0")) {
            contratoExistente.setIdCliente(Long.parseLong(idClienteStr));
        }

        String nome = Menu.lerStringDireto("Novo nome: ");
        if (!nome.isEmpty()) contratoExistente.setNome(nome);

        String registro = Menu.lerStringDireto("Novo registro: ");
        if (!registro.isEmpty()) contratoExistente.setRegistro(registro);

        String dataInicioStr = Menu.lerStringDireto("Nova data inicio (dd/MM/yyyy): ");
        if (!dataInicioStr.isEmpty()) {
            try {
                contratoExistente.setDataInicio(LocalDate.parse(dataInicioStr, formatter));
            } catch (DateTimeParseException e) {
                Menu.mensagemErro("Data invalida, mantendo a anterior.");
            }
        }

        String duracaoStr = Menu.lerStringDireto("Nova duracao em meses (ou 0 para manter): ");
        if (!duracaoStr.isEmpty() && !duracaoStr.equals("0")) {
            contratoExistente.setDuracaoEmMeses(Integer.parseInt(duracaoStr));
        }

        String valorStr = Menu.lerStringDireto("Novo valor (ou 0 para manter): ");
        if (!valorStr.isEmpty() && !valorStr.equals("0")) {
            contratoExistente.setValorContrato(Double.parseDouble(valorStr));
        }

        contratoDAO.atualizar(contratoExistente, id);
        Menu.pressioneEnterParaContinuar();
    }

    private static void excluirContrato() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do contrato a excluir: ");
        contratoDAO.deletar(id);
        Menu.pressioneEnterParaContinuar();
    }

    // ==================== MENU PAGAMENTOS ====================
    private static void menuPagamentos() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuPagamentos();
            switch (opcao) {
                case 1:
                    listarPagamentos();
                    break;
                case 2:
                    buscarPagamentoPorId();
                    break;
                case 3:
                    buscarPagamentosPorContrato();
                    break;
                case 4:
                    buscarPagamentosPorSituacao();
                    break;
                case 5:
                    adicionarPagamento();
                    break;
                case 6:
                    registrarPagamento();
                    break;
                case 7:
                    atualizarPagamento();
                    break;
                case 8:
                    excluirPagamento();
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

    private static void listarPagamentos() {
        System.out.println();
        Menu.imprimeTitulo("Lista de Pagamentos");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPagamentos();
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private static void imprimirListaPagamentos(List<Pagamento> pagamentos) {
        if (pagamentos.isEmpty()) {
            Menu.mensagemInfo("Nenhum pagamento encontrado.");
        } else {
            System.out.printf("%-5s | %-10s | %-8s | %-12s | %-12s | %-12s | %-12s%n",
                    "ID", "Contrato", "Parcela", "Valor", "Vencimento", "Pagamento", "Situacao");
            Menu.imprimeSeparador();
            for (Pagamento p : pagamentos) {
                String dataVenc = p.getDataVencimento() != null ? p.getDataVencimento().format(formatter) : "N/A";
                String dataPag = p.getDataPagamento() != null ? p.getDataPagamento().format(formatter) : "N/A";
                System.out.printf("%-5d | %-10d | %-8d | R$%-10.2f | %-12s | %-12s | %-12s%n",
                        p.getId(), p.getIdContrato(), p.getNumParcela(), p.getValor(),
                        dataVenc, dataPag, p.getSituacao());
            }
        }
    }

    private static void buscarPagamentoPorId() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do pagamento: ");
        Pagamento pagamento = pagamentoDAO.buscarPorId(id);

        if (pagamento != null) {
            System.out.println();
            Menu.imprimeTitulo("Dados do Pagamento");
            System.out.println("ID: " + pagamento.getId());
            System.out.println("ID Contrato: " + pagamento.getIdContrato());
            System.out.println("Numero da Parcela: " + pagamento.getNumParcela());
            System.out.println("Valor: R$ " + String.format("%.2f", pagamento.getValor()));
            System.out.println("Data Vencimento: " + (pagamento.getDataVencimento() != null ?
                    pagamento.getDataVencimento().format(formatter) : "N/A"));
            System.out.println("Data Pagamento: " + (pagamento.getDataPagamento() != null ?
                    pagamento.getDataPagamento().format(formatter) : "N/A"));
            System.out.println("Situacao: " + pagamento.getSituacao());
        } else {
            Menu.mensagemErro("Pagamento nao encontrado!");
        }
        Menu.pressioneEnterParaContinuar();
    }

    private static void buscarPagamentosPorContrato() {
        System.out.println();
        Long idContrato = Menu.lerLong("Digite o ID do contrato: ");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorContrato(idContrato);

        System.out.println();
        Menu.imprimeTitulo("Pagamentos do Contrato " + idContrato);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private static void buscarPagamentosPorSituacao() {
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

    private static void adicionarPagamento() {
        System.out.println();
        Menu.imprimeTitulo("Adicionar Novo Pagamento");

        Long idContrato = Menu.lerLong("ID do Contrato: ");

        // Verifica se o contrato existe
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
            dataVencimento = LocalDate.parse(dataVencStr, formatter);
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

    private static void registrarPagamento() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do pagamento a registrar: ");
        pagamentoDAO.registrarPagamento(id);
        Menu.pressioneEnterParaContinuar();
    }

    private static void atualizarPagamento() {
        System.out.println();
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
        System.out.println("Data Vencimento: " + (pagamentoExistente.getDataVencimento() != null ?
                pagamentoExistente.getDataVencimento().format(formatter) : "N/A"));
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
                pagamentoExistente.setDataVencimento(LocalDate.parse(dataVencStr, formatter));
            } catch (DateTimeParseException e) {
                Menu.mensagemErro("Data invalida, mantendo a anterior.");
            }
        }

        pagamentoDAO.atualizar(pagamentoExistente, id);
        Menu.pressioneEnterParaContinuar();
    }

    private static void excluirPagamento() {
        System.out.println();
        Long id = Menu.lerLong("Digite o ID do pagamento a excluir: ");
        pagamentoDAO.deletar(id);
        Menu.pressioneEnterParaContinuar();
    }

    // ==================== MENU RELATORIOS ====================
    private static void menuRelatorios() {
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

    private static void listarContratosPorCliente() {
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
                String dataInicio = c.getDataInicio() != null ? c.getDataInicio().format(formatter) : "N/A";
                System.out.printf("%-5d | %-25s | %-15s | %-12s | %-8d | R$%-10.2f%n",
                        c.getId(), c.getNome(), c.getRegistro(),
                        dataInicio, c.getDuracaoEmMeses(), c.getValorContrato());
            }
        }
        Menu.pressioneEnterParaContinuar();
    }

    private static void listarPagamentosEmAberto() {
        System.out.println();
        Menu.imprimeTitulo("Pagamentos em Aberto");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(SituacaoPagamento.ABERTO);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private static void listarPagamentosPagos() {
        System.out.println();
        Menu.imprimeTitulo("Pagamentos Pagos");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(SituacaoPagamento.PAGO);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }

    private static void listarPagamentosAtrasados() {
        System.out.println();
        Menu.imprimeTitulo("Pagamentos Atrasados");
        List<Pagamento> pagamentos = pagamentoDAO.buscarPorSituacao(SituacaoPagamento.PAGO_ATRASADO);
        imprimirListaPagamentos(pagamentos);
        Menu.pressioneEnterParaContinuar();
    }
}
