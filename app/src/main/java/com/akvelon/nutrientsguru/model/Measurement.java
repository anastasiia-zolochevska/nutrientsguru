package com.akvelon.nutrientsguru.model;

/**
 * @author Anastasiia Zolochevska
 */
public class Measurement {

    private String name;
    private Double grams;


    public Measurement(String name, Double grams) {
        this.name = name;
        this.grams = grams;
    }

    public Double getGrams() {
        return grams;
    }

    public void setGrams(Double grams) {
        this.grams = grams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
