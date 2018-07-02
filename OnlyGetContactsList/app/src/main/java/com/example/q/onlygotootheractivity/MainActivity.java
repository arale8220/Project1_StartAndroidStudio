package com.example.q.onlygotootheractivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv;
    private Button btn;
    private ArrayList<Map<String, String>> dataList;
    private ListView mListview;
    private TextView fno;
    private TextView sno;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView2);
        fno = (TextView) findViewById(R.id.mName);
        sno = (TextView) findViewById(R.id.mNumber);
        mListview = (ListView) findViewById(R.id.listview);
        btn = (Button) findViewById(R.id.button);
        Toast.makeText(this, "버튼 아직 안눌림", Toast.LENGTH_SHORT).show();
        btn.setOnClickListener(this);
    }

    public void onClick(View v) {
        Toast.makeText(this, "주소록", Toast.LENGTH_SHORT).show();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            Toast.makeText(this, "11111111", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }else{
            // 권한 있음
            Toast.makeText(this, "222222", Toast.LENGTH_SHORT).show();
            contact();
        }

    }

    public void contact(){
        Toast.makeText(this, "@@@@@@@@@", Toast.LENGTH_SHORT).show();
        dataList = new ArrayList<Map<String, String>>();
        Cursor c = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");
        Toast.makeText(this, "111111", Toast.LENGTH_SHORT).show();

        while (c.moveToNext()) {
            Toast.makeText(this, "wwwww", Toast.LENGTH_SHORT).show();
            HashMap<String, String> map = new HashMap<String, String>();
            // 연락처 id 값
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            // 연락처 대표 이름
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            map.put("name", name);
            fno.setText(name);

            // ID로 전화 정보 조회
            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);

            // 데이터가 있는 경우
            if (phoneCursor.moveToFirst()) {
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put("phone", number);
                sno.setText(number);
            }

            phoneCursor.close();
            dataList.add(map);
        }// end while
        Toast.makeText(this, "dddd", Toast.LENGTH_SHORT).show();
        c.close();


        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                dataList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "phone"},
                new int[]{android.R.id.text1, android.R.id.text2});
        mListview.setAdapter(adapter);

    }

}