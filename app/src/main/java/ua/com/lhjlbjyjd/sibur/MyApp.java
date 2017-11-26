package ua.com.lhjlbjyjd.sibur;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lhjlbjyjd on 26.11.2017.
 */

public class MyApp extends Application {
    Task[] tasks;
    Task currentTask = null;

    @Override
    public void onCreate(){
        super.onCreate();
        /*for(int i = 0; i < 10; i++) {
            Goal[] goalsData = new Goal[6];
            for(int j = 0; j < 6; j++) {
                goalsData[j] = new Goal("Goal " + j, false, j == 5, j == 5*2, this);
                /*if(j < 5) {
                    goalsData[j].setBeginDate(System.currentTimeMillis() + j * 600000);
                    goalsData[j].setEndDate(System.currentTimeMillis() + (j+1) * 600000);
                } else if(j == 5){
                    goalsData[j].setBeginDate(System.currentTimeMillis() + j * 600000);
                }
            }
            tasks[i] = new Task("Задание " + i, false, "0", goalsData);
        }
        //currentTask = tasks[3];*/
    }

    public Task[] getTasks(){
        return tasks;
    }

    public Task getTask(int i){
        if(tasks.length > 0)
            return tasks[i];
        else return currentTask;
    }

    public Goal getGoalFromTask(Task task, int i){
        return task.getGoals()[i];
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task task) {
        currentTask = task;
        List<Task> tmp = new LinkedList<>(Arrays.asList(tasks));
        tmp.remove(currentTask);
        tasks = tmp.toArray(new Task[tmp.size()]);


    }

    public void setTasks(Task [] tasks){
        this.tasks = tasks;
    }

    public void addTask(Task task){
        Task[] tmp = new Task[tasks.length+1];
        for(int i = 0; i < tasks.length; i++){
            tmp[i] = tasks[i];
        }
        tmp[tasks.length] = task;
        tasks = tmp;
    }
}
