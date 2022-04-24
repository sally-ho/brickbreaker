package org.cis120.brickbreaker;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Paddle extends GameObj {
    public static final int WIDTH = 40;
    public static final int HEIGHT = 10;
    public static final int INIT_POS_X = 150;
    public static final int INIT_POS_Y = 250;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private static BufferedImage img;

    public Paddle(int courtWidth, int courtHeight) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, WIDTH, HEIGHT, courtWidth,
                courtHeight
        );
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
