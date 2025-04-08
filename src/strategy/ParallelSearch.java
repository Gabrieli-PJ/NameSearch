package strategy;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import service.FileSearcher;
import service.NameFinder;

public class ParallelSearch implements SearchStrategy {

    @Override
    public void search(File directory, String targetName, JTextArea outputArea) {
        // Inicia a busca informando ao usuário pela interface.
        SwingUtilities.invokeLater(() -> outputArea.append("Iniciando busca paralela...\n"));

        // Obtém a lista de arquivos .txt do diretório (incluindo subdiretórios)
        List<File> files = FileSearcher.buscarArquivosTxt(directory);
        SwingUtilities.invokeLater(() -> outputArea.append("Arquivos encontrados: " + files.size() + "\n"));

        if (files.isEmpty()) {
            SwingUtilities.invokeLater(() -> outputArea.append("Nenhum arquivo .txt encontrado.\n"));
            return;
        }

        // Define um número de threads baseado no número de processadores disponíveis
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Para cada arquivo, submete uma tarefa que realiza a busca
        for (File file : files) {
            executor.submit(() -> {
                int lineNumber = NameFinder.buscarNomeEmArquivo(file, targetName);
                String result;
                if (lineNumber != -1) {
                    result = "Nome \"" + targetName + "\" encontrado no arquivo: " 
                           + file.getName() + " na linha: " + lineNumber + "\n";
                } else {
                    result = "Nome \"" + targetName + "\" não encontrado em: " + file.getName() + "\n";
                }
                // Atualiza a interface de forma segura usando SwingUtilities.invokeLater
                SwingUtilities.invokeLater(() -> outputArea.append(result));
            });
        }

        // Encerra o executor e espera a finalização das tarefas
        executor.shutdown();
        try {
            // Aguarda até 1 minuto para o término das tarefas
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                SwingUtilities.invokeLater(() -> outputArea.append("Tempo limite excedido na busca paralela.\n"));
            }
        } catch (InterruptedException e) {
            SwingUtilities.invokeLater(() -> outputArea.append("Busca paralela interrompida.\n"));
        }

        SwingUtilities.invokeLater(() -> outputArea.append("Busca paralela finalizada.\n"));
    }

    @Override
    public String toString() {
        return "Busca Paralela (Threads)";
    }
}
