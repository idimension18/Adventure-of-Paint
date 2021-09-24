package Paint.Game;

import Paint.Objects.*;
import Paint.Entities.*;
import Sound.*;

import static java.lang.Math.abs;

public class Engine {
    private final int noir;
    private final int jaune;

    Engine() {
        noir = -16777216;
        jaune = -8421505;
    }

    public void Collide(ChargeImage image, Entities client, Level level) {
        //prepare la colision
        for (int i = 0; i < 4; i++) {
            int nPixel;
            if (i == 0 || i == 1) {
                nPixel = client.width;
            } else {
                nPixel = client.height;
            }
            int[] points = new int[]{(client.x + level.mondeX * -1), client.y};
            int[] clientPoint = new int[]{0, 0};
            if (i == 1) {
                points[1] += client.height;
                clientPoint[1] = client.height;
            }
            if (i == 2) {
                points[0] += client.width;
                clientPoint[0] = client.width;
            }
            client.isPose = false;
            //fait la colision
            for (int k = 0; k < nPixel; k++) {
                /////////////////////DESSUS DESSOUS ///////////////////
                if (i == 0 || i == 1) {
                    //la tetes
                    if (image.getPixel(level.mondeBlack,points[0], points[1] +1) == noir && i == 0) {//1toforce
                        client.y -= client.hSaut;
                        client.force = 0;
                        break;
                    }
                    //link touche le sol
                    if (image.getPixel(level.mondeBlack,points[0], points[1]) == noir || image.getPixel(level.mondeBlack,points[0], points[1] + 1) == noir && i == 1) {
                        //debug les pentes et autres
                        if (image.getPixel(level.mondeBlack,points[0], points[1] - 1) == noir && client.name.equals("link") && !client.isLand[1]) {
                            client.y -= 1;
                        }
                        //la procedure normal
                        client.isPose = true;
                        client.force = 0;
                        client.gravity = false;
                        client.marge = 0;

                        //initialisation de link
                        if (client.name.equals("link")) {
                            client.nSaut = 0;
                            client.canSaut = true;
                            client.doSaut = false;
                            client.inertia = false;
                            client.lastObject = null;

                            //gstion des object y
                            for (Objects object : level.objectsList) {
                                if (object.isUsed) {
                                    client.y -= object.speed;
                                }
                            }
                            break;
                        }

                    } else {
                        //client est en l'air
                        if (i == 1 && !client.isPose && !client.isLand[1]) {

                            //l'inertie du personnage
                            if (!client.isLand[0]) {
                                client.inertia = true;
                                if (client.lastObject != null) {
                                    if (client.lastObject.type.equals("Platform")) {
                                        if (client.run) {
                                            client.speed = (client.initialSpeed * 2) + client.lastObject.speed;
                                        } else {
                                            client.speed = client.initialSpeed + client.lastObject.speed;
                                        }
                                        client.lastObject = null;
                                    }
                                }
                            }

                            //calcule de chute pour eviter de traverser le sol
                            client.gravity = true;
                            client.marge = image.getPixel(level.mondeBlack,points[0], points[1] + client.force);
                            if (client.marge == noir) {
                                while (image.getPixel(level.mondeBlack, points[0], points[1] + client.force) == noir) {
                                    client.marge = image.getPixel(level.mondeBlack, points[0], points[1] + client.force);
                                    client.force -= 1;
                                }
                            }

                            //limite le saut
                            if (client.name.equals("link")) {
                                if (client.nSaut > 1) {
                                    client.canSaut = false;
                                }
                            }
                        }
                    }
                    //parcours le carre de link
                    points[0] += 1;
                    clientPoint[0] += 1;
                    //////////////////////LES COTERS/////////////////////////////////
                } else {

                    // les murs et les pentes
                    if (image.getPixel(level.mondeBlack,points[0]+client.speed, points[1]) == noir) {

                        //link se fait ecraser
                        if (client.name.equals("link")) {
                            if (client.isCollide && client.gravity) {
                                client.healtPoint = 0;
                            }
                        }

                        //gestion des pente
                        if (!client.name.equals("link") || !client.doSaut) {
                            if (client.height - clientPoint[1] <= abs(client.speed) * 2 && client.isMoving) {
                                client.y -= client.height - clientPoint[1];
                            } else {
                                if (client.height - clientPoint[1] > abs(client.speed) * 2) {
                                    if (client.name.equals("link")) {
                                        client.x -= client.speed;
                                    } else {
                                        client.speed *= -1;
                                    }
                                }
                            }
                        } else {
                            client.x -= client.speed;
                        }
                        break;
                    }

                    // la fin d'un nivaux
                    if (client.name.equals("link")) {
                        if (image.getPixel(level.mondeBlack,points[0], points[1]) == jaune && i == 2) {
                            level.changeLevel = true;
                            break;
                        }
                    }
                    //parvour le carre de link
                    points[1] += 1;
                    clientPoint[1] += 1;
                }
            }
        }
    }
    ////////////////////////////////////la graviter //////////////////////////////
    public void Gravity(Entities client) {
        if (client.gravity){
            if (client.name.equals("link") && client.doSaut) {
                client.frame = 0.0d;
                if (client.sens.equals("right")){
                    client.doing = 4;
                } else {
                    client.doing = 5;
                }
            }
            if (client.marge != noir) {
                client.force += 1.;
            }
            client.y += client.force;
        }
    }
    /////////////////////////////////////////////les inter colision ///////////////////
    public void interCollide(Level level,Entities client,Sound stomp) {
        /////////COLISION avec les object//////////////////////////
        for (Objects object:level.objectsList) {
            if (object.x - client.x < client.width && client.x - object.x < object.width && abs(client.y - object.y) <= client.height && object.isApeared){

                //quand client se pose sur un Object
                if ((client.force > 0 && client.y <= object.y - client.height + client.force) || object.isUsed) {
                    client.force = 0;

                    //manipule link
                    if (client.name.equals("link")){
                        client.nSaut = 0;
                        client.canSaut = true;
                        client.inertia = false;
                        client.lastObject = object;
                    }

                    //manipule tout le monde
                    client.y = object.y - client.height;
                    client.isLand[0] = client.isLand[1];
                    client.isLand[1] = true;
                    object.isUsed = true;

                    //actionnne les trigger
                    if (!object.isActive && object.type.equals("Trigger")) {
                        object.isActive = true;
                        object.run(level, client);
                        object.channelOrder(level,client,object.channel);
                    }

                    //quand client est a cotter de l'object
                } else if (client.name.equals("link")) {
                    client.x -= client.speed;
                    object.isCollide = true;
                    client.isCollide = true;
                } else {
                    client.speed *= -1 ;
                }

                //quand client n'est plus sur l'object
            } else {
                client.isLand[0] = client.isLand[1];
                client.isLand[1] = false;
                client.isCollide = false;
                for (Objects k:level.objectsList){
                    if (k.isUsed) {
                        client.isLand[1] = true;
                    }
                    if (k.isCollide) {
                        client.isCollide = true;
                    }
                }
                object.isUsed = false;
                object.isCollide = false;
            }
        }

        ///////////////////////COLLISION avec les enemies/////////////////////////////
        for (Entities mob:level.goombaList) {
            if (mob.charged) {
                if (client.force > 0) {
                    if (abs(client.x - mob.x) < client.width - 5 && abs(client.y - mob.y) < client.height) {
                        mob.isDead = true;
                        mob.y += 20;
                        mob.Isstomped = true;
                        stomp.play();
                        if (client.up[1]) {
                            client.force -= client.force + 15 ;
                        } else {
                            client.force -= client.force + 10;
                        }
                        break;
                    }
                }
                if (abs(mob.x - client.x) < mob.width && abs(mob.y - client.y) < mob.height && client.damageTime == 0) {
                    client.healtPoint -= 1;
                    client.damage = true;
                    break;
                }
            }
        }
    }
}


