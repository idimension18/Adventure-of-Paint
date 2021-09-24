package Paint.Entities;

import Paint.Objects.Objects;

import java.awt.*;

public class Entities {
    public Image img[][], vieImg, HPImg;
    public int sheetX, x, y, width, height, speed, initialSpeed, hSaut,nothingTime, damageTime, doing, nSaut, maxVie, vie, maxHealtPoint, healtPoint, force, marge;
    public Boolean run, up[],isPose, canSaut, doSaut, canMove, isMoving, isLand[], isCollide, damage, gravity, charged, isDead, isShown, Isstomped, inertia, ready;
    public String sheet, name, sens;
    public Double frame, deadTime;
    public Objects lastObject;
}
