// Atualizado: interface SearchStrategy
package strategy;

import java.io.File;
import javax.swing.JTextArea;

public interface SearchStrategy {
    void search(File directory, String targetName, JTextArea outputArea);
    int getThreadCount();
    @Override
    String toString();
}