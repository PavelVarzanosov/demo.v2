package com.example.demo.model;

import java.util.*;

public class PropertiesList {
    static List<Property> propertyList = new LinkedList<Property>();;

    public PropertiesList(){

    }
//    public PropList(Properties prop){
//        propList = new LinkedList<Properties>();
//        propList.add(prop);
//    }

    public void addProperty(Property prop){
        propertyList.add(prop);
    }
    public List<Property> getProperties(){
        return propertyList;
    }
}
