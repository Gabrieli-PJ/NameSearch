package ui;

import strategy.SearchStrategy;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BuscaUI {

    private final JFrame frame;
    private final JTextField nomeField;
    private final JTextArea outputArea;
    private final JComboBox<SearchStrategy> estrategiaComboBox;
    private final JComboBox<String> datasetComboBox;

    public BuscaUI(List<SearchStrategy> estrategias) {
        frame = new JFrame("Buscador de Nomes");
        nomeField = new JTextField(20);
        outputArea = new JTextArea(15, 40);
        estrategiaComboBox = new JComboBox<>();
        String[] datasetOptions = {"Dataset_P", "Dataset_G", "Ambos"};
        datasetComboBox = new JComboBox<>(datasetOptions);
        
        for (SearchStrategy estrategia : estrategias) {
            estrategiaComboBox.addItem(estrategia);
        }

        setupUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();

        JButton buscarButton = new JButton("Buscar");

        inputPanel.add(new JLabel("Nome a buscar:"));
        inputPanel.add(nomeField);
        inputPanel.add(new JLabel("Algoritmo:"));
        inputPanel.add(estrategiaComboBox);
        inputPanel.add(new JLabel("Dataset:"));
        inputPanel.add(datasetComboBox);
        inputPanel.add(buscarButton);

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(panel);

        buscarButton.addActionListener(e -> executarBusca());
    }

    private void executarBusca() {
        outputArea.setText("");
        String targetName = nomeField.getText().trim();

        if (targetName.isEmpty()) {
            outputArea.append("⚠️ Por favor, insira um nome para buscar.\n");
            return;
        }

        String selectedDataset = (String) datasetComboBox.getSelectedItem();
        List<File> selectedDirectories = new ArrayList<>();

        if (selectedDataset.equals("Dataset_P") || selectedDataset.equals("Ambos")) {
            File datasetP = new File("Data/Dataset_P");
            if (datasetP.exists() && datasetP.isDirectory()) {
                selectedDirectories.add(datasetP);
            } else {
                outputArea.append("❌ Pasta 'Data/Dataset_P' não encontrada!\n");
            }
        }
        if (selectedDataset.equals("Dataset_G") || selectedDataset.equals("Ambos")) {
            File datasetG = new File("Data/Dataset_G");
            if (datasetG.exists() && datasetG.isDirectory()) {
                selectedDirectories.add(datasetG);
            } else {
                outputArea.append("❌ Pasta 'Data/Dataset_G' não encontrada!\n");
            }
        }

        if (selectedDirectories.isEmpty()) {
            outputArea.append("❌ Nenhum dataset válido selecionado.\n");
            return;
        }

        SearchStrategy estrategiaSelecionada = (SearchStrategy) estrategiaComboBox.getSelectedItem();
        if (estrategiaSelecionada == null) {
            outputArea.append("⚠️ Por favor, selecione uma estratégia de busca.\n");
            return;
        }

        outputArea.append("🔍 Usando a estratégia: " + estrategiaSelecionada.toString() + "\n");

        for (File dir : selectedDirectories) {
            outputArea.append("🔸 Buscando no diretório: " + dir.getAbsolutePath() + "\n");
            estrategiaSelecionada.search(dir, targetName, outputArea);
        }
    }
}
