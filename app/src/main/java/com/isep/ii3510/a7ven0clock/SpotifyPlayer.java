package com.isep.ii3510.a7ven0clock;

import android.content.Context;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class SpotifyPlayer {

    private static final String CLIENT_ID = "5b5e3a59e0ff47a6a30ad395c2bbe605";
    private static final String REDIRECT_URI = "sevenoclock://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private boolean isConnected = false;

    private final Context context;

    public SpotifyPlayer(Context iContext){
        context = iContext;

        connect();
    }

    private void connect() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        try {
            SpotifyAppRemote.connect(context, connectionParams, new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("SpotifyPlayer", "Connected via SpotifyActivity! Yay!");

                            isConnected = true;
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("SpotifyPlayer", throwable.getMessage(), throwable);

                            isConnected = false;
                        }
                    });
        }
        catch (Exception e){
            Log.d("Something went wrong","You must have the spotify app on your phone to play a song");
            e.printStackTrace();
        }
    }

    public void play(String iTrack){
        if(!isConnected){
            connect();
        }

        if(isConnected){
            //play a track or playlist
            mSpotifyAppRemote.getPlayerApi().play(iTrack); //  track SEVENOCLOCK twinsmatic x Dinos
/*
            // Subscribe to PlayerState
            mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
                final Track track = playerState.track;
                if (track != null) Log.d("SettingsFragment", track.name + " by " + track.artist.name);
            });*/
        }

    }

    public void stop(){
        mSpotifyAppRemote.getPlayerApi().pause();
    }

    public boolean isConnected() {
        return isConnected;
    }
}
