package com.example.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class InitialActivity extends AppCompatActivity {

    Button startGame;
    Button howToPlay;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        howToPlay = findViewById(R.id.howToplay);
        howToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.theverge.com/22892044/wordle-free-game-online-how-to-puzzle"));
                startActivity(browserIntent);
            }
        });


         startGame = findViewById(R.id.startGame);

        // initializing the shared preferences
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        RadioGroup radioGroup =(RadioGroup)findViewById(R.id.files);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userChoiceStr = "Easy level chosen";

                int fileSelected = radioGroup.getCheckedRadioButtonId();

                if(fileSelected==R.id.file2){

                    userChoiceStr = "Hard level chosen";
                }

                EditText userName =(EditText)findViewById(R.id.userName);

                String userNameStr=userName.getText().toString();

                if( TextUtils.isEmpty(userName.getText())){

                    userName.setError( "Name required!" );

                }else{
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);


                    SharedPreferences.Editor editor= sp.edit();

                    editor.putString("userChoice", userChoiceStr);
                    editor.putString("userName", userNameStr);
                    editor.commit();

                    startActivity(mainActivity);
                }

            }
        });


    }
}