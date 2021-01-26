package com.isep.ii3510.a7ven0clock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import com.spotify.sdk.android.authentication.sample.R;
//pour lire un track

public class SpotifyActivity extends AppCompatActivity {

    // Spotify mandatory parameters
    private static final String CLIENT_ID = "5b5e3a59e0ff47a6a30ad395c2bbe605";
    private static final String REDIRECT_URI = "sevenoclock://callback";

    // Requests parameters
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;


    // Activity parameters
    private EditText editText;
    List<String> uri = new ArrayList<>();
    TrackAdapter myAdapter = new TrackAdapter();
    private RecyclerView trackRecyclerView;
    private JSONArray items = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        getSupportActionBar().setTitle(String.format(
                Locale.US, "Spotify Auth Sample %s", com.spotify.sdk.android.auth.BuildConfig.VERSION_NAME));

        editText = findViewById(R.id.editText);
        trackRecyclerView = findViewById(R.id.trackRecyclerView);

        for(int i = 0; i < 20; i++){
            items.put(new JSONObject());
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myAdapter = new TrackAdapter(this, items);
        trackRecyclerView.setAdapter(myAdapter);
        trackRecyclerView.setLayoutManager(layoutManager);
        trackRecyclerView.setVisibility(View.INVISIBLE);

    }

    public void onRequestTokenClicked(View view) {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

     /*
    public void onRequestCodeClicked(View view) {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);
    }
    */

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email"})
                .setCampaign("your-campaign-token")
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();

            final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
            AuthorizationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {

            mAccessCode = response.getCode();
            try {
                onGetUserProfileClicked();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onGetUserProfileClicked() throws InterruptedException { //View view dans l'argument
        if (mAccessToken == null) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_spotify), R.string.warning_need_token, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            snackbar.show();
            return;
        }
        sendSearchRequest();
        if(items!=null){
            myAdapter.setItems(items);
            trackRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void sendSearchRequest() throws InterruptedException {
        String editTextValue = String.valueOf(editText.getText());
        editTextValue.replaceAll(" ", "%20");
        //Log.d("Text request : ", "")

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/search?q="+editTextValue+ "&type=track")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .addHeader("q", editTextValue)
                .addHeader("type", "track")
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse("Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());

                    Log.d("reponse",jsonObject.getString("tracks"));

                    items = jsonObject.getJSONObject("tracks").getJSONArray("items");
                    int nbItems = items.length();

                    String outputStr = "";
                    for(int i = 0; i < nbItems; i++){

                        Log.d("item",items.getJSONObject(i).toString());

                        uri.add(items.getJSONObject(i).getString("uri"));
                        outputStr += items.getJSONObject(i).getString("uri")+"\n";
                    }
                    //récupérer outputStr
                } catch (JSONException e) {
                    setResponse("Failed to parse data: " + e);
                }
            }
        });
        Thread.sleep(1000);
    }

    private void setResponse(final String text) {
        runOnUiThread(() -> {
            final TextView responseView = findViewById(R.id.response_text_view);
            responseView.setText(text);
        });
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    protected void onDestroy() {
        cancelCall();

        super.onDestroy();
    }

    private void updateTrackView(JSONArray items){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myAdapter = new TrackAdapter(this, items);
        trackRecyclerView.setAdapter(myAdapter);
        trackRecyclerView.setLayoutManager(layoutManager);

    }

}