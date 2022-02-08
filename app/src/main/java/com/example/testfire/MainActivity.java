package com.example.testfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //widgets

    RecyclerView recyclerView;

    //firebase

    private DatabaseReference myRef;
    //variables

    private ArrayList<Message> messagesList;
    private RecyclerAdapter recyclerAdapter;
    private Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //for offline capability
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //firebase

        myRef = FirebaseDatabase.getInstance().getReference();

        //ArrayList
        messagesList = new ArrayList<>();


        //clear Arralist

        ClearAll();


        //Get Data Method

        GetDataFromFirebase();




    }

    private void GetDataFromFirebase() {

        Query query = myRef.child("message");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                ClearAll();

                for (DataSnapshot snapshot:datasnapshot.getChildren() ){
                    Message message = new Message();
                    message.setImageurl(snapshot.child("image").getValue().toString());
                    message.setName(snapshot.child("name").getValue().toString());
                    messagesList.add(message);

                }

                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),messagesList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private  void ClearAll(){

        if (messagesList!=null) {messagesList.clear();

        if (recyclerAdapter!=null){
            recyclerAdapter.notifyDataSetChanged();
        }

        }

        messagesList = new ArrayList<>();




    }

}