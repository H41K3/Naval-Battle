
import java.io.Serializable;
import java.util.*;

public class Mapa implements Serializable {

    private final char[][] visivel;
    private final char[][] controle;
    private final int tamanho = 15;
    private final List<Embarcacao.InstanciaEmbarcacao> embarcacoes;

    public Mapa() {
        visivel = new char[tamanho][tamanho];
        controle = new char[tamanho][tamanho];
        embarcacoes = new ArrayList<>();
        inicializarMapas();
    }

    private void inicializarMapas() {
        for (int i = 0; i < tamanho; i++) {
            Arrays.fill(visivel[i], 'O');
            Arrays.fill(controle[i], 'O');
        }
    }

    public List<Embarcacao.InstanciaEmbarcacao> getEmbarcacoes() {
        return embarcacoes;
    }

    public boolean podeColocarEmbarcacao(int x, int y, int dx, int dy, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            int nx = x + i * dx;
            int ny = y + i * dy;
            if (!valido(nx, ny) || controle[nx][ny] != 'O') {
                return false;
            }

            for (int xi = -1; xi <= 1; xi++) {
                for (int yi = -1; yi <= 1; yi++) {
                    int vx = nx + xi, vy = ny + yi;
                    if (valido(vx, vy) && controle[vx][vy] != 'O') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void colocarEmbarcacao(int x, int y, int dx, int dy, int tamanho, char simbolo) {
        List<int[]> posicoes = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) {
            int nx = x + i * dx;
            int ny = y + i * dy;
            controle[nx][ny] = simbolo;
            posicoes.add(new int[]{nx, ny});
        }
        embarcacoes.add(new Embarcacao.InstanciaEmbarcacao(simbolo, posicoes));
    }

    public boolean atacar(int x, int y) {
        char simbolo = controle[x][y];
        if (simbolo != 'O' && simbolo != 'X' && simbolo != 'Y') {
            revelarEmbarcacao(x, y, simbolo);
            return true;
        } else {
            visivel[x][y] = '-';
        }
        return false;
    }

    private void revelarEmbarcacao(int x, int y, char simbolo) {
        boolean[][] visitado = new boolean[tamanho][tamanho];
        Queue<int[]> fila = new LinkedList<>();
        fila.add(new int[]{x, y});
        visitado[x][y] = true;

        while (!fila.isEmpty()) {
            int[] atual = fila.poll();
            int cx = atual[0], cy = atual[1];
            visivel[cx][cy] = simbolo;

            int[][] direcoes = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
            };

            for (int[] d : direcoes) {
                int nx = cx + d[0], ny = cy + d[1];
                if (valido(nx, ny) && !visitado[nx][ny] && controle[nx][ny] == simbolo) {
                    fila.add(new int[]{nx, ny});
                    visitado[nx][ny] = true;
                }
            }
        }
    }

    public boolean foiAbatido(char simbolo) {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (controle[i][j] == simbolo && visivel[i][j] != simbolo) {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<Character, Integer> embarcacoesAbatidas() {
        Map<Character, Integer> quantidadePorTipo = new HashMap<>();
        for (Embarcacao.InstanciaEmbarcacao inst : embarcacoes) {
            if (inst.estaAbatida(this)) {
                char tipo = inst.getTipo();
                quantidadePorTipo.put(tipo, quantidadePorTipo.getOrDefault(tipo, 0) + 1);
            }
        }
        return quantidadePorTipo;
    }

    public void exibir() {
        System.out.print("   ");
        for (char c = 'A'; c < 'A' + tamanho; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int i = 0; i < tamanho; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < tamanho; j++) {
                System.out.print(visivel[i][j] + " ");
            }
            System.out.println();
        }
    }

    public char getSimboloNaPosicao(int x, int y) {
        return controle[x][y];
    }

    public char getSimboloVisivelNaPosicao(int x, int y) {
        return visivel[x][y];
    }

    private boolean valido(int x, int y) {
        return x >= 0 && x < tamanho && y >= 0 && y < tamanho;
    }
}
