package com.example.pc_4.funnyanecdotes11.view;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc_4.funnyanecdotes11.R;
import com.example.pc_4.funnyanecdotes11.data.DataBaseHelper;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    public static final String CATEGORY = "category";

    private DataBaseHelper myDbHelper;
    private Cursor cursor;

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
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            int chosenCategory = getArguments().getInt(CATEGORY);

           /* Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }*/

            DataBaseHelper myDbHelper = new DataBaseHelper(getContext());

            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            //Toast.makeText(ItemListActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();
            //Log.v("myLog", "Successgully Imported");

            cursor = myDbHelper.query("maintable", new String[]{"IDCATEGORY", "FAVORITE", "TEXT", "VIEWED"},
                    "CATEGORY = 1",
                    null,
                    null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Toast.makeText(getContext(),
                            "ID CATEGORY: " + cursor.getString(0) + "\n" +
                                    "FAVORITE:  " + cursor.getString(1) + "\n" +
                                    "TEXT:  " + cursor.getString(2),
                            Toast.LENGTH_LONG).show();
                } while (cursor.moveToNext());


            /*Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                            KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
*/
        /*c = myDbHelper.query("maintable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Toast.makeText(ItemListActivity.this,
                        "_id: " + c.getString(0) + "\n" +
                                "CATEGORY: " + c.getString(1) + "\n" +
                                "ID CATEGORY: " + c.getString(2) + "\n" +
                                "FAVORITE:  " + c.getString(3) + "\n" +
                                "TEXT:  " + c.getString(4),
                        Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
        }*/
            }
        }
    }

   /* public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PH_NO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));

        return contact;

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

       /* // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }*/

        return rootView;
    }


}
