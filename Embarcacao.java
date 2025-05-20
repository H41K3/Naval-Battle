
import java.io.Serializable;
import java.util.*;

public abstract class Embarcacao implements Serializable {

    protected int tamanho;
    protected char simbolo;

    public abstract void posicionar(Mapa mapa);

    protected boolean tentarPosicionar(Mapa mapa, int tentativas) {
        Random rand = new Random();
        int[][] direcoes = {{0, 1}, {1, 0}, {1, 1}, {-1, 1}}; // horizontal, vertical, diagonal, diagonal invertida

        for (int t = 0; t < tentativas; t++) {
            int x = rand.nextInt(15);
            int y = rand.nextInt(15);
            int[] dir = direcoes[rand.nextInt(direcoes.length)];
            int dx = dir[0];
            int dy = dir[1];

            if (mapa.podeColocarEmbarcacao(x, y, dx, dy, tamanho)) {
                mapa.colocarEmbarcacao(x, y, dx, dy, tamanho, simbolo);
                return true;
            }
        }
        return false;
    }

    // ---------- Subclasses Internas ----------
    public static class PortaAvioes extends Embarcacao {

        public PortaAvioes() {
            this.tamanho = 8;
            this.simbolo = 'A';
        }

        @Override
        public void posicionar(Mapa mapa) {
            tentarPosicionar(mapa, 1000);
        }
    }

    public static class Destroyer extends Embarcacao {

        public Destroyer() {
            this.tamanho = 5;
            this.simbolo = 'D';
        }

        @Override
        public void posicionar(Mapa mapa) {
            tentarPosicionar(mapa, 1000);
        }
    }

    public static class Submarino extends Embarcacao {

        public Submarino() {
            this.tamanho = 4;
            this.simbolo = 'S';
        }

        @Override
        public void posicionar(Mapa mapa) {
            tentarPosicionar(mapa, 1000);
        }
    }

    public static class Fragata extends Embarcacao {

        public Fragata() {
            this.tamanho = 3;
            this.simbolo = 'F';
        }

        @Override
        public void posicionar(Mapa mapa) {
            tentarPosicionar(mapa, 1000);
        }
    }

    public static class Bote extends Embarcacao {

        public Bote() {
            this.tamanho = 2;
            this.simbolo = 'B';
        }

        @Override
        public void posicionar(Mapa mapa) {
            tentarPosicionar(mapa, 1000);
        }
    }

    // ---------- Classe auxiliar para embarcações posicionadas ----------
    public static class InstanciaEmbarcacao implements Serializable {

        private final char tipo;
        private final List<int[]> posicoes;

        public InstanciaEmbarcacao(char tipo, List<int[]> posicoes) {
            this.tipo = tipo;
            this.posicoes = posicoes;
        }

        public char getTipo() {
            return tipo;
        }

        public boolean estaAbatida(Mapa mapa) {
            for (int[] pos : posicoes) {
                int x = pos[0], y = pos[1];
                if (mapa.getSimboloVisivelNaPosicao(x, y) != tipo) {
                    return false;
                }
            }
            return true;
        }
    }
}
