package com.lojka.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import com.lojka.contact.enums.FamilyStatus;
import com.lojka.contact.enums.Gender;

public class Person implements Serializable {
    private String name;
    private String lastName;
    private String patronymic;
    private String nationality;
    private String website;
    private String email;
    private String job;
    private String comment;
    //enums
    private FamilyStatus familyStatus;
    private Gender gender;
    //Objects
    private Date birthDate;
    private Address address;
    private ArrayList<TelephoneNumber> telephoneNumbers;

    public Person(String name, String lastName){
        this.name=name;
        this.lastName=lastName;
    }

    public Person(String name, String lastName, String patronymic, String nationality, String website, String email,
                  String job, String comment, FamilyStatus familyStatus, Gender gender, Date birthDate, Address address,
                  ArrayList<TelephoneNumber> telephoneNumbers) {
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.nationality = nationality;
        this.website = website;
        this.email = email;
        this.job = job;
        this.comment = comment;
        this.familyStatus = familyStatus;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.telephoneNumbers = telephoneNumbers;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public FamilyStatus getFamilyStatus() {
        return familyStatus;
    }
    public void setFamilyStatus(FamilyStatus familyStatus) {
        this.familyStatus = familyStatus;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public ArrayList<TelephoneNumber> getTelephoneNumbers() {
        return telephoneNumbers;
    }
    public void setTelephoneNumbers(ArrayList<TelephoneNumber> telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }

    @Override
    public String toString(){
        String result = getName() + " " + getLastName();
        if (getPatronymic()!=null){
            result+=" " + getPatronymic();
        }
        if (getNationality()!=null){
            result+=".\n Nation: " + getNationality();
        }
        if (getWebsite()!=null){
            result+=".\n Website: " + getWebsite();
        }
        if (getEmail()!=null){
            result+=".\n Email: " + getEmail();
        }
        if (getJob()!=null){
            result+=".\n Job: " + getJob();
        }
        if (getAddress()!=null){
            result += ".\n Address: " + getAddress().toString();
        }
        if (getBirthDate()!=null){
            result += ".\n Birth: " + getBirthDate().toString();
        }
        if(getTelephoneNumbers()!=null){
            result +="\n";
            for(TelephoneNumber tel : getTelephoneNumbers()){
                if (result.getBytes()[result.length()-1] != (byte)'\n'){
                    result += ", ";
                }
                result += tel.toString();
            }
        }

        if (getComment()!=null){
            result+=".\n" + getComment();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(patronymic, person.patronymic) &&
                Objects.equals(nationality, person.nationality) &&
                Objects.equals(website, person.website) &&
                Objects.equals(email, person.email) &&
                Objects.equals(job, person.job) &&
                Objects.equals(comment, person.comment) &&
                familyStatus == person.familyStatus &&
                gender == person.gender &&
                Objects.equals(birthDate, person.birthDate) &&
                Objects.equals(address, person.address) &&
                Objects.equals(telephoneNumbers, person.telephoneNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, patronymic, nationality, website, email, job, comment, familyStatus, gender, birthDate, address, telephoneNumbers);
    }
}