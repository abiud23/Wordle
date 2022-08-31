package com.example.wordle;

import android.content.Context;
import android.content.res.AssetManager;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WGameProcessor {

    Context myContext;
    public WGameProcessor( Context myContext) {

        this.myContext = myContext;
    }
    
//------------------------------------------------------------------------------------------------------
    // generate a specific list of words from the given text files :
//------------------------------------------------------------------------------------------------------


    public  List<String>  generateSpecificWordList(int userChoice){


        List<String> wordlist = new ArrayList<>();
        InputStream mainFile=null;
        String word="";

         try {



            AssetManager am = myContext.getAssets();
            if(userChoice==1) {
                mainFile = am.open("wrdl5.txt");

            }else {
                mainFile = am.open("wrdltest.txt");
            }



            int size= mainFile.available();
            byte[] buffer = new byte[size];
            mainFile.read(buffer);
            mainFile.close();
            word= new String(buffer);

            String[]arrayOfWords = word.split("\n");

            // populate words
            for (int i=0;i<arrayOfWords.length;i++){

                wordlist.add(arrayOfWords[i]);
            }

        }catch (Exception ex){
            ex.getMessage();
        }

        return wordlist;
    }
//------------------------------------------------------------------------------------------------------
// generate a random word from a specific list of words
//------------------------------------------------------------------------------------------------------

public String generateRandomWord(List<String> wordlist){

        // generator number between 0 and wordlist.size()-1
        Random random = new Random();
        int low=0;
        int high = wordlist.size()-1;
        int randomNumber = random.nextInt(high-low+1) + low;

        return wordlist.get(randomNumber);
}





}
