package com.example.uiwidgettest;

public class Fruit {

    private String name;
    private int price;
    public Fruit(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    public String getName()
    {
        return this.name;
    }

    public int getPrice()
    {
        return this.price;
    }
}
