package dev.gustavorosa.controller;

import dev.gustavorosa.dao.ClienteDAO;
import dev.gustavorosa.dao.ContratoDAO;
import dev.gustavorosa.dao.PagamentoDAO;
import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.util.Formatador;
import dev.gustavorosa.util.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ContratoController {
    private final ContratoDAO contratoDAO;
    private final ClienteDAO clienteDAO;
    private final PagamentoDAO pagamentoDAO;

    public ContratoController() {
        this.contratoDAO = new ContratoDAO();
        this.clienteDAO = new ClienteDAO();
        this.pagamentoDAO = new PagamentoDAO();
    }

    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuCrud("Contratos");
            switch (opcao) {
                case 1:
                    listar();
                    break;
                case 2:
                    buscarPorId();
                    break;
                case 3:
                    adicionar();
                    break;
                case 4:
                    atualizar();
                    break;
                case 5:
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
        Menu.imprimeTitulo("Lista de Contratos");
        List<Contrato> contratos = contratoDAO.buscarContratos();

        if (contratos.isEmpty()) {
            Menu.mensagemInfo("Nenhum contrato cadastrado.");
        } else {
            System.out.printf("%-5s | %-10s | %-25s | %-15s | %-12s | %-8s | %-12s%n",
                    "ID", "ID Cliente", "Nome", "Registro", "Data Inicio", "Meses", "Valor");
            Menu.imprimeSeparador();
            for (Contrato c : contratos) {
                String dataInicio = Formatador.formatarData(c.getDataInicio());
                System.out.printf("%-5d | %-10d | %-25s | %-15s | %-12s | %-8d | R$%-10.2f%n",
                        c.getId(), c.getIdCliente(), c.getNome(), c.getRegistro(),
                        dataInicio, c.getDuracaoEmMeses(), c.getValorContrato());
            }
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void buscarPorId() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do contrato: ");
        Contrato contrato = contratoDAO.buscarPorId(id);

        if (contrato != null) {
            System.out.println();
            Menu.imprimeTitulo("Dados do Contrato");
            System.out.println("ID: " + contrato.getId());
            System.out.println("ID Cliente: " + contrato.getIdCliente());
            System.out.println("Nome: " + contrato.getNome());
            System.out.println("Registro: " + contrato.getRegistro());
            System.out.println("Data Inicio: " + Formatador.formatarData(contrato.getDataInicio()));
            System.out.println("Duracao (meses): " + contrato.getDuracaoEmMeses());
            System.out.println("Valor: R$ " + String.format("%.2f", contrato.getValorContrato()));
        } else {
            Menu.mensagemErro("Contrato nao encontrado!");
        }
        Menu.pressioneEnterParaContinuar();
    }

    private void adicionar() {
        System.out.println();
        Menu.imprimeTitulo("Adicionar Novo Contrato");

        mostrarIdsClientesDisponiveis();
        Long idCliente = Menu.lerLong("ID do Cliente: ");

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
            dataInicio = LocalDate.parse(dataInicioStr, Formatador.FORMATO_DATA);
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

    private void atualizar() {
        System.out.println();
        mostrarIdsDisponiveis();
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
        System.out.println("Data Inicio: " + Formatador.formatarData(contratoExistente.getDataInicio()));
        System.out.println("Duracao (meses): " + contratoExistente.getDuracaoEmMeses());
        System.out.println("Valor: R$ " + String.format("%.2f", contratoExistente.getValorContrato()));

        System.out.println("\nDigite os novos dados (deixe vazio/0 para manter o atual):");

        mostrarIdsClientesDisponiveis();
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
                contratoExistente.setDataInicio(LocalDate.parse(dataInicioStr, Formatador.FORMATO_DATA));
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

    private void excluir() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do contrato a excluir: ");
        
        Contrato contrato = contratoDAO.buscarPorId(id);
        if (contrato == null) {
            Menu.mensagemErro("Contrato nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        int numPagamentos = pagamentoDAO.contarPagamentosPorContrato(id);
        
        if (numPagamentos > 0) {
            System.out.println();
            Menu.mensagemInfo("Este contrato possui " + numPagamentos + " pagamento(s) associado(s).");
            System.out.println("Os pagamentos serao excluidos automaticamente em cascata.");
            System.out.println();
            System.out.println("Deseja continuar?");
            System.out.println("1. Sim, excluir contrato e todos os pagamentos");
            System.out.println("2. Nao, cancelar operacao");
            int opcao = Menu.lerInt("Escolha uma opcao: ");

            if (opcao != 1) {
                Menu.mensagemInfo("Operacao cancelada.");
                Menu.pressioneEnterParaContinuar();
                return;
            }
        }
        
        contratoDAO.deletar(id);
        Menu.pressioneEnterParaContinuar();
    }

    private void mostrarIdsDisponiveis() {
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

    public Contrato buscarPorId(Long id) {
        return contratoDAO.buscarPorId(id);
    }

    public List<Contrato> buscarPorCliente(Long idCliente) {
        return contratoDAO.buscarPorCliente(idCliente);
    }
}

