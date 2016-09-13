package com.mph.Server;
import java.util.Observable;

public class NumObservable extends Observable {
    static int ClientCount = 0;
 
    public int getClientCount() {
       return ClientCount;
    }
 
    public void setClientCount(int i) {
       ClientCount = i;
       setChanged();
       notifyObservers();
    }
}