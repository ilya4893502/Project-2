package com.example.project3.controllers;

import com.example.project3.dto.SensorDTO;
import com.example.project3.models.Sensor;
import com.example.project3.services.SensorService;
import com.example.project3.util.exceptions.SensorError;
import com.example.project3.util.exceptions.SensorNotCreatedException;
import com.example.project3.util.validations.SensorValid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValid sensorValid;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValid sensorValid) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValid = sensorValid;
    }


    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                     BindingResult bindingResult) {

        sensorValid.validate(sensorDTO, bindingResult);
        if (bindingResult.hasErrors()) {

            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new SensorNotCreatedException(errorMessage.toString());
        }

        sensorService.registerSensor(convertToSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<SensorError> sensorNotCreated(SensorNotCreatedException e) {

        SensorError sensorError = new SensorError(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(sensorError, HttpStatus.BAD_REQUEST);
    }


    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }


}
