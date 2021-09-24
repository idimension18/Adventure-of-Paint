package Paint.Main;

import Paint.Game.Jeux;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        TimerTask task = new Jeux();
        timer.schedule(task,0,16);

    }
}
