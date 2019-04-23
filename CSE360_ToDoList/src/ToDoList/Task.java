
package ToDoList;

import java.util.Date;
        
public class Task {
    
    private int priority = 0;
    private String description = " ";
    private String status = " ";
    private String dueDate = " ";
    private String startDate = " ";
    private String completedDate = " ";
    
    public static String newline = System.getProperty("line.separator");
    
    public Task(int priority, String status, String dueDate, String description, String startDate, String completedDate){
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.description = description;
        this.startDate = startDate;
        this.completedDate = completedDate;
    }
    
    public int getPriority(){ return priority; }
    public String getStatus(){ return status; }
    public String getDueDate(){ return dueDate; }
    public String getDescription(){ return description; }
    public String getStartDate(){ return startDate; }
    public String getCompletedDate(){ return completedDate; }
    
    public void setPriority(int priority){ this.priority = priority; }
    public void setStatus(String status){ this.status = status; }
    public void setDueDate(String dueDate){ this.dueDate = dueDate; }
    public void setDescription(String description){ this.description = description; }
    public void setStartDate(String startDate){ this.startDate = startDate; }
    public void setCompletedDate(String completedDate){ this.completedDate = completedDate; }
    
    public String[] toStringArray(){
        String s[] = {Integer.toString(priority), status, dueDate, description, startDate, completedDate};
        return s;
    }
    
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
