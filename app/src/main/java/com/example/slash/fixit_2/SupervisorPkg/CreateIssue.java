package com.example.slash.fixit_2.SupervisorPkg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.slash.fixit_2.Others.EndPoints;
import com.example.slash.fixit_2.R;

import com.example.slash.fixit_2.Others.VolleyMultipartRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
        import java.util.Map;

public class CreateIssue extends AppCompatActivity implements View.OnClickListener {
    ImageView targetImage;
    EditText descriptionET,nameET;
    Button upload, gallery, camera;
    Bitmap bitmap;
    final Context context = this;
    String tagText = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;
    int currentX, currentY;
    String mCurrentPhotoPath;
    String description,name,projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        projectId = bundle.getString("pId");

        targetImage = (ImageView) findViewById(R.id.IVDisplay);
        descriptionET = (EditText)findViewById(R.id.discriptionET);
        nameET = (EditText)findViewById(R.id.issueNameET);
        upload = (Button) findViewById(R.id.upload);
        gallery = (Button) findViewById(R.id.gallary);
        camera = (Button) findViewById(R.id.camera);
        upload.setOnClickListener(this);
        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);

        final int[] viewCoords = new int[2];
        targetImage.getLocationOnScreen(viewCoords);

        targetImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertDialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tagText += currentX + "/" + currentY + "/" + userInput.getText() + "/";
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                return true;
            }
        });


        targetImage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                currentX = x - viewCoords[0]; // viewCoords[0] is the X coordinate
                currentY = y - viewCoords[1]; // viewCoords[1] is the y coordinate
                Toast.makeText(getApplicationContext(), "X value: " + currentX + " Y value: " + currentY, Toast.LENGTH_SHORT).show();
                return false;
            }

            ;
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                description = descriptionET.getText().toString();
                name = nameET.getText().toString();
                uploadBitmap(bitmap);
                break;
            case R.id.gallary:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
                targetImage.setVisibility(View.VISIBLE);
                break;
            case R.id.camera:
                dispatchTakePictureIntent();   //Call camera
                targetImage.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //when data is coming from camera or gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                targetImage.setImageBitmap(imageBitmap);
                bitmap = imageBitmap;

            } else if (requestCode == REQUEST_GALLERY_IMAGE) {
                Uri targetUri = data.getData();
                try {
                    imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    bitmap = imageBitmap;
                    targetImage.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void uploadBitmap(final Bitmap bitmap) {
        final String tags = tagText;
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(getApplicationContext(), "Issue added successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateIssue.this,IssueList.class);
                        intent.putExtra("selectedProject",projectId);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("initialX", "0");
                params.put("initialY", "0");
                params.put("width", "0");
                params.put("height", "0");
                params.put("resolution", "12x12");
                params.put("tags", tags);
                params.put("description", description);
                params.put("name",name);
                params.put("projectId",projectId);
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image_file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

}

//toDO