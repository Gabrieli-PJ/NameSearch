package service;

public class ResultadoBusca {
    private final String nomeBuscado;
    private final String estrategiaNome;
    private final long tempoExecucaoMs;
    private final int threadsUsadas;
    private final int ordemExecucao;

    public ResultadoBusca(String nomeBuscado, String estrategiaNome, long tempoExecucaoMs, int threadsUsadas, int ordemExecucao) {
        this.nomeBuscado = nomeBuscado;
        this.estrategiaNome = estrategiaNome;
        this.tempoExecucaoMs = tempoExecucaoMs;
        this.threadsUsadas = threadsUsadas;
        this.ordemExecucao = ordemExecucao;
    }

    public String getNomeBuscado() {
        return nomeBuscado;
    }

    public String getEstrategiaNome() {
        return estrategiaNome;
    }

    public long getTempoExecucaoMs() {
        return tempoExecucaoMs;
    }

    public int getThreadsUsadas() {
        return threadsUsadas;
    }

    public int getOrdemExecucao() {
        return ordemExecucao;
    }
}
