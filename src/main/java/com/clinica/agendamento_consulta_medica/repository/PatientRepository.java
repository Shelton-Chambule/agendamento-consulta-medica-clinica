package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientRepository  extends JpaRepository<Patient, Long> {

}
