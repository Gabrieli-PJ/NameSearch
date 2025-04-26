package service;

public class ThreadCounter {

    public static int countUserThreads() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        while (root.getParent() != null) {
            root = root.getParent();
        }

        int estimatedSize = root.activeCount() * 2;
        Thread[] threads = new Thread[estimatedSize];
        int actualSize = root.enumerate(threads, true);

        int count = 0;
        for (int i = 0; i < actualSize; i++) {
            if (threads[i] != null && threads[i].isAlive() && !threads[i].isDaemon()) {
                count++;
            }
        }

        return count;
    }
}
