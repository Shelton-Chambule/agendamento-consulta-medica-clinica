package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entities.HistoryPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryPatientRepository extends JpaRepository<HistoryPatient,Long> {

}
