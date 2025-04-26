package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import service.ResultadoBusca;
import strategy.SearchStrategy;

public class BuscaUI {

    private final JFrame frame;
    private final JTextField nomeField;
    private final JTextArea outputArea;
    private final JComboBox<SearchStrategy> estrategiaComboBox;
    private final JComboBox<String> datasetComboBox;
    private final List<ResultadoBusca> resultados = new ArrayList<>();

    public BuscaUI(List<SearchStrategy> estrategias) {

        frame = new JFrame("🔎 Buscar Nomes");
        nomeField = new JTextField(20);
        outputArea = new JTextArea(15, 50);
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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Nome a buscar
        gbc.gridx = 0;
        gbc.gridy = y;
        inputPanel.add(new JLabel("Nome a buscar:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(nomeField, gbc);
        
        // Dataset
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Dataset:"), gbc);

        gbc.gridx = 3;
        inputPanel.add(datasetComboBox, gbc);
        
        y++;
        // Algoritmo
        gbc.gridy = y;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Algoritmo:"), gbc);

        gbc.gridy = y;
        gbc.gridx = 1;
        inputPanel.add(estrategiaComboBox, gbc);

        // Botões
        JButton buscarButton = new JButton("🔍 Buscar");
        JButton compararButton = new JButton("📊 Comparar");


        gbc.gridy = y;
        gbc.gridx = 2;
        inputPanel.add(buscarButton, gbc);

        gbc.gridx = 3;
        inputPanel.add(compararButton, gbc);

        // Área de saída
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultado da Busca"));

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(panel);

        // Listeners
        buscarButton.addActionListener(e -> executarBusca());
        compararButton.addActionListener(e -> mostrarComparacao());
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

        int threadsAntes = Thread.activeCount();
        long inicio = System.currentTimeMillis();

        for (File dir : selectedDirectories) {
            outputArea.append("🔸 Buscando no diretório: " + dir.getAbsolutePath() + "\n");
            estrategiaSelecionada.search(dir, targetName, outputArea);
        }

        long fim = System.currentTimeMillis();
        int threadsDepois = Thread.activeCount();
        int threadsUsadas = threadsDepois - threadsAntes;
        if (threadsUsadas <= 0) threadsUsadas = 1;

        long tempoExecucao = fim - inicio;

        int ordemExecucao = (int) resultados.stream()
                .filter(r -> r.getNomeBuscado().equals(targetName) && r.getEstrategiaNome().equals(estrategiaSelecionada.toString()))
                .count() + 1;

        outputArea.append("⏱️ Tempo de execução: " + tempoExecucao + " ms\n");
        outputArea.append("🧵 Threads utilizadas: " + threadsUsadas + "\n");

        resultados.add(new ResultadoBusca(targetName, estrategiaSelecionada.toString(), tempoExecucao, threadsUsadas, ordemExecucao));
    }


    private void mostrarComparacao() {
        if (resultados.isEmpty()) {
            outputArea.append("⚠️ Nenhuma pesquisa foi realizada ainda.\n");
            return;
        }

        Map<String, List<ResultadoBusca>> agrupadosPorNome = new HashMap<>();
        for (ResultadoBusca r : resultados) {
            agrupadosPorNome.computeIfAbsent(r.getNomeBuscado(), k -> new ArrayList<>()).add(r);
        }

        outputArea.setText("🔎 Comparação de pesquisas:\n");

        for (Map.Entry<String, List<ResultadoBusca>> entry : agrupadosPorNome.entrySet()) {
            outputArea.append("\nNome pesquisado: " + entry.getKey() + "\n");
            for (ResultadoBusca r : entry.getValue()) {
                outputArea.append("\n- " + r.getEstrategiaNome() + " (Tempo " + r.getOrdemExecucao() + ")\n");
                outputArea.append("  ⏱️ Tempo: " + r.getTempoExecucaoMs() + " ms\n");
                outputArea.append("  🧵 Threads: " + r.getThreadsUsadas() + "\n");
            }
        }
    }

}
