package com.isep.ii3510.a7ven0clock;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {

    private static TimerFragment timerInstance;

    private View view;
    private Button startPauseButton, resetButton, stopRingingBtn, startSpotBtn;
    private EditText editTextForTime1, editTextForTime2;
    private TextView timerTextView, point;

    private boolean timerRunning = false;
    private CountDownTimer countDownTimer = null;
    private Ringtone ringtone = null;
    private long timeLeftInMillis = 0;

    private SpotifyPlayer spotPlayer = null;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return the instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment getInstance() {
        if(timerInstance == null)
            timerInstance = new TimerFragment();
        return timerInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timer, container, false);

        startPauseButton = view.findViewById(R.id.startPauseButton);
        resetButton = view.findViewById(R.id.resetButton);
        stopRingingBtn = view.findViewById(R.id.stopRingButton);
        startSpotBtn = view.findViewById(R.id.startSpotActivity);

        editTextForTime1 = view.findViewById(R.id.editTextForTime1);
        editTextForTime2 = view.findViewById(R.id.editTextForTime2);

        timerTextView = view.findViewById(R.id.timerTextView);
        point = view.findViewById(R.id.point);

        startPauseButton.setOnClickListener(this::startStopTimer);
        resetButton.setOnClickListener(this::resetTimer);
        stopRingingBtn.setOnClickListener(this::stopRinging);
        stopRingingBtn.setOnClickListener(this::startSpotActivity);

        ringtone = RingtoneManager.getRingtone(getContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        spotPlayer = new SpotifyPlayer(getContext());
        Log.d("player connected", ""+spotPlayer.isConnected());

        return view;
    }

    private void startSpotActivity(View view) {
        Intent spotifyActivityIntent = new Intent(getActivity(), SpotifyActivity.class);
        spotifyActivityIntent.putExtra("SpotifyPlayer", (Parcelable) spotPlayer);
        startActivity(spotifyActivityIntent);
    }

    private void startStopTimer(View iView){
        if(!timerRunning){
            startPauseButton.setText("PAUSE");

            editTextForTime1.setVisibility(View.INVISIBLE);
            editTextForTime2.setVisibility(View.INVISIBLE);
            point.setVisibility(View.INVISIBLE);

            timerTextView.setVisibility(View.VISIBLE);

            if(timeLeftInMillis == 0) {
                String tmp = editTextForTime1.getText().toString();
                long minutes = Long.parseLong(tmp);

                tmp = editTextForTime2.getText().toString();
                long seconds = Long.parseLong(tmp);

                timeLeftInMillis = minutes * 60000 + seconds * 1000;
            }
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    long minutes = timeLeftInMillis / 60000;
                    long seconds = (timeLeftInMillis % 60000) / 1000;
                    String timeStr = (minutes < 10 ? "0"+ minutes : minutes) + ":" + (seconds < 10 ? "0"+ seconds : seconds);
                    timerTextView.setText(timeStr);
                }

                @Override
                public void onFinish() {
                    //ringtone.play();
                    stopRingingBtn.setVisibility(View.VISIBLE);
                    spotPlayer.play("spotify:track:3AQ5aIqSaqEAGvcrK8SDAA");
                }
            };

            countDownTimer.start();
            timerRunning = true;
        }
        else {
            startPauseButton.setText("RESUME");
            countDownTimer.cancel();
            timerRunning = false;
        }
    }


    private void resetTimer(View iView){
        startPauseButton.setText("START");

        editTextForTime1.setVisibility(View.VISIBLE);
        editTextForTime2.setVisibility(View.VISIBLE);
        point.setVisibility(View.VISIBLE);

        timerTextView.setVisibility(View.INVISIBLE);
        timeLeftInMillis = 0;
        timerRunning = false;
        countDownTimer.cancel();
    }

    private void stopRinging(View iView) {
        //ringtone.stop();
        spotPlayer.stop();
        stopRingingBtn.setVisibility(View.INVISIBLE);
        resetTimer(view);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(timerRunning){
            startPauseButton.setText("PAUSE");

            editTextForTime1.setVisibility(View.INVISIBLE);
            editTextForTime2.setVisibility(View.INVISIBLE);
            point.setVisibility(View.INVISIBLE);

            timerTextView.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    long minutes = timeLeftInMillis / 60000;
                    long seconds = (timeLeftInMillis % 60000) / 1000;
                    String timeStr = (minutes < 10 ? "0"+ minutes : minutes) + ":" + (seconds < 10 ? "0"+ seconds : seconds);
                    timerTextView.setText(timeStr);
                }

                @Override
                public void onFinish() {
                    ringtone.play();
                    stopRingingBtn.setVisibility(View.VISIBLE);
                }
            }.start();
        }
    }

}