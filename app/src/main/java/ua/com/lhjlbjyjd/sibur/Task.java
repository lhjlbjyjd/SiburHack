package ua.com.lhjlbjyjd.sibur;

import java.io.Serializable;

/**
 * Created by lhjlbjyjd on 25.11.2017.
 */

public class Task implements Serializable{
    private String name;
    private boolean isExecuting = false;
    private String executorID;
    private Goal[] goals;

    Task(String name, boolean state, String executorID, Goal[] goals){
        this.name = name;
        this.isExecuting = state;
        this.executorID = executorID;
        this.goals = goals;
    }

    public String getExecutorID() {
        return executorID;
    }

    public String getName() {
        return name;
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    public Goal[] getGoals() {
        return goals;
    }
}
