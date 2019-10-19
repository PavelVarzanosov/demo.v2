package com.example.demo.model;

import java.util.*;

public class PropList {
    static List<Properties> propList = new LinkedList<Properties>();;

    public PropList(){

    }
//    public PropList(Properties prop){
//        propList = new LinkedList<Properties>();
//        propList.add(prop);
//    }

    public void addProp(Properties prop){
        propList.add(prop);
    }
    public List<Properties> getProps(){
        return propList;
    }
}
