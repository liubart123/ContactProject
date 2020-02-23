package com.lojka.manager;

import com.lojka.contact.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

//JDBC
import com.lojka.connector.ConnectorDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

public class ContactManager {
    private ArrayList<Person> contactCollection = new ArrayList<Person>();
    private static final Logger log = Logger.getLogger(ContactManager.class);

    public boolean addContact(Person p){
        log.trace("adding new contact...");
        if (p!=null){
            contactCollection.add(p);
            log.info(p.toString() + " has been added");
            return true;
        }
        log.warn("adding new contact failed");
        return false;
    }

    public void removeContact(Person p){
        log.trace("finding contact to remove...");
        if (p!=null){
            if (contactCollection.contains(p)){
                contactCollection.remove(p);
                log.info(p.toString() + "\nwas removed");
            }
        }else{
            log.warn("removing contact failed");
        }
    }

    public void serializeCollection(){
        log.trace("serializating was started...");
        try{
            try(ObjectOutputStream objOS = new ObjectOutputStream(new FileOutputStream("colection.txt"))){
                objOS.writeObject(contactCollection);
            }
        }catch (IOException e){
            log.error("error with serialization");
        }
    }

    public void deserializeCollection(){
        log.trace("deserializating was started...");
        try{
            try(ObjectInputStream objIS = new ObjectInputStream(new FileInputStream("colection.txt"))){
                contactCollection = (ArrayList<Person>)objIS.readObject();
            }
        }catch (Exception e){
            log.error("error with deserialization");
        }
    }

    public void printAllContacts(){
        String res = "Contact book:\n";
        int iter = 0;
        for(Person p : contactCollection){
            iter++;
            res += iter + ") ";
            res += p + "\n";

        }
        log.info(res);
    }

    //find contacts with part of their information
    public ArrayList<Person> findInCollection(String searchWord, CriterionForSerach criterion, boolean printResults){
        if (searchWord == null || criterion == null){
            log.trace("seraching with null parametr");
            return null;
        }
        log.trace(String.format("seraching with word \"%s\" was started...", searchWord));
        ArrayList<String> stringsForSearching = new ArrayList<>();  //strings, were necessary word will be searched
        switch (criterion){
            case name:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getName());
                }
                break;
            case lastName:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getLastName());
                }
                break;
            case patronymic:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getPatronymic());
                }
                break;
            case nationality:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getNationality());
                }
                break;
            case website:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getWebsite());
                }
                break;
            case email:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getEmail());
                }
                break;
            case job:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getJob());
                }
                break;
            case comment:
                for (Person p : contactCollection){
                    stringsForSearching.add(p.getComment());
                }
                break;
        }
        ArrayList<Integer> listOfIndexes = getIndexesOfSubStringInStrings(searchWord,stringsForSearching);
        if (listOfIndexes.size()==0){
            log.error("Collection probably was empty");
            return null;
        } else {
            log.info("seraching was successful\n");
        }
        ArrayList<Person> result = new ArrayList<Person>();
        //TODO: code for printing persons can be moved in other place
        String stringResult = "";
        int iter = 0;
        for (Integer index : listOfIndexes){
            stringResult += "\n" + (iter + 1) + ") " + contactCollection.get(index);
            result.add(contactCollection.get(index));
            iter++;
        }
        if (printResults){
            log.info("found contacts:\n" + stringResult);
        }
        return result;
    }
    //find strings, that are substrings for strings in collection
    private ArrayList<Integer> getIndexesOfSubStringInStrings(String word, Collection<String> collection){
        ArrayList<Integer> res = new ArrayList<Integer>();
        int iter = 0;
        for (String el : collection){
            if (el != null && el.indexOf(word) != -1) {
                res.add(iter);
            }
            iter++;
        }
        return res;
    }

    //TODO: code for JDBC. Will be added in next task
    private Connection connection;
    private final static String SQL_GET_USERINFO_BY_ID = "select * from persons where idPerson=? ";
    public ContactManager(){
        log.trace("connection is opening...");
        try{
            this.connection = ConnectorDB.getConnection();
            log.trace("connection was opened successfully");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
    public void closeConnection(){
        log.trace("connection is closing...");
        try{
            connection.close();
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
    public String getUserInfobyId(final long id) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SQL_GET_USERINFO_BY_ID);
            ps.setString(1, String.valueOf(id));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
