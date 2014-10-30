package com.akvelon.nutrientsguru.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anastasiia Zolochevska
 */
public class NutrientListItem {

    private Nutrient nutrient;
    private List<NutrientValue> values;


    public NutrientListItem() {
    }

    public NutrientListItem(Nutrient nutrient) {
        this.nutrient = nutrient;
    }

    public NutrientListItem(Nutrient nutrient, List<NutrientValue> values) {
        this.nutrient = nutrient;
        this.values = values;
    }

    public void addValue(NutrientValue value) {
        if (values == null) {
            values = new ArrayList<NutrientValue>();
        }
        values.add(value);

    }

    public List<NutrientValue> getValues() {
        return values;
    }

    public void setValues(List<NutrientValue> values) {
        this.values = values;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public void setNutrient(Nutrient nutrient) {
        this.nutrient = nutrient;
    }
}
