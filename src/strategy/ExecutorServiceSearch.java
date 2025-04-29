package strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTextArea;

import service.FileSearcher;

public class ExecutorServiceSearch implements SearchStrategy {

	private AtomicInteger threadCount = new AtomicInteger(0);


    @Override
    public void search(File directory, String targetName, JTextArea logArea) {
        File[] files = directory.listFiles();
        if (files == null) return;
        threadCount.set(0);

        logArea.append("[INFO] Iniciando busca com ExecutorService na pasta: " + directory.getName() + "...\n");

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int proc = Runtime.getRuntime().availableProcessors();
        
        logArea.append("a: " + proc);
        List<Future<Boolean>> results = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) {
                Callable<Boolean> task = () -> FileSearcher.searchFile(file, targetName, logArea);
                results.add(executor.submit(task));
                threadCount.incrementAndGet();
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
        return threadCount.get();
    }

    @Override
    public String toString() {
        return "Busca ExecutorService";
    }
}
