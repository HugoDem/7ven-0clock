package com.isep.ii3510.a7ven0clock;

import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClockFragment#getInstance} factory method to
 * create/get an unique instance of this fragment.
 */
public class ClockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_CONNECTED = "isConnected";
    private static final String ARG_PARAM2 = "param2";

    private static ClockFragment clockInstance;
    private boolean isConnected;
    private String mParam2; //useless for now

    private TextView timeView;
    private TextView dateView;
    private FloatingActionButton floatingBtn;
    private ListView zoneListView;
    private RecyclerView zoneRecyclerView;

    TimeZoneAdapter myAdapter = new TimeZoneAdapter();
    HashMap<String, String> nameIDZoneMap = new HashMap<>();
    List<String> zoneNames =  new ArrayList<>();

    private View view; //to remove ?

    public ClockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param spotConnection boolean to represent if the user is connected to spot or not
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClockFragment.
     */
    public static ClockFragment getInstance(boolean spotConnection, String param2) {
        if(clockInstance == null){
            clockInstance = new ClockFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_CONNECTED, spotConnection);
            args.putString(ARG_PARAM2, param2);
            clockInstance.setArguments(args);

            return clockInstance;
        }
        return clockInstance;
    }

    public static ClockFragment getInstance(){
        return getInstance(false, "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isConnected = getArguments().getBoolean(IS_CONNECTED);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        refreshTime();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clock, container, false);

        timeView = (TextView) view.findViewById(R.id.timeView);
        dateView = (TextView) view.findViewById(R.id.dateView);
        zoneListView = (ListView) view.findViewById(R.id.listView);
        zoneRecyclerView = (RecyclerView) view.findViewById(R.id.timeZoneRecyclerView);

        zoneListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        for(String id : ZoneId.SHORT_IDS.keySet()){
            if(!zoneNames.contains(ZoneId.SHORT_IDS.get(id))){
                nameIDZoneMap.put(ZoneId.SHORT_IDS.get(id), id);
                zoneNames.add(ZoneId.SHORT_IDS.get(id));
            }
        }

        zoneListView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, zoneNames));

        floatingBtn = (FloatingActionButton) view.findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(view -> {
            if(zoneListView.isShown()) {
                zoneListView.setVisibility(View.INVISIBLE);
                updateTimezoneView();
                floatingBtn.setImageResource(R.drawable.more_time);
            }
            else {
                zoneListView.setVisibility(View.VISIBLE);
                floatingBtn.setImageResource(R.drawable.check_icon);
            }
        });

        return view;
    }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onResume() {
            super.onResume();
            updateTimezoneView();
            updateView();
        }

    private void refreshTime(){
        Handler handler = new Handler();
        final Runnable r = new Runnable(){
            @Override
            public void run(){
                updateView();
            }
        };

        handler.postDelayed(r, 1000);
    }

    private void updateView(){updateView(false);}

    private void updateView(boolean start){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault());
        DateTimeFormatter shortTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());

        view = getView();
        if(view == null)
            onPause();
        else {
            timeView = (TextView) view.findViewById(R.id.timeView);
            timeView.setText(timeFormatter.format(LocalDateTime.now()));
            timeView.setVisibility(View.VISIBLE);

            dateView = (TextView) view.findViewById(R.id.dateView);
            dateView.setText(dateFormatter.format(LocalDateTime.now()));
            dateView.setVisibility(View.VISIBLE);

            if(start) updateTimezoneView();
            for(ZoneId id : myAdapter.toUpdateTxViewMap.keySet())
                myAdapter.toUpdateTxViewMap.get(id).setText(shortTimeFormatter.format(LocalDateTime.now(id)));

            refreshTime();
        }
    }

    // TODO : Add the different zones in sharedPreferences

    private int updateTimezoneView(){

        if(zoneListView == null) zoneListView = (ListView) view.findViewById(R.id.listView);

        List<String> toDisplayIDsList = new ArrayList<>();
        SparseBooleanArray checkedItemPos = zoneListView.getCheckedItemPositions();

        for(int i = 0; i < checkedItemPos.size(); i++)
            toDisplayIDsList.add(nameIDZoneMap.get(zoneNames.get(checkedItemPos.keyAt(i))));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        myAdapter = new TimeZoneAdapter(getContext(), toDisplayIDsList);
        zoneRecyclerView.setAdapter(myAdapter);
        zoneRecyclerView.setLayoutManager(layoutManager);

        return 1;
    }
}