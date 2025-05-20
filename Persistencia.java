
import java.io.*;
import java.util.*;

public class Persistencia {

    private static final String ARQUIVO_JOGO = "jogo_salvo.dat";
    private static final String ARQUIVO_RANKING = "ranking.txt";

    // Salva o estado atual do jogo
    public static void salvarEstado(Jogo jogo) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_JOGO))) {
            out.writeObject(jogo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    // Carrega o estado salvo do jogo
    public static Jogo carregarEstado() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_JOGO))) {
            return (Jogo) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    // Registra a pontuação no ranking
    public static void registrarPontuacao(String nome, int pontos) {
        try (FileWriter fw = new FileWriter(ARQUIVO_RANKING, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(nome + ":" + pontos);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao registrar pontuação: " + e.getMessage());
        }
    }

    // Mostra o ranking ordenado
    public static void mostrarRanking() {
        File arquivo = new File(ARQUIVO_RANKING);
        if (!arquivo.exists()) {
            System.out.println("Nenhuma pontuação registrada ainda.");
            return;
        }

        Map<String, Integer> ranking = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_RANKING))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(":");
                if (partes.length == 2) {
                    String nome = partes[0];
                    int pontos = Integer.parseInt(partes[1]);
                    ranking.put(nome, ranking.getOrDefault(nome, 0) + pontos);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ranking: " + e.getMessage());
            return;
        }

        System.out.println("\n=== RANKING ===\n");
        ranking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " pontos"));
    }
}
