package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearcher {
    
    // Método recursivo para buscar todos os arquivos .txt dentro de um diretório e subdiretórios
    public static List<File> buscarArquivosTxt(File diretorio) {
        List<File> arquivosTxt = new ArrayList<>();
        if (diretorio != null && diretorio.isDirectory()) {
            File[] arquivos = diretorio.listFiles();
            if (arquivos != null) {
                for (File arq : arquivos) {
                    if (arq.isDirectory()) {
                        // Chamada recursiva para incluir arquivos dentro dos subdiretórios
                        arquivosTxt.addAll(buscarArquivosTxt(arq));
                    } else if (arq.getName().toLowerCase().endsWith(".txt")) {
                        arquivosTxt.add(arq);
                    }
                }
            }
        }
        return arquivosTxt;
    }
}
