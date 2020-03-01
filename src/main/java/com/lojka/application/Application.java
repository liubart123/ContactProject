package com.lojka.application;

import com.lojka.contact.Address;
import com.lojka.contact.Date;
import com.lojka.contact.Person;
import com.lojka.contact.TelephoneNumber;
import com.lojka.contact.enums.FamilyStatus;
import com.lojka.contact.enums.Gender;
import com.lojka.contact.enums.TypeOfNumber;
import com.lojka.manager.ContactManager;

import java.util.ArrayList;

public class Application {

    public static void main(String[] args) {
        try{
            ContactManager c = new ContactManager();
//            System.out.println(c.getUserInfobyId(1));
        }catch (Exception e){
            System.out.println((e.getMessage()));
        }
        ContactManager manager = new ContactManager();
        manager.clearDb();

        //creating some contacts
        //ivan
        Person p = new Person("ivan",
                "ivanow",
                "ivanavich",
                "belarusian",
                "ivan.com",
                "ivan@mail.com",
                "worker",
                "some comment...",
                FamilyStatus.unmarried,
                Gender.male,
                new Date((byte)(24),(byte)(5),(short)(2001)),
                new Address("Belarus",
                    "Minsk",
                    "Marks'",
                    "12A",
                    "108b",
                    "220022"),
                new ArrayList<TelephoneNumber>()
                );
        p.getTelephoneNumbers().add(new TelephoneNumber(
                "375",
                "44",
                "473-37-11",
                TypeOfNumber.mobile));
        p.getTelephoneNumbers().add(new TelephoneNumber(
                "375",
                "44",
                "474-37-11",
                TypeOfNumber.home));
        manager.addContact(p);


        //barys
        Person p2 = new Person("Barys",
                "Barysaw",
                "Barysavich",
                "belarusian",
                "barys.com",
                "barys@mail.com",
                null,
                null,
                FamilyStatus.married,
                Gender.male,
                new Date((byte)(22),(byte)(1),(short)(1992)),
                new Address("Belarus",
                        "Minsk",
                        "Niamiha",
                        "6",
                        "12",
                        "226622"),
                new ArrayList<TelephoneNumber>()
        );
        p.getTelephoneNumbers().add(new TelephoneNumber(
                "375",
                "29",
                "325-89-45",
                TypeOfNumber.mobile));
        p.getTelephoneNumbers().add(new TelephoneNumber(
                "375",
                "12",
                "395-16-94",
                TypeOfNumber.home));
        manager.addContact(p2);
        //maksim
        Person p3 = new Person("Maksim","Bogdan");
        manager.addContact(p3);

        //manager.deleteContact(p2.getPersonId());
        //manager.printAllContacts();

        p3.setNationality("uzbek");
        p3.setAddress(new Address(
                "uzbekistan",
                "ajzejbardzhan",
                "tbilisi",
                "12",
                "32",
                "some index..."
        ));
        p3.setBirthDate(new Date((byte)01, (byte)01, (short)201));
        p2.getAddress().setCountry("Russia");
        p2.getBirthDate().setYear((short)1203);
        manager.changeContact(p3);


        manager.closeConnection();
    }

}

