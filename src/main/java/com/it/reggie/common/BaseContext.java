package com.it.reggie.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentInd (Long id){
        threadLocal.set(id);
    }

    public static Long getCurrfentInd (){
        return threadLocal.get();
    }

}
