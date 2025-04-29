package strategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTextArea;

public class PrevIndexSearch implements SearchStrategy {

    private final Map<File, List<String>> indexP = new HashMap<>();
    private final Map<File, List<String>> indexG = new HashMap<>();
    private boolean indexadoP = false;
    private boolean indexadoG = false;
    private AtomicInteger threadCount = new AtomicInteger(0);

    @Override
    public void search(File directory, String targetName, JTextArea outputArea) {

        String path = directory.getAbsolutePath();
        Map<File, List<String>> indexParaBuscar = null;

        if (path.contains("dataset_p")) {
            if (!indexadoP) {
                outputArea.append("📚 Indexando arquivos de Dataset_P...\n");
                indexarArquivos(directory, indexP);
                indexadoP = true;
                outputArea.append("✅ Indexação de Dataset_P concluída.\n");
            }
            indexParaBuscar = indexP;
        } else if (path.contains("dataset_g")) {
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
    }

    private void indexarArquivos(File directory, Map<File, List<String>> index) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    threadCount.incrementAndGet();
                    
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
        return threadCount.get();
    }

    @Override
    public String toString() {
        return "Busca com Indexação Prévia";
    }
}
