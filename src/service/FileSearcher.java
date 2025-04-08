package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileSearcher {
    
    private static final Logger LOGGER = Logger.getLogger(FileSearcher.class.getName());
    
    public static List<File> buscarArquivosTxt(File diretorio) {
        List<File> arquivosTxt = new ArrayList<>();
        if (diretorio != null && diretorio.isDirectory()) {
            LOGGER.info("Buscando arquivos em: " + diretorio.getAbsolutePath());
            File[] arquivos = diretorio.listFiles();
            if (arquivos != null) {
                for (File arq : arquivos) {
                    if (arq.isDirectory()) {
                        LOGGER.info("Entrando no diretório: " + arq.getAbsolutePath());
                        arquivosTxt.addAll(buscarArquivosTxt(arq));
                    } else if (arq.getName().toLowerCase().endsWith(".txt")) {
                        LOGGER.info("Arquivo .txt encontrado: " + arq.getName());
                        arquivosTxt.add(arq);
                    }
                }
            } else {
                LOGGER.warning("Nenhum arquivo encontrado em: " + diretorio.getAbsolutePath());
            }
        } else {
            LOGGER.severe("O caminho fornecido não é um diretório: " + diretorio);
        }
        return arquivosTxt;
    }
}
