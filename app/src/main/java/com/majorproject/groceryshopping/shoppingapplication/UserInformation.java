package com.majorproject.groceryshopping.shoppingapplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shubhank Dubey on 23-03-2017.
 */

class UserInformation {
    private String userName;
    private String userNumber;
    private String userDob;
    private String userAdderss;
    private String userEmail;

    public String getUserName() {
        return userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getUserDob() {
        return userDob;
    }

    public String getUserAdderss() {
        return userAdderss;
    }

    public String getUserEmail() {
        return userEmail;
    }

    UserInformation()
    {
        this("Is Empty","Is Empty","Is Empty","Is Empty","Is Empty");
    }

    public UserInformation(String userName, String userNumber, String userDob, String userAdderss, String userEmail) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userDob = userDob;
        this.userAdderss = userAdderss;
        this.userEmail = userEmail;
    }
/*

    public UserInformation(String userName, String userNumber, String userDob, String userAdderss) {
        this(userName,userNumber,getDate(userDob),userAdderss);

    }
*/

    public void printAll()
    {
        System.out.println("Name \t: "+ userName);
        System.out.println("Number \t: "+ userNumber);
        System.out.println("D.O.B \t: "+ userDob);
        System.out.println("Address \t: "+ userAdderss);
        System.out.println("Email \t: "+ userEmail);
    }
/*
    private static Date getDate(String startDateString)
    {
        Date startDate = null;
        try {
            startDate = new SimpleDateFormat("MM/dd/yyyy").parse(startDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }*/
}
