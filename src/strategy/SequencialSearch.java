package strategy;

import service.FileSearcher;
import javax.swing.*;
import java.io.File;

public class SequencialSearch implements SearchStrategy {

    private int threadCount = 1;

    @Override
    public void search(File directory, String targetName, JTextArea logArea) {
        File[] files = directory.listFiles();
        if (files == null) return;

        logArea.append("[INFO] Iniciando busca sequencial na pasta: " + directory.getName() + "...\n");

        boolean found = false;

        for (File file : files) {
            if (file.isFile()) {
                boolean result = FileSearcher.searchFile(file, targetName, logArea);
                if (result) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            logArea.append("[INFO] Nome não encontrado na pasta: " + directory.getName() + "\n\n");
        } else {
            logArea.append("[INFO] Busca sequencial encerrada com sucesso.\n\n");
        }
    }

    @Override
    public int getThreadCount() {
        return threadCount;
    }

    @Override
    public String toString() {
        return "Busca Sequencial";
    }
}
