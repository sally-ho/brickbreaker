package org.cis120.brickbreaker;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court.
 */
public class Ball extends GameObj {
    public static final int SIZE = 10;
    public static final int INIT_POS_X = 170;
    public static final int INIT_POS_Y = 240;
    public static final int INIT_VEL_X = 2;
    public static final int INIT_VEL_Y = 6;

    public Ball(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    // bounce method in gameobj class handles bouncing off wall, this one handles
    // bouncing off bricks
    public void bounce(Brick b, Direction d) {
        int isCorner = 0;
        // if the ball hits the corner of the brick
        // Top-left
        if (getPx() + getWidth() == b.getPx() && getPy() + getHeight() == b.getPy()) {
            isCorner = 1;
        } else if (getPx() == (b.getPx() + b.WIDTH) && getPy() + getHeight() == b.getPy()) {
            //top right
            isCorner = 1;
        } else if (getPx() + getWidth() == b.getPx() && getPy() == b.getPy() + b.HEIGHT) {
            //bottom left
            isCorner = 1;
        } else if (getPx() == (b.getPx() + b.WIDTH)
                && getPy() + getHeight() == b.getPy() + b.HEIGHT) {
            //bottom right
            isCorner = 1;
        }

        switch (isCorner) {
            case 1: // Hits a corner
                setVx(getVx() * -1);
                setVy(getVy() * -1);
                move();
                break;
            default: // all other cases, just use the parent class method
                super.bounce(d);
        }
    }
}