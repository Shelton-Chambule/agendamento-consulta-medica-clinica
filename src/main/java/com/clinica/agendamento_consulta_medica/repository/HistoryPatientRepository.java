package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entity.HistoryPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryPatientRepository extends JpaRepository<HistoryPatient,Long> {

    HistoryPatient findByConsultation_Id (Long consultationId);
}
