package com.urservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.urservices.domain.enumeration.Semestre;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dispenser.
 */
@Entity
@Table(name = "dispenser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dispenser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "semestre", nullable = false)
    private Semestre semestre;

    @ManyToOne
    private AnneeAcademique anneeAcademique;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Enseignant enseignant;

    @ManyToOne
    private Matiere matiere;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dispenser id(Long id) {
        this.id = id;
        return this;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public Dispenser semestre(Semestre semestre) {
        this.semestre = semestre;
        return this;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public Dispenser anneeAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Enseignant getEnseignant() {
        return this.enseignant;
    }

    public Dispenser enseignant(Enseignant enseignant) {
        this.setEnseignant(enseignant);
        return this;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public Dispenser matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dispenser)) {
            return false;
        }
        return id != null && id.equals(((Dispenser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dispenser{" +
            "id=" + getId() +
            ", semestre='" + getSemestre() + "'" +
            "}";
    }
}
