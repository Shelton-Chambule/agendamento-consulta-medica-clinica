package com.clinica.agendamento_consulta_medica.repository.tests;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
public class PatientRepositoryTests {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public  void DeveDeletarPacienteComIdExistente(){
        Patient patient = new Patient();

        patient.setPatientId(1L);
        patient.setName("Shelton");
        patient.setPhone("973307789");
        patient.setDataNascimento(LocalDate.of(2004,07,8));

        patient = patientRepository.save(patient);

        Long id = patient.getPatientId();
        patientRepository.deleteById(id);

        Optional<Patient> patient1 = patientRepository.findById(id);
        Assertions.assertTrue(patient1.isPresent(),"Deve deletar um patient com o id existente");
    }
}
