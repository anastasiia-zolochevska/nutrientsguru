package com.akvelon.nutrientsguru.service;

import android.database.Cursor;

import com.akvelon.nutrientsguru.model.Nutrient;
import com.akvelon.nutrientsguru.model.NutrientGroup;
import com.akvelon.nutrientsguru.model.NutrientListItem;
import com.akvelon.nutrientsguru.model.NutrientValue;
import com.akvelon.nutrientsguru.model.Product;
import com.akvelon.nutrientsguru.persistence.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_NUTRIENT_GROUP;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_NUTRIENT_GROUP_ID;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_NUTRIENT_ID;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_NUTRIENT_NAME;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_NUTRIENT_UNITS;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_NUTRIENT_VALUE;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_ID;

/**
 * @author Anastasiia Zolochevska
 */
public class NutrientService {

    private DataBaseHelper dataBaseHelper;

    public NutrientService(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
    }

    public List<NutrientGroup> getNutrientGroups(Map<NutrientGroup, List<NutrientListItem>> nutrientsByGroups) {
        ArrayList<NutrientGroup> result = new ArrayList<NutrientGroup>(nutrientsByGroups.keySet());
        Collections.sort(result, new Comparator<NutrientGroup>() {
            @Override
            public int compare(NutrientGroup group1, NutrientGroup group2) {
                return group1.getId().compareTo(group2.getId());
            }
        });
        return result;
    }

    public Map<NutrientGroup, List<NutrientListItem>> getNutrientValues(Product product, Map<Long, Nutrient> nutrients) {
        Map<Long, Product> productIdToValue = new HashMap<Long, Product>();
        productIdToValue.put(product.getId(), product);
        return getNutrientValues(productIdToValue, nutrients);

    }

    public Map<NutrientGroup, List<NutrientListItem>> getNutrientValues(Map<Long, Product> products, Map<Long, Nutrient> nutrients) {

        Map<NutrientGroup, List<NutrientListItem>> result = new HashMap<NutrientGroup, List<NutrientListItem>>();
        Cursor cursor = dataBaseHelper.getProductsNutrientValues(products.keySet());

        Map<Nutrient, List<NutrientValue>> nutrientValuesMap = new HashMap<Nutrient, List<NutrientValue>>();

        while (cursor.moveToNext()) {
            Long productId = cursor.getLong(cursor.getColumnIndex(C_PRODUCT_ID));
            Product product = products.get(productId);
            Long nutrientId = cursor.getLong(cursor.getColumnIndex(C_NUTRIENT_ID));
            Double valueFor100Gram = cursor.getDouble(cursor.getColumnIndex(C_NUTRIENT_VALUE));
            NutrientValue nutrientValue = new NutrientValue(product, valueFor100Gram, null, valueFor100Gram);
            Nutrient nutrient = nutrients.get(nutrientId);
            if (nutrient != null) {
                if (!nutrientValuesMap.containsKey(nutrient)) {
                    nutrientValuesMap.put(nutrient, new ArrayList<NutrientValue>());
                }
                nutrientValuesMap.get(nutrient).add(nutrientValue);
            }
        }

        for (Nutrient nutrient : nutrientValuesMap.keySet()) {
            List<NutrientValue> nutrientValueList = nutrientValuesMap.get(nutrient);
            Collections.sort(nutrientValueList);
            NutrientListItem nutrientListItem = new NutrientListItem(nutrient, nutrientValueList);

            if (!result.containsKey(nutrient.getGroup())) {
                result.put(nutrient.getGroup(), new ArrayList<NutrientListItem>());
            }
            result.get(nutrient.getGroup()).add(nutrientListItem);
        }
        return result;

    }


    public Map<Long, Nutrient> getNutrients() {
        Cursor cursor = dataBaseHelper.getNutrients();

        Map<Long, Nutrient> nutrientsById = new HashMap<Long, Nutrient>();

        while (cursor.moveToNext()) {
            NutrientGroup group = new NutrientGroup(
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(C_NUTRIENT_GROUP_ID))),
                    cursor.getString(cursor.getColumnIndex(C_NUTRIENT_GROUP)));

            Nutrient nutrient = new Nutrient(
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(C_NUTRIENT_ID))),
                    group,
                    cursor.getString(cursor.getColumnIndex(C_NUTRIENT_NAME)),
                    cursor.getString(cursor.getColumnIndex(C_NUTRIENT_UNITS)));

            nutrientsById.put(nutrient.getId(), nutrient);
        }
        return nutrientsById;
    }


}
