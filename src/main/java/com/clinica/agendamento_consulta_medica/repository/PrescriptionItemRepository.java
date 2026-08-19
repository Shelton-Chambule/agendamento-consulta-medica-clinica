package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entities.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {

}
