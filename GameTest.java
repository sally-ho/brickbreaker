package org.cis120.brickbreaker;

import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    @Test
    public void durabilityDecrements() {
        Brick brick = new Brick(0, 0, 30, 15, 3);
        brick.hit();
        assertEquals(brick.getDurability(), 2);
    }

    @Test
    public void changesDirectionHittingLeftWall() {
        Ball ball = new Ball(690, 300);
        ball.bounce(Direction.LEFT);
        assertEquals(ball.getVx(), 2);
        assertEquals(ball.getVy(), 6);
    }

    @Test
    public void changesDirectionHittingRightWall() {
        Ball ball = new Ball(690, 300);
        ball.bounce(Direction.RIGHT);
        assertEquals(ball.getVx(), -2);
        assertEquals(ball.getVy(), 6);
    }

    @Test
    public void changesDirectionHittingBrickNonCorner() {
        Ball ball = new Ball(690, 300);
        Brick[][] bricks = new Brick[12][5];
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                bricks[i][j] = new Brick(i * 30, j * 15, 690, 300, 1);
            }
        }
        ball.bounce(bricks[0][0], Direction.RIGHT);
        assertEquals(ball.getVx(), -2);
        assertEquals(ball.getVy(), 6);
    }

    @Test
    public void changesDirectionHittingBrickCorner() {
        Ball ball = new Ball(690, 300);
        System.out.println(ball.getHeight());
        ball.setPx(30);
        ball.setPy(5);
        Brick brick = new Brick(0, 0, 690, 300, 1);
        ball.bounce(brick, Direction.RIGHT);
        assertEquals(ball.getVx(), -2);
        assertEquals(ball.getVy(), -6);
    }

    @Test
    public void gameEndsWithNoMoreBricks() {
        final JLabel status = new JLabel("Running...");
        JLabel player = new JLabel("Player: ");
        JLabel scoreboard = new JLabel("   Score: " + 0 + " points     ");
        JLabel lifeCount = new JLabel("   Lives: 3");
        GameCourt court = new GameCourt(status, player, scoreboard, lifeCount);
        // once all the bricks are gone, the "clear" boolean field should be set to
        // true, in which case playing should be false
        assertTrue(court.getClear());
        assertFalse(court.getPlaying());
    }

}
