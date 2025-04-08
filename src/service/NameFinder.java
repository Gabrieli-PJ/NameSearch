package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NameFinder {
    
    // Método que busca um nome em um arquivo e retorna o número da linha onde foi encontrado.
    // Retorna -1 se o nome não for encontrado.
    public static int buscarNomeEmArquivo(File arquivo, String nome) {
        int numeroLinha = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                // Verifica se a linha contém o nome procurado
                if (linha.contains(nome)) {
                    // Retorna o número da linha onde o nome foi encontrado.
                    return numeroLinha;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Se o nome não for encontrado, retorna -1
        return -1;
    }
}
