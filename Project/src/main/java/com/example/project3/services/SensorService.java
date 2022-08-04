package com.example.project3.services;

import com.example.project3.dto.SensorDTO;
import com.example.project3.models.Sensor;
import com.example.project3.repositories.SensorRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepositories sensorRepositories;

    @Autowired
    public SensorService(SensorRepositories sensorRepositories) {
        this.sensorRepositories = sensorRepositories;
    }


    @Transactional
    public void registerSensor(Sensor sensor) {
        sensorRepositories.save(sensor);
    }


    public Optional<Sensor> checkUniqSensor(SensorDTO sensorDTO) {

        Optional<Sensor> sensor = sensorRepositories.findByName(sensorDTO.getName());
        return sensor;
    }
}
