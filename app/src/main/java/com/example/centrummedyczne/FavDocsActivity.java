package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FavDocsActivity extends AppCompatActivity {


    RecyclerView mFavDocRecycler;

    String s1[], s2[];
    int images[] = {R.drawable.ic_profile_blue,R.drawable.ic_profile_blue,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_blue,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_blue};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_docs);




        mFavDocRecycler = (RecyclerView) findViewById(R.id.favDocRecycler);

        s1 = getResources().getStringArray(R.array.doctors);
        s2 = getResources().getStringArray(R.array.description);

        FavDocAdapter favDocAdapter = new FavDocAdapter(this, s1, s2, images);
        mFavDocRecycler.setAdapter(favDocAdapter);
        mFavDocRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onClickAccount(View view){
        Intent intent = new Intent(view.getContext(),PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickSearch(View view){
        Intent intent = new Intent(view.getContext(),StartActivity.class);
        startActivity(intent);
    }


}