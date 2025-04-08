package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class NameFinder {
    
    private static final Logger LOGGER = Logger.getLogger(NameFinder.class.getName());

    public static int buscarNomeEmArquivo(File arquivo, String nome) {
        int numeroLinha = 0;
        LOGGER.info("Buscando o nome '" + nome + "' no arquivo: " + arquivo.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                LOGGER.finer("Linha " + numeroLinha + ": " + linha);
                if (linha.contains(nome)) {
                    LOGGER.info("Nome '" + nome + "' encontrado na linha " + numeroLinha + " do arquivo " + arquivo.getName());
                    return numeroLinha;
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Erro ao ler o arquivo " + arquivo.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        LOGGER.info("Nome '" + nome + "' não encontrado no arquivo " + arquivo.getName());
        return -1;
    }
}
