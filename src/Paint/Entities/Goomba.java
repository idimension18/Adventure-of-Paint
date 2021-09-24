package Paint.Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Goomba extends Entities{
    public Goomba(int newX){
        sheet = "data/images/sheets/enemies.png";
        name = "goomba";
        x = newX;
        y = 0;
        width = 75;
        height = 75;
        speed = -3;

        isMoving = true;
        doing = 0;
        frame = 0.0d;
        force = 0;
        marge = 0;
        gravity = false;
        isPose = false;
        ready = true;

        charged = false;
        isDead = false;
        deadTime = 0.0d;
        isShown = false;
        Isstomped = false;
        isLand = new Boolean[]{false, false};

        img = new Image[2][2];
        try {
            img[0][0] = ImageIO.read(new File(sheet)).getSubimage(0, 0, 20, 20);
            img[0][1] = ImageIO.read(new File(sheet)).getSubimage(30, 0, 20, 20);
            img[1][0] = ImageIO.read(new File(sheet)).getSubimage(60, 5, 20, 15);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /////////////////////////////////////////////////////////////////////
    //load les gpublic oomba
    public void load(Link link) {
        if (x - link.x < 600) {
            if (!isDead) {
                charged = true;
                isShown = true;
            }
        } else {
            charged = false;
            isShown =false;
        }
    }

    //deplace les gpublic oomba
    public void move() {
        if (charged && !isDead) {
            x += speed;
            if (frame <1.9) {
                frame += 0.06;
            } else {
                frame = 0.0d;
            }
        }
    }

    //la mort des goomba
    public void dead() {
        if (isDead) {
            doing = 1;
            frame = 0.0d;
            charged = false;
        }
    }

    // gere l'affichage des goomba
    public void isShow() {
        if (isDead) {
            deadTime += 0.5;
            if (deadTime > 25) {
                isShown = false;
            }
        } else {
            deadTime = 0.0d;
        }
    }
}
