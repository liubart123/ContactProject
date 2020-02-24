package com.lojka.manager;

import com.lojka.contact.Person;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

//JDBC
import com.lojka.connector.ConnectorDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactManager {
    private ArrayList<Person> contactCollection = new ArrayList<Person>();
    private static final Logger log = Logger.getLogger(ContactManager.class);
    private Connection connection;
    //pattern for getting person by attribute
    //TODO: delete this string lol
    //private final static String SQL_GET_USER = "select * from persons where ?=? ";

    //collection for query by attribute
    private static HashMap<CriterionForSerach, String> SQL_TABLE_ATTRIBUTES;
    static {
        SQL_TABLE_ATTRIBUTES=new HashMap<CriterionForSerach, String>();
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.name, "select * from persons where name=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.lastName, "select * from persons where lastName=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.patronymic, "select * from persons where patronymic=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.nationality, "select * from persons where nationality=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.email, "select * from persons where email=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.website, "select * from persons where website=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.job, "select * from persons where job=? ");
        SQL_TABLE_ATTRIBUTES.put(CriterionForSerach.comment, "select * from persons where comment=? ");
    }

    public ContactManager(){
        log.trace("connection is opening...");
        try{
            this.connection = ConnectorDB.getConnection();
            log.trace("connection was opened successfully");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
    //connection to db
    public void closeConnection(){
        log.trace("connection is closing...");
        try{
            connection.close();
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

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
    public ArrayList<Person> findInCollection(String searchWord, CriterionForSerach criterion){
        PreparedStatement ps = null;
        ArrayList<Person> resultArray = new ArrayList<>();
        try {
            ps = connection.prepareStatement(SQL_TABLE_ATTRIBUTES.get(criterion));
            ps.setString(1, String.valueOf(searchWord));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                log.info(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    /*public String getUserInfobyId(final long id) {
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
*/

}
