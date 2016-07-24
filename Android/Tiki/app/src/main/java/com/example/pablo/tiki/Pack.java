package com.example.pablo.tiki;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Pack implements Serializable {

    public static final int TIMEOUT = 1000;
    public static final int LITTLE_SLEEP = 50;


    public static final int LOG_DATA = 1;
    public static final int DENIED = 2;
    public static final int ACCEPTED = 3;
    public static final int NEW_ID = 4;
    public static final int ACK = 5;
    public static final int SERVER_NAME = 6;
    public static final int QUIT = 7;
    public static final int SHUTDOWN = 8;
    public static final int LOG_ADMIN_DATA = 9;
    public static final int TOGGLE = 10;
    public static final int SEQUENCE = 11;
    public static final int ON = 12;
    public static final int OFF = 13;
    public static final int BLOCK_ACCESS = 14;
    public static final int RANDOM = 15;
    public static final int CLIENT_LIST = 16;
    private int performative;
    private HashMap<String, Object> data;

    public Pack(){}

    public Pack(int performative) {
        this.performative = performative;
        this.data = new HashMap();
    }

    public Pack(int performative, HashMap<String, Object> data) {
        this.performative = performative;
        this.data = data;
    }

    public int getPerformative() {
        return this.performative;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public static Pack readPack(ObjectInputStream input){
        Pack p = null;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < TIMEOUT){
            try {

                Log.d(Connect.LOG_TAG,input.available()+"");
                if (input.available() > 0){
                    int performative = ((Integer)input.readObject()) .intValue();
                    HashMap<String,Object> data = (HashMap<String, Object>)input.readObject();
                    p = new Pack(performative,data);
                    break;
                }else {
                    Thread.sleep(LITTLE_SLEEP);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return p;
    }


    public static boolean sendPack(Pack p, ObjectOutputStream output){
        try {
            output.writeObject(new Integer(p.getPerformative()));
            output.flush();
            output.writeObject(p.getData());
            output.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}