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
import javax.swing.JFileChooser;
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
    private final JTextField datasetPField;
    private final JTextField datasetGField;
    private final List<ResultadoBusca> resultados = new ArrayList<>();

    public BuscaUI(List<SearchStrategy> estrategias) {
        frame = new JFrame("🔎 Buscar Nomes");
        nomeField = new JTextField(20);
        outputArea = new JTextArea(15, 50);
        estrategiaComboBox = new JComboBox<>();
        String[] datasetOptions = {"Dataset_P", "Dataset_G", "Ambos"};
        datasetComboBox = new JComboBox<>(datasetOptions);

        datasetPField = new JTextField("C://Users/gabst/Downloads/dataset_p", 20);
        datasetGField = new JTextField("C://Users/gabst/Downloads/dataset_g", 20);

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
        
        gbc.gridy = y;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Caminho Dataset_P:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(datasetPField, gbc);

        JButton selecionarPButton = new JButton("Selecionar Pasta");
        gbc.gridx = 2;
        inputPanel.add(selecionarPButton, gbc);

        selecionarPButton.addActionListener(e -> escolherPasta(datasetPField));
        
        y++;

        // Dataset_G path
        gbc.gridy = y;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Caminho Dataset_G:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(datasetGField, gbc);

        JButton selecionarGButton = new JButton("Selecionar Pasta");
        gbc.gridx = 2;
        inputPanel.add(selecionarGButton, gbc);

        selecionarGButton.addActionListener(e -> escolherPasta(datasetGField));
        
        y--;
        
        // Nome a buscar
        gbc.gridx = 3;
        gbc.gridy = y;
        inputPanel.add(new JLabel("Nome a buscar:"), gbc);

        gbc.gridx = 4;
        inputPanel.add(nomeField, gbc);
        
        y++;

        // Dataset escolha
        gbc.gridx = 3;
        gbc.gridy = y;
        inputPanel.add(new JLabel("Dataset:"), gbc);

        gbc.gridx = 4;
        gbc.gridy = y;
        inputPanel.add(datasetComboBox, gbc);

        y++;

        // Algoritmo
        gbc.gridy = y;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Algoritmo:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(estrategiaComboBox, gbc);

        // Botões
        JButton buscarButton = new JButton("🔍 Buscar");
        JButton compararButton = new JButton("📊 Comparar");
        JButton speedupButton = new JButton("⚡ Medir SpeedUp");
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(buscarButton, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(compararButton, gbc);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(speedupButton, gbc);

        speedupButton.addActionListener(e -> abrirTelaSpeedUp());

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

    private void escolherPasta(JTextField targetField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int resultado = chooser.showOpenDialog(frame);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            targetField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void executarBusca() {
        outputArea.setText("");
        String nomesInput = nomeField.getText().trim();

        if (nomesInput.isEmpty()) {
            outputArea.append("⚠️ Por favor, insira um nome para buscar.\n");
            return;
        }

        String[] nomesParaBuscar = nomesInput.split(";");
        for (int i = 0; i < nomesParaBuscar.length; i++) {
            nomesParaBuscar[i] = nomesParaBuscar[i].trim();
        }

        String selectedDataset = (String) datasetComboBox.getSelectedItem();
        List<File> selectedDirectories = new ArrayList<>();

        if (selectedDataset.equals("Dataset_P") || selectedDataset.equals("Ambos")) {
            File datasetP = new File(datasetPField.getText());
            if (datasetP.exists() && datasetP.isDirectory()) {
                selectedDirectories.add(datasetP);
            } else {
                outputArea.append("❌ Pasta 'Dataset_P' não encontrada no caminho informado!\n");
            }
        }
        if (selectedDataset.equals("Dataset_G") || selectedDataset.equals("Ambos")) {
            File datasetG = new File(datasetGField.getText());
            if (datasetG.exists() && datasetG.isDirectory()) {
                selectedDirectories.add(datasetG);
            } else {
                outputArea.append("❌ Pasta 'Dataset_G' não encontrada no caminho informado!\n");
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

        int totalThreadsUsadas = 0;
        long inicioGeral = System.currentTimeMillis();

        for (String nome : nomesParaBuscar) {
            if (nome.isEmpty()) continue;

            outputArea.append("\n🔎 Buscando pelo nome: '" + nome + "'\n");

            long inicio = System.currentTimeMillis();
            int threadsUsadas = 0;

            for (File dir : selectedDirectories) {
                estrategiaSelecionada.search(dir, nome, outputArea);
                threadsUsadas += estrategiaSelecionada.getThreadCount();
            }

            long fim = System.currentTimeMillis();
            long tempoExecucao = fim - inicio;

            int ordemExecucao = (int) resultados.stream()
                    .filter(r -> r.getNomeBuscado().equals(nome) && r.getEstrategiaNome().equals(estrategiaSelecionada.toString()))
                    .count() + 1;

            outputArea.append("Tempo de execução: " + tempoExecucao + " ms\n");
            outputArea.append("Threads utilizadas para '" + nome + "': " + threadsUsadas + "\n");

            resultados.add(new ResultadoBusca(nome, estrategiaSelecionada.toString(), tempoExecucao, threadsUsadas, ordemExecucao));

            totalThreadsUsadas += threadsUsadas;
        }

        long fimGeral = System.currentTimeMillis();
        outputArea.append("\nBusca concluída para todos os nomes!\n");
        outputArea.append("Tempo total da aplicação: " + (fimGeral - inicioGeral) + " ms\n");
        outputArea.append("Threads totais utilizadas: " + totalThreadsUsadas + "\n");
    }

    private void mostrarComparacao() {
        if (resultados.isEmpty()) {
            outputArea.append("⚠️ Nenhuma pesquisa foi realizada ainda.\n");
            return;
        }

        Map<String, List<ResultadoBusca>> agrupados = new HashMap<>();
        for (ResultadoBusca r : resultados) {
            String chave = r.getOrdemExecucao() + "-" + r.getEstrategiaNome();
            agrupados.computeIfAbsent(chave, k -> new ArrayList<>()).add(r);
        }

        outputArea.setText("🔎 Comparação de pesquisas:\n");

        for (Map.Entry<String, List<ResultadoBusca>> entry : agrupados.entrySet()) {
            List<ResultadoBusca> buscas = entry.getValue();
            if (buscas.isEmpty()) continue;

            ResultadoBusca referencia = buscas.get(0);

            outputArea.append("\n🔸 Estratégia: " + referencia.getEstrategiaNome() + " (Execução " + referencia.getOrdemExecucao() + ")\n");

            long somaTempos = 0;
            for (ResultadoBusca r : buscas) {
                outputArea.append("- Nome: " + r.getNomeBuscado() + "\n");
                outputArea.append("Tempo: " + r.getTempoExecucaoMs() + " ms\n");
                outputArea.append("Threads: " + r.getThreadsUsadas() + "\n");
                somaTempos += r.getTempoExecucaoMs();
            }

            outputArea.append("Soma dos tempos: " + somaTempos + " ms\n");
        }
    }

    private void abrirTelaSpeedUp() {
        new SpeedUpUI(resultados);
    }
}
