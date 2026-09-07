package com.clinica.agendamento_consulta_medica.dto.doctor;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public class DoctorDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<String> specialtyDto;
    private List<Set<DayOfWeek>> medicalSchedule;

    public DoctorDto(Doctor doctor) {
        id = doctor.getDoctorId();
        name = doctor.getName();
        email = doctor.getEmail();
        phone = doctor.getPhone();
        specialtyDto = doctor.getSpecialties().stream().map(Specialty::getName).toList();
        medicalSchedule = doctor.getMedicalSchedules().stream().map(MedicalSchedule::getDayOfWeek).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getSpecialtyDto() {
        return specialtyDto;
    }

    public void setSpecialtyDto(List<String> specialtyDto) {
        this.specialtyDto = specialtyDto;
    }

    public List<Set<DayOfWeek>> getMedicalSchedule() {
        return medicalSchedule;
    }

    public void setMedicalSchedule(List<Set<DayOfWeek>> medicalSchedule) {
        this.medicalSchedule = medicalSchedule;
    }
}
