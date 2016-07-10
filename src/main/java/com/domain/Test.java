/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domain;

/**
 *
 * @author Bert
 */
public class Test implements java.io.Serializable {
    private String name;
    public Test(){
        
    }
    public Test(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
