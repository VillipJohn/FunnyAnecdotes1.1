package com.example.pc_4.funnyanecdotes11.view;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_4.funnyanecdotes11.R;
import com.example.pc_4.funnyanecdotes11.data.DataBaseHelper;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    public static final String CATEGORY = "category";

    private DataBaseHelper myDbHelper;
    private Cursor cursor;

    private int countAnecrotesInCategory;
    private int numberInCategory;
    private int favoriteOrNot;
    private String textAnecdote;
    public ImageView left, right;
    public TextView textViewAnecdote;
    public TextView textViewCount;

    private int min_distance = 100;
    private float downX, downY, upX, upY;

   /* *
     * The dummy content this fragment is presenting.

    private AnecdoteContent.DummyItem mItem;

    *
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDbHelper = new DataBaseHelper(getContext());

        if (getArguments().containsKey(CATEGORY)) {
             setAnecdote();
               /* Toast.makeText(getContext(),
                        "numberInCategory:  " + Integer.toString(numberInCategory) + "\n" +
                                "countAnecrotesInCategory:  " + Integer.toString(countAnecrotesInCategory) + "\n" +
                                "favoriteOrNot:  " + Integer.toString(favoriteOrNot) + "\n" +
                                "TEXT:  " + textAnecdote,
                        Toast.LENGTH_LONG).show();
*/
        }
    }

    private void setAnecdote() {
        int chosenCategory = getArguments().getInt(CATEGORY);

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        //получаем из базы в курсор все записи анекдотов данной категории
        cursor = myDbHelper.query("maintable", new String[]{"IDCATEGORY", "FAVORITE", "TEXT", "VIEWED"},
                "CATEGORY = ?",
                new String[]{Integer.toString(chosenCategory)},
                null, null, null);

        //получаем количество всех анекдотов в данной категории
        countAnecrotesInCategory = cursor.getCount();

        //определяем первый не просмотренный и если все прорсмотрены, устанавливаем последний
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(3) == 0 || cursor.getInt(0) == (countAnecrotesInCategory - 1)) {
                    numberInCategory = cursor.getInt(0);
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
        }

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        rootView.setOnTouchListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        // определяем нажатую кнопку
        switch (v.getId()) {
            // нажата кнеопка влево
            case R.id.imgBtnLeft:
                textViewAnecdote.setText("Нажата кнопка в влево");
                break;
            // Функция
            case R.id.imgBtnRight:
                if(cursor.isAfterLast())
                if(cursor.moveToNext()){
                    textAnecdote = cursor.getString(2);
                    textViewAnecdote.setText(textAnecdote);

                    numberInCategory = cursor.getInt(0);
                    textViewCount.setText(Integer.toString(numberInCategory) + "/" + Integer.toString(countAnecrotesInCategory));
                }
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
            Toast.makeText(getContext(),"left to right",
                    Toast.LENGTH_SHORT).show();
        }

        public void onRightToLeftSwipe() {
            Toast.makeText(getContext(),"right to left",
                    Toast.LENGTH_SHORT).show();
        }

}

