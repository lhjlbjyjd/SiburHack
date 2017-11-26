package ua.com.lhjlbjyjd.sibur;

import android.app.Application;

/**
 * Created by lhjlbjyjd on 26.11.2017.
 */

public class MyApp extends Application {
    Task[] tasks = new Task[10];
    int currentTaskId = 3;

    @Override
    public void onCreate(){
        super.onCreate();
        for(int i = 0; i < 10; i++) {
            Goal[] goalsData = new Goal[6];
            for(int j = 0; j < 6; j++) {
                goalsData[j] = new Goal("Goal " + j, true, j == 4, j == 5);
                goalsData[j].setBeginDate(j*j*j*100);
                goalsData[j].setEndDate((j+1)*(j+1)*(j+1)*100);
            }
            tasks[i] = new Task("Задание " + i, false, "0", goalsData);
        }
    }

    public Task[] getTasks(){
        return tasks;
    }

    public Task getTask(int i){
        return tasks[i];
    }

    public int getCurrentTaskId() {
        return currentTaskId;
    }
}
