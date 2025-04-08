package app;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import strategy.ParallelSearch;
import strategy.SearchStrategy;
import strategy.SequencialSearch;
import ui.BuscaUI;
import ui.ColorFormatter;

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
        	List<SearchStrategy> estrategias = List.of(
        		    new SequencialSearch(),
        		    new ParallelSearch()
        		);
        		BuscaUI ui = new BuscaUI(estrategias);

            LOGGER.info("Interface gráfica criada com sucesso.");
        });
    }
}
