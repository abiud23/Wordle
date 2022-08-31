package com.example.wordle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    WGameProcessor processor = new WGameProcessor(this);
    Button submitWordBtn;
    Button playAgainBtn;
    Button revealWordBtn;


    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int userChoice;

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);


        String userChoiceStr = sp.getString("userChoice","");
        String userName = sp.getString("userName","");



        // toat notification to notifiy the of the file that the user chose
        Toast.makeText(MainActivity.this,userChoiceStr, Toast.LENGTH_LONG).show();

        if(userChoiceStr.compareTo("File 1")==0){
            userChoice = 0;
        }else{
            userChoice = 1;
        }



//---------------------------------------------------------
//   generate a specific wordlist from user choice
//--------------------------------------------------------

        List<String> wordlist = processor.generateSpecificWordList(userChoice);
        //---------------------------------------------------------
//   generate a random word from the selected wordlist
//--------------------------------------------------------
         String randomWord=processor.generateRandomWord(wordlist);




        // customize toolbar
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



        // accessing the submit buttons
        submitWordBtn= findViewById(R.id.submitWord);
        playAgainBtn = findViewById(R.id.playAgain);
        revealWordBtn = findViewById(R.id.revealWord);

        // disable some buttons
        playAgainBtn.setEnabled(false);
        revealWordBtn.setEnabled(false);

        //event listener of play again button
        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent initialActivity = new Intent(getApplicationContext(), InitialActivity.class);
                startActivity(initialActivity);

            }
        });
        // event listener of reveal word button
        revealWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this,randomWord, Toast.LENGTH_SHORT).show();
            }
        });



        // event listener of the submit button
        submitWordBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

             String word="";

             int rowCount=0;


             while(rowCount !=6) {

                   word=  getSpecificRowWord(rowCount);


                    for (int i = 0; i < word.length(); i++) {
                      char letter = word.charAt(i);

                    // if the word is exactly as the as the random word
                    if(randomWord.charAt(0) == word.charAt(0) &&randomWord.charAt(1) == word.charAt(1)&&randomWord.charAt(2) == word.charAt(2)&&randomWord.charAt(3) == word.charAt(3)&&randomWord.charAt(4) == word.charAt(4)) {


                                setColorLetterExistCorrectPosition(rowCount, i);

                                // enable users to play again if they win
                                playAgainBtn.setEnabled(true);
                                Drawable stylebtn = getResources().getDrawable(R.drawable.custom_small_btn);
                                playAgainBtn.setBackground(stylebtn);

                                Toast.makeText(MainActivity.this, "CONGRAGULATIONS !! " + userName, Toast.LENGTH_LONG).show();


                      // IF WORD EXIST AND CORRECT POSITION
                     } else if (randomWord.charAt(i) == letter) {

                          setColorLetterExistCorrectPosition(rowCount, i);

                        // IF WORD EXIST BUT WRONG POSITION
                      } else if (randomWord.indexOf(letter) != -1) {

                          setColorLetterExistWrongPosition(rowCount, i);

                     // enable and style revealWord and play again buttons when user guess wrongly six time
                      }else if((randomWord.compareTo(word)!=0) && rowCount==5){

                          if(randomWord.charAt(i) != letter){
                            setColorLetterDosnotExist(rowCount, i);
                          }
                          // enable buttons
                          playAgainBtn.setEnabled(true);
                          revealWordBtn.setEnabled(true);
                          Drawable stylebtn = getResources().getDrawable(R.drawable.custom_small_btn);
                          revealWordBtn.setBackground(stylebtn);
                          playAgainBtn.setBackground(stylebtn);

                     }  else if(randomWord.charAt(i) != letter) {

                          setColorLetterDosnotExist(rowCount, i);

                       }


                  }


               rowCount++;



             }




           }

        });

    }

//------------------------------------------------------------------------------------------------------
    // this method helps to create the menu options
//------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

//------------------------------------------------------------------------------------------------------
    // this method helps to access menu items
//------------------------------------------------------------------------------------------------------


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if(id==R.id.howToP){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.theverge.com/22892044/wordle-free-game-online-how-to-puzzle"));
            startActivity(browserIntent);

            return true;
        }else if(id==R.id.editP){
            Intent updatePrefs = new Intent(getApplicationContext(),Preference.class);
            startActivity(updatePrefs);
            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    //------------------------------------------------------------------------------------------------------
    // this method helps to reset everything so the user can be able to play again
//------------------------------------------------------------------------------------------------------
public void playAgain() {
    Intent mIntent = getIntent();
    finish();
    startActivity(mIntent);
}

//------------------------------------------------------------------------------------------------------
    // this method retrieve a word from a specific row of letters
//------------------------------------------------------------------------------------------------------

    public String getSpecificRowWord(int rowN0){


        String word="";
        String [][]ids = {
                {"r1c1","r1c2","r1c3","r1c4","r1c5"},
                {"r2c1","r2c2","r2c3","r2c4","r2c5"},
                {"r3c1","r3c2","r3c3","r3c4","r3c5"},
                {"r4c1","r4c2","r4c3","r4c4","r4c5"},
                {"r5c1","r5c2","r5c3","r5c4","r5c5"},
                {"r6c1","r6c2","r6c3","r6c4","r6c5"}
        };

        for(int i=0;i<5;i++){
            // current id name from ids array
            String currentId =ids[rowN0][i];


            int resID = getResources().getIdentifier(currentId,
                    "id", getPackageName());

           EditText letter = findViewById(resID);


           // creating the word by concatenating the letters
            word= word.concat(letter.getText().toString()) ;

        }


      return word.toLowerCase();
    }
//------------------------------------------------------------------------------------------------------
    // this method helps to style the editText(change the background) if the letter exists in the word and
    // it is in the correct position
//------------------------------------------------------------------------------------------------------
    public void setColorLetterExistCorrectPosition(int rowNo, int column){
        //array of all the ids names
        String [][]ids = {
                {"r1c1","r1c2","r1c3","r1c4","r1c5"},
                {"r2c1","r2c2","r2c3","r2c4","r2c5"},
                {"r3c1","r3c2","r3c3","r3c4","r3c5"},
                {"r4c1","r4c2","r4c3","r4c4","r4c5"},
                {"r5c1","r5c2","r5c3","r5c4","r5c5"},
                {"r6c1","r6c2","r6c3","r6c4","r6c5"}
        };

        String currentId =ids[rowNo][column];


        int resID = getResources().getIdentifier(currentId,
                "id", getPackageName());

        EditText letter = findViewById(resID);
        Drawable green = getResources().getDrawable(R.drawable.letterexit_correct_position);
        letter.setBackground(green);

    }
//------------------------------------------------------------------------------------------------------
    // this method helps to style the editText(change the background) if the letter exists in the word BUT
    // it is in the wrong position
//------------------------------------------------------------------------------------------------------
    public void setColorLetterExistWrongPosition(int rowNo, int column){
        String [][]ids = {
                {"r1c1","r1c2","r1c3","r1c4","r1c5"},
                {"r2c1","r2c2","r2c3","r2c4","r2c5"},
                {"r3c1","r3c2","r3c3","r3c4","r3c5"},
                {"r4c1","r4c2","r4c3","r4c4","r4c5"},
                {"r5c1","r5c2","r5c3","r5c4","r5c5"},
                {"r6c1","r6c2","r6c3","r6c4","r6c5"}
        };

        String currentId =ids[rowNo][column];


        int resID = getResources().getIdentifier(currentId,
                "id", getPackageName());

        EditText letter = findViewById(resID);
        Drawable green = getResources().getDrawable(R.drawable.letterexist_wrongposition);
        letter.setBackground(green);

    }
//------------------------------------------------------------------------------------------------------
    // this method helps to style the editText(change the background) if the letter doesn't exist in the word and
//------------------------------------------------------------------------------------------------------
    public void setColorLetterDosnotExist(int rowNo, int column){
        String [][]ids = {
                {"r1c1","r1c2","r1c3","r1c4","r1c5"},
                {"r2c1","r2c2","r2c3","r2c4","r2c5"},
                {"r3c1","r3c2","r3c3","r3c4","r3c5"},
                {"r4c1","r4c2","r4c3","r4c4","r4c5"},
                {"r5c1","r5c2","r5c3","r5c4","r5c5"},
                {"r6c1","r6c2","r6c3","r6c4","r6c5"}
        };

        String currentId =ids[rowNo][column];


        int resID = getResources().getIdentifier(currentId,
                "id", getPackageName());

        EditText letter = findViewById(resID);
        Drawable green = getResources().getDrawable(R.drawable.letter_doesnot_exist);
        letter.setBackground(green);

    }



}

