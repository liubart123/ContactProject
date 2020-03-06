package com.lojka.contact;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
    private String index;

    public Address(String country, String city, String street, String house, String flat, String index) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
        this.index = index;
    }
    public Address(){}

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getHouse() {
        return house;
    }
    public void setHouse(String house) {
        this.house = house;
    }
    public String getFlat() {
        return flat;
    }
    public void setFlat(String flat) {
        this.flat = flat;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String toString(){
        return String.format("%s. %s, %s str., %s, %s. Index: %s",getCountry(),getCity(),getStreet(),getHouse(),getFlat(),getIndex());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) &&
                Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(house, address.house) &&
                Objects.equals(flat, address.flat) &&
                Objects.equals(index, address.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, house, flat, index);
    }
}
