package Paint.Entities;

import Paint.Game.Level;
import Paint.Objects.Objects;
import Sound.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public class Link extends Entities{

    public Link() {
        //constante
        sheet = "data/images/sheets/link.png";
        name = "link";
        width = 120;
        height = 120;
        hSaut = -14;

        //variable de position
        x = 40;
        y = 250;
        speed = 3;
        initialSpeed = 3;
        inertia = false;

        //variable d'affichage
        sens = "right";
        doing = 0;
        frame = 0.0d;

        //variable de mouvement
        ready = false;
        run = false;
        up = new Boolean[]{false, false};
        isPose = false;
        nothingTime = 0;
        nSaut = 0;
        canSaut = true;
        doSaut = false;

        //variable de la vie
        maxVie = 3;
        vie = maxVie;
        healtPoint = 3;
        maxHealtPoint = 3;

        //variable d'etat
        damage = false;
        damageTime = 0;
        canMove = true;
        isMoving = false;

        //variable physique
        force = 0;
        marge = 0;
        gravity = false;

        //variable du raport avec les objet
        isLand = new Boolean[]{false, false};
        isCollide = false;
        lastObject = null;

        //variable graphique
        try {
            img = new Image[9][10];
            img[0][0] = ImageIO.read(new File(sheet)).getSubimage(0, 400, width, height);
            img[1][0] = ImageIO.read(new File(sheet)).getSubimage(0, 140, width, height);

            sheetX = 1080;
            for (int i = 0; i < 10; i++) {
                img[2][i] = ImageIO.read(new File(sheet)).getSubimage(sheetX, 920, width, height);
                sheetX -= 120;
            }

            sheetX = 0;
            for (int i = 0; i < 10; i++) {
                img[3][i] = ImageIO.read(new File(sheet)).getSubimage(sheetX, 660, width, height);
                sheetX += 120;
            }

            img[4][0] = ImageIO.read(new File(sheet)).getSubimage(852, 920, width, height);
            img[5][0] = ImageIO.read(new File(sheet)).getSubimage(240, 660, width, height);
            img[6][0] = ImageIO.read(new File(sheet)).getSubimage(0, 270, width, height);
            img[7][0] = ImageIO.read(new File(sheet)).getSubimage(360, 920, width, height);
            img[8][0] = ImageIO.read(new File(sheet)).getSubimage(600, 650, width, height);

            HPImg = ImageIO.read(new File("data/images/sheets/life.png"));

            vieImg = ImageIO.read(new File(sheet)).getSubimage(10, 30, 110, 55);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //avance
    public void go(Level level) {
        if (ready){
            isMoving = true;
            if (speed > 0) {
                sens = "right";
                doing = 2;
            } else {
                doing = 3;
                sens = "left";
            }

            if (frame < 9) {
                frame += (abs(speed) * 0.06);
            } else {
                frame = 0.0d;
            }

            //bouge le terrain
            if ((speed > 0 && x > 255) || (speed < 0 && x < 255 && level.mondeX < 0)) {
                level.mondeX -= speed;
                level.paysageX -= speed / (1.0 * level.depht);

                //bouge les object

                for (Objects object:level.objectsList) {
                    object.x -= speed;
                }
                //bouge les enemies
                for (Goomba enemies:level.goombaList) {
                    enemies.x -= speed;
                }

            } else {
                x += speed;
            }
        }
    }
    //cours
    public void goRun(){
        if (!inertia) {
            if (run) {
                speed = initialSpeed * 2;
                hSaut = -16;
            } else {
                speed = initialSpeed;
                hSaut = -14;
            }
        }
    }
    //saute
    public void jump(Sound jump) {
        if (up[0]) {
            jump.play();
            nSaut += 1;
            up[0] = false;
            up[1] = true;
            doSaut = true;
            isLand[0] = isLand[1];
            isLand[1] = false;
            frame = 0.0d;
            y += hSaut;
            force = hSaut;
            nothingTime = 0;
        }
    }
    //ne fait rien
    public void DoNothing(){
        if (!ready) {
            if (sens.equals("right")){
                doing = 0;
            } else {
                doing = 1;
            }
            frame = 0.0d;
        }
    }

    //se fait mal
    public void douleur() {
        if (damage) {
            damageTime += 1;
            if (damageTime < 30){
                canMove = false;
                frame = 0.0d;
                if (sens.equals("right")) {
                    doing = 7;
                    x -= 1;
                } else {
                    doing = 8;
                    x += 1;
                }
            } else {
                canMove = true;
            }
            if (damageTime > 200){
                damage = false;
            }
        } else {
            damageTime = 0;
        }
    }
}
