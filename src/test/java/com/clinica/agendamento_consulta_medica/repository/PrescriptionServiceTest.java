package com.clinica.agendamento_consulta_medica.repository;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PrescriptionServiceTest {

    @Autowired
    private PrescriptionRepsitory prescriptionRepsitory;

    @Test
    public void ShouldDeleteWithIdExisting() {
        Long id = 1L;
        Optional<Prescription> prescription = prescriptionRepsitory.findById(id);

        prescriptionRepsitory.deleteById(id);

        Assertions.assertEquals(prescription, id);

    }
}
