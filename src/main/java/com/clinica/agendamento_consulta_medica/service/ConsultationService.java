package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.service.exception.ScheduleConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultationService {

    private  final ConsulationRepository consulationRepository;
    private final DoctorRepository doctorRepository;
    private final  PatientRepository patientRepository;

    public ConsultationService(ConsulationRepository consulationRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.consulationRepository = consulationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Boolean hasScheduleConflict(Doctor doctor, LocalTime startTime, LocalTime duration) {

        for (Consultation existing : doctor.getConsultations()){

            LocalTime existingStart = existing.getStarTime();
            LocalTime existingEnd = existingStart.plusMinutes(existing.getDuration());

            if (startTime.isBefore(existingEnd) && existingStart.isBefore(duration)) {
                return true;
            }
        }
        return false;
    }

    public ConsultationResponseDTO save(ConsultationRequestDTO consultationDto) {

        Doctor doctor = doctorRepository.getReferenceById(consultationDto.getDoctor());
        Patient patient = patientRepository.getReferenceById(consultationDto.getPatient());

        LocalTime starTime = consultationDto.getStarTime();
        LocalTime endTime = starTime.plusMinutes(consultationDto.getDuration());

        if(hasScheduleConflict(doctor,starTime, endTime)){
                throw new ScheduleConflictException("The doctor already has an appointment scheduled for that time");
        }

        Consultation consultation = new Consultation();
        consultation.setMoment(LocalDateTime.now());
        consultation.setDate(LocalDate.now());
        consultation.setDuration(consultationDto.getDuration());
        consultation.setStarTime(starTime);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setStatusConsultation(StatusConsultation.WAITING);
        consulationRepository.save(consultation);
        return new ConsultationResponseDTO(consultation);
    }

    public List<ConsultationResponseDTO> findAll() {
        List<Consultation> consultation = consulationRepository.findAll();
        return consultation.stream().map(ConsultationResponseDTO::new).collect(Collectors.toList());
    }

    public ConsultationResponseDTO findById(Long id) {
        Optional<Consultation> consultation = consulationRepository.findById(id);
        return new ConsultationResponseDTO(consultation.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!consulationRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }

        try {
            consulationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public ConsultationResponseDTO update(Long id, ConsultationRequestDTO consultation) {
        try {
            Consultation consultation1 = consulationRepository.getReferenceById(id);
            updateData(consultation1, consultation);
            consulationRepository.save(consultation1);
            return new ConsultationResponseDTO(consultation1);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Consultation consultation1, ConsultationRequestDTO consultation) {
        consultation1.setStarTime(consultation.getStarTime());
        consultation1.setDate(consultation.getDate());
    }
}
