package app;

import ui.BuscaUI;

public class Main {
    // Ponto de entrada da aplicação
    public static void main(String[] args) {
        // Cria a interface gráfica na thread de eventos do Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new BuscaUI();
        });
    }
}
