/** Group Project: To Do List
  Members: 	Nicholas Breuer
		Nathaniel Troksa
		Mays Jabbar
		Shivanni Methuku
  Description:	This is the main class and much of the implementation of the ToDoList form and code.
  		The form enables a user to keep track of a list of "To Do" tasks which the user can:
		-add to the list
		-complete a task
		-delete a task
		-sort by: Priority, Status, and Description
		-print a report which includes all inprogress, completed, and deleted tasks
*/

package ToDoList;
import java.io.*;
import java.awt.Desktop;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;

import javax.swing.GroupLayout.Alignment;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Form_ToDoList {

	// global variables 
	private JFrame frame;
	private JTable table;
	
	private String saveFile = "saveFile.txt";
	private static List<Task> taskList = new ArrayList<Task>();
	private static List<Task> completedList = new ArrayList<Task>();
	private static List<Task> deletedList = new ArrayList<Task>();

	public static String newline = System.getProperty("line.separator");
	
	/**
	 * Main method to Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Form_ToDoList window = new Form_ToDoList();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Form_ToDoList() {
		initialize();
	}

	/**
	 * Method to print and render the report
	 * @param DefaultTableModel 
	 * @return void*/
	private void printReport(DefaultTableModel model) throws IOException  {
		File file = new File("printReport.txt");
	      
        file.createNewFile();
        FileWriter writer = new FileWriter(file);  
        
        String saveString = "";
        
        saveString = writeToSaveString(saveString, "INCOMPLETE: ", taskList); //generate incomplete string in file
        saveString = writeToSaveString(saveString, "COMPLETED: ", completedList); //generate completed task string in file
        saveString = writeToSaveString(saveString, "DELETED: ", deletedList); //generate deleted tasks in file

        writer.write(saveString); 
        writer.flush();
        writer.close();
        
        try { //Open the Print report file to show to viewer
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
              desktop = Desktop.getDesktop();
            }

             desktop.open(new File("printReport.txt"));
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
	}
	
	/**
	 * Method to store the content of the items to save to the file
	 * @param String saveString, String type, and List<Task> objectList 
	 * @return String of the saved items*/
	private String writeToSaveString(String saveString, String Type, List<Task> objectList) {
        saveString += newline + Type;
        for (int i = 0 ; i < objectList.size(); i++){
        	Task T = objectList.get(i);
        	saveString += newline + "\tPriority: " + T.getPriority() + newline;
        	saveString += "\tStatus: " + T.getStatus() + newline;
        	saveString += "\tDate: " + T.getDueDate() + newline;
        	saveString += "\tDescription: " + T.getDescription() + newline;
        	saveString += "\tStart Date: " + T.getStartDate() + newline;
        	saveString += "\tCompleted Date: " + T.getCompletedDate() + newline;
        }
		return saveString;
	}
	
	/**
	 * Method to save the content for reopening
	 * @param DefaultTableModel
	 * @return void*/
	private void save(DefaultTableModel model) throws IOException  {
        File file = new File(saveFile);
      
        file.createNewFile();
        FileWriter writer = new FileWriter(file);  
        
        String saveString = "";
        
        saveString = writePadding(saveString, taskList);
        saveString = writePadding(saveString, completedList);
        saveString = writePadding(saveString, deletedList);
        
        writer.write(saveString); 
        writer.flush();
        writer.close();
	}
	
	/**
	 * Method to generate the padding in the save file
	 * @param String saveString, List<Task> objectList 
	 * @return String for the padding*/
	private String writePadding(String saveString, List<Task> objectList) {
        for (int i = 0 ; i < objectList.size(); i++){
        	String S[] = objectList.get(i).toStringArray();
        	for (int j = 0; j<6; j++) {
        		saveString += S[j] + "\n";
        	}
        }
		return saveString;
	}
	
	/**
	 * Method load function called in the startup
	 * @param DefaultTableModel model
	 * @return void*/
	private void load(DefaultTableModel model)  throws IOException{
		
		model.setRowCount(0);
        File file = new File(saveFile);
        file.createNewFile();

        FileReader fr = new FileReader(file);
        char [] a = new char[1000]; // file buffer
        fr.read(a); 

        int fileCounter = 0;
        int filePriority = 0;
        String fileStatus = " ";
        String fileDueDate = " ";
        String fileDescription = " ";
        String fileStartDate = " ";
        String fileCompletedDate = " ";

        String input = "";
        for(char c : a){ //for every character in a
            if (c == '\n'){ //if new task
                if (fileCounter == 0){filePriority = Integer.parseInt(input);}
                else if (fileCounter == 1){fileStatus = input;}
                else if (fileCounter == 2){fileDueDate = input;}
                else if (fileCounter == 3){fileDescription = input;}
                else if (fileCounter == 4){fileStartDate = input;}
                else if (fileCounter == 5){fileCompletedDate = input;}
                input = "";
                fileCounter++;

                if (fileCounter == 6) {
                	Task T = new Task(filePriority, fileStatus, fileDueDate, fileDescription, fileStartDate, fileCompletedDate);
                	fileCounter = 0;
                	add(model, T);
                }
            }
            else{
                input += c;
            }
        }
        fr.close();
	}
	
	
	/**
	 * Method to add a new task to the ToDoList
	 * @param DefaultTableModel model, Task t object
	 * @return void*/
    static void add(DefaultTableModel model, Task T) {
		if (T.getStatus().equals("Deleted")) {
			deletedList.add(T);
		}
		else if (T.getStatus().equals("Completed")) {
			completedList.add(T);
		}
		else {
			taskList.add(T);
		}
		display(model);
    }
    
	/**
	 * Method to display the updated list
	 * @param DefaultTableModel model
	 * @return void*/
    static void display(DefaultTableModel model) {
    	model.setRowCount(0);
    	for (int i = 0; i < taskList.size(); i ++) {
    		Task T = taskList.get(i);
    		String S[] = {Integer.toString(T.getPriority()),T.getStatus(),T.getDueDate(),T.getDescription()};
    		model.addRow(S);
    	}
    }
    
	/**
	 * Method to delete item from the list 
	 * @param DefaultTableModel model, integer row value 
	 * @return void*/
    static void delete(DefaultTableModel model,int row) {
    	for (int i = 0; i < model.getRowCount(); i++) { //update every value below the given value
    		Task T = taskList.get(i);
    		if (Integer.toString(T.getPriority()).equals(model.getValueAt(row, 0))) {
    			T.setStatus("Deleted");
    			deletedList.add(T);
    			taskList.remove(i);
    			updatePriorities(T.getPriority());
    			break;
    		}
    	}
    	model.removeRow(row);
    	display(model);
    }
    
	/**
	 * Method to mark a task as complete and save the content for printReport
	 * @param DefaultTableModel model, integer row value
	 * @return void*/
    static void complete(DefaultTableModel model,int row) {
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		for (int i = 0; i < model.getRowCount(); i++) {//update every value below the given value
			Task T = taskList.get(i);
			if (Integer.toString(T.getPriority()).equals(model.getValueAt(row, 0))) {
				T.setStatus("Completed");
				T.setCompletedDate(dateFormat.format(date));
    			completedList.add(T);
    			taskList.remove(i);
    			updatePriorities(T.getPriority());
    			break;
    		}
    	}
    	model.removeRow(row);
    	display(model);
    }
    
	/**
	 * Method to sort Tasks by priority
	 * @param none
	 * @return void*/
    static void sortByPriority() {
		int n = taskList.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (taskList.get(j).getPriority() > taskList.get(j+1).getPriority()) 
                { 
                    Task temp = new Task(taskList.get(j).getPriority(),
            	        	taskList.get(j).getStatus(),
            	        	taskList.get(j).getDueDate(),
            	        	taskList.get(j).getDescription(),
            	        	taskList.get(j).getStartDate(),
            	        	taskList.get(j).getCompletedDate());
                    
                    taskList.get(j).setPriority(taskList.get(j+1).getPriority());
    	        	taskList.get(j).setStatus(taskList.get(j+1).getStatus());
    	        	taskList.get(j).setDueDate(taskList.get(j+1).getDueDate());
    	        	taskList.get(j).setDescription(taskList.get(j+1).getDescription());
    	        	taskList.get(j).setStartDate(taskList.get(j+1).getStartDate());
    	        	taskList.get(j).setCompletedDate(taskList.get(j+1).getCompletedDate());

    	        	taskList.get(j+1).setPriority(temp.getPriority());
    	        	taskList.get(j+1).setStatus(temp.getStatus());
    	        	taskList.get(j+1).setDueDate(temp.getDueDate());
    	        	taskList.get(j+1).setDescription(temp.getDescription());
    	        	taskList.get(j+1).setStartDate(temp.getStartDate());
    	        	taskList.get(j+1).setCompletedDate(temp.getCompletedDate());
                } 
            }
        }
	}
    
	/**
	 * Method to restart to a new list with no Tasks from before
	 * @param DefaultTableModel model
	 * @return void*/
    static void restart(DefaultTableModel model) {
    	model.setRowCount(0);
    	taskList.clear();
    	completedList.clear();
    	deletedList.clear();
    }

	/**
	 * Method for updating the priorities of the Tasks
	 * @param integer target priority
	 * @return void*/
	static void updatePriorities(int targetPriority) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getPriority() > targetPriority) {
				taskList.get(i).setPriority(taskList.get(i).getPriority()-1);
			}
		}
	}
    
	/**
	 * Method to initialize content on page during startup
	 * @param void
	 * @return void*/
	private void initialize() {
		frame = new JFrame("To Do List");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//Pop up a Are you Sure dialog
				int test1 = JOptionPane.showConfirmDialog(frame, "Do you wish to save first?", "Remove Dialog", JOptionPane.YES_NO_OPTION);
				if(test1 == JOptionPane.YES_OPTION) {
					try {
						save((DefaultTableModel)table.getModel());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		frame.setBounds(100, 100, 830, 545);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //add button click's listener
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                //Open new form for add
                NumberFormat format = NumberFormat.getInstance();
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(0);
                formatter.setMaximum(Integer.MAX_VALUE);
                JFormattedTextField priorityField = new JFormattedTextField(formatter);
                priorityField.setText("1");
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

                //check for any repeated values
                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter the Task to add", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    StringBuilder errorString = new StringBuilder();
                    if (dateField.getText().length() == 0) {
                        errorString.append("Enter date in correct format mm/dd/yyyy.\n");
                    }
                    if (Integer.parseInt(priorityField.getText()) <= 0) {
                        errorString.append("Enter value greater than 0.\n");
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
                    	
                    	String newDate = " ";
                    	int newPriority = Integer.parseInt(priorityField.getText());
                    	if (String.valueOf(status.getSelectedItem()).equals("In Progress")){
                            date = new Date();
                            newDate = dateFormat.format(date);
                    	}
                    	if (newPriority > taskList.size()+1) {
                    		newPriority = taskList.size()+1;
                    	}
                        Task T = new Task(newPriority, String.valueOf(status.getSelectedItem()), dateField.getText(), description.getText(), newDate, " ");
                        add(model,T);
                    }
                }
            }
        });
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		
		JButton btnCompleteTask = new JButton("Complete Task"); //complete Task button
		btnCompleteTask.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCompleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //Complete task item click listener
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				try {
				int SelectedRow = table.getSelectedRow();
				SelectedRow = Integer.parseInt(table.getValueAt(SelectedRow, 0).toString());
				complete(model, SelectedRow - 1);
				
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Please select a row first!", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JLabel lblDisplayBy = new JLabel("Display By:");
		lblDisplayBy.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					TableRowSorter<TableModel> sortByColumn = new TableRowSorter<>(table.getModel());
					
					for(int i = 0; i < table.getColumnCount(); i++) //disable double click sorting on each column
						sortByColumn.setSortable(i, false);
					
					table.setRowSorter(sortByColumn); //make a row sorter
					
					List<RowSorter.SortKey> sortKeys = new ArrayList<>();
					
					if(comboBox.getSelectedIndex() == 2) //dont allow user to sort by date
						sortKeys.add(new RowSorter.SortKey(comboBox.getSelectedIndex()+1, SortOrder.ASCENDING));
					else
						sortKeys.add(new RowSorter.SortKey(comboBox.getSelectedIndex(), SortOrder.ASCENDING));
					
					sortByColumn.setSortKeys(sortKeys);
					sortByColumn.sort();
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Priority", "Status", "Description"}));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnDelete = new JButton("Delete"); //delete
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				
				try {
					int SelectedRow = table.getSelectedRow();
					SelectedRow = Integer.parseInt(table.getValueAt(SelectedRow, 0).toString());
					
					delete(model, SelectedRow - 1);
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Please select a row first!");
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					save((DefaultTableModel)table.getModel());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				
				int SelectedRow = 0;
				try {
					SelectedRow = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()) - 1;
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Please select a row first!");
					return;
				}
				
				for (int i = 0; i < model.getRowCount(); i++) {
					Task T = taskList.get(i);
					if (Integer.toString(T.getPriority()).equals(model.getValueAt(SelectedRow, 0))) {
						//Open new form for add
		                NumberFormat format = NumberFormat.getInstance();
		                NumberFormatter formatter = new NumberFormatter(format);
		                formatter.setValueClass(Integer.class);
		                formatter.setMinimum(0);
		                formatter.setMaximum(Integer.MAX_VALUE);
		                JFormattedTextField priorityField = new JFormattedTextField(formatter);
		                priorityField.setText(Integer.toString(T.getPriority()));
		                priorityField.setColumns(5);

		                JComboBox status = new JComboBox();
		                status.setModel(new DefaultComboBoxModel(new String[]{"Not Started", "In Progress"}));
		                if (T.getStatus().equals("Not Started")) {
		                	status.setSelectedIndex(0);
		                }
		                else {
		                	status.setSelectedIndex(1);
		                }
		                
		                Date date = new Date();
		                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		                String dateString = dateFormat.format(date);
		                JFormattedTextField dateField = new JFormattedTextField(dateFormat);
		                dateField.setText(T.getDueDate()); // today

		                JTextArea description = new JTextArea(5, 15);
		                description.setLineWrap(true);
		                description.setText(T.getDescription());
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
		                        "Update Task", JOptionPane.OK_CANCEL_OPTION);
		                if (result == JOptionPane.OK_OPTION) {
		                    StringBuilder errorString = new StringBuilder();
		                    if (dateField.getText().length() == 0) {
		                        errorString.append("Enter date in correct format mm/dd/yyyy.\n");
		                    }
		                    if (Integer.parseInt(priorityField.getText()) <= 0) {
		                        errorString.append("Enter value greater than 0.\n");
		                    }
		                    for (int row = 0; row < model.getRowCount(); row++) {
		                        if (model.getValueAt(row, 0).equals(priorityField.getText())) {
		                        	if (!priorityField.getText().equals(Integer.toString(T.getPriority()))){
		                        		errorString.append("Repeated priority value.\n");
		                        	}
		                        }
		                        if (model.getValueAt(row, 3).equals(description.getText())) {
		                        	if (!description.getText().equals(T.getDescription())){
		                        		errorString.append("Repeated description value.\n");
		                        	}
		                        }
		                    }
		                    if (errorString.toString().length() != 0) {
		                        JOptionPane.showMessageDialog(frame, errorString.toString());
		                    } else {
		                    	
		                    	String newDate = " ";
		                    	int newPriority = Integer.parseInt(priorityField.getText());
		                    	if (!T.getStatus().equals("In Progress")) {
			                    	if (String.valueOf(status.getSelectedItem()).equals("In Progress")){
			                            date = new Date();
			                            newDate = dateFormat.format(date);
			                    	}
		                    	}
		                    	if (newPriority > taskList.size()+1) {
		                    		newPriority = taskList.size()+1;
		                    	}
		                        Task newTask = new Task(newPriority, String.valueOf(status.getSelectedItem()), dateField.getText(), description.getText(), newDate, " ");
		                        model.removeRow(SelectedRow);
		                        int tempP = T.getPriority();
		                        taskList.get(SelectedRow).setPriority(newTask.getPriority());
		                        taskList.get(SelectedRow).setStatus(newTask.getStatus());
		                        taskList.get(SelectedRow).setDueDate(newTask.getDueDate());
		                        taskList.get(SelectedRow).setDescription(newTask.getDescription());
		                        taskList.get(SelectedRow).setStartDate(newTask.getStartDate());
		                        taskList.get(SelectedRow).setCompletedDate(newTask.getCompletedDate());
		                        if (newTask.getPriority()>taskList.size()) {
		                        	updatePriorities(tempP);
		                        }
		                        sortByPriority();
		                        display(model);
		                    }
		                }
						break;
					}
				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnRemoveAll = new JButton("Remove All");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Pop up a Are you Sure dialog
				int rmvDiaResult = JOptionPane.showConfirmDialog(frame, "Are you sure to wish to remove all tasks?", "Remove All Dialog", JOptionPane.YES_NO_OPTION);
				if(rmvDiaResult == JOptionPane.YES_OPTION) {
					DefaultTableModel model = (DefaultTableModel)table.getModel();
					restart(model);
				}
			}
		});
		btnRemoveAll.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnPrintReport = new JButton("Print Report");
		btnPrintReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				try {
					printReport(model);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
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
			new Object[][] {
			},
			new String[] {
				"Priority", "Status", "Date", "Description"
			}
		) {
			Class[] columnTypes = new Class[] {
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
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setMinWidth(75);
		table.getColumnModel().getColumn(1).setMaxWidth(75);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setMinWidth(75);
		table.getColumnModel().getColumn(2).setMaxWidth(75);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setMinWidth(200);
		scrollPane.setViewportView(table);
		frame.getContentPane().setLayout(groupLayout);
		
		
		try {
			load((DefaultTableModel)table.getModel());
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}
