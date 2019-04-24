/** Group Project: To Do List
  Members: 	Nicholas Breuer
		Nathaniel Troksa
		Mays Jabbar
		Shivanni Methuku
		
  Description:	This is the Task helper class which assists in keeping track of each individual task and its parameters
  */

package ToDoList;
        
public class Task {
    
	// global variabels
    private int priority = 0;
    private String description = " ";
    private String status = " ";
    private String dueDate = " ";
    private String startDate = " ";
    private String completedDate = " ";
    
    public static String newline = System.getProperty("line.separator");
    
    /**
     * Constructor for the Task Class 
     * @param int priority, String status, String dueDate, String description, String startDate, String completedDate
     * @return void*/
    public Task(int priority, String status, String dueDate, String description, String startDate, String completedDate){
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.description = description;
        this.startDate = startDate;
        this.completedDate = completedDate;
    }
    
    /**
     * Accessor Method to get Task objects priority
     * @param void
     * @return priority*/
    public int getPriority(){ return priority; }
    
    /**
     * Accessor Method to get Task objects status
     * @param void
     * @return status*/
    public String getStatus(){ return status; }
    
    /**
     * Accessor Method to get Task objects dueDate
     * @param void
     * @return dueDate*/
    public String getDueDate(){ return dueDate; }
    
    /**
     * Accessor Method to get Task objects description
     * @param void
     * @return description*/
    public String getDescription(){ return description; }
    
    /**
     * Accessor Method to get Task objects startDate
     * @param void
     * @return startDate*/
    public String getStartDate(){ return startDate; }
    
    /**
     * Accessor Method to get Task objects completeDate
     * @param void
     * @return completeDate*/
    public String getCompletedDate(){ return completedDate; }
    
    /**
     * Mutator Method to set Task objects priority
     * @param priority
     * @return void*/
    public void setPriority(int priority){ this.priority = priority; }
    
    /**
     * Mutator Method to set Task objects status
     * @param status
     * @return void*/
    public void setStatus(String status){ this.status = status; }
    
    /**
     * Mutator Method to set Task objects dueDate
     * @param dueDate
     * @return void*/
    public void setDueDate(String dueDate){ this.dueDate = dueDate; }
    
    /**
     * Mutator Method to set Task objects description
     * @param description
     * @return void*/
    public void setDescription(String description){ this.description = description; }
    
    /**
     * Mutator Method to set Task objects startDate
     * @param startDate
     * @return void*/
    public void setStartDate(String startDate){ this.startDate = startDate; }
    
    /**
     * Mutator Method to set Task objects completedDate
     * @param completedDate
     * @return void*/
    public void setCompletedDate(String completedDate){ this.completedDate = completedDate; }
    
    /**
     * toString Method for Task Object
     * @param void
     * @return String[] array*/
    public String[] toStringArray(){
        String s[] = {Integer.toString(priority), status, dueDate, description, startDate, completedDate};
        return s;
    }
    
    /**
     * toString Method for Task value
     * @param void
     * @return String[] array*/
    public String toString(){
        String s = "";
        s += "Priority: " + priority + newline;
        s += "Status: " + status + newline;
        s += "Due Date: " + dueDate + newline;
        s += "Description: " + description + newline;
        s += "Start Date: " + startDate + newline;
        s += "Completed Date: " + completedDate + newline;
        return s;
    }
}
