package Paint.Game;

import Paint.Objects.*;
import Paint.Entities.*;
import Sound.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Level {
    public int mondeX, nLevel, depht;
    public float paysageX;
    Boolean changeLevel;
    String name;
    public Objects[] objectsList;
    public Goomba[] goombaList;
    Image monde, paysage;
    BufferedImage mondeBlack;
    Sound Song;

    public Level() {
        paysageX = 0;
        mondeX = 0;
        nLevel = 0;
        changeLevel = false;
    }

     void charge() {

        //level 0
        //level 1
        //level 2
        //level 3
        //level 4
        switch (nLevel) {
            case 1 -> {
                name = "nothing";
                depht = 5;
                objectsList = new Objects[]{};
                goombaList = new Goomba[]{new Goomba(1300),
                        new Goomba(1200),
                        new Goomba(1800),
                        new Goomba(2400)};
            }
            case 2 -> {
                name = "main";
                depht = 5;
                objectsList = new Objects[]{};
                goombaList = new Goomba[]{new Goomba(600),
                        new Goomba(1200),
                        new Goomba(1800),
                        new Goomba(2400)
                };
            }
            case 3 -> {
                name = "desert";
                depht = 5;
                objectsList = new Objects[]{};
                goombaList = new Goomba[]{
                        new Goomba(600),
                        new Goomba(1200),
                        new Goomba(1800),
                        new Goomba(2400)
                };
            }
            case 4 -> {
                name = "serveur";
                depht = 2;
                objectsList = new Objects[]{
                        new Buttun(700, 360, 1),
                        new UpDoor(1000, 0, 1),
                        new DownDoor(1000, 227, 1)
                };
                goombaList = new Goomba[]{};
            }
            case 5 -> {
                name = "lab";
                depht = 5;
                objectsList = new Objects[]{
                        new Buttun(400, 350, 1),
                        new Platform(820, 401, 1)};
                goombaList = new Goomba[]{};
            }
        }

         try {
             monde = ImageIO.read(new File("data/images/maps/"+name+"/map.png"));
             mondeBlack = ImageIO.read(new File("data/images/maps/"+name+"/black.png"));
             paysage = ImageIO.read(new File("data/images/maps/"+name+"/background.png"));
         } catch (IOException e) {
             e.printStackTrace();
         }
         Song = new Sound(new File("data/music/"+name+".wav"));
    }


}
