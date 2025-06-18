package screens;

import Models.HomePageModel;
import ilcompiler.memoryvariable.MemoryVariable;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.ArrayList;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import java.util.Map; // Para Map em updateDataTable

public class ListaDeVariaveisPg extends javax.swing.JFrame {

    private JTable variablesTable;
    private DefaultTableModel tableModel;

    public ListaDeVariaveisPg() {
        initComponents();
        setupVariablesTable();
        setTitle("Monitor de Variáveis");
        this.setResizable(false);
    }

    public ListaDeVariaveisPg(Map<String, Boolean> inputs, Map<String, Boolean> outputs) {
        this(); // Chama o construtor padrão para inicializar componentes e tabela
        updateDataTable(inputs, outputs); // Popula a tabela com dados iniciais
    }

    private void setupVariablesTable() {
        String[] columns = {"ID", "CurrentValue", "Counter", "MaxTimer", "EndTimer"}; // Colunas da tabela

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta das células
            }
        };

        variablesTable = new JTable(tableModel);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        variablesTable.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        // A coluna 0 é o "ID"
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        
        // Renderizador para colorir o estado (verde para TRUE, vermelho para FALSE)
        variablesTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 1 && value instanceof Boolean) { // Se for a coluna 'Estado'
                    renderer.setBackground((Boolean) value ? new Color(144, 238, 144) : new Color(255, 99, 71));
                    renderer.setForeground((Boolean) value ? Color.BLACK : Color.WHITE);
                } else { // Cores padrão para outras colunas/seleção
                    renderer.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    renderer.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
                return renderer;
            }
        });

        jScrollPane1.setViewportView(variablesTable); // Conecta a tabela ao painel de rolagem
    }

    public void updateDataTable(Map<String, Boolean> inputs, Map<String, Boolean> outputs) {
        tableModel.setRowCount(0); // Limpa todas as linhas

        for (Map.Entry<String, Boolean> entry : inputs.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()}); // Adiciona ID e Estado dos Inputs
        }
        for(Map.Entry<String, Boolean> entry : outputs.entrySet()){
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()}); // Adiciona ID e Estado dos Outputs
        }
        
        for (Map.Entry<String, MemoryVariable> entry : HomePageModel.getMemoryVariables().entrySet()) {
            switch (entry.getKey().charAt(0)) {
                case 'T' -> {
                    tableModel.addRow(new Object[]{entry.getKey(), entry.getValue().currentValue, 
                        entry.getValue().counter, entry.getValue().maxTimer, entry.getValue().endTimer});
                }
                case 'C' -> {
                    tableModel.addRow(new Object[]{entry.getKey(), "", entry.getValue().counter, 
                        entry.getValue().maxTimer, entry.getValue().endTimer});
                }
            }
        }
    }

    // Métodos setText/getText adaptados para a JTable (podem ser removidos se não forem usados)
    public void setText(String text) {
        tableModel.setRowCount(0);
        String[] rows = text.split("\n");
        for (String row : rows) {
            String[] parts = row.split(";");
            if (parts.length >= 2) {
                tableModel.addRow(new Object[]{parts[0], Boolean.parseBoolean(parts[1])});
            }
        }
    }

    public String getText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            stringBuilder.append(tableModel.getValueAt(i, 0)).append(";")
                         .append(tableModel.getValueAt(i, 1)).append("\n");
        }
        return stringBuilder.toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Lista_de_variaveis = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Lista_de_variaveis.setColumns(20);
        Lista_de_variaveis.setRows(5);
        jScrollPane1.setViewportView(Lista_de_variaveis);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        // Bloco de configuração de look and feel (gerado automaticamente)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListaDeVariaveisPg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Inicia e exibe a janela na Event Dispatch Thread
        java.awt.EventQueue.invokeLater(() -> {
            Map<String, Boolean> InputsExample = new java.util.HashMap<>();
            Map<String, Boolean> OutputsExample = new java.util.HashMap<>();

            new ListaDeVariaveisPg(InputsExample, OutputsExample).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Lista_de_variaveis;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
