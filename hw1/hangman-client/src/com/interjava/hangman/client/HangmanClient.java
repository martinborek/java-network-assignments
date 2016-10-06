/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.hangman.client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Main class for Hangman client.
 * 
 * @author Martin Borek (mborekcz@gmail.com)
 * @date 19/Nov/2015
 * @file HangmanClient.java
 */
public class HangmanClient extends JPanel {

    private HangmanClientConnection connection;
    private Thread connectionThread;
    GameStatus gameStatus;

	private JButton connectButton;
	private JButton newGameButton;
	private JButton sendButton;
	private JTextField serverText;
	private JTextField portText;
	private JTextField characterText;
	private JLabel gameLabel;
	private JLabel remainingAttemptsLabel;
    private JLabel connectInfoLabel;
    private JLabel totalScoreLabel;
    private JLabel gameInfoLabel;
    private JButton connectCancelButton;
    private JButton guessCancelButton;

    private JProgressBar connectProgressBar;
    private JProgressBar gameProgressBar;

    private CardLayout mainLayout;

	HangmanClient() {
		buildGui();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Hangman");
		frame.setContentPane(new HangmanClient());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
    /**
     * Creates GUI for the Hangman client. 
     */
	private void buildGui() {
        mainLayout = new CardLayout();
        setLayout(mainLayout);
        setBackground(java.awt.Color.lightGray);
        add(createConnectPanel(), "connectPanel");
        add(createGamePanel(), "gamePanel");
	}

    /**
     * Creates a panel for connecting to server.
     * 
     * @return Created panel
     */
    private JComponent createConnectPanel() {
        JPanel connectPanel = new JPanel(new GridBagLayout());

        JPanel connectSubPanel = new JPanel (new GridLayout(2, 1));

        JPanel connectFormPanel = new JPanel ();
        connectFormPanel.add(new JLabel("Server: "));
        serverText = new JTextField("localhost");
        serverText.setColumns(20);
        connectFormPanel.add(serverText);

        connectFormPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        connectFormPanel.add(new JLabel("Port: "));
        portText = new JTextField("6666");
        portText.setColumns(5);
        connectFormPanel.add(portText);

        connectFormPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        connectButton = new JButton("Connect");
        connectFormPanel.add(connectButton);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                connectAction();
            }
        });

        connectSubPanel.add(connectFormPanel);
        
        JPanel connectInfoPanel = new JPanel();
        connectInfoLabel = new JLabel();
        connectInfoLabel.setFont(new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize()));
        connectInfoPanel.add(connectInfoLabel);

        connectProgressBar = new JProgressBar();
        connectInfoPanel.add(connectProgressBar);

        connectCancelButton = new JButton("Abort");
        connectCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                connectCancelAction();            
            }
        });
        connectInfoPanel.add(connectCancelButton);

        connectSubPanel.add(connectInfoPanel);

        connectPanel.add(connectSubPanel);

        displayInitialConnectPanel();

        return connectPanel;
    }

    /**
     * Sets GUI to the initial state; form for connecting to server.
     */
    private void displayInitialConnectPanel()
    {
        connectInfoLabel.setVisible(false);
        connectProgressBar.setVisible(false);
        connectCancelButton.setVisible(false);
        connectButton.setEnabled(true);
        serverText.setEnabled(true);
        portText.setEnabled(true);

        mainLayout.show(this, "connectPanel");
    }

    /**
     * Creates a panel for the actual game (after connection has been established).
     * 
     * @return Created panel
     */
    private JComponent createGamePanel() {
        JPanel gamePanel = new JPanel();
        GridLayout mainGameLayout = new GridLayout(3, 1);
        gamePanel.setLayout(mainGameLayout);

        gameLabel = new JLabel("------", SwingConstants.CENTER);
        gameLabel.setFont(new Font(this.getFont().getName(), Font.BOLD, 30));
        gamePanel.add(gameLabel);

        JPanel guessPanel = new JPanel(new GridLayout(2,1));
        JPanel guessSubPanel = new JPanel();
        guessSubPanel.add(new JLabel("Letter / word: "));
        characterText = new JTextField();
        characterText.setColumns(10);
        guessSubPanel.add(characterText);

        guessSubPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        sendButton = new JButton("Guess");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                guessAction();            
            }
        });
        guessSubPanel.add(sendButton);
        
        guessSubPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        remainingAttemptsLabel = new JLabel ("(Attempts: 10)");
        guessSubPanel.add(remainingAttemptsLabel);

        guessPanel.add(guessSubPanel);

        JPanel gameInfoPanel = new JPanel();
        gameInfoLabel = new JLabel("Guessing");
        gameInfoLabel.setFont(new Font(this.getFont().getName(), Font.ITALIC, this.getFont().getSize()));
        gameInfoPanel.add(gameInfoLabel);

        gameProgressBar = new JProgressBar();
        gameInfoPanel.add(gameProgressBar);
        guessCancelButton = new JButton("Abort");
        guessCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                guessCancelAction();            
            }
        });
        gameInfoPanel.add(guessCancelButton);

        guessPanel.add(gameInfoPanel);
        gamePanel.add(guessPanel);

        JPanel newGameBagPanel = new JPanel(new GridBagLayout());
        JPanel newGameSubPanel = new JPanel(new GridLayout(2,1));
        newGameButton = new JButton("New game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                newGameAction();            
            }
        });
        //newGameButton.getPreferredSize();
        newGameSubPanel.add(newGameButton);

        totalScoreLabel = new JLabel("Total score: 0");
        newGameSubPanel.add(totalScoreLabel);

        newGameBagPanel.add(newGameSubPanel);

        gamePanel.add(newGameBagPanel);

        gameInfoLabel.setVisible(false);
        gameProgressBar.setVisible(false);
        guessCancelButton.setVisible(false);

        return gamePanel;
    }
    
    /**
     * Handles connecting to a server.
     */
    private void connectAction()
    {
        connectInfoLabel.setVisible(true);
        connectInfoLabel.setForeground(Color.BLACK);
        connectInfoLabel.setText("Connecting");
        connectProgressBar.setVisible(true);
        connectProgressBar.setIndeterminate(true);
        connectCancelButton.setVisible(true);
        connectButton.setEnabled(false);
        serverText.setEnabled(false);
        portText.setEnabled(false);
        String server; 
        int port;
        try {
            server = serverText.getText();
            port = Integer.parseInt(portText.getText());
        }  catch (Exception e) {
            connectErrorAction("Wrong server or port format.");
            return;
        }
        gameStatus = new GameStatus();

        connection = new HangmanClientConnection(HangmanClient.this, server, port);
        connectionThread = new Thread(connection);
        connectionThread.start();
    }

    /**
     * Displays connection error message and sets GUI accordingly.
     *
     * @param message Error message
     */
    public void connectErrorAction(String message)
    {
        connectInfoLabel.setVisible(true);
        connectInfoLabel.setForeground(Color.RED);
        connectInfoLabel.setText(message);
        connectProgressBar.setVisible(false);
        connectCancelButton.setVisible(false);
        connectButton.setEnabled(true);
        serverText.setEnabled(true);
        portText.setEnabled(true);
    }
    
    /**
     * Updates game status (GUI)
     */
    public void updateEvent()
    {
        gameInfoLabel.setVisible(false);
        gameProgressBar.setVisible(false);
        guessCancelButton.setVisible(false);
        sendButton.setEnabled(true);
        characterText.setEnabled(true);
        characterText.setText("");
        newGameButton.setEnabled(true);

        remainingAttemptsLabel.setText("(Attempts: " + Integer.toString(gameStatus.getAttempts()) + ")");
        gameLabel.setText(gameStatus.getWord());
        totalScoreLabel.setText("Total score: "+gameStatus.getScore());

        if (gameStatus.isWin() || gameStatus.isLost()) {

            /**
            if (gameStatus.isWin())
                gameInfoLabel.setForeground(Color.GREEN);
            else
                gameInfoLabel.setForeground(Color.RED);
          
            gameInfoLabel.setText(gameStatus.getMessage());
            gameInfoLabel.setVisible(true);
            */

            JOptionPane.showMessageDialog(this.getParent(), gameStatus.getMessage());
            sendButton.setEnabled(false);
            characterText.setEnabled(false);
        }

        mainLayout.show(this, "gamePanel");
    }

    /**
     * Cancels connection initialization.
     */
    private void connectCancelAction()
    {
        connectionThread.interrupt();
        connection.disconnect();
        displayInitialConnectPanel();
    }

    /**
     * Guessing a letter or a word.
     */
    private void guessAction()
    {

        String guessedCharacter = characterText.getText();
        if (guessedCharacter.isEmpty())
        {
            gameInfoLabel.setForeground(Color.RED);
            gameInfoLabel.setText("Please fill a letter or a word");
            gameInfoLabel.setVisible(true);
            return;
        }

        gameInfoLabel.setForeground(Color.BLACK);
        gameInfoLabel.setText("Guessing");
        gameInfoLabel.setVisible(true);
        gameProgressBar.setVisible(true);
        gameProgressBar.setIndeterminate(true);
        guessCancelButton.setVisible(true);
        sendButton.setEnabled(false);
        characterText.setEnabled(false);
        newGameButton.setEnabled(false);
        
        String character = characterText.getText();

        //This flag causes that the new thread calls method sendCharacter()
        connection.setSendCharacterFlag(guessedCharacter);  
        connectionThread = new Thread(connection);
        connectionThread.start();
    }

    /**
     * Starts a new game.
     */
    private void newGameAction()
    {
        if (!gameStatus.isLost() && !gameStatus.isWin()) {
           int dialogResult = JOptionPane.showConfirmDialog(this.getParent(),
               "This would close the current game. Are you sure you want to start a new game?", "New game",
               JOptionPane.YES_NO_OPTION);

           if (dialogResult == JOptionPane.NO_OPTION)
               return;
        }

        gameInfoLabel.setForeground(Color.BLACK);
        gameInfoLabel.setText("Creating new game");
        gameInfoLabel.setVisible(true);
        gameProgressBar.setVisible(true);
        gameProgressBar.setIndeterminate(true);
        guessCancelButton.setVisible(true);
        sendButton.setEnabled(false);
        characterText.setEnabled(false);
        characterText.setEnabled(false);
        newGameButton.setEnabled(false);

        gameStatus = new GameStatus();
        //This flag causes that the new thread calls method newGame()
        connection.setNewGameFlag();
        connectionThread = new Thread(connection);
        connectionThread.start();
    }

    /**
     * Cancels guessing a letter / word.
     */
    private void guessCancelAction()
    {
        connectionThread.interrupt();
        connection.disconnect();
        displayInitialConnectPanel();
    }
}
