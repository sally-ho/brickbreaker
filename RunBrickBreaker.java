package org.cis120.brickbreaker;

// imports necessary libraries for Java swing

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunBrickBreaker implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("BrickBreaker");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // instructions popup
        final JFrame instructions = new JFrame("Instructions");
        instructions.setMinimumSize(new Dimension(420, 320));
        instructions.setLocation(300, 175);
        final JPanel instructs = new JPanel();
        instructs.setBackground(Color.WHITE);
        JLabel label = new JLabel(
                "<html>Welcome to BrickBreaker! <br/><html/> " +
                        "<html>Use the left and right arrow keys to control the white paddle." +
                        "<br/><html/>"
                        +
                        "<html>Clear as many bricks as you can; the darker bricks take more " +
                        "hits to clear, "
                        +
                        "but are worth more points.<br/><html/> "
                        +
                        "<html>You have three lives – " +
                        "you lose a life if the ball hits the bottom of the screen.<br/><html/>"
        );
        instructs.add(label);
        instructions.add(instructs, BorderLayout.CENTER);
        instructions.setBackground(Color.WHITE);
        // Set visible on screen
        instructions.pack();
        instructions.setVisible(true);

        // Player label
        final JLabel player = new JLabel("Player: ");
        control_panel.add(player);

        // Score label
        final JLabel scoreboard = new JLabel("   Score: " + 0 + " points     ");
        control_panel.add(scoreboard);

        // Lives label
        final JLabel lifeCount = new JLabel("   Lives: 3");
        control_panel.add(lifeCount);

        // Main playing area
        final GameCourt court = new GameCourt(status, player, scoreboard, lifeCount);
        frame.add(court, BorderLayout.CENTER);

        // leaderboard popup
        final JFrame leaderboard = new JFrame("Leaderboard");
        leaderboard.setMinimumSize(new Dimension(420, 320));
        leaderboard.setLocation(400, 175);
        final JPanel brd = new JPanel();
        brd.setBackground(Color.WHITE);
        brd.setLayout(new BoxLayout(brd, BoxLayout.Y_AXIS));
        String str = "";
        ArrayList<String> highscores = court.getLeaderboard();
        for (String entry : highscores) {
            JLabel lbl = new JLabel("<html>" + entry + "<br/></html>");
            brd.add(lbl);
        }

        leaderboard.add(brd, BorderLayout.CENTER);
        leaderboard.setBackground(Color.WHITE);
        // Set visible on screen
        leaderboard.pack();
        leaderboard.setVisible(false);

        // buttons
        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        final JButton inst = new JButton("Instructions");
        inst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instructions.setVisible(true);
            }
        });
        control_panel.add(inst);

        final JButton lbd = new JButton("Leaderboard");
        lbd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leaderboard.setVisible(true);
            }
        });
        control_panel.add(lbd);

        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        control_panel.add(quit);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}