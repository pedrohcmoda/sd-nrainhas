import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RainhaThreads {

    // Verifica se é seguro colocar uma Rainha na posição tabuleiro[linha][coluna]
    private static boolean seguro(int[][] tabuleiro, int linha, int coluna) {
        // Checa a linha à esquerda
        for (int i = 0; i < coluna; i++) {
            if (tabuleiro[linha][i] == 1) {
                return false;
            }
        }

        // Checa a diagonal superior esquerda
        for (int i = linha, j = coluna; i >= 0 && j >= 0; i--, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        // Checa a diagonal inferior esquerda
        for (int i = linha, j = coluna; i < tabuleiro.length && j >= 0; i++, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Função para encontrar todas as soluções de maneira recursiva --> Utilizando técnica de backtracking
    private static void resolverRainhas(int[][] tabuleiro, int coluna, List<String> solucoes) {
        if (coluna >= tabuleiro.length) { // Checa se com sucesso foi completo o tabuleiro, se foi, entra no "IF"
            synchronized (solucoes) {
                //Converte o tabuleiro em uma string da posição das rainhas e adiciona ao Vetor "solucoes" criado na função "resolverProblemaRainhas"
                solucoes.add(converterTabuleiroParaPosicoes(tabuleiro));
            }
            return;
        }

        for (int i = 0; i < tabuleiro.length; i++) {
            if (seguro(tabuleiro, i, coluna)) {
                tabuleiro[i][coluna] = 1;
                resolverRainhas(tabuleiro, coluna + 1, solucoes);
                tabuleiro[i][coluna] = 0; // Remove a rainha da posição definida após voltar a função recursiva
            }
        }
    }

    // Converte o tabuleiro para uma string de posições
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
        if (sb.length() > 0) { //Apenas remove a "," e o " " que foram adicionados ao final da ultima posição
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    // Função principal para começar a resolução
    public static void resolverProblemaRainhas(int n) {
        List<String> solucoes = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(n); // Cria um pool de threads com um número de threads igual ao tamanho do tabuleiro(n)

        long tempoInicio = System.currentTimeMillis(); // Início da medição de tempo

        /* 
         * Cada thread irá começar com a rainha em uma linha diferente da primeira coluna.
         * Sendo assim, cada thread fica responsavel por um galho da arvore de possiveis soluções
        */
        for (int i = 0; i < n; i++) {
            int linhaInicial = i;
            executor.execute(() -> {
                int[][] tabuleiro = new int[n][n];
                tabuleiro[linhaInicial][0] = 1;
                resolverRainhas(tabuleiro, 1, solucoes);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long tempoFim = System.currentTimeMillis(); // Fim da medição de tempo

        if (!solucoes.isEmpty()) {
            for (String solucao : solucoes) {
                System.out.println(solucao);
            }
        } else {
            System.out.println("Não há solução para " + n + " rainhas.");
        }

        System.out.printf("Tempo de execução (Paralelo): %.4f segundos\n", (tempoFim - tempoInicio) / 1000.0);
        System.out.println("Foram encontradas " + solucoes.size() + " soluções para " + n + " rainhas.");
    }

    public static void main(String[] args) {
        resolverProblemaRainhas(16);
    }
}