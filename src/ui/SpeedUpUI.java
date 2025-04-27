package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import service.ResultadoBusca;

public class SpeedUpUI {

    private final JFrame frame;
    private final JComboBox<String> estrategiaParalelaComboBox;
    private final JTextArea outputArea;
    private final List<ResultadoBusca> resultados;

    public SpeedUpUI(List<ResultadoBusca> resultados) {
        this.resultados = resultados;

        frame = new JFrame("⚡ Comparar SpeedUp");
        estrategiaParalelaComboBox = new JComboBox<>();
        outputArea = new JTextArea(15, 50);

        setupUI();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Escolha a estratégia paralela:"));

        Set<String> estrategiasDisponiveis = new HashSet<>();
        for (ResultadoBusca r : resultados) {
            if (!r.getEstrategiaNome().toLowerCase().contains("sequencial")) {
                estrategiasDisponiveis.add(r.getEstrategiaNome());
            }
        }

        for (String nome : estrategiasDisponiveis) {
            estrategiaParalelaComboBox.addItem(nome);
        }

        topPanel.add(estrategiaParalelaComboBox);

        JButton calcularButton = new JButton("📈 Calcular SpeedUp");
        topPanel.add(calcularButton);

        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados do SpeedUp"));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(panel);

        calcularButton.addActionListener(e -> calcularSpeedUp());
    }

    private void calcularSpeedUp() {
        outputArea.setText("");

        String estrategiaParalelaSelecionada = (String) estrategiaParalelaComboBox.getSelectedItem();
        if (estrategiaParalelaSelecionada == null) {
            outputArea.append("⚠️ Nenhuma estratégia paralela selecionada.\n");
            return;
        }

        // Agrupar por ordem de execução
        Map<Integer, Map<String, ResultadoBusca>> mapaResultados = new HashMap<>();
        for (ResultadoBusca r : resultados) {
            mapaResultados
                .computeIfAbsent(r.getOrdemExecucao(), k -> new HashMap<>())
                .put(r.getEstrategiaNome(), r);
        }

        boolean encontrouComparacoes = false;

        // 🔥 Ignorar a primeira busca (ordemExecucao == 1)
        List<Integer> ordens = new ArrayList<>(mapaResultados.keySet());
        Collections.sort(ordens); // garantir ordem correta

        for (int ordem : ordens) {
            if (ordem == 1) continue; // Pular primeira execução

            Map<String, ResultadoBusca> comparacao = mapaResultados.get(ordem);
            ResultadoBusca sequencial = null;
            ResultadoBusca paralelo = null;

            for (Map.Entry<String, ResultadoBusca> entry : comparacao.entrySet()) {
                String estrategiaNome = entry.getKey();
                ResultadoBusca resultado = entry.getValue();
                if (estrategiaNome.toLowerCase().contains("sequencial")) {
                    sequencial = resultado;
                } else if (estrategiaNome.equals(estrategiaParalelaSelecionada)) {
                    paralelo = resultado;
                }
            }

            if (sequencial != null && paralelo != null) {
                encontrouComparacoes = true;

                outputArea.append("🔸 Nome buscado: " + sequencial.getNomeBuscado() + "\n");
                outputArea.append("  ➡️ Sequencial: " + sequencial.getTempoExecucaoMs() + " ms\n");
                outputArea.append("  ➡️ " + paralelo.getEstrategiaNome() + ": " + paralelo.getTempoExecucaoMs() + " ms\n");

                double speedUp = (double) sequencial.getTempoExecucaoMs() / paralelo.getTempoExecucaoMs();
                outputArea.append("  ⚡ SpeedUp: " + String.format("%.2f", speedUp) + "x\n\n");
            }
        }

        if (!encontrouComparacoes) {
            outputArea.append("⚠️ Não foram encontrados resultados para calcular o SpeedUp.\n");
        }
    }

}
