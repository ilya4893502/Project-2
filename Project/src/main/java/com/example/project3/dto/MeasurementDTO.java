package com.example.project3.dto;

import com.example.project3.models.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

public class MeasurementDTO {

    @Max(value = 100, message = "Value should be between 100 and -100")
    @Min(value = -100, message = "Value should be between 100 and -100")
    private double value;

    private boolean raining;

    @NotNull(message = "Sensor shouldn't be empty")
    private Sensor sensor;


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

}
