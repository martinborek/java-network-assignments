package com.interjava.hangman.hangdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.interjava.hangman.client.GameStatus;
import com.interjava.hangman.client.HangmanClient;
import com.interjava.hangman.client.HangmanClientConnection;

public class MainActivity extends AppCompatActivity implements HangmanClient {
    public static final String RESULT = "result";
    // logic references
    private GameStatus gameStatus;
    private Thread connectionThread;
    private HangmanClientConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i = getIntent();
        gameStatus = (GameStatus) i.getSerializableExtra("game_status");

        String server = (String) i.getSerializableExtra("server");
        int port = (int) i.getSerializableExtra("port");

        connection = new HangmanClientConnection(this, server, port);
        connectionThread = new Thread(connection);
        connectionThread.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGameAction();
            }
        });

        Button mConnectButton = (Button) findViewById(R.id.guess_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guessAction();
            }
        });

        Button abortButton = (Button) findViewById(R.id.cancel_button);
        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void guessAction() {
        TextView attempt = (TextView) findViewById(R.id.letter);
        String guessedCharacter = attempt.getText().toString();
        if (guessedCharacter.isEmpty()) {
            attempt.setError("Please fill a letter or a word");
            return;
        }

        ProgressBar progressView = (ProgressBar) findViewById(R.id.guess_progress);
        progressView.setVisibility(View.VISIBLE);

        TextView guessingText = (TextView) findViewById(R.id.guess_label);
        guessingText.setText(getString(R.string.guessing));
        disableGuess();
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.VISIBLE);

        //This flag causes that the new thread calls method sendCharacter()
        connection.setSendCharacterFlag(guessedCharacter);
        connectionThread = new Thread(connection);
        connectionThread.start();
    }

    private void disableGuess() {
        Button guessButton = (Button) findViewById(R.id.guess_button);
        guessButton.setEnabled(false);
        TextView letterText = (TextView) findViewById(R.id.letter);
        letterText.setEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(false);
    }

    private void enableGuess() {
        Button guessButton = (Button) findViewById(R.id.guess_button);
        guessButton.setEnabled(true);
        TextView letterText = (TextView) findViewById(R.id.letter);
        letterText.setEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(true);
    }

    private void newGameAction() {
        if (!gameStatus.isLost() && !gameStatus.isWin()) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New game")
                    .setMessage("This would close the current game. Are you sure you want to start a new game?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with new game

                            newGame();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            newGame();
        }
    }

    private void newGame() {

        ProgressBar progressView = (ProgressBar) findViewById(R.id.guess_progress);
        progressView.setVisibility(View.GONE);

        TextView guessingText = (TextView) findViewById(R.id.guess_label);
        guessingText.setText(getString(R.string.newgame));

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.VISIBLE);
        disableGuess();

        gameStatus = new GameStatus();
        //This flag causes that the new thread calls method newGame()
        connection.setNewGameFlag();
        connectionThread = new Thread(connection);
        connectionThread.start();
    }

    @Override
    public void updateEvent() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ProgressBar connectProgressView = (ProgressBar) findViewById(R.id.connect_game_progress);
                connectProgressView.setVisibility(View.GONE);

                View gameForm = findViewById(R.id.game_form);
                gameForm.setVisibility(View.VISIBLE);

                ProgressBar progressView = (ProgressBar) findViewById(R.id.guess_progress);
                progressView.setVisibility(View.GONE);
                Button cancelButton = (Button) findViewById(R.id.cancel_button);
                cancelButton.setVisibility(View.GONE);
                TextView guessingText = (TextView) findViewById(R.id.guess_label);
                guessingText.setText("");

                enableGuess();

                TextView attemptsText = (TextView) findViewById(R.id.attempts);
                attemptsText.setText(getString(R.string.attempts) + gameStatus.getAttempts());

                TextView wordText = (TextView) findViewById(R.id.word);
                wordText.setText(gameStatus.getWord());

                TextView scoreText = (TextView) findViewById(R.id.score);
                scoreText.setText(getString(R.string.score) + gameStatus.getScore());

                TextView attempt = (TextView) findViewById(R.id.letter);
                attempt.setText("");

                if (gameStatus.isWin() || gameStatus.isLost()) {
                    disableGuess();
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setEnabled(true);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Result")
                            .setMessage(gameStatus.getMessage())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            }
        });

    }

    @Override
    public void connectErrorAction(final String s) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT, s);
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
