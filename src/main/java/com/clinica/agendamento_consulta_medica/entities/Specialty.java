    package com.clinica.agendamento_consulta_medica.entities;
    import jakarta.persistence.*;
    import lombok.*;
    import java.util.*;
    import java.util.ArrayList;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    @Table(name = "tb_specialty")
    public class Specialty {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private Double price;

        @Builder.Default
        @ManyToMany(mappedBy = "specialties")
        private List<Doctor> doctors = new ArrayList<>();


        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Specialty that = (Specialty) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
