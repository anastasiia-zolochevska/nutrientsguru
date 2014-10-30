package com.akvelon.nutrientsguru.model;

/**
 * @author Anastasiia Zolochevska
 */
public class NutrientValue implements Comparable<NutrientValue> {

    private Product product;
    private Double valueFor100Gram;
    private Measurement measurement;
    private Double value;


    public NutrientValue(Product product, Double valueFor100Gram, Measurement measurement, Double value) {
        this.product = product;
        this.valueFor100Gram = valueFor100Gram;
        this.measurement = measurement;
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getValueFor100Gram() {
        return valueFor100Gram;
    }

    public void setValueFor100Gram(Double valueFor100Gram) {
        this.valueFor100Gram = valueFor100Gram;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public int compareTo(NutrientValue another) {
        return this.product.getId().compareTo(another.product.getId());
    }
}
