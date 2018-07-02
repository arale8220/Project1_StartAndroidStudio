package com.example.q.onlygotootheractivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 현재 스마트폰의 연락처와 통화내역과 문자 내역을 읽어와서 파일로 백업 처리해줌.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv;
    private Button btn;
    private ArrayList<Map<String, String>> dataList;
    private ListView mListview;
    private TextView fno;
    private TextView sno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView2);
        fno = (TextView) findViewById(R.id.mName);
        sno = (TextView) findViewById(R.id.mNumber);
        mListview = (ListView) findViewById(R.id.listview);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    public void onClick(View v) {
        Toast.makeText(this, "주소록", Toast.LENGTH_SHORT).show();
        contacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("test", "onCreateOptionsMenu - 최초 메뉴키를 눌렀을 때 호출됨");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int v_itemId = item.getItemId();

        switch (v_itemId){
            case R.id.action_settings:
                Toast.makeText(this, "통화내역", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item1:
                Toast.makeText(this, "주소록", Toast.LENGTH_SHORT).show();
                contacts();
                return true;

        }
/*
        if (v_itemId == R.id.action_settings) {
            Toast.makeText(this, "통화내역", Toast.LENGTH_SHORT).show();
            getHistory();
        } else if (v_itemId == R.id.item1) {
            Toast.makeText(this, "주소록", Toast.LENGTH_SHORT).show();
            contacts();
        }
*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * 주소록 정보 가져오기.
     */
    public void contacts() {
        dataList = new ArrayList<Map<String, String>>();
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");
/*
        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.PHOTO_ID
                },
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        );*/

        while (c.moveToNext()) {
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
        c.close();




        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                dataList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "phone"},
                new int[]{android.R.id.text1, android.R.id.text2});
        mListview.setAdapter(adapter);



        /*
        while (cursor.moveToNext()) {
            try {
                String v_id = cursor.getString(0);
                String v_display_name = cursor.getString(1);
                String v_phone = contactsPhone(v_id);
                String v_email = contactsEmail(v_id);

                System.out.println("id = " + v_id);
                System.out.println("display_name = " + v_display_name);
                System.out.println("phone = " + v_phone);
                System.out.println("email = " + v_email);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();*/
    }

    /**
     * 주소록 상세정보(전화번호) 가져오기.
     */
    public String contactsPhone(String p_id) {
        String reuslt = null;

        if (p_id == null || p_id.trim().equals("")) {
            return reuslt;
        }

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (cursor.moveToNext()) {
            try {
                reuslt = cursor.getString(0);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();

        return reuslt;
    }

    /**
     * 주소록 상세정보(이메일주소) 가져오기.
     */
    public String contactsEmail(String p_id) {
        String reuslt = null;

        if (p_id == null || p_id.trim().equals("")) {
            return reuslt;
        }

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Email.DATA
                },
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (cursor.moveToNext()) {
            try {
                reuslt = cursor.getString(0);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();

        return reuslt;
    }



}
