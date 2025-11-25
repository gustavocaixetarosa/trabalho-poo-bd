package dev.gustavorosa.util;

import java.util.Scanner;

public class Menu {
    private final static Scanner scan = new Scanner(System.in);

    public static int mostrarMenu() {
        imprimeTitulo("Funções");
        System.out.println("1. Ler informações");
        System.out.println("2. Editar informações");
        System.out.println("3. Adicionar informações");
        System.out.println("4. Excluir informações");
        System.out.println("0. Sair");
        return scan.nextInt();
    }

    public static void mensagemInicial() {
        imprimeTitulo("Gerenciador de Clientes e Pagamentos - feito por Gustavo Caixeta");
    }

    public static void imprimeTitulo(String titulo) {
        System.out.printf("***** %s *****\n", titulo);
    }

    public static int escolherTipo() {
        System.out.println("1. Cliente");
        System.out.println("2. Contrato");
        System.out.println("3. Pagamento");
        System.out.println("4. Relatorio");
        return scan.nextInt();
    }
}
