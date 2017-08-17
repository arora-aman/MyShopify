package com.aroraaman.myshopify.model;

public class Customer {
    public final static String CUSTOMER_KEY = "customer";
    public final static String BILLING_ADDRESS_KEY = "billing_address";
    public final static String FIRST_NAME_KEY = "first_name";
    public final static String LAST_NAME_KEY = "last_name";

    public final String firstName;
    public final String lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
