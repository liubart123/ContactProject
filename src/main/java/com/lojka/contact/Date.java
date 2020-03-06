package com.lojka.contact;

import java.io.Serializable;
import java.util.Objects;

public class Date implements Serializable {
    private Byte day;
    private Byte month;
    private Short year;

    public Byte getDay() {
        return day;
    }
    public void setDay(Byte day) {
        this.day = day;
    }
    public Byte getMonth() {
        return month;
    }
    public void setMonth(Byte month) {
        this.month = month;
    }
    public Short getYear() {
        return year;
    }
    public void setYear(Short year) {
        this.year = year;
    }
    public Date(Byte day, Byte month, Short year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public Date(){}

    @Override
    public String toString(){
        return String.format("%d.%d.%d",getDay(),getMonth(),getYear());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return Objects.equals(day, date.day) &&
                Objects.equals(month, date.month) &&
                Objects.equals(year, date.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }
}

