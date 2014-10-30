package com.akvelon.nutrientsguru.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.akvelon.nutrientsguru.R;
import com.akvelon.nutrientsguru.adapter.NutrientListAdapter;
import com.akvelon.nutrientsguru.model.Measurement;
import com.akvelon.nutrientsguru.model.Nutrient;
import com.akvelon.nutrientsguru.model.NutrientGroup;
import com.akvelon.nutrientsguru.model.NutrientListItem;
import com.akvelon.nutrientsguru.model.Product;
import com.akvelon.nutrientsguru.persistence.DataBaseHelper;
import com.akvelon.nutrientsguru.service.NutrientService;
import com.akvelon.nutrientsguru.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDetailsFragment extends Fragment {

    public static final String PRODUCT_ID = "productId";

    private Product product;
    private ProductService productService;
    private NutrientService nutrientService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View productDetailsFragmentView = inflater.inflate(R.layout.product_details, container, false);
        ((ImageButton) productDetailsFragmentView.findViewById(R.id.recalculateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText changeServingsEditText = (EditText) getActivity().findViewById(R.id.changeServingsEditText);
                Editable text = changeServingsEditText.getText();
                //todo: check if it's double.  Add client validation
                Double factor = Double.parseDouble(text.toString()) / 100;
                ExpandableListView nutrientList = (ExpandableListView) getActivity().findViewById(R.id.nutrientListTable);
                ((NutrientListAdapter) (nutrientList.getExpandableListAdapter())).recalculateNutrientValues(factor);

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(changeServingsEditText.getWindowToken(), 0);
            }
        });


        return productDetailsFragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity());

        productService = new ProductService(dataBaseHelper);
        nutrientService = new NutrientService(dataBaseHelper);

        Long productId = (Long) getArguments().get(PRODUCT_ID);
        product = productService.getProduct(productId);
        Map<Long, Nutrient> nutrientById = nutrientService.getNutrients();
        Map<NutrientGroup, List<NutrientListItem>> nutrientValues = nutrientService.getNutrientValues(product, nutrientById);

        NutrientListAdapter adapter = new NutrientListAdapter(getActivity(), nutrientService.getNutrientGroups(nutrientValues), nutrientValues);

        ExpandableListView expandableListView = (ExpandableListView) getActivity().findViewById(R.id.nutrientListTable);

        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);

        ((TextView) getActivity().findViewById(R.id.productTitle)).setText(product.getName());
        ((TextView) getActivity().findViewById(R.id.carbohydrateFactor)).setText("Carbohydrate Factor: " + product.getCarbohydrateFactor().toString());
        ((TextView) getActivity().findViewById(R.id.fatFactor)).setText("Fat Factor: " + product.getFatFactor().toString());
        ((TextView) getActivity().findViewById(R.id.proteinFactor)).setText("Protein Factor: " + product.getProteinFactor().toString());
        ((TextView) getActivity().findViewById(R.id.nitrogenFactor)).setText("Nitrogen to Protein Conversion Factor: " + product.getNitrogenToProteinFactor().toString());


        ArrayAdapter<String> measurementAdapter = new ArrayAdapter<String>(getActivity(), R.layout.measurment_spinner_item, getMeasurementTitles());

        measurementAdapter.setDropDownViewResource(R.layout.measurment_spinner_item);


        ((Spinner) getActivity().findViewById(R.id.measurementSpinner)).setAdapter(measurementAdapter);

        ((Spinner) getActivity().findViewById(R.id.measurementSpinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Measurement measurement = findMeasurementByTitle(((TextView) view).getText());
                if (measurement != null) {
                    Double factor = measurement.getGrams() / 100;
                    ((EditText) getActivity().findViewById(R.id.changeServingsEditText)).setText(measurement.getGrams().toString());
                    ExpandableListView nutrientList = (ExpandableListView) getActivity().findViewById(R.id.nutrientListTable);
                    ((NutrientListAdapter) (nutrientList.getExpandableListAdapter())).recalculateNutrientValues(factor);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    List<String> getMeasurementTitles() {
        List<Measurement> measurements = productService.getMeasurements(product.getId());
        List<String> result = new ArrayList<String>();
        for (Measurement measurement : measurements) {
            result.add(measurement.getName());
        }
        return result;
    }


    private Measurement findMeasurementByTitle(CharSequence title) {
        List<Measurement> measurements = productService.getMeasurements(product.getId());
        for (Measurement measurement : measurements) {
            if (measurement.getName().equals(title)) {
                return measurement;
            }
        }
        return null;
    }


}
