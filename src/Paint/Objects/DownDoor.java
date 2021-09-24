package Paint.Objects;

import Paint.Game.*;
import Paint.Entities.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DownDoor extends Objects {
    public DownDoor(int newX, int newY, int newChannel){
        nom = "downDoor";
        type = "Porte";
        try {
            img = ImageIO.read(new File("data/images/objects/downDoor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        x = newX;
        y = newY;
        channel = newChannel;

        width = 60;
        height = 230;
        speed = 3;

        isActive = false;
        moveTime = 120;
        timer = 0;
        isApeared = true;
        isUsed = false;
        isCollide = false;
    }

    public void run(Level level, Entities client) {
        if (timer < moveTime) {
            y += speed;
            timer += 1;
        } else {
            timer = 0;
            isActive = false;
        }
    }
}