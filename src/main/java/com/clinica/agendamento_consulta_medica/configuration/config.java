package com.clinica.agendamento_consulta_medica.configuration;
import com.clinica.agendamento_consulta_medica.entities.*;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@Profile("test")
public class config implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsulationRepository consulationRepository;

    @Autowired
    private SpecialtyRepository especialtyRepository;

    @Autowired
    private MedicalScheduleRepository medicalScheduleRepository;

    @Autowired
    private HistoryPatientRepository historyPatientRepository;

    @Autowired
    private PrescriptionItemRepository prescriptionItemRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private PrescriptionRepsitory prescriptionRepsitory;

    @Override
    public void run(String... args) throws Exception {

        Specialty e = Specialty.builder().name("Dermatologia").price(1500.00).build();
        Specialty e1 = Specialty.builder().name("Medicina Esportiva").price(1800.00).build();
        Specialty e2 = Specialty.builder().name("Oftalmologia").price(1200.00).build();
        Specialty e3 = Specialty.builder().name("Neurocirugia").price(2500.00).build();
        Specialty e4 = Specialty.builder().name("Psiquiatra").price(1700.00).build();
        Specialty e5 = Specialty.builder().name("Cirrugia Geral").price(3500.00).build();
        Specialty e6 = Specialty.builder().name("Circurgia Plastica").price(2600.00).build();

        especialtyRepository.saveAll(Arrays.asList(e,e1,e2,e3,e4,e5,e6));

        Patient p =  Patient.builder().name("Shelton Chambule").email("chambue@gmail.com").phone("873307789").password("mrx").build();
        Patient p1 =  Patient.builder().name("Belga Chambule").email("belga@gmail.com").phone("843307789").password("belga").build();
        Patient p2 =  Patient.builder().name("Yola Manhinda").email("yola@gmail.com").phone("823307789").password("12345678").build();
        Patient p3 =  Patient.builder().name("Humberto Winner").email("winner@gmail.com").phone("853307789").password("9876543").build();
        Patient p4 =  Patient.builder().name("Milton Munhuco").email("milton@gmail.com").phone("863307789").password("isdb1234").build();
        Patient p5 =  Patient.builder().name("Arnaldo Joao").email("arnaldo@gmail.com").phone("863307789").password("isdb12345").build();

        patientRepository.saveAll(Arrays.asList(p,p1,p2,p3,p4,p5));

        Doctor d1 = Doctor.builder().name("Joao Mapisse").email("mapisse@gmail.com").phone("843928398").password("91774181").build();
        Doctor d2 = Doctor.builder().name("Olga Roberto").email("olga@gmail.com").phone("853928398").password("91774181").build();
        Doctor d3 = Doctor.builder().name("Loide Nhatumbo").email("nhatumbo@gmail.com").phone("863928398").password("91774181").build();
        Doctor d4 = Doctor.builder().name("Bento Arnaldo").email("arnaldo@gmail.com").phone("873928398").password("91774181").build();
        Doctor d5 = Doctor.builder().name("Lidia Cossa").email("cossa@gmail.com").phone("863928397").password("917741812").build();
        Doctor d6 = Doctor.builder().name("Aline Castigo").email("@aline").phone("8983352625").password("9027276372").build();

        doctorRepository.saveAll(Arrays.asList(d1,d2,d3,d4,d5,d6));


        Consultation c1 =  Consultation.builder().moment(Instant.parse("2026-07-06T10:15:30Z")).date(LocalDate.of(2026,8,20)).starTime(LocalTime.of(16,0)).statusConsultation(StatusConsultation.WAITING).patient(p4).duration(40).doctor(d1).build();
        Consultation c2 =  Consultation.builder().moment(Instant.parse("2026-07-06T10:15:30Z")).date(LocalDate.of(2026,8,22)).starTime(LocalTime.of(16,0)).statusConsultation(StatusConsultation.WAITING).patient(p1).duration(50).doctor(d1).build();
        Consultation c3 = Consultation.builder().moment(Instant.parse("2026-07-06T10:15:30Z")).date(LocalDate.of(2026,8,22)).starTime(LocalTime.of(15,0)).statusConsultation(StatusConsultation.SCHEDULED).patient(p4).duration(40).doctor(d6).build();
        Consultation c4 = Consultation.builder().moment(Instant.parse("2026-07-06T10:15:30Z")).date(LocalDate.of(2026,8,15)).starTime(LocalTime.of(14,0)).statusConsultation(StatusConsultation.CONFIRMED).patient(p2).duration(50).doctor(d3).build();
        Consultation c5 = Consultation.builder().moment(Instant.parse("2026-07-06T10:15:30Z")).date(LocalDate.of(2026,8,10)).starTime(LocalTime.of(16,0)).statusConsultation(StatusConsultation.CANCELED).patient(p3).duration(30).doctor(d2).build();

        consulationRepository.saveAll(Arrays.asList(c1,c2,c3,c4,c5));

        d6.getSpecialties().add(e);
        d2.getSpecialties().add(e4);
        d1.getSpecialties().add(e5);
        d3.getSpecialties().add(e6);
        d4.getSpecialties().add(e1);
        d3.getSpecialties().add(e3);

        doctorRepository.saveAll(Arrays.asList(d6,d1,d2,d3,d4));

        MedicalSchedule m1 = MedicalSchedule.builder().starTime(LocalTime.of(8,0)).endTime(LocalTime.of(15,30)).dayOfWeek(Set.of(DayOfWeek.FRIDAY,DayOfWeek.SATURDAY)).breakTimes(LocalTime.of(2,0)).doctor(d6).build();
        MedicalSchedule m2 = MedicalSchedule.builder().starTime(LocalTime.of(9,0)).endTime(LocalTime.of(16,30)).dayOfWeek(Set.of(DayOfWeek.MONDAY,DayOfWeek.WEDNESDAY)).breakTimes(LocalTime.of(2,0)).doctor(d1).build();
        MedicalSchedule m3 = MedicalSchedule.builder().starTime(LocalTime.of(7,30)).endTime(LocalTime.of(15,0)).dayOfWeek(Set.of(DayOfWeek.FRIDAY,DayOfWeek.SATURDAY)).breakTimes(LocalTime.of(2,30)).doctor(d3).build();
        MedicalSchedule m4 = MedicalSchedule.builder().starTime(LocalTime.of(8,0)).endTime(LocalTime.of(16,0)).dayOfWeek(Set.of(DayOfWeek.THURSDAY,DayOfWeek.MONDAY)).breakTimes(LocalTime.of(1,30)).doctor(d4).build();
        MedicalSchedule m5 = MedicalSchedule.builder().starTime(LocalTime.of(7,30)).endTime(LocalTime.of(15,0)).dayOfWeek(Set.of(DayOfWeek.WEDNESDAY,DayOfWeek.SATURDAY)).breakTimes(LocalTime.of(3,0)).doctor(d2).build();

        medicalScheduleRepository.saveAll(Arrays.asList(m1,m2,m3,m4,m5));

        HistoryPatient h1 = HistoryPatient.builder().consultations(c1).statusConsultation(c1.getStatusConsultation()).build();
        HistoryPatient h2 = HistoryPatient.builder().consultations(c2).statusConsultation(c2.getStatusConsultation()).build();
        HistoryPatient h3 = HistoryPatient.builder().consultations(c3).statusConsultation(c3.getStatusConsultation()).build();
        HistoryPatient h4= HistoryPatient.builder().consultations(c4).statusConsultation(c4.getStatusConsultation()).build();

        historyPatientRepository.saveAll(Arrays.asList(h1,h2,h3,h4));

        Prescription prescription0 = Prescription.builder().date(LocalDate.of(2026,8,11)).validity(LocalDate.of(2026,8,20)).observations("Lista de  medicamentos junto com as datas").consultation(c1).build();
        Prescription prescription1 = Prescription.builder().date(LocalDate.of(2026,8,11)).validity(LocalDate.of(2026,8,20)).observations("Lista de  medicamentos junto com as datas").consultation(c2).build();
        Prescription prescription2 = Prescription.builder().date(LocalDate.of(2026,8,11)).validity(LocalDate.of(2026,8,20)).observations("Lista de  medicamentos junto com as datas").consultation(c3).build();
        Prescription prescription3 = Prescription.builder().date(LocalDate.of(2026,8,11)).validity(LocalDate.of(2026,8,20)).observations("Lista de  medicamentos junto com as datas").consultation(c4).build();

        prescriptionRepsitory.saveAll(Arrays.asList(prescription0,prescription1,prescription2,prescription3));

        Medications medications0 = Medications.builder().name("Paracetamol").prescription(prescription0).build();
        Medications medications1 = Medications.builder().name("Ibuprofeno").prescription(prescription0).build();
        Medications medications2 = Medications.builder().name("Amoxicillin").build();
        Medications medications3 = Medications.builder().name("Nitroglicerina").build();
        Medications medications4 = Medications.builder().name("Amiodarone").build();
        Medications medications5 = Medications.builder().name("Irbesartan ").build();

        medicationRepository.saveAll(Arrays.asList(medications0,medications1,medications2,medications3,medications4,medications5));

        PrescriptionItem prescriptionItem = new PrescriptionItem( prescription0 , medications0 ,"40mg" ,"Tomar duas vezes  ao dia");

        prescriptionItemRepository.save(prescriptionItem);

    }
}
