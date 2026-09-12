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
import jakarta.transaction.Transactional;
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
    private  final  HistoryPatientService historyPatientService;

    public ConsultationService(ConsulationRepository consulationRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, HistoryPatientService historyPatientService) {
        this.consulationRepository = consulationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.historyPatientService = historyPatientService;
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

    @Transactional
    public ConsultationResponseDTO save(ConsultationRequestDTO consultationDto) {

        Doctor doctor = doctorRepository.findById(consultationDto.getDoctor()).orElseThrow(() -> new ResourceNotFoundException(consultationDto.getDoctor()));
        Patient patient = patientRepository.findById(consultationDto.getPatient()).orElseThrow(() -> new ResourceNotFoundException(consultationDto.getPatient()));

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

        historyPatientService.save(consultation);

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

    public ConsultationResponseDTO update(Long id, ConsultationRequestDTO consultationRequestDTO) {
        try {
            Consultation consultation = consulationRepository.getReferenceById(id);
            updateData(consultation, consultationRequestDTO);
            consulationRepository.save(consultation);
            return new ConsultationResponseDTO(consultation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Consultation consultation, ConsultationRequestDTO consultationRequestDTO) {
        Doctor doctor = doctorRepository.findById(consultationRequestDTO.getDoctor()).orElseThrow(() -> new ResourceNotFoundException(consultationRequestDTO.getDoctor()));

        consultation.setStarTime(consultationRequestDTO.getStarTime());
        consultation.setDuration(consultationRequestDTO.getDuration());
        consultation.setDoctor(doctor);
    }
}
