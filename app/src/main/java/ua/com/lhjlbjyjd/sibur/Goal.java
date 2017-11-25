package ua.com.lhjlbjyjd.sibur;

/**
 * Created by Toreno on 25.11.2017.
 */

public class Goal {
    private boolean state;
    private String description;

    Goal(String description, boolean state) {
        this.state = state;
        this.description = description;
    }
}
