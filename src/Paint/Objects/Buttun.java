package Paint.Objects;

import Paint.Game.*;
import Paint.Entities.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Buttun extends Objects{

    public Buttun(int newX, int newY, int newChannel) {
        nom = "Button";
        type = "Trigger";
        try {
            img = ImageIO.read(new File("data/images/objects/boutton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        x = newX;
        y= newY;
        channel = newChannel;

        width = 130;
        height = 110;
        speed = 3;

        isActive = false;
        moveTime = 1200;
        timer = 0;
        isApeared = true;
        isUsed = false;
        isCollide = false;
    }
    public void run(Level level, Entities client) {
        if (timer < moveTime) {
            y += speed;
            if (isUsed) {
                client.y += speed;
            }
            timer += 1;
        } else {
            timer = 0;
            isActive = false;
        }
    }
}