package dev.gustavorosa;

import dev.gustavorosa.controller.ClienteController;
import dev.gustavorosa.controller.ContratoController;
import dev.gustavorosa.controller.PagamentoController;
import dev.gustavorosa.controller.RelatorioController;
import dev.gustavorosa.util.Menu;

import java.util.Scanner;

public class App {
    private static final Scanner scan = Menu.getScanner();
    private static final ClienteController clienteController = new ClienteController();
    private static final ContratoController contratoController = new ContratoController();
    private static final PagamentoController pagamentoController = new PagamentoController();
    private static final RelatorioController relatorioController = new RelatorioController();

    public static void main(String[] args) {
        Menu.mensagemInicial();

        boolean continuar = true;
        while (continuar) {
            try {
                int opcao = Menu.mostrarMenuPrincipal();
                switch (opcao) {
                    case 1:
                        clienteController.executar();
                        break;
                    case 2:
                        contratoController.executar();
                        break;
                    case 3:
                        pagamentoController.executar();
                        break;
                    case 4:
                        relatorioController.executar();
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
}
