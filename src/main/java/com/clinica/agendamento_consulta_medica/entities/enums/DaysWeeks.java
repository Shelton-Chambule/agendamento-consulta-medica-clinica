package com.clinica.agendamento_consulta_medica.entities.enums;
public enum DaysWeeks {

    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String daysOfWeeks;

    DaysWeeks(String daysOfWeeks) {
        this.daysOfWeeks = daysOfWeeks;
    }

    public String getDaysOfWeeks() {
        return daysOfWeeks;
    }
}
