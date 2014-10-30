package com.akvelon.nutrientsguru.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.akvelon.nutrientsguru.R;
import com.akvelon.nutrientsguru.adapter.ProductListAdapter;
import com.akvelon.nutrientsguru.persistence.DataBaseHelper;

import java.util.Set;

public class ProductListFragment extends ListFragment implements OnQueryTextListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_list, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView list = (ListView) getActivity().findViewById(android.R.id.list);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity());

        CursorAdapter adapter = new ProductListAdapter(getActivity(), dataBaseHelper.getAllProducts(), CursorAdapter.NO_SELECTION);
        list.setAdapter(adapter);


    }

    @Override
    public boolean onQueryTextChange(String query) {
        ListView list = (ListView) getActivity().findViewById(android.R.id.list);
        ((ProductListAdapter) list.getAdapter()).getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.product_list, menu);
        SearchView searchItem = (SearchView) menu.findItem(R.id.product_search).getActionView();
        searchItem.setOnQueryTextListener(this);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        // update the main content by replacing fragments
        Fragment fragment = new ProductDetailsFragment();

        Bundle args = new Bundle();
        args.putLong(ProductDetailsFragment.PRODUCT_ID, id);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.product_compare).setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_compare:
                ListView list = (ListView) getActivity().findViewById(android.R.id.list);
                Set<String> selectedProductIds = ((ProductListAdapter) list.getAdapter()).getSelectedProductIds();

                StringBuilder intentParam = new StringBuilder();
                for (String id : selectedProductIds) {
                    intentParam.append(id);
                    intentParam.append(",");
                }

                Fragment fragment = new ProductComparisonFragment();

                Bundle args = new Bundle();
                args.putString(ProductComparisonFragment.SELECTED_IDS, intentParam.toString());
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        return true;
    }


}
