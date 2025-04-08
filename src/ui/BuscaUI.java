package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import service.FileSearcher;
import service.NameFinder;

public class BuscaUI extends JFrame {

    // Componentes de interface
    private JTextField txtNome;
    private JButton btnBuscar;
    private JTextArea txtAreaResultados;
    
    private final File diretorioData = new File("Data/Dataset_P");

    public BuscaUI() {
        super("Busca de Nomes em Arquivos");
        inicializarComponentes();
        configurarJanela();
    }

    private void inicializarComponentes() {
        txtNome = new JTextField(20);
        btnBuscar = new JButton("Buscar Nome");
        txtAreaResultados = new JTextArea(15, 40);
        txtAreaResultados.setEditable(false);
        
        JPanel painelInput = new JPanel();
        painelInput.add(new JLabel("Nome a buscar:"));
        painelInput.add(txtNome);
        painelInput.add(btnBuscar);

        this.setLayout(new BorderLayout());
        this.add(painelInput, BorderLayout.NORTH);
        this.add(new JScrollPane(txtAreaResultados), BorderLayout.CENTER);

        if(diretorioData.exists() && diretorioData.isDirectory()){
            txtAreaResultados.append("Diretório utilizado: " + diretorioData.getAbsolutePath() + "\n");
        } else {
            txtAreaResultados.append("Pasta 'Data' não encontrada!\n");
        }

        btnBuscar.addActionListener((ActionEvent e) -> {
            if (!diretorioData.exists() || !diretorioData.isDirectory()) {
                JOptionPane.showMessageDialog(this, "A pasta 'Data' não foi encontrada no diretório do projeto!");
                return;
            }
            String nomeProcurado = txtNome.getText().trim();
            if (nomeProcurado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o nome a buscar!");
                return;
            }
            
            txtAreaResultados.append("\nIniciando busca por: " + nomeProcurado + "\n");
            txtAreaResultados.append("------------------------------\n");

            List<File> listaArquivos = FileSearcher.buscarArquivosTxt(diretorioData);
            if(listaArquivos.isEmpty()){
                txtAreaResultados.append("Nenhum arquivo .txt encontrado na pasta 'Data'.\n");
                return;
            }
            
            for (File arquivo : listaArquivos) {
                int linhaEncontrada = NameFinder.buscarNomeEmArquivo(arquivo, nomeProcurado);
                if (linhaEncontrada != -1) {
                    txtAreaResultados.append("Nome encontrado no arquivo: " + arquivo.getName() + " na linha: " + linhaEncontrada + "\n");
                }
            }
            txtAreaResultados.append("Busca finalizada.\n");
        });
    }

    private void configurarJanela() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
