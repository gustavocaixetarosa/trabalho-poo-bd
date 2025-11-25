package dev.gustavorosa;

import dev.gustavorosa.dao.ClienteDAO;
import dev.gustavorosa.dao.ContratoDAO;
import dev.gustavorosa.models.Cliente;
import dev.gustavorosa.models.Contrato;
import dev.gustavorosa.util.Menu;

import java.util.List;
import java.util.Scanner;

public class App
{
    private final static Scanner scan = new Scanner(System.in);

    public static void main( String[] args )
    {
        Menu.mensagemInicial();
        int opcaoEscolhida = Menu.mostrarMenu();
        switch (opcaoEscolhida) {
            case 1:
                lerInformacoes();
                break;
            case 2:
                editarInformacoes();
                break;
            case 3:
                adicionarInformacoes();
                break;
            case 4:
                excluirInformacoes();
                break;
            case 0:
                sair();
            default:
                System.out.println("Opcao nao encontrada. Tente novamente!");
                break;
        }
    }

    private static void sair() {
        System.exit(0);
    }

    private static void excluirInformacoes() {

    }

    private static void adicionarInformacoes() {

    }

    private static void editarInformacoes() {

    }

    private static void lerInformacoes() {
        int opcao = Menu.escolherTipo();
        switch (opcao) {
            case 1:
                lerInformacoesClientes();
                break;
            case 2:
                lerInformacoesContratos();
                break;
        }
    }

    private static void lerInformacoesContratos() {
        ContratoDAO contratoDAO = new ContratoDAO();
        //List<Contrato> todosContratos = contratoDAO.buscarContratos();
    }

    private static void lerInformacoesClientes() {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> todosClientes = clienteDAO.buscarClientes();
        Menu.imprimeTitulo("Todos clientes");
        System.out.println(todosClientes);
    }


}
