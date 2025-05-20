
import java.io.Serializable;

public class Jogador implements Serializable {

    private final String nome;
    private int pontos;
    private int embarcacoesAbatidas;

    public Jogador(String nome) {
        this.nome = nome;
        this.pontos = 0;
        this.embarcacoesAbatidas = 0;
    }

    public void incrementarPontos() {
        this.pontos++;
    }

    public void incrementarAbates() {
        this.embarcacoesAbatidas++;
    }

    public int getPontos() {
        return pontos;
    }

    public int getEmbarcacoesAbatidas() {
        return embarcacoesAbatidas;
    }

    public String getNome() {
        return nome;
    }

    public void setEmbarcacoesAbatidas(int embarcacoesAbatidas) {
        this.embarcacoesAbatidas = embarcacoesAbatidas;
    }

}
