package com.noteitapp.dev.noteit;

import java.util.ArrayList;

/**
 * Created by Jeeva on 4/7/2016.
 */
public class Note {


    private String id;
    private String title;
    private String content;
    private String priority;
    private String category;
    private String timestamp;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String color;

    public Note(){

    }

    public Note(String id,String title,String content,String priority,String category,String timestamp){

        this.title = title;
        this.content = content;
        this.priority = priority;
        this.category = category;
        this.timestamp = timestamp;
    }



    ///////////////////////////////////////////

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }




}
