package Paint.Objects;

import Paint.Game.*;
import Paint.Entities.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Platform extends Objects {
    public Platform(int newX, int newY, int newChannel) {
        nom = "Platform";
        type = "Platform";
        try {
            img = ImageIO.read(new File("data/images/objects/platform.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        x = newX;
        y= newY;
        channel = newChannel;

        width = 200;
        height = 40;
        speed = 3;

        isActive = false;
        moveTime = 120;
        timer = 0;
        isApeared = false;
        isUsed = false;
        isCollide = false;
    }
    public void run(Level level, Entities client) {
        if (timer < moveTime) {
            timer += 1;
            if (isUsed || isCollide) {
                level.mondeX += speed * (-1);
                level.paysageX += (speed * -1) / (1.0 * level.depht);
            } else {
                x += speed;
            }
        } else {
            timer = 0;
            speed *= -1;
        }
    }
}
