package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    Button submitButton;
    EditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitLoc (View view){
        //Create intent to start new activity
        Intent submitIntent =new Intent(this, MapActivity.class);

        //Get TextView of address location
        TextView textAddress =(TextView) findViewById(R.id.inputText);

        //Get value of the text view
        String address =textAddress.getText().toString();

        //random int value to be added to extras along with address
        submitIntent.putExtra("KEY", address);

        //start activity
        startActivity(submitIntent);
    }


}
