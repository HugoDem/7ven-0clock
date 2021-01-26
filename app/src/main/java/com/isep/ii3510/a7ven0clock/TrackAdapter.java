package com.isep.ii3510.a7ven0clock;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackToDisplay>  {
    private JSONArray items; // will contain all items of search request (each item is a track)
    private int nbItems;
    private Context context;

    public static class TrackToDisplay extends RecyclerView.ViewHolder {
        TextView trackView;
        TextView artistsView;

        public TrackToDisplay(View v){
            super(v);
            trackView = v.findViewById(R.id.trackRecycleView);
            artistsView = v.findViewById(R.id.artistsRecycleView);
        }
    }

    public TrackAdapter(){};

    public TrackAdapter(Context iContext, JSONArray myItems, int myNbItems) {
        items = myItems;
        nbItems = myNbItems;
        context = iContext;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public TrackAdapter.TrackToDisplay onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.track_display, parent, false);
        return new TrackAdapter.TrackToDisplay(view);
    }

    @Override
    public void onBindViewHolder(final TrackAdapter.TrackToDisplay holder, int position) {
        JSONObject item = null;
        try {

            item = items.getJSONObject(position);
            if(item.names() == null) return;

            holder.trackView.setText(getTrackName(item));
            System.out.println("TRACK NAME : "+ getTrackName(item)  );
            holder.artistsView.setText(getArtistsNames(item));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTrackName(JSONObject item) {
        String str = "7venOCLOCK";
        try{
            str = item.getString("name");
        } catch (Exception e) { e.printStackTrace(); }
        return str;
    }

    public String getArtistsNames(JSONObject item) {
        String artistsNames = "";
        JSONArray artists = new JSONArray();
        try{
            artists = item.getJSONArray("artists");
            for (int i = 0 ; i < artists.length() ; i++) {
                item.has("name");
                artistsNames+= artists.getJSONObject(i).getString("name") + " ";
            }
        }catch (Exception e){ e.printStackTrace(); artistsNames+="erreur(s)"; }

        return artistsNames;
    }

    public void setItems(JSONArray iItems){
        items = iItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return nbItems;
    }

}
