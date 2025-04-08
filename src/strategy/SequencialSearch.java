package strategy;

import java.io.File;
import java.util.List;
import javax.swing.JTextArea;
import service.FileSearcher;
import service.NameFinder;

public class SequencialSearch implements SearchStrategy {

    @Override
    public void search(File directory, String targetName, JTextArea outputArea) {
        outputArea.append("Iniciando busca sequencial...\n");

        // Obtém a lista de arquivos .txt no diretório (incluindo subdiretórios)
        List<File> files = FileSearcher.buscarArquivosTxt(directory);
        outputArea.append("Arquivos encontrados: " + files.size() + "\n");

        if (files.isEmpty()) {
            outputArea.append("Nenhum arquivo .txt encontrado.\n");
            return;
        }

        // Para cada arquivo, busca o nome
        for (File file : files) {
            int lineNumber = NameFinder.buscarNomeEmArquivo(file, targetName);
            if (lineNumber != -1) {
                outputArea.append("Nome \"" + targetName + "\" encontrado no arquivo: "
                        + file.getName() + " na linha: " + lineNumber + "\n");
            } else {
                outputArea.append("Nome \"" + targetName + "\" não encontrado em: "
                        + file.getName() + "\n");
            }
        }

        outputArea.append("Busca sequencial finalizada.\n");
    }

    @Override
    public String toString() {
        // Esse nome será exibido no ComboBox da interface para seleção da estratégia
        return "Busca Sequencial";
    }
}
