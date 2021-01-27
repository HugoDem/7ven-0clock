package com.isep.ii3510.a7ven0clock;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> a1942f404746fbfbf72324290345011cfa9798f8
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment {

    private static AlarmFragment alarmFragment = null;
    private TimePicker alarmTime;
    private TextView currentTime;
<<<<<<< HEAD
    private Button alarmButton;
    List<String> alarms = new ArrayList<>();

=======
    private Button alarmButton, stopRingingBtn;

    private SpotifyPlayer spotPlayer = null;
>>>>>>> a1942f404746fbfbf72324290345011cfa9798f8

    public AlarmFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment AlarmFragment.
     */
    public static AlarmFragment getInstance() {
        if(alarmFragment == null)
            alarmFragment = new AlarmFragment();
        return alarmFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmTime = (TimePicker) view.findViewById(R.id.alarmToggle);
        currentTime = (TextView) view.findViewById(R.id.alarmText);
        alarmButton = (Button) view.findViewById(R.id.alarmButton);
        stopRingingBtn = (Button) view.findViewById(R.id.stopRingButton);

<<<<<<< HEAD
=======
        alarmButton.setOnClickListener(v -> setAlarmOn());

        spotPlayer = new SpotifyPlayer(getContext());
        Log.d("player connected", ""+spotPlayer.isConnected());

>>>>>>> a1942f404746fbfbf72324290345011cfa9798f8
        return view;
    }

    public void setAlarmOn(){
<<<<<<< HEAD
        //final Ringtone r = RingtoneManager.getRingtone(getContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        assert notification != null;
        Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
        r.play();

        alarms.add(AlarmTime());

=======
        final Ringtone r = RingtoneManager.getRingtone(getContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
>>>>>>> a1942f404746fbfbf72324290345011cfa9798f8
        currentTime.setText("Set alarm on:"+AlarmTime());
        DateTimeFormatter shortTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());


        DateTimeFormatter shortTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
<<<<<<< HEAD

                for(String alarm : alarms){
                    if (shortTimeFormatter.format(LocalDateTime.now()).equals(alarm)){
                        r.play();

                    }else{
                        r.stop();
                    }
=======
                if (shortTimeFormatter.format(LocalDateTime.now()).equals(AlarmTime())){
                    //r.play();
                    spotPlayer.play("spotify:track:3AQ5aIqSaqEAGvcrK8SDAA");

                } else {
                    //r.stop();
                    spotPlayer.stop();
>>>>>>> a1942f404746fbfbf72324290345011cfa9798f8
                }

            }
        }, 0, 1000);
    }

<<<<<<< HEAD
=======
    private void stopRinging(View iView) {
        //ringtone.stop();
        spotPlayer.stop();
        stopRingingBtn.setVisibility(View.INVISIBLE);
    }
>>>>>>> a1942f404746fbfbf72324290345011cfa9798f8

    public String AlarmTime(){

        int alarmHours = alarmTime.getHour();
        int alarmMinutes = alarmTime.getMinute();
        String stringAlarmMinutes;

        if (alarmMinutes<10){
            stringAlarmMinutes = "0";
            stringAlarmMinutes = stringAlarmMinutes.concat(Integer.toString(alarmMinutes));
        }else{
            stringAlarmMinutes = Integer.toString(alarmMinutes);
        }
        String stringAlarmTime;

        if(alarmHours>12){
            alarmHours = alarmHours - 12;
            stringAlarmTime = Integer.toString(alarmHours).concat(":").concat(stringAlarmMinutes).concat(" PM");
        }else{
            stringAlarmTime = Integer.toString(alarmHours).concat(":").concat(stringAlarmMinutes).concat(" AM");
        }
        return stringAlarmTime;
    }

}


