package com.urservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "moyenne", nullable = false)
    private Float moyenne;

    @Column(name = "observation")
    private String observation;

    @Column(name = "credit_matiere")
    private Integer creditMatiere;

    @Column(name = "credit_obtenu")
    private Integer creditObtenu;

    @ManyToOne
    private SessionExamen sessionExamen;

    @ManyToOne
    @JsonIgnoreProperties(value = { "filiere", "anneeAcademique" }, allowSetters = true)
    private Inscription etudiant;

    @ManyToOne
    private Examen examen;

    @ManyToOne
    private Matiere matiere;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Enseignant enseignant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Note id(Long id) {
        this.id = id;
        return this;
    }

    public Float getMoyenne() {
        return this.moyenne;
    }

    public Note moyenne(Float moyenne) {
        this.moyenne = moyenne;
        return this;
    }

    public void setMoyenne(Float moyenne) {
        this.moyenne = moyenne;
    }

    public String getObservation() {
        return this.observation;
    }

    public Note observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getCreditMatiere() {
        return this.creditMatiere;
    }

    public Note creditMatiere(Integer creditMatiere) {
        this.creditMatiere = creditMatiere;
        return this;
    }

    public void setCreditMatiere(Integer creditMatiere) {
        this.creditMatiere = creditMatiere;
    }

    public Integer getCreditObtenu() {
        return this.creditObtenu;
    }

    public Note creditObtenu(Integer creditObtenu) {
        this.creditObtenu = creditObtenu;
        return this;
    }

    public void setCreditObtenu(Integer creditObtenu) {
        this.creditObtenu = creditObtenu;
    }

    public SessionExamen getSessionExamen() {
        return this.sessionExamen;
    }

    public Note sessionExamen(SessionExamen sessionExamen) {
        this.setSessionExamen(sessionExamen);
        return this;
    }

    public void setSessionExamen(SessionExamen sessionExamen) {
        this.sessionExamen = sessionExamen;
    }

    public Inscription getEtudiant() {
        return this.etudiant;
    }

    public Note etudiant(Inscription inscription) {
        this.setEtudiant(inscription);
        return this;
    }

    public void setEtudiant(Inscription inscription) {
        this.etudiant = inscription;
    }

    public Examen getExamen() {
        return this.examen;
    }

    public Note examen(Examen examen) {
        this.setExamen(examen);
        return this;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public Note matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Enseignant getEnseignant() {
        return this.enseignant;
    }

    public Note enseignant(Enseignant enseignant) {
        this.setEnseignant(enseignant);
        return this;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", moyenne=" + getMoyenne() +
            ", observation='" + getObservation() + "'" +
            ", creditMatiere=" + getCreditMatiere() +
            ", creditObtenu=" + getCreditObtenu() +
            "}";
    }
}
