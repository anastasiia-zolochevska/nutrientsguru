package com.akvelon.nutrientsguru.service;

import android.database.Cursor;

import com.akvelon.nutrientsguru.model.Measurement;
import com.akvelon.nutrientsguru.model.Product;
import com.akvelon.nutrientsguru.persistence.DataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_MEASUREMENT_DESC;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_MEASUREMENT_WEIGHT;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_CARBOHYDRATE_FACTOR;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_FAT_FACTOR;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_ID;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_NAME;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_NITROGEN_TO_PROTEIN_FACTOR;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_PROTEIN_FACTOR;

/**
 * @author Anastasiia Zolochevska
 */
public class ProductService {

    private DataBaseHelper dataBaseHelper;

    public ProductService(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
    }

    public Map<Long, Product> getProducts(List<Long> productIds) {
        Cursor cursor = dataBaseHelper.getProducts(productIds);
        Map<Long, Product> result = new HashMap<Long, Product>();
        while (cursor.moveToNext()) {
            Product product = new Product.ProductBuilder()
                    .setId(cursor.getLong(cursor.getColumnIndex(C_PRODUCT_ID)))
                    .setName(cursor.getString(cursor.getColumnIndex(C_PRODUCT_NAME)))
                    .setCarbohydrateFactor(cursor.getDouble(cursor.getColumnIndex(C_PRODUCT_CARBOHYDRATE_FACTOR)))
                    .setFatFactor(cursor.getDouble(cursor.getColumnIndex(C_PRODUCT_FAT_FACTOR)))
                    .setNitrogenToProteinFactor(cursor.getDouble(cursor.getColumnIndex(C_PRODUCT_NITROGEN_TO_PROTEIN_FACTOR)))
                    .setProteinFactor(cursor.getDouble(cursor.getColumnIndex(C_PRODUCT_PROTEIN_FACTOR)))
                    .createProduct();
            result.put(product.getId(), product);
        }

        return result;
    }

    public Product getProduct(Long productId) {
        return (Product) getProducts(Arrays.asList(productId)).values().toArray()[0];
    }

    public List<Measurement> getMeasurements(Long productId) {
        Cursor cursor = dataBaseHelper.getMeasurements(productId);
        List<Measurement> measurements = new ArrayList<Measurement>();
        measurements.add(new Measurement("100 g", 100.0));
        while (cursor.moveToNext()) {
            measurements.add(new Measurement(cursor.getString(cursor.getColumnIndex(C_MEASUREMENT_DESC)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(C_MEASUREMENT_WEIGHT)))));
        }

        return measurements;
    }

}
