package org.cis120.brickbreaker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Ball ball; // the ball, bounces
    private Brick[][] bricks; // bricks that don't move, but disappear when their durability hits 0
    private Paddle paddle; // the paddle that the user controls with a keyboard
    private String name; // name of the current player
    private int lives; // number of lives left; game ends when this hits 0
    private int score; // current score of player
    private boolean clear = true; // keeps track of whether the board is clear -- if it is,
                                  // then end the game

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."
    private JLabel scoreboard; // current score
    private JLabel player; // name of the current player
    private JLabel lifeCount; // number of lives left

    // Game constants
    public static final int COURT_WIDTH = 690;
    public static final int COURT_HEIGHT = 300;
    public static final int PADDLE_VELOCITY = 5;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 25;

    public GameCourt(JLabel status, JLabel scoreboard, JLabel player, JLabel lifeCount) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.setVx(-PADDLE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.setVx(PADDLE_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                paddle.setVx(0);
            }
        });

        setBackground(Color.BLACK);
        score = 0;
        this.status = status;
        this.scoreboard = scoreboard;
        this.player = player;
        this.lifeCount = lifeCount;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        // resetting objects
        paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT);
        ball = new Ball(COURT_WIDTH, COURT_HEIGHT);
        bricks = new Brick[23][5];
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                // generate random strengths, but making the less durable bricks more likely
                int num = (int) ((Math.random() * 60) + 1);
                int strength;
                if (num >= 55) {
                    strength = 5;
                } else if (num >= 45) {
                    strength = 4;
                } else if (num >= 35) {
                    strength = 3;
                } else if (num >= 25) {
                    strength = 2;
                } else {
                    strength = 1;
                }
                bricks[i][j] = new Brick(i * 30, j * 15, COURT_WIDTH, COURT_HEIGHT, strength);
            }
        }

        // resetting local variables
        score = 0;
        lives = 3;
        playing = false;

        // resetting labels
        status.setText("Press enter to start a new game!");
        player.setText("Player: ");
        scoreboard.setText("Score: 0");
        lifeCount.setText("Lives: 3");

        repaint();

        // ask for user's name
        JPanel myPanel = new JPanel(new GridLayout(1, 1, 0, 1));
        myPanel.add(new Label("Name: "));
        String username = JOptionPane.showInputDialog(myPanel);
        name = username;
        // starts game once user hits enter
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    playing = true;
                    status.setText("Playing Breakout...");
                    player.setText("Player: " + name);
                }
            }
        });

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            // advance the paddle and ball in their current direction.
            paddle.move();
            ball.move();

            // make the ball bounce off the paddle
            if (ball.intersects(paddle)) {
                ball.bounce(Direction.DOWN);
            }

            // make the ball bounce off walls except bottom one; lose a life if it hits
            // bottom wall
            Direction dir = ball.hitWall();
            if (dir != null && dir.equals(Direction.DOWN)) {
                lives--;
                lifeCount.setText("Lives:  " + lives);
                // reset paddle and ball
                paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT);
                ball = new Ball(COURT_WIDTH, COURT_HEIGHT);
                if (lives <= 0) {
                    endGame(score, name);
                    status.setText("You lost! Press reset to start a new game.");
                    // //starts new game once user hits enter
                    // addKeyListener(new KeyAdapter() {
                    // public void keyPressed(KeyEvent e) {
                    // if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // reset();
                    // }
                    // }
                    // });
                }
            } else if (dir != null) {
                ball.bounce(dir);
            }

            // check for ball hitting brick and update score
            for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[0].length; j++) {
                    Brick brick = bricks[i][j];
                    if (brick != null) {
                        if (brick.getDurability() > 0) {
                            clear = false; // if there's a durable brick out there, the board is not
                                           // clear
                        }
                        if (ball.intersects(brick)) {
                            ball.bounce(brick, ball.hitObj(brick)); // call the method in ball.java
                            score += brick.getDurability();
                            brick.hit(); // decrementing durability
                            if (brick.getDurability() <= 0) {
                                ball.bounce(ball.hitObj(brick));
                                bricks[i][j] = null;
                            }
                            scoreboard.setText("Score: " + score);
                        }
                    }
                }
            }

            if (clear) {
                endGame(score, name);
                status.setText("You won!");
            }

            // update the display
            repaint();
        }
    }

    public void endGame(int score, String name) {
        try {
            playing = false;
            ArrayList<String> leaderboard = getLeaderboard();
            // System.out.println (leaderboard);
            ArrayList<Integer> scores = getScores(leaderboard);
            // System.out.println (scores);
            if (isHighScore(score, scores)) {
                int place = getLeaderboardPlace(score, scores);
                // System.out.println (place);
                editLeaderboard(score, name, place, leaderboard);
                // System.out.println (getLeaderboard());
                status.setText("Score: " + score + ". Congrats on your high score!");
            } else {
                status.setText("Score: " + score + ". Press enter to play again.");
            }
            return;
        } catch (IOException e) {
            System.out.println("endGame error");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, COURT_WIDTH, COURT_HEIGHT);

        // draw objects
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] != null) {
                    bricks[i][j].draw(g);
                }
            }
        }
        paddle.draw(g);
        ball.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    public static boolean isHighScore(int score, ArrayList<Integer> scores) {

        if (scores.size() < 5) {
            return true;
        } else {
            return score > scores.get(scores.size() - 1);
        }

    }

    public static int getLeaderboardPlace(int score, ArrayList<Integer> scores) {
        for (int i = 0; i < scores.size(); i++) {
            if (score > scores.get(i)) {
                return i;
            }
        }
        return scores.size(); // Returns to be the last index
    }

    public static ArrayList<Integer> getScores(ArrayList<String> list) {
        ArrayList<Integer> scores = new ArrayList<Integer>();
        try {
            for (String line : list) {
                if (!line.equals("")) {
                    Integer score = Integer.parseInt(line.substring(line.indexOf(',') + 2));
                    scores.add(score);
                }
            }
            return scores;
        } catch (Exception e) {
            return scores;
        }
    }

    public static ArrayList<String> getLeaderboard() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            FileReader f = new FileReader("leaderboard.txt");
            BufferedReader br = new BufferedReader(f);
            String r = br.readLine();
            while (r != null) {
                list.add(r);
                r = br.readLine();
            }
            br.close();
            return list;
        } catch (Exception e) {
            return list;
        }
    }

    public static void editLeaderboard(
            int score, String name,
            int index, ArrayList<String> entries
    ) throws IOException {
        String entry = name + ", " + score + '\n';
        FileWriter fw = new FileWriter("leaderboard.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        if (entries.size() == 0) {
            bw.write(entry);
            bw.close();
            return;
        }
        // rewriting the scores before the one to be inserted
        for (int i = 0; i < index; i++) {
            bw.write(entries.get(i) + '\n');
        }
        bw.write(entry);
        // rewriting the scores after the one to be inserted
        for (int i = index; i < 4; i++) {
            if (entries.get(i) != null) {
                bw.write(entries.get(i) + '\n');
            }
        }
        bw.close();
        return;
    }

    public boolean getClear() {
        return clear;
    }

    public boolean getPlaying() {
        return playing;
    }
}