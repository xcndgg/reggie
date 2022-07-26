package com.xc.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取当前线程的id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
