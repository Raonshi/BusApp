package com.example.demo.data;

import java.util.HashMap;

public class DataController {
    private static DataController instance = null;

    public static DataController Singleton(){
        if(instance == null){
            instance = new DataController();
        }
        return instance;
    }

    public HashMap<String, String> cityTable = new HashMap<>();

}
