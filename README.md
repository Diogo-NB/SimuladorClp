# Simulador de CLP com Interface Interativa

O projeto desenvolvido consiste na criação de um ambiente de simulação para um Controlador Lógico Programável (CLP), possibilitando a operação de entradas e saídas de forma interativa.
Inspirado no funcionamento de CLPs convencionais, o simulador apresenta as funcionalidades básicas.

![Interface do programa com simulação](/docs/simulation_interface.png)

# **Operações disponíveis da lista de instruções**

- **LD:** Carrega um valor para o acumulador.
- **LDN:** Carrega um valor negado para o acumulador.
- **ST:** Armazena o conteúdo do acumulador no local especificado.
- **STN:** Armazena o conteúdo do acumulador negado no local especificado.
- **AND:** Função booleana AND entre o operando indicado e o valor do acumulador.
- **ANDN:** Função booleana AND entre o operando indicado negado e o valor do acumulador.
- **OR:** Função booleana OR entre o operando indicado e o valor do acumulador.
- **ORN:** Função booleana OR entre o operando indicado negado e o valor do acumulador.
- **TON:** Temporizador ON Delay (ativa após um intervalo de tempo definido).
- **TOF:** Temporizador OFF Delay (desativa após um intervalo de tempo definido).
- **T1, T2, T3...:** Referências aos temporizadores específicos.
- **Q0.1, Q1.7, Q1.0 ...:** Referências a saídas do sistema.
- **CTU:** Contador crescente.
- **CTD:** Contador decrescente.

# **Memórias Booleanas Locais**

Disponíveis no mínimo 32 memórias booleanas.

# **Temporizadores**

- 32 Temporizadores com base de tempo em 0.1s.
- ON DELAY: Retardo na ativação.
- OFF DELAY: Retardo na desativação.

# **Contadores**

- 32 contadores Progressivos (UP) ou Regressivos (DOWN).

# **Ciclo de Varredura**

- Simula o ciclo padrão de operação de um CLP, com as seguintes etapas:
- Inicialização do sistema.
- Leitura de entradas e armazenamento na memória imagem.
- Processamento do programa do usuário e atualização da memória imagem de saída.
- Atualização das saídas físicas.
- Repetição do ciclo.

# **Modos de Operação**

- **PROGRAM:** Permite a edição do programa lógico, sem leitura/escrita nas saídas físicas.
- **STOP:** Interrompe o programa lógico.
- **RUN:** Executa o programa lógico criado pelo usuário.

# **Armazenamento e Carregamento de Programas**

Possibilidade de salvar e carregar programas escritos previamente.

# **Linguagem de Programação**

O simulador utiliza a Lista de Instrução (IL) para a lógica do CLP.

# **Exemplos de Uso**

Foram desenvolvidos três programas exemplos para demonstrar o uso do simulador, cobrindo todas as funcionalidades, como operações lógicas, temporizadores e contadores.

# **Tecnologia Utilizada**

O projeto foi implementado em Java, utilizando o NetBeans como ambiente de desenvolvimento.

# **Interface e Referências**

A interface interativa permite a manipulação simples de entradas e saídas.
O projeto tem como referência o LogixPro, adaptado para o formato de Lista de Instrução.

# **Youtube**

Uma breve explicação do funcionamento do aplicativo: <https://www.youtube.com/watch?v=Qdy83gkzqz0>
