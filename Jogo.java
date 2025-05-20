
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Jogo implements Serializable {

    private final Mapa mapa1;
    private final Mapa mapa2;
    private final Jogador jogador1;
    private final Jogador jogador2;
    private boolean finalizado = false;
    private transient Scanner scanner = new Scanner(System.in);

    public Jogo(String nome1, String nome2) {
        this.jogador1 = new Jogador(nome1);
        this.jogador2 = new Jogador(nome2);
        this.mapa1 = new Mapa();
        this.mapa2 = new Mapa();
        posicionarEmbarcacoes(mapa1);
        posicionarEmbarcacoes(mapa2);
    }

    public void iniciarPartida() {
        while (!finalizado) {
            if (!rodada(jogador1, mapa2)) {
                break;
            }
            if (!rodada(jogador2, mapa1)) {
                break;
            }
        }
    }

    public void continuarPartida() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        iniciarPartida();
    }

    private boolean rodada(Jogador jogador, Mapa mapaAlvo) {
        System.out.println("\n           Mapa de " + jogador.getNome());
        mapaAlvo.exibir();

        System.out.print("\n" + jogador.getNome() + ", digite sua jogada (ex: A5): ");
        String jogada = scanner.nextLine().toUpperCase().trim();

        if (!jogada.matches("[A-O][1-9]|[A-O]1[0-5]")) {
            System.out.println("\nJogada inválida!");
            return true;
        }

        int coluna = jogada.charAt(0) - 'A';
        int linha = Integer.parseInt(jogada.substring(1)) - 1;

        boolean acerto = mapaAlvo.atacar(linha, coluna);

        if (acerto) {
            jogador.incrementarPontos();
            System.out.println("\nAcertou! Embarcação atingida!");
        } else {
            System.out.println("\nErrou!");
        }

        System.out.println();

        mapaAlvo.exibir();

        // Atualizar contagem de embarcações abatidas
        Map<Character, Integer> abatidas = mapaAlvo.embarcacoesAbatidas();
        int totalAbatidas = abatidas.values().stream().mapToInt(Integer::intValue).sum();
        jogador.setEmbarcacoesAbatidas(totalAbatidas);

        System.out.println("\n" + jogador.getNome() + " abateu " + totalAbatidas + " embarcação(ões).");

        // Mostrar os tipos de embarcações abatidas
        Map<Character, String> nomes = new HashMap<>();
        nomes.put('A', "\nPorta-Aviões");
        nomes.put('D', "Destroyers");
        nomes.put('S', "Submarinos");
        nomes.put('F', "Fragatas");
        nomes.put('B', "Botes");

        for (char tipo : Arrays.asList('A', 'D', 'S', 'F', 'B')) {
            int qtd = abatidas.getOrDefault(tipo, 0);
            System.out.println(nomes.get(tipo) + ": " + qtd + ";");
        }

        System.out.println("\n --------------------------------");

        if (jogador.getEmbarcacoesAbatidas() >= 5) {
            System.out.println();
            System.out.println(jogador.getNome() + " venceu a partida!");
            finalizado = true;
            Persistencia.registrarPontuacao(jogador.getNome(), jogador.getEmbarcacoesAbatidas());
            Persistencia.salvarEstado(this);
            return false;
        }

        Persistencia.salvarEstado(this);
        return true;
    }

    private void posicionarEmbarcacoes(Mapa mapa) {
        Embarcacao[] embarcacoes = new Embarcacao[]{
            new Embarcacao.PortaAvioes(), new Embarcacao.PortaAvioes(),
            new Embarcacao.Destroyer(), new Embarcacao.Destroyer(), new Embarcacao.Destroyer(),
            new Embarcacao.Submarino(), new Embarcacao.Submarino(), new Embarcacao.Submarino(), new Embarcacao.Submarino(),
            new Embarcacao.Fragata(), new Embarcacao.Fragata(), new Embarcacao.Fragata(), new Embarcacao.Fragata(),
            new Embarcacao.Bote(), new Embarcacao.Bote(), new Embarcacao.Bote(),
            new Embarcacao.Bote(), new Embarcacao.Bote(), new Embarcacao.Bote()
        };

        for (Embarcacao e : embarcacoes) {
            e.posicionar(mapa);
        }
    }

    public boolean estaFinalizado() {
        return finalizado;
    }

    public String getVencedor() {
        if (jogador1.getEmbarcacoesAbatidas() >= 5) {
            return jogador1.getNome();
        }
        if (jogador2.getEmbarcacoesAbatidas() >= 5) {
            return jogador2.getNome();
        }
        return null;
    }
}
