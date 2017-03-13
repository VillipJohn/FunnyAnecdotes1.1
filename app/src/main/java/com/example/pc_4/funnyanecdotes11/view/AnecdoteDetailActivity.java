package com.example.pc_4.funnyanecdotes11.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pc_4.funnyanecdotes11.R;


public class AnecdoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            int numberOfCategory = getIntent().getIntExtra(AnecdoteDetailCategoryFragment.CATEGORY, 0);

            Log.d("LOG", "numberOfCategory" + numberOfCategory);

            if(numberOfCategory == 0){
                arguments.putInt(AnecdoteFavoriteCategoryFragment.CATEGORY, numberOfCategory);
                AnecdoteFavoriteCategoryFragment fragment = new AnecdoteFavoriteCategoryFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                arguments.putInt(AnecdoteDetailCategoryFragment.CATEGORY, numberOfCategory);
                AnecdoteDetailCategoryFragment fragment = new AnecdoteDetailCategoryFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }
        }
    }
}
