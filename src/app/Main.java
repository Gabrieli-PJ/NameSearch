package app;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import strategy.ExecutorServiceSearch;
import strategy.ParallelSearch;
import strategy.PrevIndexSearch;
import strategy.SearchStrategy;
import strategy.SequencialSearch;
import ui.BuscaUI;
import ui.ColorFormatter;
import ui.Trabaio;



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
            try {
            	Trabaio.setup();
            } catch (Exception e) {
                LOGGER.severe("❌ Falha ao carregar o tema personalizado: " + e.getMessage());
            }
            
            List<SearchStrategy> estrategias = List.of(
                new SequencialSearch(),
                new ParallelSearch(),
                new ExecutorServiceSearch(),
                new PrevIndexSearch()
            );
            @SuppressWarnings("unused")
			BuscaUI ui = new BuscaUI(estrategias);

            LOGGER.info("Interface gráfica criada com sucesso.");
        });

    }
}
