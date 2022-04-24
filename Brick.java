package org.cis120.brickbreaker;

import java.awt.*;

public class Brick extends GameObj {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 15;
    private int durability;

    // position variables
    private int x;
    private int y;

    public Brick(int x, int y, int courtWidth, int courtHeight, int sturdy) {
        super(0, 0, x, y, WIDTH, HEIGHT, courtWidth, courtHeight);
        // clipping values
        if (x <= 0) {
            this.x = 1;
        } else {
            this.x = x;
        }
        if (y <= 0) {
            this.y = 1;
        } else {
            this.y = y;
        }
        if (sturdy <= 0) {
            durability = 1;
        } else {
            durability = sturdy;
        }
    }

    @Override
    public void draw(Graphics g) {
        // draw border
        g.setColor(Color.white);
        g.drawRect(x, y, WIDTH, HEIGHT);
        // setting color of brick based on durability
        if (durability >= 5) {
            g.setColor(new Color(0, 20, 30));
        } else if (durability == 4) {
            g.setColor(new Color(21, 52, 80));
        } else if (durability == 3) {
            g.setColor(new Color(41, 64, 82));
        } else if (durability == 2) {
            g.setColor(new Color(68, 114, 148));
        } else {
            g.setColor(new Color(143, 188, 219));
        }
        g.fillRect(x + 1, y + 1, WIDTH - 2, HEIGHT - 2);
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int sturdy) {
        durability = sturdy;
    }

    public void hit() {
        durability--;
    }
}