/*
package com.example.q.contactpractice;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.ListView;


public class ListviewActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listview);
        ArrayList<Listviewitem> data=new ArrayList<>();
        Listviewitem lion=new Listviewitem(R.drawable.ic_people,"Lion");
        Listviewitem tiger=new Listviewitem(R.drawable.ic_people,"Tiger");
        Listviewitem dog=new Listviewitem(R.drawable.ic_people,"Dog");
        Listviewitem cat=new Listviewitem(R.drawable.ic_people,"Cat");
        data.add(lion);
        data.add(tiger);
        data.add(dog);
        data.add(cat);
        ListviewAdapter adapter=new ListviewAdapter(this,R.layout.listviewitem,data);
        listView.setAdapter(adapter);
    }
}
*/