package com.sanaltebesir.stb_student;

import android.net.Uri;

public class DataModel {

    private String itemTitle,itemMessageDate, itemAskingDate, itemViewed, itemTvsolved;
    private int itemImageid2, itemImageid;

    public int getImageid(){
        return this.itemImageid;
    }

    public int getImageid2(){
        return this.itemImageid2;
    }

    public String getTitle(){
        return this.itemTitle;
    }

    public String getAskingDate(){
        return this.itemAskingDate;
    }


    public String getMessageDate(){
        return this.itemMessageDate;
    }

    public String getViewed(){
        return this.itemViewed;
    }

    public String getTvsolved(){
        return this.itemTvsolved;
    }

    public void setImageid(int imageid){
        this.itemImageid = imageid;
    }

    public void setImageid2(int imageid2){
        this.itemImageid2 = imageid2;
    }

    public void setTitle(String title){
        this.itemTitle = title;
    }

    public void setAskingDate(String askingDate){
        this.itemAskingDate = askingDate;
    }

    public void setMessageDate(String messageDate){
        this.itemMessageDate = messageDate;
    }

    public void setViewed(String viewed){
        this.itemViewed = viewed;
    }

    public void setTvsolved(String tvsolved){
        this.itemTvsolved = tvsolved;
    }

}
