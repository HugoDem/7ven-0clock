package com.isep.ii3510.a7ven0clock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.TimezoneToDisplay> {
    private List<String> mDataset; // will contain all the ids of the zones to display

    private Context context;

    public static class TimezoneToDisplay extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView timeView;
        TextView dateView;

        public TimezoneToDisplay(View v){
            super(v);
            nameView = v.findViewById(R.id.nameRecycleView);
            timeView = v.findViewById(R.id.timeRecycleView);
            dateView = v.findViewById(R.id.dateRecycleView);
        }
    }

    public TimeZoneAdapter(){};

    public TimeZoneAdapter(Context iContext, List<String> myDataset) {
        mDataset = myDataset;
        context = iContext;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @NonNull
    @Override
    public TimeZoneAdapter.TimezoneToDisplay onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.timezone_display, parent, false);

        return new TimezoneToDisplay(view);
    }

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
    public HashMap<ZoneId, TextView> toUpdateTxViewMap = new HashMap<>();

    @Override
    public void onBindViewHolder(final TimezoneToDisplay holder, int position) {
        ZoneId id = ZoneId.of(mDataset.get(position));
        holder.nameView.setText(ZoneId.SHORT_IDS.get(id.toString()));
        holder.timeView.setText(timeFormatter.format(LocalDateTime.now(id)));
        holder.dateView.setText(dateFormatter.format(LocalDateTime.now(id)));
        toUpdateTxViewMap.put(id, holder.timeView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
