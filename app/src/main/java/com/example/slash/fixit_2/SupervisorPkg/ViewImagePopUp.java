package com.example.slash.fixit_2.SupervisorPkg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.slash.fixit_2.Models.Image;
import com.example.slash.fixit_2.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class ViewImagePopUp extends AppCompatActivity {

    ImageView ImageView;
    String imgURL,jsonURL;
    DownLoadImageTask downloadImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_pop_up);

        final ImageView issueImage = (ImageView) findViewById(R.id.issueIMG);

        //Converting activity into poUp activity
        DisplayMetrics dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,height);
        getWindow().setLayout((int)(width*.9),(int)(height*.9));

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        imgURL = bundle.getString("imageUrl");
        jsonURL = bundle.getString("jsonUrl");

        downloadImageTask = new DownLoadImageTask(issueImage);
        downloadImageTask.execute(imgURL);


    }

    @Override
    public void onBackPressed() {
        downloadImageTask.cancel(true);
        finish();
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;



        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {

            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            finish();
        }
    }

    private void sendRequest(String url, final TextView view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Image image = gson.fromJson(response.toString(), Image.class);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Image metadata not found!!!", Toast.LENGTH_SHORT).show();

                    }
                });
        requestQueue.add(jsObjRequest);
    }

}

//toDO


