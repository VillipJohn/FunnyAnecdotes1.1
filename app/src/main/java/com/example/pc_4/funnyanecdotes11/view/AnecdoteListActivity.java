package com.example.pc_4.funnyanecdotes11.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc_4.funnyanecdotes11.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AnecdoteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AnecdoteListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        String[] categoryList = getResources().getStringArray(R.array.category_list);
        List<String> list = Arrays.asList(categoryList);
        ArrayList<String> listOfCategory = new ArrayList<>(list);

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(listOfCategory));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<String> mValues;

        public SimpleItemRecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.mItem = mValues.get(position);

            if(mValues.get(position).equals("ИЗБРАННОЕ")){
                holder.mImgView.setImageResource(R.drawable.star);
            } else {
                holder.mImgView.setImageResource(R.drawable.smile);
            }

            holder.mContentView.setText(mValues.get(position));

            holder.chosenCategory = position;

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(AnecdoteListActivity.this, holder.mContentView.toString() ,Toast.LENGTH_LONG).show();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        //arguments.putString(AnecdoteDetailCategoryFragment.ARG_ITEM_ID, holder.mItem.id);
                        AnecdoteDetailCategoryFragment fragment = new AnecdoteDetailCategoryFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AnecdoteDetailActivity.class);

                        intent.putExtra(AnecdoteDetailCategoryFragment.CATEGORY, holder.chosenCategory);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImgView;
            public final TextView mContentView;
            public int chosenCategory;
            //public AnecdoteContent.DummyItem mItem;

            public ViewHolder (View view){
                super(view);

                mView = view;
                mImgView = (ImageView) view.findViewById(R.id.imageLabel);
                mContentView = (TextView) view.findViewById(R.id.content);
            }


            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
