package service;

public class SearchStatus {
    private volatile boolean found = false;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
