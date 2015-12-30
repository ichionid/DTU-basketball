package com.example.giannis.dtu_basketball;

import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
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
    // Create necessary data structures, team,subs and active squad.
    ArrayList<Sub> subs= new ArrayList<Sub>();
    Map<Integer,Player> teamMap = new HashMap<Integer,Player>();
    ArrayList<ActiveSquad> activeSquads = new ArrayList<ActiveSquad>();
    // activeSquads will always keep a copy of the current activeSquad on top.
    private ActiveSquad activeSquad;
    // activeSquad will always keep the active squad live.
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
        startButton = (Button) this.findViewById(R.id.buttonStart);
        stopButton = (Button) this.findViewById(R.id.buttonStop);
        activeSquad = new ActiveSquad();
    }
    // Initialize the team.
    private void initializePlayers() {
        for(int i=4;i<=15;i++) {
            teamMap.put(i, new Player(i));
        }
    }

    public void onPlayerClick(View view) {
        // @todo implement logic here. Toggle players according to the isPlaying parameter.
        Button b = (Button)view;
        Integer playerNumber = Integer.parseInt(b.getText().toString());
        Player p = teamMap.get(playerNumber);

        if (p.isPlaying()) {
           // Sub player out.
            b.getBackground().setColorFilter(Color.parseColor("#d3d3d3"), PorterDuff.Mode.MULTIPLY);
            p.setPlaying(false);
            p.getTimeEnteredCourt();
            // Register sub with time stamp
            Sub s = new Sub();
            s.setTimeSubedIn(p.getTimeEnteredCourt());
            s.setTimeSubedOut(timeRemaining);
            subs.add(s);
            // Remove from active squad.

            activeSquad.removePlayer(p.getNumber());
        }
        else {
            // Sub player in
            b.getBackground().setColorFilter(Color.parseColor("#009933"), PorterDuff.Mode.MULTIPLY);
            p.setPlaying(true);
            p.setTimeEnteredCourt(timeRemaining);
            // Add to active squad
            activeSquad.addPlayer(p);
        }
    }

    public void onStart(View view) throws CloneNotSupportedException {
        Log.i("onStart", "Start");
        // Start only if 5 players are available
        if (activeSquad.getPlayers().size() == 5) {
            Log.i("onStart", "5 players are registered!");
            // Check is squad exists already
            if (activeSquads.size()==0) {
                Log.i("Starting squad", "!!!");
                // Starting squad
                activeSquad.setStartTime(timeRemaining);
                // Active squad will also be stored at the active squads list
                ActiveSquad clonedSquad = new ActiveSquad();
                clonedSquad.cloneObject(activeSquad);
                activeSquads.add(clonedSquad);
            } else if(activeSquadIsNew()) {
                Log.i("New squad", "!!!");
                // New squad. Set the stop time in the old one and store it.
                activeSquads.get(activeSquads.size() - 1).setStopTime(timeRemaining);
                // Set new start time for the active squad.
                activeSquad.setStopTime(timeRemaining);
                // Add a copy of the new squad
                ActiveSquad clonedSquad = new ActiveSquad();
                clonedSquad.cloneObject(activeSquad);
                activeSquads.add(clonedSquad);
            }
            printAllActiveSquads();
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
            // If we have 5 players we can register .
        }
    }

    /**
     * Function checking whether or not a squad on start time is new.
     * @return boolean
     */
    private boolean activeSquadIsNew() {

        ActiveSquad lastRegisteredSquad = activeSquads.get(activeSquads.size() - 1);
        Map<Integer, Player> tempActiveSquadPlayers = activeSquad.getPlayers();
        Map<Integer, Player> tempLastRegSquadPlayers = activeSquad.getPlayers();
        // Check if all players are registered. If not return FALSE
        for (Map.Entry<Integer, Player> entry : tempActiveSquadPlayers.entrySet()) {
            Player tempP = tempLastRegSquadPlayers.get(entry.getKey());
            if (tempP == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function.
     */
    private void printSquadPlayers(ActiveSquad as) {
        for (Map.Entry<Integer, Player> entry : as.getPlayers().entrySet()) {
            Log.i("ActivePlayer", entry.getKey().toString());
        }
    }
    /**
     * Helper function.
     */
    private void printAllActiveSquads() {
        for(int i=0;i<activeSquads.size();i++) {
            Log.i("Squad entered" + i +":", timeConversion(activeSquads.get(i).getStartTime()));
            for (Map.Entry<Integer, Player> entry : activeSquads.get(i).getPlayers().entrySet()) {
                Log.i("ActivePlayer", entry.getKey().toString());
            }
        }
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
