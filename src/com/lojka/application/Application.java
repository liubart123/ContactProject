package com.lojka.application;

import com.lojka.contact.Person;
import com.lojka.manager.ContactManager;
import com.lojka.manager.CriterionForSerach;

import java.util.ArrayList;

public class Application {

    public static void main(String[] args) {
        /*TODO: code for JDBC
        try{
            ContactManager c = new ContactManager();
            System.out.println(c.getUserInfobyId(1));
        }catch (Exception e){
            System.out.println((e.getMessage()));
        }
        */
        ContactManager manager = new ContactManager();

        //getting collection from file
        manager.deserializeCollection();
        //printing collection
        manager.printAllContacts();

        //creating some contacts
        //ivan
        /*Person p = new Person("ivan",
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
        manager.addContact(p);*/
        //barys
        /*Person p = new Person("Barys",
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
        manager.addContact(p);*/
        //maksim
        /*Person p = new Person("Maksim","Bogdan");
        manager.addContact(p);
        manager.serializeCollection();*/

        //finding contacts
        manager.findInCollection("belarusian", CriterionForSerach.nationality, true);
        //finding and removing contact
        ArrayList<Person> foundPersons = manager.findInCollection("ivan", CriterionForSerach.name, false);
        if (foundPersons != null && foundPersons.size()!=0){
            Person person = foundPersons.get(0);
            manager.removeContact(person);
            manager.printAllContacts();
            //adding removed contact back
            manager.addContact(person);
            manager.printAllContacts();
        }

    }

}

