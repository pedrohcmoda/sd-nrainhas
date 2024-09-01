import java.util.ArrayList;
import java.util.List;

public class RainhaSequencial {

    // Verifica se é seguro colocar uma rainha em tabuleiro[linha][coluna]
    private static boolean estaSeguro(int[][] tabuleiro, int linha, int coluna) {
        // Verifica a linha à esquerda
        for (int i = 0; i < coluna; i++) {
            if (tabuleiro[linha][i] == 1) {
                return false;
            }
        }

        // Verifica a diagonal superior esquerda
        for (int i = linha, j = coluna; i >= 0 && j >= 0; i--, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        // Verifica a diagonal inferior esquerda
        for (int i = linha, j = coluna; i < tabuleiro.length && j >= 0; i++, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Função recursiva para encontrar todas as soluções
    private static void resolverRainhas(int[][] tabuleiro, int coluna, List<String> solucoes) {
        if (coluna >= tabuleiro.length) {
            solucoes.add(converterTabuleiroParaPosicoes(tabuleiro));
            return;
        }

        for (int i = 0; i < tabuleiro.length; i++) {
            if (estaSeguro(tabuleiro, i, coluna)) {
                tabuleiro[i][coluna] = 1;
                resolverRainhas(tabuleiro, coluna + 1, solucoes);
                tabuleiro[i][coluna] = 0;
            }
        }
    }

    // Converte o tabuleiro para uma solução em formato de posições
    private static String converterTabuleiroParaPosicoes(int[][] tabuleiro) {
        StringBuilder sb = new StringBuilder();
        for (int coluna = 0; coluna < tabuleiro.length; coluna++) {
            for (int linha = 0; linha < tabuleiro.length; linha++) {
                if (tabuleiro[linha][coluna] == 1) {
                    char colunaChar = (char) ('a' + coluna); // Converte número da coluna para letra
                    sb.append("R").append(coluna + 1).append(" - ").append(colunaChar).append(linha + 1).append(", ");
                }
            }
        }
        // Remove a última vírgula e espaço
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    // Função principal para resolver o problema das N rainhas
    public static void resolverProblemaRainhas(int n) {
        int[][] tabuleiro = new int[n][n];
        List<String> solucoes = new ArrayList<>();

        long tempoInicio = System.currentTimeMillis(); // Início da medição de tempo

        resolverRainhas(tabuleiro, 0, solucoes);

        long tempoFim = System.currentTimeMillis(); // Fim da medição de tempo

        if (!solucoes.isEmpty()) {
            for (String solucao : solucoes) {
                System.out.println(solucao);
            }
        } else {
            System.out.println("Não há solução para " + n + " rainhas.");
        }

        System.out.printf("Tempo de execução (Sequencial): %.4f segundos\n", (tempoFim - tempoInicio) / 1000.0);
        System.out.println("Foram encontradas " + solucoes.size() + " soluções para " + n + " rainhas.");
    }

    // Testando com N = 15
    public static void main(String[] args) {
        resolverProblemaRainhas(16);
    }
}
