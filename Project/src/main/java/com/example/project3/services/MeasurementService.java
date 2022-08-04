package com.example.project3.services;

import com.example.project3.models.Measurement;
import com.example.project3.models.Sensor;
import com.example.project3.repositories.MeasurementRepositories;
import com.example.project3.repositories.SensorRepositories;
import com.example.project3.util.exceptions.NotFoundSensorException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepositories measurementRepositories;
    private final SensorRepositories sensorRepositories;

    private final EntityManager entityManager;

    @Autowired
    public MeasurementService(MeasurementRepositories measurementRepositories, SensorRepositories sensorRepositories, EntityManager entityManager) {
        this.measurementRepositories = measurementRepositories;
        this.sensorRepositories = sensorRepositories;
        this.entityManager = entityManager;
    }


    public List<Measurement> allMeasurement() {

        List<Measurement> measurements = measurementRepositories.findAll();
        return measurements;
    }


    public int rainingDays() {

        int count = 0;
        List<Measurement> measurements = allMeasurement();
        for (Measurement measurement : measurements) {
            if (measurement.isRaining()) {
                count++;
            }
        }
        return count;
    }


    @Transactional
    public void registerMeasurements(Measurement measurement) {

        Sensor sensor = measurement.getSensor();

        if (sensorRepositories.findByName(sensor.getName()).isPresent()) {
            enrichProperties(measurement);
            measurementRepositories.save(measurement);
        } else {
            throw new NotFoundSensorException();
        }
    }

    private void enrichProperties(Measurement measurement) {

        Sensor sensor  = sensorRepositories.findByName(measurement.getSensorName()).get();

        measurement.setCreatedAt(LocalDateTime.now());
        measurement.setSensorName(sensor.getName());

        sensor.setMeasurements(new ArrayList<>(List.of(measurement)));
        measurement.setSensor(sensor);
    }
}
