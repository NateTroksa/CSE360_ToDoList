

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

public class toDoList {

    private JFrame frame;
    private JTable table;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    toDoList window = new toDoList();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void actionPerformed(ActionEvent e) {

    }

    /**
     * Create the application.
     */
    public toDoList() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("To Do List");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Pop up a Are you Sure dialog
                int test1 = JOptionPane.showConfirmDialog(frame, "Do you wish to save first?", "Remove Dialog", JOptionPane.YES_NO_OPTION);
                if (test1 == JOptionPane.YES_OPTION) {
                    //Save current data into file
                }
            }
        });
        frame.setBounds(100, 100, 830, 545);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane();

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                //Open new form for add
                NumberFormat format = NumberFormat.getInstance();
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(0);
                formatter.setMaximum(Integer.MAX_VALUE);
                JFormattedTextField priorityField = new JFormattedTextField(formatter);
                priorityField.setText("0");
                priorityField.setColumns(5);

                JComboBox status = new JComboBox();
                status.setModel(new DefaultComboBoxModel(new String[]{"Not Started", "In Progress"}));

                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = dateFormat.format(date);
                JFormattedTextField dateField = new JFormattedTextField(dateFormat);
                dateField.setText(dateString); // today

                JTextArea description = new JTextArea(5, 15);
                description.setLineWrap(true);
                description.setText("Add your description here...");
                JScrollPane scrollpane = new JScrollPane(description);

                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Priority:"));
                myPanel.add(priorityField);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Status:"));
                myPanel.add(status);
                myPanel.add(Box.createHorizontalStrut(20)); // a spacer
                myPanel.add(new JLabel("Date:"));
                myPanel.add(dateField);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Description:"));
                myPanel.add(scrollpane);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter the Task to add", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    StringBuilder errorString = new StringBuilder();
                    if (dateField.getText().length() == 0) {
                        errorString.append("Enter date in correct format mm/dd/yyyy.\n");
                    }
                    if (priorityField.getText().length() == 0) {
                        errorString.append("Enter non negative priority value.\n");
                    }
                    for (int row = 0; row < model.getRowCount(); row++) {
                        if (model.getValueAt(row, 0).equals(priorityField.getText())) {
                            errorString.append("Repeated priority value.\n");
                        }
                        if (model.getValueAt(row, 3).equals(description.getText())) {
                            errorString.append("Repeated description value.\n");
                        }
                    }
                    if (errorString.toString().length() != 0) {
                        JOptionPane.showMessageDialog(frame, errorString.toString());
                    } else {
                        String[] newRow = {priorityField.getText(), String.valueOf(status.getSelectedItem()), dateField.getText(), description.getText()};
                        model.addRow(newRow);
                    }
                }
            }
        });

        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton btnCompleteTask = new JButton("Complete Task");
        btnCompleteTask.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnCompleteTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                try {
                    int SelectedRow = table.getSelectedRow();
                    model.removeRow(SelectedRow);

                    //save to file as a Deleted task
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please select a row first!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lblDisplayBy = new JLabel("Display By:");
        lblDisplayBy.setFont(new Font("Tahoma", Font.BOLD, 16));

        JComboBox comboBox = new JComboBox();
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    TableRowSorter<TableModel> sortByColumn = new TableRowSorter<>(table.getModel());
                    table.setRowSorter(sortByColumn);

                    List<RowSorter.SortKey> sortKeys = new ArrayList<>();

                    sortKeys.add(new RowSorter.SortKey(comboBox.getSelectedIndex(), SortOrder.ASCENDING));
                    sortByColumn.setSortKeys(sortKeys);
                    sortByColumn.sort();
                    //Sort by current selected item "String.valueOf(comboBox.getSelectedItem())"
                }
            }
        });
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Priority", "Status", "Date", "Description"}));
        comboBox.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                try {
                    int SelectedRow = table.getSelectedRow();

                    //Save Deleted Item first or put into an arr
                    model.removeRow(SelectedRow);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please select a row first!");
                }
            }
        });
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[][] rowData = new Object[table.getRowCount()][table.getColumnCount()];
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        rowData[i][j] = table.getValueAt(i, j);
                    }
                }

                //Call function to save file save(Object[][] table)
            }
        });
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Call for new form
                //Delete old row
                //add new row with "Updated Info"
                //Re sort based on "String.valueOf(comboBox.getSelectedItem())"
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton btnRemoveAll = new JButton("Remove All");
        btnRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Pop up a Are you Sure dialog
                int rmvDiaResult = JOptionPane.showConfirmDialog(frame, "Are you sure to wish to remove all tasks?", "Remove All Dialog", JOptionPane.YES_NO_OPTION);
                if (rmvDiaResult == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                }
            }
        });
        btnRemoveAll.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton btnPrintReport = new JButton("Print Report");
        btnPrintReport.setFont(new Font("Tahoma", Font.BOLD, 16));

        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(12)
                                .addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
                                .addGap(194)
                                .addComponent(btnCompleteTask, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(99)
                                                .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(155)
                                                .addComponent(lblDisplayBy)))
                                .addPreferredGap(ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                                .addGap(115)
                                .addComponent(btnRemoveAll, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                                .addComponent(btnPrintReport, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                .addGap(45)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCompleteTask, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                .addGroup(groupLayout.createSequentialGroup()
                                                        .addGap(35)
                                                        .addComponent(lblDisplayBy)
                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                        .addComponent(comboBox, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                                                .addGroup(groupLayout.createSequentialGroup()
                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                        .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))))
                                .addGap(35)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnPrintReport, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnRemoveAll, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                .addGap(36))
        );

        table = new JTable();
        table.setCellSelectionEnabled(true);
        table.setFillsViewportHeight(true);
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Priority", "Status", "Date", "Description"
                }
        ) {
            Class[] columnTypes = new Class[]{
                Integer.class, String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.getColumnModel().getColumn(0).setResizable(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(100);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(1).setMinWidth(75);
        table.getColumnModel().getColumn(1).setMaxWidth(75);
        table.getColumnModel().getColumn(2).setResizable(false);
        table.getColumnModel().getColumn(2).setMinWidth(95);
        table.getColumnModel().getColumn(2).setMaxWidth(95);
        table.getColumnModel().getColumn(3).setResizable(true);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(200);
        table.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(table);
        frame.getContentPane().setLayout(groupLayout);

    }

}
