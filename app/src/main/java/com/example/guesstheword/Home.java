package com.example.guesstheword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;


public class Home extends AppCompatActivity {


    /*Starting Game Button*/
    TextView btnStartGame;

    /*Spinner*/
    Spinner spinnerGameType;
    ArrayAdapter<String> adapter;

    /*Array for adding Game types to spinner*/
    String[] arrGameTypesSpinner;

    /*Adding words*/
    String[] arrWords;

    /*Setter and Getter for adding words*/
    private String[] words;

    /*Size of each word*/
    private int[] arrSizeOfEachWord;

    /*Size of all words*/
    private int wordsCount;

    /*Random Letters for (DOWN) layout */
    private String[] arrRandomLetters;

    private int[] images;
    int[] arrImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        init();

        /*Selecting a game type*/
        spinnerGameType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*Setting game type (Animals, Fruits, Things, ...)*/
                switch (position) {
                    /*Fruits*/
                    case 0:
                        arrWords = new String[]{"KIWI", "ORANGE", "APPLE", "MANGO"};
                        arrRandomLetters = new String[]{getString(R.string.random_letter_kiwi), getString(R.string.random_letter_orange),
                                getString(R.string.random_letter_apple), getString(R.string.random_letter_mango)};

                        arrImages = new int[]{R.drawable.kiwi, R.drawable.orange, R.drawable.apple, R.drawable.mango};
                        break;
                    /*Animals*/
                    case 1:
                        arrWords = new String[]{"CAT", "HORSE", "DOG", "LION"};
                        arrRandomLetters = new String[]{getString(R.string.random_letter_cat), getString(R.string.random_letter_horse)
                                , getString(R.string.random_letter_dog), getString(R.string.random_letter_lion)};

                        arrImages = new int[]{R.drawable.cat, R.drawable.horse, R.drawable.dog, R.drawable.lion};

                        break;
                    /*Things*/
//                    case 2:
//                        arrGameTypes = new String[]{"", "", "", "", "", "", "", ""};
//                        arrRandomLetters = new String[]{"EALEPSDPFE", "AEBEANIENA", "CERFYRDHSE", "MTELBJONUE"};
//                        setArrRandomLetters(arrRandomLetters);
//                        break;
                }

                /*Setting length of each word to the sizeOfLetter array*/
                int[] sizeOfEachWords = new int[arrWords.length];
                for (int i = 0; i < arrWords.length; i++) {
                    sizeOfEachWords[i] = arrWords[i].length();
                }

                /*Setting length of each letter e.x  Apple == 5  The word "Apple" has 5 letters */
                setArrSizeOfEachWord(sizeOfEachWords);
//                Toast.makeText(Home.this, "Length of each word: " + getArrLetterLengthCount()[0], Toast.LENGTH_SHORT).show();

                setWordsCount(arrWords.length);
//                Toast.makeText(Home.this, "Length of all words: " + getWordsCount(), Toast.LENGTH_SHORT).show();

                /*Sending image*/
                setImages(arrImages);


                /*Setting Random Letters for (DOWN) layout*/
                setArrRandomLetters(arrRandomLetters);
//                Toast.makeText(Home.this, "RandomLetters: " + getArrRandomLetters()[0], Toast.LENGTH_SHORT).show();

                /*Setting game type (Fruits, Animals, Things, ...)*/
                setWords(arrWords);
//                Toast.makeText(Home.this, "" + Arrays.toString(getArrLetterLengthCount()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*Stating the game*/
        btnStartGame.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Game.class);

            /*Sending all the words*/
            intent.putExtra("words", getWords());

            /*Sending length of each word*/
            intent.putExtra("sizeOfEachWord", getArrSizeOfEachWord());

            /*Sending random letters for (DOWN layout)*/
            intent.putExtra("randomLetters", getArrRandomLetters());

            /*Length of all words
             * when the game should finish?*/
            intent.putExtra("wordsCount", getWordsCount());

            /*Sending images*/
            intent.putExtra("images", getImages());

            startActivity(intent);
        });

    }

    public void init() {
        spinnerGameType = findViewById(R.id.spinner_game_type_home);
        btnStartGame = findViewById(R.id.btn_start_game_home);
        arrGameTypesSpinner = new String[]{"Fruits", "Animals"};
//        arrGameTypesSpinner = new String[]{"Fruits", "Animals", "Things"};
        adapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_spinner_item, arrGameTypesSpinner);
        spinnerGameType.setAdapter(adapter);
    }


    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }


    /*Length of all words*/

    public int[] getArrSizeOfEachWord() {
        return arrSizeOfEachWord;
    }

    public void setArrSizeOfEachWord(int[] arrSizeOfEachWord) {
        this.arrSizeOfEachWord = arrSizeOfEachWord;
    }


    /*Random Letters*/

    public String[] getArrRandomLetters() {
        return arrRandomLetters;
    }

    public void setArrRandomLetters(String[] arrRandomLetters) {
        this.arrRandomLetters = arrRandomLetters;
    }


    /*Words count*/

    public int getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    /*Image*/

    public int[] getImages() {
        return images;
    }

    public void setImages(int[] images) {
        this.images = images;
    }
}