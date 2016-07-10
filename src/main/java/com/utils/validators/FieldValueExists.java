/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.validators;

/**
 *
 * @author Bert
 */
public interface FieldValueExists {
    boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
}
