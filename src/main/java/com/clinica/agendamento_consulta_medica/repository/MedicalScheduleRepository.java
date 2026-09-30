package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entity.MedicalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalScheduleRepository  extends JpaRepository<MedicalSchedule,Long> {
}
