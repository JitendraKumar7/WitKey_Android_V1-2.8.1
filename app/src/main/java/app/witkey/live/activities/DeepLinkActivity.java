package app.witkey.live.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.EnumUtils;

public class DeepLinkActivity extends AppCompatActivity {

    static String string = "Hey check out my streaming at: http://witkeyapp.com/witkey/api/share/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent intent = getIntent();
            Uri data = intent.getData();

            assert data != null;
            Log.d("JKS", "DATA " + data.toString());
            //Toast.makeText(getApplicationContext(), "Deep Linking Working", Toast.LENGTH_SHORT).show();

            String url = data.toString();
            StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.
                    try {
                        Log.d("JKS", "DATA " + response);

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getInt("Response") == 200) {

                            String result = String.valueOf(jsonObject.getJSONObject("Result"));
                            StreamBO streamBO = new Gson().fromJson(result, StreamBO.class);

                            startActivity(new Intent(DeepLinkActivity.this, EnumUtils.getCurrentGoLiveViewType())
                                    .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_2)
                                    .putExtra(GoLiveActivity.ARG_PARAM_3, streamBO));
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                }
            });
            RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
            ExampleRequestQueue.add(ExampleStringRequest);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public static void share(Context context, String uid) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, string + uid);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
