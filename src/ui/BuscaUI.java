package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import service.FileSearcher;
import service.NameFinder;

public class BuscaUI extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(BuscaUI.class.getName());
    
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

        if (diretorioData.exists() && diretorioData.isDirectory()){
            String msg = "Diretório utilizado: " + diretorioData.getAbsolutePath();
            txtAreaResultados.append(msg + "\n");
            LOGGER.info(msg);
        } else {
            String msg = "Pasta 'Data' não encontrada!";
            txtAreaResultados.append(msg + "\n");
            LOGGER.severe(msg);
        }

        btnBuscar.addActionListener((ActionEvent e) -> {
            LOGGER.info("Botão 'Buscar Nome' acionado.");
            if (!diretorioData.exists() || !diretorioData.isDirectory()) {
                JOptionPane.showMessageDialog(this, "A pasta 'Data' não foi encontrada no diretório do projeto!");
                LOGGER.severe("Diretório 'Data' não encontrado.");
                return;
            }
            String nomeProcurado = txtNome.getText().trim();
            if (nomeProcurado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o nome a buscar!");
                LOGGER.warning("Campo de busca vazio.");
                return;
            }
            
            txtAreaResultados.append("\nIniciando busca por: " + nomeProcurado + "\n");
            txtAreaResultados.append("------------------------------\n");
            LOGGER.info("Iniciando busca por: " + nomeProcurado);

            List<File> listaArquivos = FileSearcher.buscarArquivosTxt(diretorioData);
            LOGGER.info("Total de arquivos .txt encontrados: " + listaArquivos.size());
            if (listaArquivos.isEmpty()){
                txtAreaResultados.append("Nenhum arquivo .txt encontrado na pasta 'Data'.\n");
                LOGGER.warning("Nenhum arquivo .txt encontrado.");
                return;
            }

            for (File arquivo : listaArquivos) {
                int linhaEncontrada = NameFinder.buscarNomeEmArquivo(arquivo, nomeProcurado);
                if (linhaEncontrada != -1) {
                    String resultado = "Nome encontrado no arquivo: " + arquivo.getName() + " na linha: " + linhaEncontrada;
                    txtAreaResultados.append(resultado + "\n");
                    LOGGER.info(resultado);
                } else {
                    LOGGER.fine("Nome não encontrado em: " + arquivo.getName());
                }
            }
            txtAreaResultados.append("Busca finalizada.\n");
            LOGGER.info("Busca finalizada.");
        });
    }

    private void configurarJanela() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
