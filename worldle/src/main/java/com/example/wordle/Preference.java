package com.example.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Preference extends AppCompatActivity {

    Button updatePrefs;

    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);



        updatePrefs = findViewById(R.id.updatePrefs);

        // initializing the shared preferences
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        RadioGroup radioGroup =(RadioGroup)findViewById(R.id.files);

      updatePrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userChoiceStr = "File 1";

                int fileSelected = radioGroup.getCheckedRadioButtonId();

                if(fileSelected==R.id.file2){

                    userChoiceStr = "File 2";
                }

                EditText userName =(EditText)findViewById(R.id.userName);

                String userNameStr=userName.getText().toString();

                if( TextUtils.isEmpty(userName.getText())){

                    userName.setError( "First name is required!" );

                }else{
                    Intent mainActivity = new Intent(getApplicationContext(), InitialActivity.class);


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