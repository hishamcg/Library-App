package com.strata.justbooksclc;

public class SharedValue{
    private volatile static SharedValue shareData;
    public static SharedValue data(){
    if(shareData == null){
        synchronized (SharedValue.class) {
            if (shareData == null) {
                shareData = new SharedValue();
            }
        }
    }
    return shareData;
    }  

    public String position;
    public boolean hasBeenChanged = true;
    
}
