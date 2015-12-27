package com.example.giannis.dtu_basketball;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.giannis.dtu_basketball.MESSAGE";
    private MalibuCountDownTimer quarterTimer;
    private long timeRemaining;
    private Button startButton,stopButton,p1Button,p2Button,p3Button,p4Button,p5Button,p6Button,p7Button,p8Button,p9Button,p10Button,p11Button,p12Button;
    private TextView timeElapsedView;
    //Declare a variable to hold count down timer's paused status
    private boolean isPaused = false;
    private boolean isCancelled= false;
    //Time set to 40'
    private final long gameDuration = 2400000;
    private final long interval = 1000;
    final long MINUTES_IN_AN_HOUR = 60;
    final long MILLISECONDS_IN_A_MINUTE = 60000;
    // Players/Team
    Map<Integer,Player> teamMap = new HashMap<Integer,Player>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Set player's numbers
        initializePlayers();

        timeElapsedView = (TextView) this.findViewById(R.id.timeElapsed);
        startButton=(Button) this.findViewById(R.id.buttonStart);
        stopButton=(Button) this.findViewById(R.id.buttonStop);
    }
    // Initialize the team.
    private void initializePlayers() {
        for(int i=4;i<=15;i++) {
            teamMap.put(i, new Player(i));
        }
    }

    private void onPlayerClick(View view) {
        // @todo implement logic here. Toggle players according to the isPlaying parameter.
    }

    public void onStart(View view) {
        Log.i("onStart", "Start");

        startButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        //Initialize a new CountDownTimer instance
        if (isPaused && !isCancelled) {
            // Continue counting down.
            quarterTimer = new MalibuCountDownTimer(timeRemaining, interval);
        }
        else {
            // Create new counter.
            quarterTimer = new MalibuCountDownTimer(gameDuration, interval);
        }
        quarterTimer.start();
        isPaused = false;
        isCancelled = false;
    }
    public void onStop(View view) {
        Log.i("onStop", "Stop");
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        isPaused = true;
    }
    public void onRestart(View view) {
        Log.i("onRestart", "Restart");
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        // Restart timer
        timeElapsedView.setText("Countdown:" + timeConversion(gameDuration));
        isCancelled = true;
    }
    // CountDownTimer class
    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            timeElapsedView.setText("Quarter Finished!");
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            //Do something in every tick
            if (isPaused)
            {
                Log.i("Paused:", Long.toString(millisUntilFinished));
                //If user requested to pause or cancel the count down timer
                timeRemaining = millisUntilFinished;
                timeElapsedView.setText("Stopped at:" + timeConversion(timeRemaining));
                cancel();
            }
            else if(isCancelled) {
                Log.i("Paused:", Long.toString(millisUntilFinished));
                //If user requested to pause or cancel the count down timer
                timeRemaining = millisUntilFinished;
                timeElapsedView.setText("Stopped at:" + timeConversion(timeRemaining));
                cancel();
            }
            else {
                Log.i("onTick2:", Long.toString(millisUntilFinished));
                timeRemaining = millisUntilFinished;
                timeElapsedView.setText("Countdown: " + timeConversion(timeRemaining));

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private String timeConversion(long milliseconds) {

        long seconds = milliseconds % MILLISECONDS_IN_A_MINUTE;
        long totalMinutes = milliseconds/ MILLISECONDS_IN_A_MINUTE;
        long minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        String secondsString = Long.toString(seconds);
        return minutes + "\'" + secondsString.substring(0, Math.min(secondsString.length(), 2)) + "\"";
    }
}
