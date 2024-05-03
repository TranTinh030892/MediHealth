package com.example.medihealth.models;

public class Drug {
    private String name;
    private String ingredients;
    private String function;
    private String expiry;
    private String sideEffects;
    private String contraindicated;
    private String interactions;

    public Drug() {
    }

    public Drug(String name, String ingredients, String function, String expiry, String sideEffects,
                String contraindicated, String interactions) {
        this.name = name;
        this.ingredients = ingredients;
        this.function = function;
        this.expiry = expiry;
        this.sideEffects = sideEffects;
        this.contraindicated = contraindicated;
        this.interactions = interactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getContraindicated() {
        return contraindicated;
    }

    public void setContraindicated(String contraindicated) {
        this.contraindicated = contraindicated;
    }

    public String getInteractions() {
        return interactions;
    }

    public void setInteractions(String interactions) {
        this.interactions = interactions;
    }
}
