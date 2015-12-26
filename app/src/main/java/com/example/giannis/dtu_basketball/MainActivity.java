package com.example.giannis.dtu_basketball;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.giannis.dtu_basketball.MESSAGE";
    private MalibuCountDownTimer countDownTimer;
    private long timeElapsed;
    private boolean timerHasStarted = false;
    private Button startB;
    private TextView text;
    private TextView timeElapsedView;

    private final long startTime = 50000;
    private final long interval = 1000;

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
        timeElapsedView = (TextView) this.findViewById(R.id.timeElapsed);
        countDownTimer = new MalibuCountDownTimer(startTime, interval);
        countDownTimer.start();
        timerHasStarted = true;
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
            timeElapsedView.setText("Time Elapsed: " + String.valueOf(startTime));
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            timeElapsed = startTime - millisUntilFinished;
            timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
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


}
