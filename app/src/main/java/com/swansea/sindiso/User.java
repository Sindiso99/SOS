package com.swansea.sindiso;

import java.util.ArrayList;

public class User {
    private String userName;
    private ArrayList<Container> containers;

    public User(String userName){
        containers = new ArrayList<Container>();
    }

    public String getUserName(){
        return userName;
    }

}
