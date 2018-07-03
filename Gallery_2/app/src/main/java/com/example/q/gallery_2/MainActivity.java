package com.example.q.gallery_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> dataList1;
    private ArrayList<String> dataList2;
    private GridView mGridView;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridView);
        Toast.makeText(this, "App Start!!!", Toast.LENGTH_SHORT).show();

        checkPermission();
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "initial_Permit Denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            // requestPermissions => call onRequestPermissionsResult
        } else {
            Toast.makeText(this, "initial_Permit Accepted", Toast.LENGTH_SHORT).show();
            gallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    Toast.makeText(this, "now, Permit Accepted", Toast.LENGTH_SHORT).show();
                    gallery();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "now, Permit Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    public String thumbnail(String id) {
        Cursor tn_c = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Images.Thumbnails.DATA},
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[] {id},
                null );
        if(tn_c.moveToFirst()) {
            return tn_c.getString(tn_c.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        } else {
            MediaStore.Images.Thumbnails.getThumbnail(
                    getContentResolver(),
                    Long.parseLong(id),
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null );
            tn_c.close();
            return thumbnail(id);
        }
    }

    public void gallery(){
        dataList1 = new ArrayList<String>();
        dataList2 = new ArrayList<String>();
        Cursor c = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null );

        if(c.moveToFirst()) do {
            String data = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            dataList1.add(data);

            String id = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
            String tn = thumbnail(id);
            dataList2.add(tn);
        } while (c.moveToNext());

        c.close();
        mGridView.setAdapter(new ImageAdapter(this, dataList2));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }
}
