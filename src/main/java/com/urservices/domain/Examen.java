package com.urservices.domain;

import com.urservices.domain.enumeration.Semestre;
import com.urservices.domain.enumeration.TypeExamen;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Examen.
 */
@Entity
@Table(name = "examen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Examen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_examen", nullable = false)
    private LocalDate dateExamen;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeExamen type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "semestre", nullable = false)
    private Semestre semestre;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Examen id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateExamen() {
        return this.dateExamen;
    }

    public Examen dateExamen(LocalDate dateExamen) {
        this.dateExamen = dateExamen;
        return this;
    }

    public void setDateExamen(LocalDate dateExamen) {
        this.dateExamen = dateExamen;
    }

    public TypeExamen getType() {
        return this.type;
    }

    public Examen type(TypeExamen type) {
        this.type = type;
        return this;
    }

    public void setType(TypeExamen type) {
        this.type = type;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public Examen semestre(Semestre semestre) {
        this.semestre = semestre;
        return this;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Examen)) {
            return false;
        }
        return id != null && id.equals(((Examen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Examen{" +
            "id=" + getId() +
            ", dateExamen='" + getDateExamen() + "'" +
            ", type='" + getType() + "'" +
            ", semestre='" + getSemestre() + "'" +
            "}";
    }
}
