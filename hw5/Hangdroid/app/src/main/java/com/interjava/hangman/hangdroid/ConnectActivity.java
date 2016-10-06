package com.interjava.hangman.hangdroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.interjava.hangman.client.GameStatus;
import com.interjava.hangman.client.HangmanClient;
import com.interjava.hangman.client.HangmanClientConnection;

/**
 * A login screen that offers login via email/password.
 */
public class ConnectActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "test.com:1234", "example.com:1111"
    };
    private static final int CONNECT = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private HangmanClientConnection connection = null;

    // UI references.
    private AutoCompleteTextView mServerView;
    private EditText mPortView;
    private View mProgressView;
    private View mLoginFormView;

    // logic references
    private GameStatus gameStatus;
    private Thread connectionThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        // Set up the login form.
        mServerView = (AutoCompleteTextView) findViewById(R.id.server);

        mPortView = (EditText) findViewById(R.id.port);
        mPortView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptConnect();
                    return true;
                }
                return false;
            }
        });

        Button mConnectButton = (Button) findViewById(R.id.connect_button);
        mConnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptConnect();
            }
        });

        mLoginFormView = findViewById(R.id.connect_form);
        mProgressView = findViewById(R.id.connect_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptConnect() {
        if (connection != null) {
            return;
        }

        // Reset errors.
        mServerView.setError(null);
        mPortView.setError(null);

        // Store values at the time of the login attempt.
        String server = mServerView.getText().toString();
        int port = Integer.parseInt(mPortView.getText().toString());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid address.
        if (TextUtils.isEmpty(server)) {
            mServerView.setError(getString(R.string.error_field_required));
            focusView = mServerView;
            cancel = true;
        } else if (!isConnectValid(server)) {
            mServerView.setError(getString(R.string.error_invalid_connect));
            focusView = mServerView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            gameStatus = new GameStatus();

//            connection = new HangmanClientConnection(this, server, port);
//            connectionThread = new Thread(connection);
//            connectionThread.start();

            Button mConnectButton = (Button) findViewById(R.id.connect_button);
            Intent myIntent = new Intent(mConnectButton.getContext(), MainActivity.class);
            myIntent.putExtra("game_status", gameStatus);
            myIntent.putExtra("server", server);
            myIntent.putExtra("port", port);
            startActivityForResult(myIntent, CONNECT);
        }
    }

    private boolean isConnectValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && CONNECT == requestCode) {
            if (resultCode == Activity.RESULT_CANCELED) {
                final String error = data.getStringExtra(MainActivity.RESULT);
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        mServerView.setError(error);
                    }
                });
            } else if(resultCode == Activity.RESULT_OK) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                    }
                });
            }
        } else {
            showProgress(false);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

