package strategy;

import service.FileSearcher;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceSearch implements SearchStrategy {

    private int threadCount = 0;

    @Override
    public void search(File directory, String targetName, JTextArea logArea) {
        File[] files = directory.listFiles();
        if (files == null) return;

        logArea.append("[INFO] Iniciando busca com ExecutorService na pasta: " + directory.getName() + "...\n");

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Boolean>> results = new ArrayList<>();
        threadCount = 0;

        for (File file : files) {
            if (file.isFile()) {
                Callable<Boolean> task = () -> FileSearcher.searchFile(file, targetName, logArea);
                results.add(executor.submit(task));
                threadCount++;
            }
        }

        boolean found = false;
        try {
            for (Future<Boolean> result : results) {
                if (result.get()) {
                    found = true;
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            logArea.append("[ERRO] Falha na execução da tarefa.\n");
        } finally {
            executor.shutdownNow();
        }

        if (!found) {
            logArea.append("[INFO] Nome não encontrado na pasta: " + directory.getName() + "\n\n");
        } else {
            logArea.append("[INFO] Busca com ExecutorService encerrada com sucesso.\n\n");
        }
    }

    @Override
    public int getThreadCount() {
        return threadCount;
    }

    @Override
    public String toString() {
        return "Busca ExecutorService";
    }
}
