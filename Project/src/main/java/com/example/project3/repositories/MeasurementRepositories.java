package com.example.project3.repositories;

import com.example.project3.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepositories extends JpaRepository<Measurement, Integer> {
}
