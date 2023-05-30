/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infilos.relax.error;

public class TypeInferringException extends TableSchemaException {

    public TypeInferringException() {
    }

    public TypeInferringException(String message) {
        super(message);
    }

    public TypeInferringException(Throwable cause) {
        super(cause);
    }
}
