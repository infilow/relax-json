/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infilos.relax.error;

public class InvalidCastException extends TableSchemaException {

    public InvalidCastException(String message) {
        super(message);
    }

    public InvalidCastException(Throwable cause) {
        super(cause);
    }
}
