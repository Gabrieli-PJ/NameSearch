package service;

import javax.swing.*;
import java.io.*;

public class FileSearcher {

    public static boolean searchFile(File file, String targetName, JTextArea logArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.trim().equalsIgnoreCase(targetName)) {
                    appendToLog(logArea, "[✔️ ENCONTRADO] '" + targetName + "' encontrado no arquivo: " + file.getName() + " (linha " + lineCount + ")\n");
                    return true;
                }
            }

            appendToLog(logArea, "[🔍] '" + targetName + "' **não** encontrado no arquivo: " + file.getName() + "\n");
        } catch (IOException e) {
            appendToLog(logArea, "[ERRO] Falha ao ler o arquivo: " + file.getName() + "\n");
        }

        return false;
    }

    private static void appendToLog(JTextArea logArea, String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message));
    }
}
