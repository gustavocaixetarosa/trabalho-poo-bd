package dev.gustavorosa.util;

import java.util.Scanner;

public class Menu {
    private final static Scanner scan = new Scanner(System.in);

    public static int mostrarMenuPrincipal() {
        System.out.println();
        imprimeTitulo("Menu Principal");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Contratos");
        System.out.println("3. Gerenciar Pagamentos");
        System.out.println("4. Relatorios");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opcao: ");
        return scan.nextInt();
    }

    public static int mostrarMenuCrud(String entidade) {
        System.out.println();
        imprimeTitulo("Gerenciar " + entidade);
        System.out.println("1. Listar todos");
        System.out.println("2. Buscar por ID");
        System.out.println("3. Adicionar novo");
        System.out.println("4. Atualizar");
        System.out.println("5. Excluir");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opcao: ");
        return scan.nextInt();
    }

    public static int mostrarMenuPagamentos() {
        System.out.println();
        imprimeTitulo("Gerenciar Pagamentos");
        System.out.println("1. Listar todos os pagamentos");
        System.out.println("2. Buscar pagamento por ID");
        System.out.println("3. Buscar pagamentos por contrato");
        System.out.println("4. Buscar pagamentos por situacao");
        System.out.println("5. Adicionar novo pagamento");
        System.out.println("6. Registrar pagamento (marcar como pago)");
        System.out.println("7. Atualizar pagamento");
        System.out.println("8. Excluir pagamento");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opcao: ");
        return scan.nextInt();
    }

    public static int mostrarMenuRelatorios() {
        System.out.println();
        imprimeTitulo("Relatorios");
        System.out.println("1. Listar contratos de um cliente");
        System.out.println("2. Listar pagamentos em aberto");
        System.out.println("3. Listar pagamentos pagos");
        System.out.println("4. Listar pagamentos atrasados");
        System.out.println("5. Gerar/Visualizar relatorio mensal de cliente");
        System.out.println("6. Listar todos os relatorios mensais");
        System.out.println("7. Calcular/Visualizar meus ganhos mensais");
        System.out.println("8. Listar todos os ganhos mensais");
        System.out.println("9. Resumo consolidado de clientes (VIEW)");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opcao: ");
        return scan.nextInt();
    }

    public static void mensagemInicial() {
        System.out.println();
        imprimeTitulo("Sistema de Gerenciamento de Cobrancas");
        System.out.println("Desenvolvido por Gustavo Caixeta");
        System.out.println();
    }

    public static void imprimeTitulo(String titulo) {
        System.out.println("========================================");
        System.out.printf("  %s%n", titulo);
        System.out.println("========================================");
    }

    public static void imprimeSeparador() {
        System.out.println("----------------------------------------");
    }

    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        scan.nextLine();
        return scan.nextLine();
    }

    public static String lerStringDireto(String mensagem) {
        System.out.print(mensagem);
        return scan.nextLine();
    }

    public static Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return scan.nextLong();
    }

    public static int lerInt(String mensagem) {
        System.out.print(mensagem);
        return scan.nextInt();
    }

    public static double lerDouble(String mensagem) {
        System.out.print(mensagem);
        return scan.nextDouble();
    }

    public static void mensagemSucesso(String mensagem) {
        System.out.println("[SUCESSO] " + mensagem);
    }

    public static void mensagemErro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }

    public static void mensagemInfo(String mensagem) {
        System.out.println("[INFO] " + mensagem);
    }

    public static void pressioneEnterParaContinuar() {
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scan.nextLine();
        scan.nextLine();
    }

    public static Scanner getScanner() {
        return scan;
    }
}
