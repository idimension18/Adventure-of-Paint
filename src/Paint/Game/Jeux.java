package Paint.Game;

import Paint.Objects.*;
import Paint.Entities.*;
import Paint.Main.Fenetre;
import Sound.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;

import static java.lang.Math.abs;

public class Jeux extends TimerTask {
    Fenetre windows;
    ChargeImage image;
    Link link;
    Level level;
    Engine machine;
    Boolean pause, debugMode, saveOk;
    Boolean[] gameOverOk;
    int saveCode;
    Image[] gameOver;
    Scanner fichierR;
    FileWriter fichierW;
    Sound jump, stomp, ZUT, retry;

    public Jeux() throws Exception {
        //construit des truc
        windows = new Fenetre();
        link = new Link();
        level = new Level();
        machine = new Engine();

        //variable essentiel
        pause = false;
        debugMode = false;
        saveCode = 0;
        saveOk = true;
        gameOverOk = new Boolean[]{false, false};

        //charge la sauvegarde
        fichierR = new Scanner(new FileInputStream("data/save.txt"));
        saveCode = Integer.parseInt(fichierR.next());
        System.out.println(saveCode);
        level.nLevel = saveCode / 100;
        link.vie = (saveCode - (level.nLevel*100))/10;
        link.healtPoint = (saveCode - level.nLevel*100 - link.vie*10);

        level.charge();

        //variable graphique
        gameOver = new Image[2];
        gameOver[0] = ImageIO.read(new File("data/images/gameOver/GameOver.png"));
        gameOver[1] = ImageIO.read(new File("data/images/gameOver/GameOverSuite.png"));

        image = new ChargeImage(link, level, gameOverOk, gameOver);
        windows.setContentPane(image);
        windows.pack();

        //variable sonor
        jump = new Sound(new File("data/music/jump.wav"));
        stomp = new Sound(new File("data/music/stomp.wav"));
        ZUT = new Sound(new File("data/music/ZUT.wav"));
        retry = new Sound(new File("data/music/retry.wav"));
        level.Song.loop();

        //evenement clavier
        KeyListener f = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'f' && link.vie > 0) {
                    link.ready = true;
                    link.speed = abs(link.speed);
                    link.initialSpeed = abs(link.initialSpeed);
                }

                if (e.getKeyChar() == 's' && link.vie > 0) {
                    link.ready = true;
                    link.speed = abs(link.speed) * -1;
                    link.initialSpeed = abs(link.initialSpeed) * -1;
                }

                if (e.getKeyChar() == 'e' && link.canSaut && link.vie > 0) {
                    link.up[0] = true;
                }

                if (e.getKeyChar() == 'q' && !link.run) {
                    link.run = true;
                    link.isMoving = true;
                    if (link.ready) {
                        link.x -= link.speed;
                    }
                }
                if (e.getKeyChar() == 'p') {
                    if (pause = true) {
                        pause = false;
                        level.Song.loop();
                    } else {
                        pause = true;
                        level.Song.stop();
                    }
                }
                if (e.getKeyChar() == 'b' && saveOk) {
                    saveOk = false;
                    try {
                        fichierW = new FileWriter(new File("data/save.txt"), true);
                        saveCode = level.nLevel*100 + link.vie*10 + link.healtPoint;
                        fichierW.write(saveCode);
                        fichierW.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (e.getKeyChar() == 'r' && link.vie < 1) {
                    retry.stop();
                    link.vie = 3;
                    pause = false;
                    gameOverOk = new Boolean[]{false, false};
                    level.Song.loop();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if ((e.getKeyChar() == 'f' && link.speed > 0) || (e.getKeyChar() == 's' && link.speed < 0)) {
                    link.ready = false;
                }
                if (e.getKeyChar() == 'e') {
                    link.up[1] = false;
                }
                if (e.getKeyChar() == 'q') {
                    link.run = false;
                }
                if (e.getKeyChar() == 's') {
                    saveOk = true;
                }
            }
        };
        windows.addKeyListener(f);
    }

    //boucle infinie
    public void run() {
        if (!pause) {

            ///////////////////////////////////////////LINK///////////////////////
            if (link.canMove) {
                link.isMoving = false;
                //avance
                link.go(level);

                //cours !
                link.goRun();
                //saute ^^
                link.jump(jump);

                //regarde le paysage XD
                link.DoNothing();
            }

            //////////////////////////les enemies//////////////////
            //load les goomba
            for (Goomba mob:level.goombaList) {
                mob.load(link);
            }

            // deplace les goomba
            for (Goomba mob:level.goombaList){
                mob.move();
            }

            ////////////////////////physique/////////////////////////////////
            machine.interCollide(level,link,stomp);
            machine.Collide(image, link, level);
            if (!link.isLand[1]) {
                machine.Gravity(link);
            }

            for (Entities mob:level.goombaList) {
                if (mob.charged) {
                    machine.Collide(image, mob, level);
                    machine.Gravity(mob);
                }
            }

            // CHEK les object
            for (Objects object:level.objectsList) {
                if (object.isActive) {
                    object.run(level, link);
                }
            }

            //////////////////////////////////////////
            // link se fait mal
            link.douleur();

            // la mort des goomba
            for (Goomba mob:level.goombaList) {
                mob.dead();
            }

            //gere l'affichage des goomba
            for (Goomba mob:level.goombaList) {
                mob.isShow();
            }
        }
        //////////////////////////les grand changement///////////////////////

        //reset
        if (link.y > 500 || link.healtPoint == 0) {
            link.vie -= 1;
            level.mondeX = 0;
            link.y = 240;
            link.x = 40;
            level.Song.stop();
            link.healtPoint = link.maxHealtPoint;
            level.paysageX = 0;
            level.charge();
            if (link.vie > 0) {
                level.Song.loop();
            }
        }

        // change de level
        if (level.changeLevel) {
            System.out.println("yey");
            level.Song.stop(); //la sa marche pas
            System.out.println("yoy");
            level.changeLevel = false;
            level.nLevel += 1;
            level.charge();
            image = new ChargeImage(link, level, gameOverOk, gameOver);
            windows.setContentPane(image);
            windows.pack();
            link.x = 40;
            link.y = 250;
            level.mondeX = 0;
            level.paysageX = 0;
            level.Song.loop();
        }
        //game over
        if (link.vie < 1) {
            pause = true;
            level.Song.stop();
            gameOverOk[0] = true;
            if (link.vie ==0 ) {
                ZUT.play();
                link.vie -= 1;
            }
            else if (!ZUT.isPlaying()) {
                gameOverOk[1] = true;
                if (link.vie == -1 ) {
                    retry.loop();
                    link.vie -= 1;
                }
            }
        }
        //affiche tout
        windows.repaint();
        //System.out.println(gameOverOk[1]);
    }
}

