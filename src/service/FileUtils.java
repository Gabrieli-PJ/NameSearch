package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<File> listarArquivosTxt(File pastaRaiz) {
        List<File> arquivosTxt = new ArrayList<>();
        File[] arquivos = pastaRaiz.listFiles();

        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.isDirectory()) {
                    arquivosTxt.addAll(listarArquivosTxt(arquivo));
                } else if (arquivo.getName().toLowerCase().endsWith(".txt")) {
                    arquivosTxt.add(arquivo);
                }
            }
        }

        return arquivosTxt;
    }
}
