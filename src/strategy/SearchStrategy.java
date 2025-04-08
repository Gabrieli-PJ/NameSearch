package strategy;

import java.io.File;
import javax.swing.JTextArea;

public interface SearchStrategy {
    /**
     * Realiza a busca do nome em arquivos dentro do diretório especificado.
     *
     * @param directory O diretório onde os arquivos serão buscados.
     * @param targetName O nome que está sendo procurado.
     * @param outputArea A área de texto onde a estratégia pode exibir logs ou resultados.
     */
    void search(File directory, String targetName, JTextArea outputArea);
    
    /**
     * Retorna uma representação em string da estratégia,
     * para ser exibida no ComboBox de seleção.
     *
     * @return Nome da estratégia.
     */
    @Override
    String toString();
}
