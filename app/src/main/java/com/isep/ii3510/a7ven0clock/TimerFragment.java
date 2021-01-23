package com.isep.ii3510.a7ven0clock;

import android.os.Bundle;
import android.os.CountDownTimer;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_CONNECTED = "isConnected";
    private static final String ARG_PARAM2 = "param2";

    private static TimerFragment timerInstance;
    private boolean spotConnection;
    private String mParam2;

    private View view;
    private Button startPauseButton, resetButton;
    private EditText editTextForTime1, editTextForTime2;
    private TextView timerTextView, point;

    private boolean timerRunning = false;
    CountDownTimer countDownTimer = null;
    private long timeLeftInMillis = 0;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param spotConnection boolean to tell if connected.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment getInstance(boolean spotConnection, String param2) {
        if(timerInstance == null){
            timerInstance = new TimerFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_CONNECTED, spotConnection);
            args.putString(ARG_PARAM2, param2);
            timerInstance.setArguments(args);
        }
        return timerInstance;
    }

    public static TimerFragment getInstance(){return getInstance(false,"");}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotConnection = getArguments().getBoolean(IS_CONNECTED);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timer, container, false);

        startPauseButton = view.findViewById(R.id.startPauseButton);
        resetButton = view.findViewById(R.id.resetButton);

        editTextForTime1 = view.findViewById(R.id.editTextForTime1);
        editTextForTime2 = view.findViewById(R.id.editTextForTime2);

        timerTextView = view.findViewById(R.id.timerTextView);
        point = view.findViewById(R.id.point);

        startPauseButton.setOnClickListener(this::startStopTimer);
        resetButton.setOnClickListener(this::resetTimer);

        return view;
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
        countDownTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}