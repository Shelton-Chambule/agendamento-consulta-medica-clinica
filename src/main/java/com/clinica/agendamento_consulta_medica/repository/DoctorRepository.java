package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository  extends JpaRepository<Doctor,Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Doctor d where d.doctorId = :id")
    Optional<Doctor> findByIdWithLock(@Param("id") Long id);
}
