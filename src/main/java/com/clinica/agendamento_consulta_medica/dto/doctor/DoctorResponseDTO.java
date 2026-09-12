package com.clinica.agendamento_consulta_medica.dto.doctor;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequestDTO;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import java.util.List;
import java.util.stream.Collectors;
public class DoctorResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<String> specialtyDto;
    private List<MedicalScheduleRequestDTO> medicalSchedule;

    public DoctorResponseDTO(){}

    public DoctorResponseDTO(Doctor doctor) {
        id = doctor.getDoctorId();
        name = doctor.getName();
        email = doctor.getEmail();
        phone = doctor.getPhone();
        specialtyDto = doctor.getSpecialties().stream().map(Specialty::getName).toList();
        medicalSchedule = doctor.getMedicalSchedules().stream().map(MedicalScheduleRequestDTO::new).collect(Collectors.toList());
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

    public List<MedicalScheduleRequestDTO> getMedicalSchedule() {
        return medicalSchedule;
    }

    public void setMedicalSchedule(List<MedicalScheduleRequestDTO> medicalSchedule) {
        this.medicalSchedule = medicalSchedule;
    }

}
