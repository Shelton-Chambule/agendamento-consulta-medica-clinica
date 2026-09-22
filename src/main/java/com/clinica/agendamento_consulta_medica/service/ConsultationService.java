package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequest;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ProcessConsultation;
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

    public boolean hasScheduleConflict(Doctor doctor, LocalDate consultationDate, LocalTime startTime, LocalTime endTime) {

        for (Consultation existing : doctor.getConsultations()) {

            boolean sameDate = existing.getDate().equals(consultationDate);

            if (!sameDate) {
                continue;   // Mesmo horário em outro dia: permitido
            }

            LocalTime existingStart = existing.getStarTime();
            LocalTime existingEnd = existingStart.plus(existing.getDuration());

            boolean timeOverlaps = startTime.isBefore(existingEnd) && existingStart.isBefore(endTime);

            if (timeOverlaps) {
                return true;  // Mesmo médico, mesma data e horários sobrepostos
            }
        }
        return false;
    }
    @Transactional
    public ConsultationResponse save(ConsultationRequest consultationDto) {

        Doctor doctor = doctorRepository.findById(consultationDto.getDoctor()).orElseThrow(() -> new ResourceNotFoundException(consultationDto.getDoctor()));
        Patient patient = patientRepository.findById(consultationDto.getPatient()).orElseThrow(() -> new ResourceNotFoundException(consultationDto.getPatient()));

        LocalDate consultationDate = consultationDto.getDate();
        LocalTime starTime = consultationDto.getStartTime();
        LocalTime endTime = starTime.plus(consultationDto.getDuration());

        if(hasScheduleConflict(doctor,consultationDate,starTime, endTime)) throw new ScheduleConflictException("The doctor already has an appointment scheduled for that time");

        Consultation consultation = new Consultation();

        consultation.setMoment(LocalDateTime.now());
        consultation.setDate(consultationDate);
        consultation.setDuration(consultationDto.getDuration());
        consultation.setStarTime(starTime);
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setStatusConsultation(StatusConsultation.WAITING);
        consulationRepository.save(consultation);

        historyPatientService.save(consultation);

        return new ConsultationResponse(consultation);
    }

    @Transactional
    public ConsultationResponse processConsultation(Long consultationId) {

        Consultation consultation = consulationRepository.findById(consultationId).orElseThrow(() -> new ResourceNotFoundException(consultationId));

        if (consultation.getStatusConsultation() != StatusConsultation.WAITING)
            throw new ProcessConsultation("The query can only be processed when it is in the WAITING state..");

        consultation.setStatusConsultation(StatusConsultation.CARRIED_OUT);
        consultation.setMoment(LocalDateTime.now());

        Consultation savedConsultation = consulationRepository.save(consultation);
        historyPatientService.updateStatus(consultation);
        return new ConsultationResponse(savedConsultation);
    }

    public List<ConsultationResponse> findAll() {
        List<Consultation> consultation = consulationRepository.findAll();
        return consultation.stream().map(ConsultationResponse::new).collect(Collectors.toList());
    }

    public ConsultationResponse findById(Long id) {
        Optional<Consultation> consultation = consulationRepository.findById(id);
        return new ConsultationResponse(consultation.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!consulationRepository.existsById(id)) throw new ResourceNotFoundException(id);

        try {
            consulationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public ConsultationResponse update(Long id, ConsultationRequest consultationRequestDTO) {
        try {
            Consultation consultation = consulationRepository.getReferenceById(id);
            updateData(consultation, consultationRequestDTO);
            consulationRepository.save(consultation);
            return new ConsultationResponse(consultation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Consultation consultation, ConsultationRequest consultationRequestDTO) {
        Doctor doctor = doctorRepository.findById(consultationRequestDTO.getDoctor()).orElseThrow(() -> new ResourceNotFoundException(consultationRequestDTO.getDoctor()));

        consultation.setStarTime(consultationRequestDTO.getStartTime());
        consultation.setDuration(consultationRequestDTO.getDuration());
        consultation.setDoctor(doctor);
    }
}
