package com.example.project3.controllers;

import com.example.project3.dto.MeasurementDTO;
import com.example.project3.models.Measurement;
import com.example.project3.models.Sensor;
import com.example.project3.repositories.SensorRepositories;
import com.example.project3.services.MeasurementService;
import com.example.project3.util.exceptions.MeasurementNotCreatedException;
import com.example.project3.util.exceptions.NotFoundSensorException;
import com.example.project3.util.exceptions.SensorError;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
    }


    @GetMapping()
    public List<MeasurementDTO> allMeasurements() {
        List<MeasurementDTO> measurementDTOList = measurementService.allMeasurement().stream()
                .map(this::convertToMeasurementDTO).collect(Collectors.toList());
        return measurementDTOList;
    }


    @GetMapping("/rainyDaysCount")
    public int countRainingDays() {
        return measurementService.rainingDays();
    }


    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                stringBuilder.append(error.getField()).append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementNotCreatedException(stringBuilder.toString());
        }
        measurementService.registerMeasurements(convertToMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<SensorError> measurementNotCreatedException(MeasurementNotCreatedException e) {

        SensorError sensorError = new SensorError(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(sensorError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<SensorError> notFoundSensorException(NotFoundSensorException e) {

        SensorError sensorError = new SensorError(
                "This sensor is not exist",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(sensorError, HttpStatus.BAD_REQUEST);
    }


    private Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        MeasurementDTO measurementDTO = modelMapper.map(measurement, MeasurementDTO.class);
        return measurementDTO;
    }

}
