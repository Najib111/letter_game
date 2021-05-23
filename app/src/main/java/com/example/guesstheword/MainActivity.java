package com.example.guesstheword;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {


    int positionOfSelectedLetter;

    boolean[] arrIsLetterSelectedPosition = {false, false, false, false, false};

    /*String == hello   random ()*/
//    boolean[] arrSelectedLetterForAddingWord = {false, false, false, false, false, false, false, false, false, false};


    int[] arrWhichLetterSelectedPosition = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


    /*FOR UP STAFF*/
    LinearLayout layoutSelectedLetters;

    /*FOR DOWN STAFF*/
    String letters = "OABSFCSTSD";
    char[] charsLetters = letters.toCharArray();


    GridLayout layoutSelectionLetter;


    int selectedLettersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.guess_the_word);

        /*Initialization everything*/
        init();

    }

    /*Initialization of everything*/
    public void init() {

        /*Initialization 2 Layouts (UP) and (DOWN)*/
        layoutSelectionLetter = findViewById(R.id.layout_letter_selection);
        layoutSelectedLetters = findViewById(R.id.layout_selected_letters);

        /*Setting selectedLettersCount to 0 because nothing is selected*/
        selectedLettersCount = 0;

        /*Setting positionOfSelectedLetter to 0 because nothing is selected*/
        positionOfSelectedLetter = 0;

        initLayoutLetterSelection();
        initLayoutSelectedLetters();


    }

    /*UP*/
    public void initLayoutSelectedLetters() {
        for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
            TextView textSelectedLetter = (layoutSelectedLetters.getChildAt(i) instanceof TextView) ? (TextView) layoutSelectedLetters.getChildAt(i) : null;
            if (textSelectedLetter == null) return;

            /*Setting background image for all the selected letters*/
            textSelectedLetter.setBackgroundResource(R.drawable.empty_letter_frame);

            int finalI = i;

            textSelectedLetter.setOnClickListener(v -> {
                if (arrIsLetterSelectedPosition[finalI]) {

                    /*whether we can deselect the wrong answer or not*/
                    /*We can*/
//                    if (!arrIsLetterSelectedPosition[finalI]) {
//                        return;
//                    } else {
//                        selectedLettersCount--;
//                    }
                    selectedLettersCount--;

                    /*Setting text of the current TextView to null  for deselecting*/
                    textSelectedLetter.setText("");

                    /*Deselecting the current position*/
                    arrIsLetterSelectedPosition[finalI] = false;

                    /*Setting the position of positionOfSelectedLetter to the Current selected position*/
                    positionOfSelectedLetter = finalI;

                    /*Setting visibility of the the selected letter to VISIBLE */
                    layoutSelectionLetter.getChildAt(arrWhichLetterSelectedPosition[finalI]).setVisibility(View.VISIBLE);
                    layoutSelectionLetter.getChildAt(arrWhichLetterSelectedPosition[finalI]).setEnabled(true);

                    arrWhichLetterSelectedPosition[finalI] = -1;

                    /*Setting background image to empty_letter_frame*/
                    layoutSelectedLetters.getChildAt(finalI).setBackgroundResource(R.drawable.empty_letter_frame);

//                    Toast.makeText(this, "Position: " + positionOfSelectedLetter + "\nSelected Count: " + selectedLettersCount, Toast.LENGTH_SHORT).show();

                }

            });

        }
    }

    /*DOWN*/
    public void initLayoutLetterSelection() {
        for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
            TextView textLetterSelection = (layoutSelectionLetter.getChildAt(i) instanceof TextView) ? (TextView) layoutSelectionLetter.getChildAt(i) : null;
            if (textLetterSelection == null) return;
            arrWhichLetterSelectedPosition[positionOfSelectedLetter] = i;

            int finalI = i;

            /*Setting the letters into it's own place*/
            textLetterSelection.setText("" + charsLetters[i]);

            /*Selecting a letter*/
            textLetterSelection.setOnClickListener(v -> {

                /*If the button is VISIBLE; we can select one*/
//                if (textLetterSelection.getVisibility() == View.VISIBLE) {

                /*Condition: if the positionOfSelectedLetter != length of selected letter*/
                ++selectedLettersCount;
                if (positionOfSelectedLetter != layoutSelectedLetters.getChildCount()) {

                    /*Selecting the letter*/
                    if (!arrIsLetterSelectedPosition[0]) {
                        selectALetter(0, finalI);
                    } else if (!arrIsLetterSelectedPosition[1]) {
                        selectALetter(1, finalI);
                    } else if (!arrIsLetterSelectedPosition[2]) {
                        selectALetter(2, finalI);
                    } else if (!arrIsLetterSelectedPosition[3]) {
                        selectALetter(3, finalI);
                    } else if (!arrIsLetterSelectedPosition[4]) {
                        selectALetter(4, finalI);
                    }


                    /*Setting the visibility of the selected letter to INVISIBLE*/
                    textLetterSelection.setVisibility(View.INVISIBLE);

                    /*Setting enable to false*/
//                        textLetterSelection.setEnabled(false);


                    /*Setting the position of selected letter in order to turn it back to it's own place
                     * when the answer is wrong*/
                    arrWhichLetterSelectedPosition[positionOfSelectedLetter] = finalI;

                    /*Selecting next letter*/
                    positionOfSelectedLetter++;


//                    Toast.makeText(this, "Position: " + positionOfSelectedLetter + "\nSelected Count: " + selectedLettersCount, Toast.LENGTH_SHORT).show();

                    /*Game is finished*/
                    if (selectedLettersCount == layoutSelectedLetters.getChildCount()) {
//                        layoutEnable("disable");
                        for (int j = 0; j < layoutSelectedLetters.getChildCount(); j++) {
                            layoutSelectedLetters.getChildAt(j).setEnabled(false);
                        }
                        new Handler().postDelayed(() -> reset(v), 1000);
                    }


                }

//                }

            });


        }

    }

    /*For selecting a letter*/
    public void selectALetter(int whichLetter, int finalI) {

        /*For setting the text of UP TextView's to the selected Letter*/
        ((TextView) layoutSelectedLetters.getChildAt(whichLetter)).setText("" + charsLetters[finalI]);

        /*Means: the letter is selected*/
        arrIsLetterSelectedPosition[whichLetter] = true;

        /*For bringing back the letter and setting the visibility of the text to VISIBLE*/
        arrWhichLetterSelectedPosition[whichLetter] = finalI;

        /*Setting background image for the selected TextView (Letter (UP))*/
        layoutSelectedLetters.getChildAt(whichLetter).setBackgroundResource(R.drawable.letter_frame);

        positionOfSelectedLetter = whichLetter;

    }

    /*Reset*/
    public void reset(View view) {

        /*Setting positionOfSelectedLetter of letter to 0 because nothing is selected yet*/
        positionOfSelectedLetter = 0;

        /*Setting selectedLettersCount to 0 because nothing is selected yet*/
        selectedLettersCount = 0;


        /*Setting the enable to true*/
        layoutEnable("enable");


        /*For setting false (those selected letters' places), for setting text to null (UP) and setting arrWhichLetterSelectedPosition to -1*/
        for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
            arrIsLetterSelectedPosition[i] = false;
            ((TextView) layoutSelectedLetters.getChildAt(i)).setText("");
            layoutSelectedLetters.getChildAt(i).setEnabled(true);
            arrWhichLetterSelectedPosition[i] = -1;
        }


        /*Setting background image of all (UP) textViews' to empty_letter_frame*/
        for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
            layoutSelectedLetters.getChildAt(i).setBackgroundResource(R.drawable.empty_letter_frame);
        }

//        /*For setting text for all TextViews' (DOWN)*/
//        for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
//            ((TextView) layoutSelectionLetter.getChildAt(i)).setText("" + charsLetters[i]);
//        }

        /*VISIBILITY of letters */
        for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
            layoutSelectionLetter.getChildAt(i).setVisibility(View.VISIBLE);
        }


    }

    /*Setting enable of all the components whether "enable or disable"*/
    public void layoutEnable(String type) {
        switch (type) {

            /*Setting enable == true*/
            case "enable":
                for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
                    layoutSelectionLetter.getChildAt(i).setEnabled(true);
                }
                break;

            /*Setting enable == false*/
            case "disable":
                for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
                    layoutSelectionLetter.getChildAt(i).setEnabled(false);
                }
                break;
        }
    }

}


/*LAST VERSION*/
//
//package com.example.guesstheword;
//
//
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import android.annotation.SuppressLint;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.view.View;
//        import android.view.WindowManager;
//        import android.widget.GridLayout;
//        import android.widget.ImageView;
//        import android.widget.LinearLayout;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//
//public class MainActivity extends AppCompatActivity {
//
//
//    int positionOfSelectedLetter;
//
//    boolean[] arrIsLetterSelectedPosition = {false, false, false, false, false};
//
//    /*String == hello   random ()*/
//    boolean[] arrSelectedLetterForAddingWord = {false, false, false, false, false, false, false, false, false, false};
//
//
//    int[] arrWhichLetterSelectedPosition = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//
//
//    String word;
//    int image;
//
//    ImageView imgForWord;
//
//    GridLayout layoutSelectionLetter;
//    LinearLayout layoutSelectedLetters;
//
//
//    String[] letters = {"O", "H", "C", "N", "T", "A", "E", "C", "G", "D"};
//
//    int[] which = {0, 1, 2, 3, 4};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.guess_the_word);
//
//        imgForWord = findViewById(R.id.image_for_word);
//        positionOfSelectedLetter = 0;
//
//        init();
//
//    }
//
//
//    public void init() {
//        layoutSelectionLetter = findViewById(R.id.layout_letter_selection);
//        layoutSelectedLetters = findViewById(R.id.layout_selected_letters);
//
//        initLayoutLetterSelection();
//        initLayoutSelectedLetters();
//    }
//
//    /*DOWN*/
//    @SuppressLint("SetTextI18n")
//    public void initLayoutLetterSelection() {
//        for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
//            TextView textLetterSelection = (layoutSelectionLetter.getChildAt(i) instanceof TextView) ? (TextView) layoutSelectionLetter.getChildAt(i) : null;
//            if (textLetterSelection == null) return;
//            arrWhichLetterSelectedPosition[positionOfSelectedLetter] = i;
//
//            int finalI = i;
//
//            textLetterSelection.setText(letters[i]);
//
//            textLetterSelection.setOnClickListener(v -> {
//                if (positionOfSelectedLetter != layoutSelectedLetters.getChildCount()) {
//
//                    if (!arrIsLetterSelectedPosition[positionOfSelectedLetter]) {
//
//                        ((TextView) layoutSelectedLetters.getChildAt(which[positionOfSelectedLetter])).setText(letters[finalI]);
//                        arrIsLetterSelectedPosition[positionOfSelectedLetter] = true;
//
//                        arrWhichLetterSelectedPosition[positionOfSelectedLetter] = finalI;
//
//                        textLetterSelection.setText("-");
//                        positionOfSelectedLetter++;
//                    } else {
//                        positionOfSelectedLetter++;
//                        ((TextView) layoutSelectedLetters.getChildAt(which[positionOfSelectedLetter])).setText(letters[finalI]);
//                        positionOfSelectedLetter++;
//
//                    }
//                }
//
//            });
//
//
//        }
//
//    }
//
//    public void initLayoutSelectedLetters() {
//        for (int i = 0; i < layoutSelectedLetters.getChildCount(); i++) {
//            TextView textSelectedLetter = (layoutSelectedLetters.getChildAt(i) instanceof TextView) ? (TextView) layoutSelectedLetters.getChildAt(i) : null;
//            if (textSelectedLetter == null) return;
//
//            int finalI = i;
//            textSelectedLetter.setOnClickListener(v -> {
//                positionOfSelectedLetter = finalI;
//                arrIsLetterSelectedPosition[finalI] = false;
//
//                which[positionOfSelectedLetter] = finalI;
//
//                textSelectedLetter.setText("");
//                Toast.makeText(this, "" + positionOfSelectedLetter, Toast.LENGTH_SHORT).show();
//                arrWhichLetterSelectedPosition[finalI] = -1;
//            });
//        }
//    }
//
//    public void reset(View view) {
//        positionOfSelectedLetter = 0;
//        for (int i = 0; i < arrIsLetterSelectedPosition.length; i++) {
//            arrIsLetterSelectedPosition[i] = false;
//            ((TextView) layoutSelectedLetters.getChildAt(i)).setText("");
//            arrWhichLetterSelectedPosition[i] = -1;
//        }
//        for (int i = 0; i < which.length; i++) {
//            which[i] = i;
//        }
//        for (int i = 0; i < layoutSelectionLetter.getChildCount(); i++) {
//            ((TextView) layoutSelectionLetter.getChildAt(i)).setText(letters[i]);
//        }
//    }
//
//
//}