package com.aroraaman.myshopify.model;

public class Customer {

    public final String firstName;
    public final String lastName;
    public final Province province;

    public Customer(String firstName, String lastName, Province province) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.province = province;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
