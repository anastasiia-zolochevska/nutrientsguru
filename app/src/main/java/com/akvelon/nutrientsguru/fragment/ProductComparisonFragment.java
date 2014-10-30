package com.akvelon.nutrientsguru.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.akvelon.nutrientsguru.R;
import com.akvelon.nutrientsguru.adapter.NutrientListAdapter;
import com.akvelon.nutrientsguru.model.Nutrient;
import com.akvelon.nutrientsguru.model.NutrientGroup;
import com.akvelon.nutrientsguru.model.NutrientListItem;
import com.akvelon.nutrientsguru.model.Product;
import com.akvelon.nutrientsguru.persistence.DataBaseHelper;
import com.akvelon.nutrientsguru.service.NutrientService;
import com.akvelon.nutrientsguru.service.ProductService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProductComparisonFragment extends Fragment {

    public static final String SELECTED_IDS = "productIds";

    private ProductService productService;
    private NutrientService nutrientService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_comparison, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity());

        productService = new ProductService(dataBaseHelper);
        nutrientService = new NutrientService(dataBaseHelper);

        Map<Long, Product> productsById = productService.getProducts(getProductIds());
        Map<Long, Nutrient> nutrientsById = nutrientService.getNutrients();
        Map<NutrientGroup, List<NutrientListItem>> nutrientValues = nutrientService.getNutrientValues(productsById, nutrientsById);

        NutrientListAdapter adapter = new NutrientListAdapter(getActivity(), nutrientService.getNutrientGroups(nutrientValues), nutrientValues);

        ExpandableListView expandableListView = (ExpandableListView) getActivity().findViewById(R.id.productComparisonTable);

        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);

        List<Product> sortedProducts = new ArrayList<Product>(productsById.values());
        Collections.sort(sortedProducts);
        ((TextView) getActivity().findViewById(R.id.productName1)).setText(sortedProducts.get(0).getName());
        if (sortedProducts.size() > 1) {
            TextView textView2 = (TextView) getActivity().findViewById(R.id.productName2);
            textView2.setText(sortedProducts.get(1).getName());
            textView2.setVisibility(View.VISIBLE);
        }
        if (sortedProducts.size() > 2) {
            TextView textView3 = (TextView) getActivity().findViewById(R.id.productName3);
            textView3.setText(sortedProducts.get(2).getName());
            textView3.setVisibility(View.VISIBLE);
        }

    }


    private List<Long> getProductIds() {
        String[] stringsIds = ((String) getArguments().get(SELECTED_IDS)).split(",");
        List<Long> productIds = new ArrayList<Long>();
        for (String id : stringsIds) {
            productIds.add(Long.parseLong(id));
        }
        return productIds;
    }


}
