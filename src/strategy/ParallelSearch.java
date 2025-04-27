package strategy;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTextArea;

import service.FileSearcher;

public class ParallelSearch implements SearchStrategy {

	 private AtomicInteger threadCount = new AtomicInteger(0);

	 @Override
	 public void search(File directory, String targetName, JTextArea logArea) {
	     File[] files = directory.listFiles();
	     if (files == null) return;
	     threadCount.set(0);
	     logArea.append("[INFO] Iniciando busca paralela na pasta: " + directory.getName() + "...\n");

	     AtomicBoolean found = new AtomicBoolean(false);
	     Thread[] threads = new Thread[files.length];
	     

	     for (int i = 0; i < files.length; i++) {
	    	    File file = files[i];
	    	    if (file.isFile()) {
	    	        threads[i] = new Thread(() -> {
	    	            threadCount.incrementAndGet();
	    	            if (found.get()) {
	    	                return;
	    	            }
	    	            boolean result = FileSearcher.searchFile(file, targetName, logArea);
	    	            if (result) {
	    	                found.set(true);
	    	            }
	    	        }, "Thread-" + i);
	    	        threads[i].start();
	    	    }
	    	}

	     for (Thread thread : threads) {
	         if (thread != null) {
	             try {
	                 thread.join();
	             } catch (InterruptedException e) {
	                 logArea.append("[ERRO] Falha ao esperar thread finalizar.\n");
	             }
	         }
	     }

	     if (!found.get()) {
	         logArea.append("[INFO] Nome não encontrado na pasta: " + directory.getName() + "\n\n");
	     } else {
	         logArea.append("[INFO] Busca paralela encerrada com sucesso.\n\n");
	     }
	 }

    @Override
    public int getThreadCount() {
        return threadCount.get();
    }

    @Override
    public String toString() {
        return "Busca Paralela (Threads)";
    }
}
