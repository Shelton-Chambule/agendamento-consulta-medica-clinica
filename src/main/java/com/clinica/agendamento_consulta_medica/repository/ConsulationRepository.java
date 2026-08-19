package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsulationRepository extends JpaRepository<Consultation, Long> {

}
