# Trabalho

Este é um trabalho da matéria de Computação Paralela e Distribuida.

# Diretório do trabalho

📦 src/  
 ├── 📁 app/  
 │ └── Main.java → Classe principal que inicia o app  
 │  
 ├── 📁 service/ → classes utilitárias   
 │ ├── FileSearcher.java → Classe auxiliar para procurar os arquivos
 │ ├── ResultadoBusca.java → Classe auxiliar que guarda os dados das buscas já feitas  
 │ └── ThreadCounter.java → Classe auxiliar para contagem de Threads  
 │  
 ├── 📁 strategy/ → Estratégias de busca  
 │ ├── ExecutorServiceSearch.java → Busca por Pool Threads  
 │ ├── ParallelSearch.java → Busca paralela simples  
 │ ├── PrevIndexSearch.java → Busca por indexação prévia  
 │ ├── SearchStrategy.java → Classe básica de pesquisa  
 │ └── SequencialSearch.java → Busca sequencial  
 │  
 ├── 📁 ui/ → Parte grafica  
 │ ├── BuscaUI.java → Parte principal  
 │ ├── ColorFormatter.java → Coloca cor nos logs do terminal  
 │ ├── SpeedUpUI.java → Tela do calculo de SpeedUP  
 │ ├── Trabaio.java → Classe para o editor de estilo FlatLaf  
 │ └── Trabaio.properties → Classe para o editor de estilo FlatLaf  
 ├── 📁 libs/ → biblioteca do Flatlaf  
