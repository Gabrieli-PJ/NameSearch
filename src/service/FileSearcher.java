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
                    logArea.append("[✔️ ENCONTRADO] '" + targetName + "' encontrado no arquivo: " + file.getName() + " (linha " + lineCount + ")\n");
                    return true;
                }
            }

            logArea.append("[🔍] '" + targetName + "' **não** encontrado no arquivo: " + file.getName() + "\n");
        } catch (IOException e) {
            logArea.append("[ERRO] Falha ao ler o arquivo: " + file.getName() + "\n");
        }

        return false;
    }
}
