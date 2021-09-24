package Paint.Game;

import Paint.Objects.Objects;
import Paint.Entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import static java.lang.Math.*;

public class ChargeImage extends JComponent {
    Link link;
    Level level;
    Boolean[] gameOverOk;
    Image[] gameOver;

    public ChargeImage(Link Link, Level Level, Boolean[] GameOverOk, Image[] GameOver) {
        setPreferredSize(new Dimension(600, 500));
        link = Link;
        level = Level;
        gameOverOk = GameOverOk;
        gameOver = GameOver;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //affiche le background
        g.drawImage(level.paysage, (int) (level.paysageX), 0, this);

        //affiche link
        if (link.damageTime % 5 == 0) {
            g.drawImage(link.img[link.doing][link.frame.intValue()], link.x, link.y, this);
            //fill(255, 0, 0, 100);
            //rect(link.x, link.y,link.width,link.height);
        }

        //affiche les enemies
        for (Entities mob : level.goombaList) {
            if (mob.isShown) {
                g.drawImage(mob.img[mob.doing][mob.frame.intValue()], mob.x, mob.y, mob.width, mob.height, this);
            }
        }

        //affiche les objects
        for (Objects object : level.objectsList) {
            if (object.isApeared) {
                g.drawImage(object.img, object.x, object.y, object.width, object.height, this);
            }
        }

        //affiche le terrain
        g.drawImage(level.monde, level.mondeX, 0, this);

        //la vie <3
        for (int i = 0; i < link.healtPoint; i++) {
            g.drawImage(link.HPImg, i * 45, 0, 45, 45, this);
        }
        for (int i = 0; i < link.vie; i++) {
            g.drawImage(link.vieImg, 600 - 60 * (i + 1), 0, 70, 35, this);
        }

        //essai de lumiere
        if (level.nLevel == 4) {
            g.setColor(new Color(1, 1, 1, (float) (1-(1/exp(abs(level.mondeX/5000.0))))));
            g.fillRect(0, 0, 600, 500);
        }

        //gameover
        if (gameOverOk[0]) {
            g.drawImage(gameOver[0], 0, 0, this);
        }
        if (gameOverOk[1]) {
            g.drawImage(gameOver[1], 0, 199, this);
        }
        // debug
        /*fill(255, 255, 255);
        text("link.speed : " +" "+ str(link.speed), 200, 10);
         */
    }

    public int getPixel(BufferedImage image,int x,int y) {
        if (x > 0 && x < image.getWidth() && y > 0 && y < image.getHeight()) {
            Raster trame = image.getRaster();
            int[] c = new int[4];
            trame.getPixel(x,y,c);
            Color couleur = new Color(c[0], c[1], c[2], c[3]);
            return couleur.getRGB();
        } else {
            return -1;
        }

    }
}
