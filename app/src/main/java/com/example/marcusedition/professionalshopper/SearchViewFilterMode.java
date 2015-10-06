package com.example.marcusedition.professionalshopper;

/**
 * Created by victor on 06.10.15.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * Shows a list that can be filtered in-place with a SearchView in non-iconified <span id="IL_AD9" class="IL_AD">mode</span>.
 */
public class SearchViewFilterMode extends Activity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;

    private final String[] mStrings = {"a","ab","adv","f","fgh"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.list);

        mSearchView = (SearchView) findViewById(R.id.searchView2);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mStrings));
        mListView.setTextFilterEnabled(true);
        setupSearchView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}