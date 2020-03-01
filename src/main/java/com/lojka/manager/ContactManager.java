package com.lojka.manager;

import com.lojka.contact.Address;
import com.lojka.contact.Date;
import com.lojka.contact.Person;

import java.io.*;
import java.sql.*;
import java.util.*;


//JDBC
import com.lojka.connector.ConnectorDB;
import com.lojka.contact.TelephoneNumber;
import com.lojka.contact.enums.FamilyStatus;
import com.lojka.contact.enums.Gender;
import com.lojka.contact.enums.TypeOfNumber;
import org.apache.log4j.Logger;

public class ContactManager {
    private ArrayList<Person> contactCollection = new ArrayList<Person>();
    private static final Logger log = Logger.getLogger(ContactManager.class);
    private Connection connection;

    private static HashMap<PersonDbColumns, String> SQL_SERACH_REQUESTS;
    static {
        SQL_SERACH_REQUESTS =new HashMap<PersonDbColumns, String>();
        SQL_SERACH_REQUESTS.put(PersonDbColumns.name, "select * from persons where name=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.lastName, "select * from persons where lastName=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.patronymic, "select * from persons where patronymic=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.nationality, "select * from persons where nationality=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.email, "select * from persons where email=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.website, "select * from persons where website=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.job, "select * from persons where job=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.comment, "select * from persons where comment=? ");
        SQL_SERACH_REQUESTS.put(PersonDbColumns.all_special, "select * from persons");
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

    //TODO: create sceptions for sql, every method throws its own error
    private static final String SQL_INSERT_PERSON = "insert persons values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String  SQL_INSERT_ADDR = "insert addresses values (?,?,?,?,?,?,?)";
    private static final String SQL_INSERT_TEL = "insert telephones values(?,?,?,?,?,?)";
    private static final String SQL_INSERT_BIRTHDATE = "insert birthdays values(?,?,?,?)";

    //add contact to db with person and address info
    public boolean addContact(Person p){
        log.trace("adding new contact...");
        if (p!=null){
            int persId = getPersonId(p);
            int adrId = getAddressId(p);
            int birthId = getBirthId(p);
            PreparedStatement ps = null;
            try {
                //insert person
                ps = connection.prepareStatement(SQL_INSERT_PERSON);
                ps.executeQuery("set foreign_key_checks=0");
                ps.setInt(PersonDbColumns.idPerson.ordinal(), persId);
                ps.setString(PersonDbColumns.name.ordinal(), p.getName());
                ps.setString(PersonDbColumns.lastName.ordinal(), p.getLastName());
                if (p.getPatronymic() != null) ps.setString(PersonDbColumns.patronymic.ordinal(),p.getPatronymic());
                else ps.setNull(PersonDbColumns.patronymic.ordinal(), Types.VARCHAR);
                if (p.getWebsite() != null) ps.setString(PersonDbColumns.website.ordinal(), p.getWebsite());
                else ps.setNull(PersonDbColumns.website.ordinal(), Types.VARCHAR);
                if (p.getEmail() != null) ps.setString(PersonDbColumns.email.ordinal(), p.getEmail());
                else ps.setNull(PersonDbColumns.email.ordinal(), Types.VARCHAR);
                if (p.getJob() != null) ps.setString(PersonDbColumns.job.ordinal(), p.getJob());
                else ps.setNull(PersonDbColumns.job.ordinal(), Types.VARCHAR);
                if (p.getComment() != null) ps.setString(PersonDbColumns.comment.ordinal(), p.getComment());
                else ps.setNull(PersonDbColumns.comment.ordinal(), Types.VARCHAR);
                if (p.getFamilyStatus() != null) ps.setString(PersonDbColumns.familyStatus.ordinal(), p.getFamilyStatus().name());
                else ps.setNull(PersonDbColumns.familyStatus.ordinal(), Types.VARCHAR);
                if (p.getGender() != null) ps.setString(PersonDbColumns.gender.ordinal(), p.getGender().name());
                else ps.setNull(PersonDbColumns.gender.ordinal(), Types.VARCHAR);
                if (p.getNationality() != null) ps.setString(PersonDbColumns.nationality.ordinal(), p.getNationality());
                else ps.setNull(PersonDbColumns.nationality.ordinal(), Types.VARCHAR);
                if (p.getBirthDate() != null) ps.setInt(PersonDbColumns.idBirthDate.ordinal(), birthId);
                else ps.setNull(PersonDbColumns.idBirthDate.ordinal(), Types.INTEGER);
                if (p.getAddress() != null) ps.setInt(PersonDbColumns.idAddress.ordinal(), adrId);
                else ps.setNull(PersonDbColumns.idAddress.ordinal(), Types.INTEGER);

                ps.executeUpdate();


                //insert address
                if (p.getAddress() != null){
                    addAddressToDb(p,adrId);
                }

                if (p.getTelephoneNumbers()!=null) addTelephonesToDb(p,persId);

                if (p.getBirthDate()!=null) addDateToDb(p.getBirthDate(), birthId);

                ps.executeQuery("set foreign_key_checks=1");
                p.setPersonId(persId);
            }
            catch (SQLException e) {
                log.error("error with adding new person");
                e.printStackTrace();
            }
            log.info(p.getName() + " has been added");
            return true;
        }
        log.warn("adding new contact failed");
        return false;
    }

    //add to db telephones and date and address
    private void addTelephonesToDb(Person p, int persId){
        log.trace("adding telephones to database");
        PreparedStatement ps = null;
        try {
            for (TelephoneNumber tel : p.getTelephoneNumbers()){
                int telId = getTelephoneId(p);
                //insert telephones
                ps = connection.prepareStatement(SQL_INSERT_TEL);
                ps.setInt(1, telId);
                ps.setString(2,tel.getCountryNumber());
                ps.setString(3,tel.getOperatorNumber());
                ps.setString(4,tel.getNumber());
                ps.setString(5,tel.getTypeOfNumber().name());
                ps.setInt(6, persId);
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            log.error("error with adding new telephone");
            e.printStackTrace();
        }
    }
    private void addDateToDb(Date date, int dateId){
        log.trace("adding date to database");
        PreparedStatement ps = null;
        try {
            //insert person
            ps = connection.prepareStatement(SQL_INSERT_BIRTHDATE);
            ps.setInt(1, dateId);
            ps.setInt(2, date.getDay());
            ps.setInt(3, date.getMonth());
            ps.setInt(4, date.getYear());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            log.error("error with adding date");
            e.printStackTrace();
        }
    }
    private void addAddressToDb(Person p, int adrId) throws SQLException{
        log.trace("adding address to database");
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ADDR);
        ps.setInt(1, adrId);
        if (p.getAddress().getCountry()!=null) ps.setString(2,p.getAddress().getCountry());
        else ps.setNull(2,Types.NVARCHAR);
        if (p.getAddress().getCity()!=null) ps.setString(3,p.getAddress().getCity());
        else ps.setNull(3,Types.NVARCHAR);
        if (p.getAddress().getStreet()!=null) ps.setString(4,p.getAddress().getStreet());
        else ps.setNull(4,Types.NVARCHAR);
        if (p.getAddress().getHouse()!=null) ps.setString(5,p.getAddress().getHouse());
        else ps.setNull(5,Types.NVARCHAR);
        if (p.getAddress().getFlat()!=null) ps.setString(6,p.getAddress().getFlat());
        else ps.setNull(6,Types.NVARCHAR);
        if (p.getAddress().getIndex()!=null) ps.setString(7,p.getAddress().getIndex());
        else ps.setNull(7,Types.NVARCHAR);
        ps.executeUpdate();
    }

    public void deleteContact(int personId){
        log.trace("finding contact to delete...");
        try{
            PreparedStatement psRemove = connection.prepareStatement("delete from persons where idPerson=" + personId);
            psRemove.executeUpdate();
            log.info("object was deleted");
        }catch (Exception e){
            log.error("error during finding and deleting person from db");
            log.error(e.getMessage());
        }
    }

    public void changeContact(Person p){
        try{
            log.trace("changing person...");
            PreparedStatement ps = connection.prepareStatement("select idBirthDate, idAddress from persons where idPerson=" + p.getPersonId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int addrId = rs.getInt(2);
            if (addrId==0){
                addrId = getAddressId(p);
            } else {
                //deleting address
                ps = connection.prepareStatement("delete from addresses where idAddress=" + addrId);
                ps.executeUpdate();
            }

            int birthId = rs.getInt(1);
            if (birthId == 0) {
                birthId = getBirthId(p);
            } else {
                //deleting birthDate
                ps = connection.prepareStatement("delete from birthdays where idDate=" + birthId);
                ps.executeUpdate();
            }

            if (p.getAddress() != null){
                addAddressToDb(p,addrId);
            }
            if (p.getBirthDate() != null){
                addDateToDb(p.getBirthDate(),birthId);
            }

            ps = connection.prepareStatement("SET SQL_SAFE_UPDATES = 0;");
            ps.execute();

            ps = connection.prepareStatement("update persons set idPerson=?, name=?,lastName=?,patronymic=?,website=?,email=?,job=?,comment=?,familyStatus=?,gender=?,nationality=?, idBirthDate=?,idAddress=? where idPerson="+p.getPersonId());

            ps.setInt(1,p.getPersonId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getLastName());
            //TODO:change numbers to enum
//            if (p.getPatronymic() != null) ps.setString(4,p.getPatronymic());
//            else ps.setNull(4, Types.VARCHAR);
//            if (p.getWebsite() != null) ps.setString(5, p.getWebsite());
//            else ps.setNull(5, Types.VARCHAR);
//            if (p.getEmail() != null) ps.setString(6, p.getEmail());
//            else ps.setNull(6, Types.VARCHAR);
//            if (p.getJob() != null) ps.setString(7, p.getJob());
//            else ps.setNull(7, Types.VARCHAR);
//            if (p.getComment() != null) ps.setString(8, p.getComment());
//            else ps.setNull(8, Types.VARCHAR);
//            if (p.getFamilyStatus() != null) ps.setString(9, p.getFamilyStatus().name());
//            else ps.setNull(9, Types.VARCHAR);
//            if (p.getGender() != null) ps.setString(10, p.getGender().name());
//            else ps.setNull(10, Types.VARCHAR);
//            if (p.getNationality() != null) ps.setString(11, p.getNationality());
//            else ps.setNull(11, Types.VARCHAR);
//            if (p.getBirthDate() != null) ps.setInt(12, birthId);
//            else ps.setNull(12, Types.INTEGER);
//            if (p.getAddress() != null) ps.setInt(13, addrId);
//            else ps.setNull(13, Types.INTEGER);

            ps.setInt(PersonDbColumns.idPerson.ordinal(), p.getPersonId());
            ps.setString(PersonDbColumns.name.ordinal(), p.getName());
            ps.setString(PersonDbColumns.lastName.ordinal(), p.getLastName());
            if (p.getPatronymic() != null) ps.setString(PersonDbColumns.patronymic.ordinal(),p.getPatronymic());
            else ps.setNull(PersonDbColumns.patronymic.ordinal(), Types.VARCHAR);
            if (p.getWebsite() != null) ps.setString(PersonDbColumns.website.ordinal(), p.getWebsite());
            else ps.setNull(PersonDbColumns.website.ordinal(), Types.VARCHAR);
            if (p.getEmail() != null) ps.setString(PersonDbColumns.email.ordinal(), p.getEmail());
            else ps.setNull(PersonDbColumns.email.ordinal(), Types.VARCHAR);
            if (p.getJob() != null) ps.setString(PersonDbColumns.job.ordinal(), p.getJob());
            else ps.setNull(PersonDbColumns.job.ordinal(), Types.VARCHAR);
            if (p.getComment() != null) ps.setString(PersonDbColumns.comment.ordinal(), p.getComment());
            else ps.setNull(PersonDbColumns.comment.ordinal(), Types.VARCHAR);
            if (p.getFamilyStatus() != null) ps.setString(PersonDbColumns.familyStatus.ordinal(), p.getFamilyStatus().name());
            else ps.setNull(PersonDbColumns.familyStatus.ordinal(), Types.VARCHAR);
            if (p.getGender() != null) ps.setString(PersonDbColumns.gender.ordinal(), p.getGender().name());
            else ps.setNull(PersonDbColumns.gender.ordinal(), Types.VARCHAR);
            if (p.getNationality() != null) ps.setString(PersonDbColumns.nationality.ordinal(), p.getNationality());
            else ps.setNull(PersonDbColumns.nationality.ordinal(), Types.VARCHAR);
            if (p.getBirthDate() != null) ps.setInt(PersonDbColumns.idBirthDate.ordinal(), birthId);
            else ps.setNull(PersonDbColumns.idBirthDate.ordinal(), Types.INTEGER);
            if (p.getAddress() != null) ps.setInt(PersonDbColumns.idAddress.ordinal(), addrId);
            else ps.setNull(PersonDbColumns.idAddress.ordinal(), Types.INTEGER);

            ps.executeUpdate();



            ps = connection.prepareStatement("SET SQL_SAFE_UPDATES = 1;");
            ps.execute();

        } catch(Exception e){
            log.error(e.getMessage());
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
        log.info("printing all contacts: ");
        String res = "";
        ArrayList<Person> list = findInCollection("", PersonDbColumns.all_special);
        for (Person p : list){
            res += p.toString();
        }
        if (res=="") log.info("empty");
        else log.info(res);
    }

    public void clearDb(){
        log.trace("clearing database");
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SET SQL_SAFE_UPDATES = 0;");
            ps.executeQuery();

            ps = connection.prepareStatement("delete from persons");
            ps.executeUpdate();
            ps = connection.prepareStatement("delete from addresses");
            ps.executeUpdate();
            ps = connection.prepareStatement("delete from birthdays");
            ps.executeUpdate();
            ps = connection.prepareStatement("delete from telephones");
            ps.executeUpdate();

            ps = connection.prepareStatement("SET SQL_SAFE_UPDATES = 1;");
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //find contacts with part of their information
    public ArrayList<Person> findInCollection(String searchWord, PersonDbColumns criterion){
        log.trace("searching persons was started");
        PreparedStatement ps = null;
        ArrayList<Person> resultArray = new ArrayList<>();
        try {
            ps = connection.prepareStatement(SQL_SERACH_REQUESTS.get(criterion));
            if (criterion != PersonDbColumns.all_special) ps.setString(1, String.valueOf(searchWord));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Person temp = getPersonFromResultSet(rs);

                PreparedStatement addrPs = connection.prepareStatement("select * from addresses where idaddress=" + rs.getInt(PersonDbColumns.idAddress.name()));
                ResultSet addrRs = addrPs.executeQuery();
                Address tempAddr = getAddressFromResultSet(addrRs);
                if (tempAddr!=null) temp.setAddress(tempAddr);

                PreparedStatement telPs = connection.prepareStatement("select * from telephones where personId=" + rs.getInt("idPerson"));
                ResultSet telRs = telPs.executeQuery();
                ArrayList tels = getTelephoneNumFromResultSet(telRs);
                if (tels != null) temp.setTelephoneNumbers(tels);


                resultArray.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultArray;
    }
    private Person getPersonFromResultSet(ResultSet rs){
        log.trace("getting person from result set");
        Person p = null;
        try{
            p = new Person(
                    rs.getString(PersonDbColumns.name.name()),
                    rs.getString(PersonDbColumns.lastName.name()));
            p.setPatronymic(rs.getString(PersonDbColumns.patronymic.name()));
            p.setWebsite(rs.getString(PersonDbColumns.website.name()));
            p.setEmail(rs.getString(PersonDbColumns.email.name()));
            p.setJob(rs.getString(PersonDbColumns.job.name()));
            p.setComment(rs.getString(PersonDbColumns.comment.name()));
            if (rs.getString(PersonDbColumns.familyStatus.name())!= null) {
                p.setFamilyStatus(FamilyStatus.valueOf(rs.getString(PersonDbColumns.familyStatus.name())));
            }
            if (rs.getString(PersonDbColumns.gender.name()) != null) {
                p.setGender(Gender.valueOf(rs.getString(PersonDbColumns.gender.name())));
            }
            p.setNationality(rs.getString(PersonDbColumns.nationality.name()));
            p.setPersonId(rs.getInt("idPerson"));
            return p;
        }catch (Exception e){
            log.error("error with getting person data from db\n" + e.getMessage());
        }
        return null;
    }
    private Address getAddressFromResultSet(ResultSet rs){
        try{
            log.trace("getting address from result set");
            if (rs.next()) {
                return new Address(
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("street"),
                        rs.getString("house"),
                        rs.getString("flat"),
                        rs.getString("index")
                );
            } else return null;
        }catch (Exception e){
            log.error("error with getting person data from db\n" + e.getMessage());
        }
        return null;
    }
    private ArrayList<TelephoneNumber> getTelephoneNumFromResultSet(ResultSet rs){
        try{
            log.trace("getting telephone numbers from database");
            ArrayList<TelephoneNumber> list = new ArrayList<>();
            while (rs.next()){
                list.add(new TelephoneNumber(
                        rs.getString("countryNumber"),
                        rs.getString("operatorNumber"),
                        rs.getString("number"),
                        TypeOfNumber.valueOf(rs.getString("typeOfTelephone"))
                ));
            }
            if (list.size()==0) return null;
            return list;
        }catch (Exception e){
            log.error("error with getting person data from db\n" + e.getMessage());
        }
        return null;
    }

    //TODO:remove parametr
    //get primary keys for tables
    private int getPersonId(Person p){
        PreparedStatement ps = null;
        ArrayList<Person> resultArray = new ArrayList<>();
        try {
            ps = connection.prepareStatement("select * from persons order by idPerson DESC");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 1;
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int getAddressId(Person p){
        PreparedStatement ps = null;
        ArrayList<Person> resultArray = new ArrayList<>();
        try {
            ps = connection.prepareStatement("select * from addresses order by idaddress DESC");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 1;
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int getBirthId(Person p){
        PreparedStatement ps = null;
        ArrayList<Person> resultArray = new ArrayList<>();
        try {
            ps = connection.prepareStatement("select * from birthdays order by idDate DESC");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 1;
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int getTelephoneId(Person p){
        PreparedStatement ps = null;
        ArrayList<Person> resultArray = new ArrayList<>();
        try {
            ps = connection.prepareStatement("select * from telephones order by telephoneId DESC");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 1;
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
