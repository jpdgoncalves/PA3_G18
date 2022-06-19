package Gui.Common.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;


public class TablePanel extends TitledPanel {
    private final DefaultTableModel model;
    private final JTable table;
    private final JScrollPane scroller;

    private final ArrayList<Integer> idList = new ArrayList<>();
    private final HashMap<String, Integer> colNameToColMap = new HashMap<>();


    public TablePanel(String title, String[] fields) {
        super(title);
        setLayout(new GridLayout(1, 1));

        for (String field: fields) {
            int colIdx = colNameToColMap.size();
            colNameToColMap.put(field, colIdx);
        }

        model = new DefaultTableModel(fields,0);
        table = new JTable(model);
        scroller = new JScrollPane(table);

        table.setFillsViewportHeight(true);
        scroller.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scroller);
    }

    public boolean hasRow(int id) {
        return idList.contains(id);
    }

    public void addRow(int id, Object[] data) {
        SwingUtilities.invokeLater(() -> {
            if (hasRow(id)) {
                setAllValuesAt(id, data);
            } else {
                Object[] row = Stream.concat(Arrays.stream(new Object[] {id}), Arrays.stream(data)).toArray();
                idList.add(id);
                model.addRow(row);
            }
        });
    }

    public void removeRow(int id) {
        SwingUtilities.invokeLater(() -> {
            if (!hasRow(id)) return;

            int index = idList.indexOf(id);
            idList.remove(index);
            model.removeRow(index);
        });
    }

    public void setValueAt(int id, String columnName, Object value) {
        SwingUtilities.invokeLater(() -> {
            if(!hasRow(id) || !colNameToColMap.containsKey(columnName)) return;

            int rowIdx = idList.indexOf(id);
            int columnIdx = colNameToColMap.get(columnName);
            model.setValueAt(value, rowIdx, columnIdx);
        });
    }

    private void setAllValuesAt(int id, Object[] data) {
        if (!hasRow(id) || data.length + 1 != colNameToColMap.size()) return;

        int rowIdx = idList.indexOf(id);
        Object[] row = Stream.concat(Arrays.stream(new Object[] {id}), Arrays.stream(data))
                .toArray();
        model.removeRow(rowIdx);
        model.insertRow(rowIdx, row);
    }
}

