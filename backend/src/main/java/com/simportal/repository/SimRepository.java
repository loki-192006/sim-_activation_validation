package com.simportal.repository;

import com.simportal.entity.Sim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SimRepository extends JpaRepository<Sim, Long> {
    Optional<Sim> findBySimNumber(String simNumber);
    boolean existsBySimNumber(String simNumber);
}
