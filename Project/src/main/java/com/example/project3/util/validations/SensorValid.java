package com.example.project3.util.validations;

import com.example.project3.dto.SensorDTO;
import com.example.project3.models.Sensor;
import com.example.project3.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValid implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValid(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SensorDTO sensorDTO = (SensorDTO) target;
        if (sensorService.checkUniqSensor(sensorDTO).isPresent()) {
            errors.rejectValue("name", "", "This sensor name already exist!");
        }
    }
}
