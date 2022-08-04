package com.example.project3.util.exceptions;

public class MeasurementNotCreatedException extends RuntimeException{

    public MeasurementNotCreatedException(String errorMessage) {
        super(errorMessage);
    }
}
