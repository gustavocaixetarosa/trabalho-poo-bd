package dev.gustavorosa.controller;

import dev.gustavorosa.dao.ClienteDAO;
import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.util.Menu;

import java.util.List;

public class ClienteController {
    private final ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            int opcao = Menu.mostrarMenuCrud("Clientes");
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

    private void buscarPorId() {
        System.out.println();
        mostrarIdsDisponiveis();
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

    private void adicionar() {
        System.out.println();
        Menu.imprimeTitulo("Adicionar Novo Cliente");

        String nome = Menu.lerString("Nome: ");
        String registro = Menu.lerStringDireto("Registro (CPF/CNPJ): ");
        String endereco = Menu.lerStringDireto("Endereco: ");

        Cliente cliente = new Cliente(nome, registro, endereco);
        clienteDAO.salvar(cliente);
        Menu.pressioneEnterParaContinuar();
    }

    private void atualizar() {
        System.out.println();
        mostrarIdsDisponiveis();
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

    private void excluir() {
        System.out.println();
        mostrarIdsDisponiveis();
        Long id = Menu.lerLong("Digite o ID do cliente a excluir: ");
        
        Cliente cliente = clienteDAO.buscarClientePorId(id);
        if (cliente == null || cliente.getId() == null) {
            Menu.mensagemErro("Cliente nao encontrado!");
            Menu.pressioneEnterParaContinuar();
            return;
        }

        int numContratos = clienteDAO.contarContratosPorCliente(id);
        
        if (numContratos > 0) {
            System.out.println();
            Menu.mensagemInfo("Este cliente possui " + numContratos + " contrato(s) associado(s).");
            System.out.println("Os contratos e pagamentos serao excluidos automaticamente em cascata.");
            System.out.println();
            System.out.println("Deseja continuar?");
            System.out.println("1. Sim, excluir cliente e todos os dados relacionados");
            System.out.println("2. Nao, cancelar operacao");
            int opcao = Menu.lerInt("Escolha uma opcao: ");

            if (opcao != 1) {
                Menu.mensagemInfo("Operacao cancelada.");
                Menu.pressioneEnterParaContinuar();
                return;
            }
        }
        
        clienteDAO.deletar(id);
        Menu.pressioneEnterParaContinuar();
    }

    private void mostrarIdsDisponiveis() {
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

    public Cliente buscarClientePorId(Long id) {
        return clienteDAO.buscarClientePorId(id);
    }
}

