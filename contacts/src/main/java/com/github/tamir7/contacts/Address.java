package com.github.tamir7.contacts;

import android.provider.ContactsContract;

/**
 * Represents an Address
 */
public class Address {
    private final String formattedAddress;
    private final Type type;
    private final String street;
    private final String city;
    private final String region;
    private final String postcode;
    private final String country;
    private final String label;


    public enum Type {
        CUSTOM,
        HOME,
        WORK,
        OTHER,
        UNKNOWN;

        static Type fromValue(int value) {
            switch (value) {
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM:
                    return CUSTOM;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
                    return HOME;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
                    return WORK;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER:
                    return OTHER;
                default:
                    return UNKNOWN;
            }
        }
    }

    private Address(String address,
                    String street,
                    String city,
                    String region,
                    String postcode,
                    String country,
                    Type type,
                    String label) {
        this.formattedAddress = address;
        this.street = street;
        this.city = city;
        this.region = region;
        this.postcode = postcode;
        this.country = country;
        this.type = type;
        this.label = label;
    }

    Address(String address,
            String street,
            String city,
            String region,
            String postcode,
            String country,
            Type type) {
        this(address, street, city, region, postcode, country, type, null);
    }

    Address(String address,
            String street,
            String city,
            String region,
            String postcode,
            String country,
            String label) {
        this(address, street, city, region, postcode, country, Type.CUSTOM, label);
    }

    /**
     * Gets the formatted address
     *
     * @return Formatted address
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * Gets the type of the address
     *
     * @return type of address
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the street name
     *
     * @return street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gets the city name
     *
     * @return city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the region name
     *
     * @return region name
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the post code
     *
     * @return post code
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Gets the country of the address
     *
     * @return country of the address
     */
    public String getCountry() {
        return country;
    }

    /**
     * The label of the address. (null unless type = TYPE_CUSTOM)
     *
     * @return the label of the address
     */
    public String getLabel() {
        return label;
    }

}
