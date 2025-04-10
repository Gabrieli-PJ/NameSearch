package strategy;

import service.FileSearcher;

import javax.swing.*;
import java.io.File;

public class SequencialSearch implements SearchStrategy {

    @Override
    public void search(File directory, String targetName, JTextArea logArea) {
        File[] files = directory.listFiles();
        if (files == null) return;

        logArea.append("[INFO] Iniciando busca sequencial...\n");

        boolean found = false;
        for (File file : files) {
            if (file.isFile()) {
                boolean foundInFile = FileSearcher.searchFile(file, targetName, logArea);
                if (foundInFile) {
                    logArea.append("[INFO] Nome encontrado. Interrompendo a busca nos demais arquivos dessa pasta.\n\n");
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            logArea.append("[INFO] Nome *não* encontrado na pasta: " + directory.getName() + "\n\n");
        }
    }

    @Override
    public String toString() {
        return "Busca Sequencial";
    }
}
