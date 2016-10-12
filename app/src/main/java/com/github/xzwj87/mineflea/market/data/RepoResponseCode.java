package com.github.xzwj87.mineflea.market.data;

/**
 * Created by jason on 9/28/16.
 */

public class RepoResponseCode {

    public static final int RESP_SUCCESS = 0x00;

    public static final int RESP_NETWORK_ERROR = 0x01;

    public static final int RESP_NETWORK_NOT_CONNECTED = 0x02;

    public static final int RESP_DATABASE_SQL_ERROR = 0x03;

    public static final int RESP_AV_SAVED_FAILURE = 0x04;

    private int mCode = RESP_SUCCESS;

    public RepoResponseCode(){}

    public RepoResponseCode(int code){
        mCode = code;
    }

    public void setCode(int code){
        mCode = code;
    }

    public int getCode(){
        return mCode;
    }

    @Override
    public String toString(){
        return String.valueOf(mCode);
    }

}
