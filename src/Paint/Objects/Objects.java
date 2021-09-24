package Paint.Objects;

import Paint.Entities.Entities;
import Paint.Game.Level;

import java.awt.*;

public abstract class Objects {
    public Image img;
    public String type, nom;
    public int x, y, width, height, speed, moveTime, timer, channel;
    public Boolean isActive, isApeared, isUsed, isCollide;

    public abstract void run(Level level, Entities client);

    // gere les channel
    public void channelOrder (Level level, Entities client, int triggerChannel) {
        for (Objects object:level.objectsList) {
            if (object.channel == triggerChannel && !object.isActive) {
                object.isActive = true;
                object.isApeared= true;
                object.run(level, client);
            }
        }
    }
}
