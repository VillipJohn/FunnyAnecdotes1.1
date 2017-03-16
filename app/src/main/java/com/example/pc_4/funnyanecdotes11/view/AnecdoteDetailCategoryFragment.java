package com.example.pc_4.funnyanecdotes11.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pc_4.funnyanecdotes11.R;
import com.example.pc_4.funnyanecdotes11.data.DataBaseHelper;


public class AnecdoteDetailCategoryFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    public static final String CATEGORY = "category";

    private DataBaseHelper myDbHelper;
    private Cursor cursor;

    private int countAnecrotesInCategory;
    private int numberInCategory;
    private int favoriteOrNot;
    private String textAnecdote;
    public ImageView left, right, home, favorite;
    public TextView textViewAnecdote;
    public TextView textViewCount;
    public ScrollView contentAnecdote;

    private int min_distance = 100;
    private float downX, downY, upX, upY;


    public AnecdoteDetailCategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDbHelper = new DataBaseHelper(getContext());

      /*  if (getArguments().containsKey(CATEGORY)) {
             setAnecdote();
               *//* Toast.makeText(getContext(),
                        "numberInCategory:  " + Integer.toString(numberInCategory) + "\n" +
                                "countAnecrotesInCategory:  " + Integer.toString(countAnecrotesInCategory) + "\n" +
                                "favoriteOrNot:  " + Integer.toString(favoriteOrNot) + "\n" +
                                "TEXT:  " + textAnecdote,
                        Toast.LENGTH_LONG).show();
*//*
        }*/
    }

    private void setAnecdote() {
        int chosenCategory = getArguments().getInt(CATEGORY);

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        //получаем из базы в курсор все записи анекдотов данной категории
        cursor = myDbHelper.query(DataBaseHelper.TABLE_NAME, new String[]{DataBaseHelper.KEY_ID, DataBaseHelper.KEY_FAVORITE, DataBaseHelper.KEY_TEXT, DataBaseHelper.KEY_VIEWED},
                "CATEGORY = ?",
                new String[]{Integer.toString(chosenCategory)},
                null, null, null);

        //получаем количество всех анекдотов в данной категории
        countAnecrotesInCategory = cursor.getCount();

        //определяем первый не просмотренный и если все прорсмотрены, устанавливаем последний
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(3) == 0 || cursor.getInt(0) == countAnecrotesInCategory) {
                    numberInCategory = cursor.getPosition() + 1;
                    favoriteOrNot = cursor.getInt(1);
                    textAnecdote = cursor.getString(2);
                }
            } while (textAnecdote == null && cursor.moveToNext());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (textAnecdote != null) {
            textViewAnecdote = (TextView) rootView.findViewById(R.id.textAnecdote);
            textViewAnecdote.setText(textAnecdote);

            textViewCount = (TextView) rootView.findViewById(R.id.textCount);
            textViewCount.setText(Integer.toString(numberInCategory) + "/" + Integer.toString(countAnecrotesInCategory));

            left = (ImageView) rootView.findViewById(R.id.imgBtnLeft);
            right = (ImageView) rootView.findViewById(R.id.imgBtnRight);
            home = (ImageView) rootView.findViewById(R.id.imgBtnHome);
            favorite = (ImageView) rootView.findViewById(R.id.imgBtnFavorite);

            left.setOnClickListener(this);
            right.setOnClickListener(this);
            home.setOnClickListener(this);
            favorite.setOnClickListener(this);

            contentAnecdote = (ScrollView) rootView.findViewById(R.id.scrollContentAnecdote);
            contentAnecdote.setOnTouchListener(this);

        }


        return rootView;
    }

    @Override
    public void onClick(View v) {

        // определяем нажатую кнопку
        switch (v.getId()) {
            // нажата кнеопка влево
            case R.id.imgBtnLeft:
                offViewedAnecdote();
                onPreviousAnecdote();
                break;
            // нажата кнопка вправо
            case R.id.imgBtnRight:
                onViewedAnecdote();
                onNextAnecdote();
                break;
            case R.id.imgBtnHome:
                Intent intent = new Intent(getContext(), AnecdoteListActivity.class);
                getActivity().startActivity(intent);
                break;
            // Функция
            case R.id.imgBtnFavorite:
                ContentValues values = new ContentValues();
                values.put(DataBaseHelper.CATEGORY, 0);
                values.put(DataBaseHelper.KEY_FAVORITE, 1);
                values.put(DataBaseHelper.KEY_TEXT, textAnecdote);
                values.put(DataBaseHelper.KEY_VIEWED, 0);
                //cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
                myDbHelper.addFavorite(DataBaseHelper.TABLE_NAME, values);
        /*Toast.makeText(getContext(),"VIEWED = " + Integer.toString(cursor.getInt(3)),
                Toast.LENGTH_SHORT).show();*/
                break;
        }
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
            //this.v = v;

            switch(event.getAction()) { // Check vertical and horizontal touches
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    //HORIZONTAL SCROLL
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > min_distance) {
                            // left or right
                            if (deltaX < 0) {
                                this.onLeftToRightSwipe();
                                return true;
                            }
                            if (deltaX > 0) {
                                this.onRightToLeftSwipe();
                                return true;
                            }
                        } else {
                            //not long enough swipe...
                            return false;
                        }
                    }
                }
            }
            return false;
        }

    public void onLeftToRightSwipe(){
        offViewedAnecdote();
        onPreviousAnecdote();
    }

    public void onRightToLeftSwipe() {
        onViewedAnecdote();
        onNextAnecdote();
    }

    private void onViewedAnecdote() {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_VIEWED, 1);
        //cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
        myDbHelper.updateViewed(DataBaseHelper.TABLE_NAME, values, DataBaseHelper.KEY_ID + " = ?", new String[] {Integer.toString(cursor.getInt(0))});
        /*Toast.makeText(getContext(),"VIEWED = " + Integer.toString(cursor.getInt(3)),
                Toast.LENGTH_SHORT).show();*/
    }

    private void onNextAnecdote() {
        if(((cursor.getPosition() + 1) != countAnecrotesInCategory) && cursor.moveToNext()) {
            textAnecdote = cursor.getString(2);
            textViewAnecdote.setText(textAnecdote);

            numberInCategory = cursor.getPosition() + 1;
            textViewCount.setText(Integer.toString(numberInCategory) + "/" + Integer.toString(countAnecrotesInCategory));
        }
    }

    private void offViewedAnecdote() {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.KEY_VIEWED, 0);
        //cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
        myDbHelper.updateViewed(DataBaseHelper.TABLE_NAME, values, DataBaseHelper.KEY_ID + " = ?", new String[] {Integer.toString(cursor.getInt(0))});
        /*Toast.makeText(getContext(),"VIEWED = " + Integer.toString(cursor.getInt(3)),
                Toast.LENGTH_SHORT).show();*/
    }

    private void onPreviousAnecdote() {
        if(((cursor.getPosition() + 1) != 1)  && cursor.moveToPrevious()) {
            textAnecdote = cursor.getString(2);
            textViewAnecdote.setText(textAnecdote);

            numberInCategory = cursor.getPosition() + 1;
            textViewCount.setText(Integer.toString(numberInCategory) + "/" + Integer.toString(countAnecrotesInCategory));
            offViewedAnecdote();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        // Закрываем подключение и курсор
        myDbHelper.close();
        //cursor.close();
    }
}

