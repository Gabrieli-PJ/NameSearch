package ui;

import strategy.SearchStrategy;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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
        datasetComboBox = new JComboBox<>();

        // Preenche o ComboBox com as estratégias
        for (SearchStrategy estrategia : estrategias) {
            estrategiaComboBox.addItem(estrategia);
        }

        // Preenche o ComboBox para seleção de dataset
        // Opções: Apenas Dataset_P, Apenas Dataset_G, Ambos
        datasetComboBox.addItem("Dataset_P");
        datasetComboBox.addItem("Dataset_G");
        datasetComboBox.addItem("Ambos");

        setupUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();

        JButton buscarButton = new JButton("Buscar");

        // Adiciona os componentes de entrada: campo nome, escolha de estratégia e dataset
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
        outputArea.setText(""); // limpa logs

        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            outputArea.append("⚠️ Por favor, insira um nome para buscar.\n");
            return;
        }

        // Define o diretório com base na seleção de dataset
        String datasetSelecionado = (String) datasetComboBox.getSelectedItem();
        File pastaData;
        if ("Dataset_P".equals(datasetSelecionado)) {
            pastaData = new File("Data/Dataset_P");
        } else if ("Dataset_G".equals(datasetSelecionado)) {
            pastaData = new File("Data/Dataset_G");
        } else { // "Ambos"
            pastaData = new File("Data");
        }

        if (!pastaData.exists() || !pastaData.isDirectory()) {
            outputArea.append("❌ Pasta " + pastaData.getPath() + " não encontrada!\n");
            return;
        }

        // Seleciona a estratégia de busca escolhida
        SearchStrategy estrategiaSelecionada = (SearchStrategy) estrategiaComboBox.getSelectedItem();
        if (estrategiaSelecionada != null) {
            outputArea.append("🔍 Usando estratégia: " + estrategiaSelecionada.toString() + "\n");
            outputArea.append("📂 Dataset selecionado: " + datasetSelecionado + "\n");
            estrategiaSelecionada.search(pastaData, nome, outputArea);
        }
    }
}
