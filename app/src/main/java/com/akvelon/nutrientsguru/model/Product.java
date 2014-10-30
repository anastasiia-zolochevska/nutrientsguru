package com.akvelon.nutrientsguru.model;

import java.util.Comparator;

/**
 * @author Anastasiia Zolochevska
 */
public class Product implements Comparable<Product>{

    private Long id;
    private String name;
    private String group;
    private Double CarbohydrateFactor;
    private Double FatFactor;
    private Double ProteinFactor;
    private Double NitrogenToProteinFactor;

    private Product(Long id, String name, String group, Double carbohydrateFactor, Double fatFactor, Double proteinFactor, Double nitrogenToProteinFactor) {
        this.id = id;
        this.name = name;
        this.group = group;
        CarbohydrateFactor = carbohydrateFactor;
        FatFactor = fatFactor;
        ProteinFactor = proteinFactor;
        NitrogenToProteinFactor = nitrogenToProteinFactor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Double getCarbohydrateFactor() {
        return CarbohydrateFactor;
    }

    public void setCarbohydrateFactor(Double carbohydrateFactor) {
        CarbohydrateFactor = carbohydrateFactor;
    }

    public Double getFatFactor() {
        return FatFactor;
    }

    public void setFatFactor(Double fatFactor) {
        FatFactor = fatFactor;
    }

    public Double getProteinFactor() {
        return ProteinFactor;
    }

    public void setProteinFactor(Double proteinFactor) {
        ProteinFactor = proteinFactor;
    }

    public Double getNitrogenToProteinFactor() {
        return NitrogenToProteinFactor;
    }

    public void setNitrogenToProteinFactor(Double nitrogenToProteinFactor) {
        NitrogenToProteinFactor = nitrogenToProteinFactor;
    }

    @Override
    public int compareTo(Product another) {
        return this.getId().compareTo(another.getId());
    }


    public static class ProductBuilder {
        private Long id;
        private String name;
        private String group;
        private Double carbohydrateFactor;
        private Double fatFactor;
        private Double proteinFactor;
        private Double nitrogenToProteinFactor;

        public ProductBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setGroup(String group) {
            this.group = group;
            return this;
        }

        public ProductBuilder setCarbohydrateFactor(Double carbohydrateFactor) {
            this.carbohydrateFactor = carbohydrateFactor;
            return this;
        }

        public ProductBuilder setFatFactor(Double fatFactor) {
            this.fatFactor = fatFactor;
            return this;
        }

        public ProductBuilder setProteinFactor(Double proteinFactor) {
            this.proteinFactor = proteinFactor;
            return this;
        }

        public ProductBuilder setNitrogenToProteinFactor(Double nitrogenToProteinFactor) {
            this.nitrogenToProteinFactor = nitrogenToProteinFactor;
            return this;
        }

        public Product createProduct() {
            return new Product(id, name, group, carbohydrateFactor, fatFactor, proteinFactor, nitrogenToProteinFactor);
        }
    }
}
