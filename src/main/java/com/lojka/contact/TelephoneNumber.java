package com.lojka.contact;
import com.lojka.contact.enums.TypeOfNumber;

import java.io.Serializable;
import java.util.Objects;

public class TelephoneNumber implements Serializable {
    private String countryNumber;
    private String operatorNumber;
    private String number;
    private TypeOfNumber typeOfNumber;

    public TelephoneNumber(String countryNumber, String operatorNumber, String number, TypeOfNumber typeOfNumber) {
        this.countryNumber = countryNumber;
        this.operatorNumber = operatorNumber;
        this.number = number;
        this.typeOfNumber = typeOfNumber;
    }
    public TelephoneNumber(){}

    public String getCountryNumber() {
        return countryNumber;
    }
    public void setCountryNumber(String countryNumber) {
        this.countryNumber = countryNumber;
    }
    public String getOperatorNumber() {
        return operatorNumber;
    }
    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public TypeOfNumber getTypeOfNumber() {
        return typeOfNumber;
    }
    public void setTypeOfNumber(TypeOfNumber typeOfNumber) {
        this.typeOfNumber = typeOfNumber;
    }

    @Override
    public String toString(){
        return String.format("+%s (%s) %s",getCountryNumber(),getOperatorNumber(),getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelephoneNumber that = (TelephoneNumber) o;
        return Objects.equals(countryNumber, that.countryNumber) &&
                Objects.equals(operatorNumber, that.operatorNumber) &&
                Objects.equals(number, that.number) &&
                typeOfNumber == that.typeOfNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryNumber, operatorNumber, number, typeOfNumber);
    }
}
