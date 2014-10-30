package com.akvelon.nutrientsguru.model;

/**
 * @author Anastasiia Zolochevska
 */
public class Nutrient {

    private Long id;
    private NutrientGroup group;
    private String name;
    private String unit;

    public Nutrient(Long id, NutrientGroup group, String name, String unit) {
        this.id = id;
        this.group = group;
        this.name = name;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NutrientGroup getGroup() {
        return group;
    }

    public void setGroup(NutrientGroup group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nutrient nutrient = (Nutrient) o;

        if (group != null ? !group.equals(nutrient.group) : nutrient.group != null) return false;
        if (!id.equals(nutrient.id)) return false;
        if (name != null ? !name.equals(nutrient.name) : nutrient.name != null) return false;
        if (unit != null ? !unit.equals(nutrient.unit) : nutrient.unit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }
}
