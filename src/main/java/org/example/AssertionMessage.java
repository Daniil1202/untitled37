package org.example;

public class AssertionMessage extends RuntimeException {
    public AssertionMessage(String message){
        super(message);
    }
}
