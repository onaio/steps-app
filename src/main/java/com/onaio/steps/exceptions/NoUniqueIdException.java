package com.onaio.steps.exceptions;

/**
 * Created by Jason Rogena - jrogena@ona.io on 29/09/2016.
 */
public class NoUniqueIdException extends Exception {
    public NoUniqueIdException(){
        super();
    }

    public NoUniqueIdException(String message){
        super(message);
    }
}
