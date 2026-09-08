package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_history_patient")
public class HistoryPatient {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_consultation")
    private Consultation consultations;

    @Enumerated(EnumType.STRING)
    private StatusConsultation statusConsultation;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistoryPatient that = (HistoryPatient) o;
        return Objects.equals(id, that.id);
    }

    public HistoryPatient(){}

    public HistoryPatient(Long id, Consultation consultations, StatusConsultation statusConsultation) {
        this.id = id;
        this.consultations = consultations;
        this.statusConsultation = statusConsultation;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultation getConsultations() {
        return consultations;
    }

    public void setConsultations(Consultation consultations) {
        this.consultations = consultations;
    }

    public StatusConsultation getStatusConsultation() {
        return statusConsultation;
    }

    public void setStatusConsultation(StatusConsultation statusConsultation) {
        this.statusConsultation = statusConsultation;
    }
}
