package com.lojka.manager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.contact.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import com.lojka.contact.TelephoneNumber;
import org.apache.log4j.Logger;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import org.apache.poi;
//import org.apache.poi.ooxml;

//JDBC
/*import com.lojka.connector.ConnectorDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;*/

public class ContactManager {
    protected ArrayList<Person> contactCollection = new ArrayList<Person>();
    private static final Logger log = Logger.getLogger(ContactManager.class);

    public ContactManager(){}
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
            try(FileWriter fw = new FileWriter("collection.txt",false)){
                ObjectMapper mapper = new ObjectMapper();
                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                String serializedObject  = mapper.writeValueAsString(this);
                fw.write(serializedObject);
                log.trace("serializating was finished");
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void deserializeCollection(){
        log.trace("deserializating was started...");
        try{
            File f = new File("collection.txt");
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            contactCollection = mapper.readValue(f,this.getClass()).contactCollection;
            log.trace("deserializating was finished");
        }catch (Exception e){
            log.error(e.getMessage());
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

    private final static String xlsFile = "contactBook.xlsx";
    public void serializeCollectionExcel(){
        try {
            FileOutputStream out = new FileOutputStream(new File(xlsFile));
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("contacts");
            int rowindex = 0;

            XSSFRow row = sheet.createRow((short) rowindex);

            //creating 1st row
            sheet.setColumnWidth(0,3000);
            row.createCell(0).setCellValue("name");
            sheet.setColumnWidth(1,4000);
            row.createCell(1).setCellValue("last name");
            sheet.setColumnWidth(2,5000);
            row.createCell(2).setCellValue("patronymic");
            sheet.setColumnWidth(3,4000);
            row.createCell(3).setCellValue("nationality");
            sheet.setColumnWidth(4,5000);
            row.createCell(4).setCellValue("website");
            sheet.setColumnWidth(5,5000);
            row.createCell(5).setCellValue("email");
            sheet.setColumnWidth(6,4000);
            row.createCell(6).setCellValue("job");
            sheet.setColumnWidth(7,4000);
            row.createCell(7).setCellValue("comment");
            sheet.setColumnWidth(8,4000);
            row.createCell(8).setCellValue("family status");
            sheet.setColumnWidth(9,4000);
            row.createCell(9).setCellValue("gender");
            sheet.setColumnWidth(10,19000);
            row.createCell(10).setCellValue("address");
            sheet.setColumnWidth(11,10000);
            row.createCell(11).setCellValue("telephones");

            //writing contact info
            for (Person p : contactCollection){
                row = sheet.createRow((short) ++rowindex);
                row.createCell(0).setCellValue(p.getName());
                row.createCell(1).setCellValue(p.getLastName());
                row.createCell(2).setCellValue(p.getPatronymic());
                row.createCell(3).setCellValue(p.getNationality());
                row.createCell(4).setCellValue(p.getWebsite());
                row.createCell(5).setCellValue(p.getEmail());
                row.createCell(6).setCellValue(p.getJob());
                row.createCell(7).setCellValue(p.getComment());
                if (p.getFamilyStatus() != null) row.createCell(8).setCellValue(p.getFamilyStatus().name());
                if (p.getGender() != null) row.createCell(9).setCellValue(p.getGender().name());
                if (p.getAddress() != null) row.createCell(10).setCellValue(p.getAddress().toString());
                if (p.getTelephoneNumbers() != null) {
                    String tels = "";
                    for (TelephoneNumber tel : p.getTelephoneNumbers()){
                        tels += tel.toString() + ", ";
                    }
                    tels = tels.substring(0,tels.length()-3);
                    row.createCell(11).setCellValue(tels);
                }
            }
            workbook.write(out);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deserializeCollectionExcel(){

    }


}
