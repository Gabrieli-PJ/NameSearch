package app;

import ui.BuscaUI;
import ui.ColorFormatter;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new ColorFormatter());
        handler.setLevel(Level.INFO);
        rootLogger.addHandler(handler);
        rootLogger.setLevel(Level.INFO);

        LOGGER.info("Iniciando a aplicação de busca de nomes...");

        javax.swing.SwingUtilities.invokeLater(() -> {
            new BuscaUI();
            LOGGER.info("Interface gráfica criada com sucesso.");
        });
    }
}
