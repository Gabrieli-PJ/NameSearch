package strategy;

import service.FileSearcher;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelSearch implements SearchStrategy {

    @Override
    public void search(File directory, String targetName, JTextArea logArea) {
        File[] files = directory.listFiles();
        if (files == null) return;

        logArea.append("[INFO] Iniciando busca paralela na pasta: " + directory.getName() + "...\n");

        AtomicBoolean found = new AtomicBoolean(false);
        Thread[] threads = new Thread[files.length];

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                int finalI = i;
                threads[i] = new Thread(() -> {
                    if (found.get()) {
                        logArea.append("[THREAD " + finalI + "] Encerrada: nome já foi encontrado por outra thread.\n");
                        return;
                    }

                    boolean result = FileSearcher.searchFile(file, targetName, logArea);
                    if (result) {
                        found.set(true);
                        logArea.append("[THREAD " + finalI + "] Nome encontrado! Interrompendo outras buscas.\n");
                    } else {
                        logArea.append("[THREAD " + finalI + "] Finalizou sem encontrar o nome.\n");
                    }
                }, "Thread-" + i);
                threads[i].start();
            }
        }
        for (Thread thread : threads) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    logArea.append("[ERRO] Falha ao esperar thread finalizar.\n");
                }
            }
        }

        if (!found.get()) {
            logArea.append("[INFO] Nome não encontrado na pasta: " + directory.getName() + "\n\n");
        } else {
            logArea.append("[INFO] Busca paralela encerrada com sucesso.\n\n");
        }
    }

    @Override
    public String toString() {
        return "Busca Paralela (Threads)";
    }
}
