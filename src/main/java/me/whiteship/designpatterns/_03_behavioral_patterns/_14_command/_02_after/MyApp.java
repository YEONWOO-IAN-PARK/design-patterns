package me.whiteship.designpatterns._03_behavioral_patterns._14_command._02_after;

import me.whiteship.designpatterns._03_behavioral_patterns._14_command._01_before.Game;
import me.whiteship.designpatterns._03_behavioral_patterns._14_command._01_before.Light;

public class MyApp {

    private Command command;

    public MyApp(Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }

    public static void main(String[] args) {
        MyApp myApp = new MyApp(new GameStartCommand(new Game()));
        myApp.press();
        myApp.command.undo();

        Button btn = new Button();
        btn.press(new LightOnCommand(new Light()));
        btn.undo();
    }
}
