package com.github.xzwj87.mineflea.market.model;

/**
 * Created by JasonWang on 2016/9/22.
 */
public class PublisherModel {

    private long mId;

    private String mName;

    private String mHeadIconUri;

    private String mTelNumber;

    private String mEmail;

    private int mCredit;

    private int mFollowers;

    private int mGoodsCount;

    private String mLocation;

    private double mDistance;


    public PublisherModel(){

    }

    public void setId(long id){
        mId = id;
    }

    public long getId(){
        return mId;
    }

    public void setName(String name){
        mName = name;
    }

    public String getName(){
        return mName;
    }

    public String getHeadIconUri(){
        return mHeadIconUri;
    }

    public void setHeadIconUri(String iconUri){
        mHeadIconUri = iconUri;
    }

    public void setTelNumber(String no){
        mTelNumber = no;
    }

    public String getTelNumber(){
        return mTelNumber;
    }

    public void setEmail(String email){
        mEmail = email;
    }

    public String getEmail(){
        return mEmail;
    }

    public void setCredit(int credit){
        mCredit = credit;
    }

    public int getCredit(){
        return mCredit;
    }

    public void setFollowers(int followers){
        mFollowers = followers;
    }

    public int getFollowers(){
        return mFollowers;
    }

    public void setGoodsCount(int count){
        mGoodsCount = count;
    }

    public int getGoodsCount(){
        return mGoodsCount;
    }

    public void setLocation(String location){
        mLocation = location;
    }

    public String getLocation(){
        return mLocation;
    }

    public void setDistance(double distance){
        mDistance = distance;
    }

    public double getDistance(){
        return mDistance;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("-------- Publisher Detail --------\n");
        sb.append("id: " + mId + "\n");
        sb.append("name: " + mName + "\n");
        sb.append("email: " + mEmail + "\n");
        sb.append("credit: " + mCredit + "\n");
        sb.append("location: " + mLocation + "\n");
        sb.append("distance: " + mDistance + "\n");
        sb.append("----------------------------------\n");

        return sb.toString();
    }

}
