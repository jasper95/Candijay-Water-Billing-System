/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domain.enums;

/**
 *
 * @author Bert
 */
public enum UserType {
    SUPERUSER("Superuser","SUPERUSER"),
    ENCODER("Encoder", "ENCODER"),
    TREASURER("Treasurer", "TREASURER"),
    REPORTS_VIEWER("Reports Viewer", "REPORTS_VIEWER");

    private String label;
    private String value;

    UserType(String label, String value){
        this.label = label;
        this.value = value;
    }

    public String getLabel(){
        return this.label;
    }

    public String getValue(){
        return this.value;
    }

}
