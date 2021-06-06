package com.urservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Etudiant etudiant;

    @ManyToOne
    private Filiere filiere;

    @ManyToOne
    private AnneeAcademique anneeAcademique;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inscription id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Inscription date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public Inscription etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public Inscription filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public Inscription anneeAcademique(AnneeAcademique anneeAcademique) {
        this.setAnneeAcademique(anneeAcademique);
        return this;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscription)) {
            return false;
        }
        return id != null && id.equals(((Inscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
