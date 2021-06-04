package com.example.guesstheword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetTextI18n")
public class Game extends AppCompatActivity {


    /*CardView and ImageView for the 'Word-Image' */
    CardView cardViewImg;
    ImageView imgWord;


    /*Reset and Restart the game*/
    RelativeLayout layoutRestartGame;
    TextView btnRestartGame;
    TextView btnReset;


    /*For knowing which letter of the layout is selected in order to
     * bring them back when the (UP) layout is selected*/
    int[] arrWhichLetterSelectedPosition;
    boolean[] arrIsLetterSelected;
    int positionOfSelectedLetter;

    /*FOR UP STAFF*/
    LinearLayout layoutSelectedLetters;

    /*FOR DOWN STAFF*/
    char[] charsLetters;
    GridLayout layoutSelectionLetter;

    /*if positionOfSelectedLetter == length of the word
     * the question is finished....*/
    int selectedLettersCount;


    /*Position of questions
     * is game finished or not*/
    int questionPosition;


    /*Bundle for getting data from Previous Activity*/

    /*Words*/
    String[] bundleWords;

    /*Size of each word for setting TextView's in size of this.*/
    int[] bundleSizeOfEachWord;

    /*Random letters for (DOWN) layout*/
    String[] bundleRandomLetters;

    /*Size of all words*
     * when the game should finish?*/
    int bundleWordsCount;

    /*Getting image*/
    int[] bundleImages;

    /*Appending the letters to make a word
     * and checking whether the answer is correct or not*/
    StringBuilder resultWord = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        /*Initialization everything*/
        init();

        btnReset.setOnClickListener(v -> reset());

        btnRestartGame.setOnClickListener(v -> {
            layoutRestartGame.setVisibility(View.GONE);
            questionPosition = 0;
            cardViewImg.setAlpha(1f);
            reset();
        });

    }

    /*Initialization of everything*/
    public void init() {

        /*Initialization of bundles*/
        initBundle();

        /*ImageView for word*/
        imgWord = findViewById(R.id.image_word_game);
        cardViewImg = findViewById(R.id.card_view_img_word);

        /*Initialization 2 Layouts (UP) and (DOWN)*/
        layoutSelectionLetter = findViewById(R.id.layout_letter_selection);
        layoutSelectedLetters = findViewById(R.id.layout_selected_letters);

        /*Reset and Restart Game*/
        layoutRestartGame = findViewById(R.id.layout_restart_game);
        btnRestartGame = findViewById(R.id.btn_restart_game);
        btnReset = findViewById(R.id.btn_reset_game);

        /*Setting selectedLettersCount to 0 because nothing is selected*/
        selectedLettersCount = 0;

        /*Setting positionOfSelectedLetter to 0 because nothing is selected*/
        positionOfSelectedLetter = 0;

        /*Position of each question*/
        questionPosition = 0;


        /*Converting String to array of char to setting each one to the (DOWN) layout*/
        charsLetters = bundleRandomLetters[questionPosition].toCharArray();

        /*Image of the word*/
        imgWord.setBackgroundResource(bundleImages[questionPosition]);

        /*Is word selected or not*/
        arrIsLetterSelected = new boolean[layoutSelectedLetters.getChildCount()];
        arrWhichLetterSelectedPosition = new int[layoutSelectedLetters.getChildCount()];

        initLayoutLetterSelection();
        initLayoutSelectedLetters();
    }

    /*Init bundle*/
    public void initBundle() {
        Bundle bundle = getIntent().getExtras();

        /*Random letters for (DOWN) layout*/
        bundleRandomLetters = bundle.getStringArray("randomLetters");

        /*Size of each word for setting text view visible as much as size of each word*/
        bundleSizeOfEachWord = bundle.getIntArray("sizeOfEachWord");

        /*All words*/
        bundleWords = bundle.getStringArray("words");

        /*Count of all words for knowing when the program finishes.*/
        bundleWordsCount = bundle.getInt("wordsCount");

        /*Images*/
        bundleImages = bundle.getIntArray("images");

    }

    /*UP*/
    public void initLayoutSelectedLetters() {
        for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
            TextView textSelectedLetter = (layoutSelectedLetters.getChildAt(i) instanceof TextView) ? (TextView) layoutSelectedLetters.getChildAt(i) : null;
            if (textSelectedLetter == null) return;

            layoutSelectedLetters.getChildAt(i).setVisibility(View.GONE);
            layoutSelectedLetters.getChildAt(i).setEnabled(true);

            for (int j = 0; j < bundleSizeOfEachWord[questionPosition]; j++) {
                layoutSelectedLetters.getChildAt(j).setVisibility(View.VISIBLE);
            }

            /*Setting background image for all the selected letters*/
            textSelectedLetter.setBackgroundResource(R.drawable.empty_letter_frame);

            int finalI = i;

            layoutSelectedLetters.getChildAt(i).setOnClickListener(v -> {

                if ((arrIsLetterSelected[finalI]) || (arrWhichLetterSelectedPosition[finalI] != -1)) {

                    selectedLettersCount--;

                    /*Setting text of the current TextView to null  for deselecting*/
                    ((TextView) v).setText("");

                    /*Deselecting the current position*/
                    arrIsLetterSelected[finalI] = false;

                    /*Setting the position of positionOfSelectedLetter to the Current selected position*/
                    positionOfSelectedLetter = finalI;

                    /*Setting visibility of the the selected letter to VISIBLE */
                    layoutSelectionLetter.getChildAt(arrWhichLetterSelectedPosition[finalI]).setVisibility(View.VISIBLE);
                    layoutSelectionLetter.getChildAt(arrWhichLetterSelectedPosition[finalI]).setEnabled(true);

                    arrWhichLetterSelectedPosition[finalI] = -1;

                    /*Setting background image to empty_letter_frame*/
                    layoutSelectedLetters.getChildAt(finalI).setBackgroundResource(R.drawable.empty_letter_frame);

                }
            });
        }

    }

    /*DOWN*/
    public void initLayoutLetterSelection() {
        for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
            TextView textLetterSelection = (layoutSelectionLetter.getChildAt(i) instanceof TextView) ? (TextView) layoutSelectionLetter.getChildAt(i) : null;
            if (textLetterSelection == null) return;

            int finalI = i;

            /*Setting the letters into it's own place*/
            charsLetters = bundleRandomLetters[questionPosition].toCharArray();
            textLetterSelection.setText("" + charsLetters[i]);

            /*Selecting a letter*/
            layoutSelectionLetter.getChildAt(i).setOnClickListener(v -> {

                /*Game is not Finished
                 * The questions are not all answered*/
                if (questionPosition != bundleWordsCount) {

                    /*Question is not finished*/
                    if (selectedLettersCount != bundleSizeOfEachWord[questionPosition]) {

                        selectedLettersCount++;

                        /*Condition for setting text to each letter (UP)*/
                        selectionCondition(questionPosition, finalI);

                        /*Setting the visibility of the selected letter to INVISIBLE*/
                        v.setVisibility(View.INVISIBLE);

                        /*Setting the position of selected letter in order to turn it back to it's own place
                         * when the answer is wrong*/
                        arrWhichLetterSelectedPosition[positionOfSelectedLetter] = finalI;

                        /*Selecting next letter*/
                        positionOfSelectedLetter++;

                        /*Question is finished*/
                        if (selectedLettersCount == bundleSizeOfEachWord[questionPosition]) {

                            /*Game is finished*/
                            if (questionPosition == (bundleWordsCount - 1)) {
                                layoutRestartGame.setVisibility(View.VISIBLE);
                                cardViewImg.setAlpha(0.4f);
                            }

                            /*Game is not finished, the question is finished*/
                            else {
                                /*Disabling all the components (UP/DOWN)*/
                                layoutEnable("disable");

                                checkAnswer();

                                /*Next question or the game is finished completely*/
                                questionPosition++;

                                bundleSizeOfEachWord[questionPosition] = bundleSizeOfEachWord[questionPosition];


                                new Handler().postDelayed(this::reset, 1000);
                            }


                        }
                    }

                }

            });

        }

    }

    /*Checking the answer when the game is end*/
    public void checkAnswer() {
        for (int j = 0; j < bundleSizeOfEachWord[questionPosition]; j++) {
            String text = ((TextView) layoutSelectedLetters.getChildAt(j)).getText().toString();
            resultWord.append(text);
        }
        String result = resultWord.toString();
        if (result.equals(bundleWords[questionPosition])) {
            Toast.makeText(this, "CORRECT: " + resultWord.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "WRONG: " + resultWord.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*For selecting a letter*/
    /* WhichLetter == position of the letter int layout (UP)
     * finalI ==  position of the letter in layout (DOWN)*/
    public void selectALetter(int whichLetterInLayoutUP, int whichLetterInLayoutDOWN) {

        /*Setting background image for the selected TextView (Letter (UP))*/
        layoutSelectedLetters.getChildAt(whichLetterInLayoutUP).setBackgroundResource(R.drawable.letter_frame);

        /*For setting the text of UP TextView's to the selected Letter*/
        ((TextView) layoutSelectedLetters.getChildAt(whichLetterInLayoutUP)).setText("" + charsLetters[whichLetterInLayoutDOWN]);

        /*Means: the letter is selected*/
        arrIsLetterSelected[whichLetterInLayoutUP] = true;

        /*For bringing back the letter and setting the visibility of the text to VISIBLE*/
        arrWhichLetterSelectedPosition[whichLetterInLayoutUP] = whichLetterInLayoutDOWN;

        positionOfSelectedLetter = whichLetterInLayoutUP;

    }

    public void selectionCondition(int questionPosition, int finalI) {

        switch (bundleSizeOfEachWord[questionPosition]) {

            case 3:
                for (int i = 0; i < 3; i++) {
                    if (!arrIsLetterSelected[i]) {
                        selectALetter(i, finalI);
                        return;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < 4; i++) {
                    if (!arrIsLetterSelected[i]) {
                        selectALetter(i, finalI);
                        return;
                    }
                }
                break;

            case 5:
                for (int i = 0; i < 5; i++) {
                    if (!arrIsLetterSelected[i]) {
                        selectALetter(i, finalI);
                        return;
                    }
                }
                break;

            case 6:
                for (int i = 0; i < 6; i++) {
                    if (!arrIsLetterSelected[i]) {
                        selectALetter(i, finalI);
                        return;
                    }
                }
                break;

            case 7:
                for (int i = 0; i < 7; i++) {
                    if (!arrIsLetterSelected[i]) {
                        selectALetter(i, finalI);
                        return;
                    }
                }
                break;

            case 8:
                for (int i = 0; i < 8; i++) {
                    if (!arrIsLetterSelected[i]) {
                        selectALetter(i, finalI);
                        return;
                    }
                }
                break;
        }
    }

    public void reset() {

        /*Setting positionOfSelectedLetter of letter to 0 because nothing is selected yet*/
        positionOfSelectedLetter = 0;

        /*Setting selectedLettersCount to 0 because nothing is selected yet*/
        selectedLettersCount = 0;

        /*Setting resultWord to null when the game is reset*/
        resultWord = new StringBuilder();

        /*For setting false (those selected letters' places), for setting text to null (UP) and setting arrWhichLetterSelectedPosition to -1*/
        for (int j = 0; j < layoutSelectedLetters.getChildCount(); j++) {
            arrWhichLetterSelectedPosition[j] = -1;
            ((TextView) layoutSelectedLetters.getChildAt(j)).setText("");
            layoutSelectedLetters.getChildAt(j).setVisibility(View.GONE);
            arrIsLetterSelected[j] = false;
        }

        /*setting VISIBILITY of selected letters to VISIBLE*
         * for setting the length of texts as much as the length of letters*/
        for (int j = 0; j < bundleSizeOfEachWord[questionPosition]; j++) {
            layoutSelectedLetters.getChildAt(j).setVisibility(View.VISIBLE);
            layoutSelectedLetters.getChildAt(j).setBackgroundResource(R.drawable.empty_letter_frame);
        }

        /*Casting; for setting the letters into (DOWN) layout*/
        charsLetters = bundleRandomLetters[questionPosition].toCharArray();

        /*Setting text for (DOWN) letters*/
        for (int j = 0; j < layoutSelectionLetter.getChildCount(); j++) {
            ((TextView) layoutSelectionLetter.getChildAt(j)).setText("" + charsLetters[j]);
            layoutSelectionLetter.getChildAt(j).setVisibility(View.VISIBLE);
        }

        /*Setting image for Image Word*/
        imgWord.setBackgroundResource(bundleImages[questionPosition]);

        /*Setting the enable to true*/
        layoutEnable("enable");

    }

    /*Setting enable of all the components whether "enable or disable"*/
    public void layoutEnable(String type) {

        if (type.equals("enable")) {
            for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
                layoutSelectionLetter.getChildAt(i).setEnabled(true);
            }
            for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
                layoutSelectedLetters.getChildAt(i).setEnabled(true);
            }
        } else if (type.equals("disable")) {
            for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
                layoutSelectionLetter.getChildAt(i).setEnabled(false);
            }
            for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
                layoutSelectedLetters.getChildAt(i).setEnabled(false);
            }
        }
    }

}
