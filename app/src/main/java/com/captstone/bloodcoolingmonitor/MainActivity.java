package com.captstone.bloodcoolingmonitor;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button startStopButton, resetButton;
    TextView timerText;
    TabHost tabHost;
    long savedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = (TextView) findViewById(R.id.timerTextView);
        setupButtons();
        setupTabs();
    }

    CountDownTimer timer;
    public void setupButtons() {
        startStopButton = (Button) findViewById(R.id.startStop);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer == null) {
                    timer = createCountdownTimer(180000);
                }
                Button b = (Button) view;
                if(b.getText().toString().contains("tart")) {
                    b.setText("Pause");
                    timer.start();
                } else if(b.getText().toString().contains("Resume")) {
                    b.setText("Pause");
                    timer = createCountdownTimer(savedTime);
                    timer.start();
                } else {
                    b.setText("Resume");
                    timer.cancel();
                }
            }
        });

        resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timerText.setText(formatTime(180000));
                startStopButton.setText("Start");
            }
        });
    }

    public CountDownTimer createCountdownTimer(long time) {
        return new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                savedTime = l;
                timerText.setText(formatTime(l));
            }

            @Override
            public void onFinish() {
                timerText.setText("Done");
            }
        };
    }

    public void setupTabs() {
        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("controls");
        tabSpec.setContent(R.id.Controls);
        tabSpec.setIndicator("Controls");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("sensors");
        tabSpec.setContent(R.id.Sensors);
        tabSpec.setIndicator("Sensors");
        tabHost.addTab(tabSpec);
    }

    public String formatTime(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
