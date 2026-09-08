package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.dto.prescriptionItem.PrescriptionItemDto;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionDto {

    private Long id;
    private LocalDate date;
    private LocalDate validity;
    private String observations;
    private List<PrescriptionItemDto> prescriptionItem;

    public  PrescriptionDto(Prescription prescription){
        id = prescription.getId();
        date = prescription.getDate();
        validity = prescription.getValidity();
        observations = prescription.getObservations();
        prescriptionItem = prescription.getPrescriptionItem().stream().map(PrescriptionItemDto::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getValidity() {
        return validity;
    }

    public void setValidity(LocalDate validity) {
        this.validity = validity;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<PrescriptionItemDto> getPrescriptionItem() {
        return prescriptionItem;
    }

    public void setPrescriptionItem(List<PrescriptionItemDto> prescriptionItem) {
        this.prescriptionItem = prescriptionItem;
    }
}
