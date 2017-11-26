package ua.com.lhjlbjyjd.sibur;

import android.app.Application;
import android.util.Log;

/**
 * Created by lhjlbjyjd on 26.11.2017.
 */

public class MyApp extends Application {
    Task[] tasks = new Task[10];
    Task currentTask = null;

    @Override
    public void onCreate(){
        super.onCreate();
        for(int i = 0; i < 10; i++) {
            Goal[] goalsData = new Goal[6];
            for(int j = 0; j < 6; j++) {
                goalsData[j] = new Goal("Goal " + j, false, j == 5, j == 5*2, this);
                /*if(j < 5) {
                    goalsData[j].setBeginDate(System.currentTimeMillis() + j * 600000);
                    goalsData[j].setEndDate(System.currentTimeMillis() + (j+1) * 600000);
                } else if(j == 5){
                    goalsData[j].setBeginDate(System.currentTimeMillis() + j * 600000);
                }*/
            }
            tasks[i] = new Task("Задание " + i, false, "0", goalsData);
        }
        //currentTask = tasks[3];
    }

    public Task[] getTasks(){
        return tasks;
    }

    public Task getTask(int i){
        return tasks[i];
    }

    public Goal getGoalFromTask(Task task, int i){
        return task.getGoals()[i];
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task task) {
        currentTask = task;
    }

    public void setTasks(Task [] tasks){
        this.tasks = tasks;
    }
}
