
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean executando = true;

            while (executando) {
                System.out.println("\n ---------------------");
                System.out.println("\n=== BATALHA NAVAL ===");
                System.out.println("\n1. Iniciar Partida");
                System.out.println("2. Continuar Partida Anterior");
                System.out.println("3. Ranking");
                System.out.println("0. Sair");
                System.out.print("\nEscolha uma opção: ");
                String opcao = scanner.nextLine().trim();

                switch (opcao) {
                    case "1" -> {
                        System.out.print("\nDigite o nome do(a) jogador(a) 1: ");
                        String nome1 = scanner.nextLine().trim();
                        System.out.print("Digite o nome do(a) jogador(a) 2: ");
                        String nome2 = scanner.nextLine().trim();

                        System.out.println("\n --------------------------------");

                        Jogo novoJogo = new Jogo(nome1, nome2);
                        novoJogo.iniciarPartida();
                    }

                    case "2" -> {
                        Jogo jogoSalvo = Persistencia.carregarEstado();
                        if (jogoSalvo != null) {
                            if (jogoSalvo.estaFinalizado()) {
                                System.out.println("\nPartida finalizada com " + jogoSalvo.getVencedor() + " vencedor.");
                            } else {
                                jogoSalvo.continuarPartida();
                            }
                        } else {
                            System.out.println("\nNenhuma partida anterior encontrada.");
                        }
                    }

                    case "3" ->
                        Persistencia.mostrarRanking();

                    case "0" -> {
                        System.out.println("\nEncerrando o jogo. Até a próxima!");
                        executando = false;
                    }

                    default ->
                        System.out.println("\nOpção inválida. Tente novamente.");
                }
            }
        }
    }
}
