package strategy;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class PrevIndexSearch implements SearchStrategy {

    private final Map<File, List<String>> indexP = new HashMap<>();
    private final Map<File, List<String>> indexG = new HashMap<>();
    private boolean indexadoP = false;
    private boolean indexadoG = false;
    private int threadsUsadas = 0;

    @Override
    public void search(File directory, String targetName, JTextArea outputArea) {
        int threadsAntes = Thread.activeCount();

        String path = directory.getAbsolutePath();
        Map<File, List<String>> indexParaBuscar = null;

        if (path.contains("Dataset_P")) {
            if (!indexadoP) {
                outputArea.append("📚 Indexando arquivos de Dataset_P...\n");
                indexarArquivos(directory, indexP);
                indexadoP = true;
                outputArea.append("✅ Indexação de Dataset_P concluída.\n");
            }
            indexParaBuscar = indexP;
        } else if (path.contains("Dataset_G")) {
            if (!indexadoG) {
                outputArea.append("📚 Indexando arquivos de Dataset_G...\n");
                indexarArquivos(directory, indexG);
                indexadoG = true;
                outputArea.append("✅ Indexação de Dataset_G concluída.\n");
            }
            indexParaBuscar = indexG;
        }

        if (indexParaBuscar == null) {
            outputArea.append("❌ Diretório não reconhecido para indexação.\n");
            return;
        }

        outputArea.append("🔎 Buscando pelo nome: " + targetName + "\n");

        boolean encontrado = false;
        for (Map.Entry<File, List<String>> entry : indexParaBuscar.entrySet()) {
            File file = entry.getKey();
            List<String> linhas = entry.getValue();

            for (int i = 0; i < linhas.size(); i++) {
                String linha = linhas.get(i);
                if (linha.contains(targetName)) {
                    outputArea.append("✔️ Encontrado em: " + file.getName() + " (linha " + (i + 1) + ")\n");
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            outputArea.append("❌ Nome não encontrado nos arquivos.\n");
        }

        int threadsDepois = Thread.activeCount();
        threadsUsadas = threadsDepois - threadsAntes;
    }

    private void indexarArquivos(File directory, Map<File, List<String>> index) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        List<String> linhas = new ArrayList<>();
                        String linha;
                        while ((linha = reader.readLine()) != null) {
                            linhas.add(linha);
                        }
                        index.put(file, linhas);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int getThreadCount() {
        return threadsUsadas > 0 ? threadsUsadas : 1;
    }

    @Override
    public String toString() {
        return "Busca com Indexação Prévia";
    }
}
