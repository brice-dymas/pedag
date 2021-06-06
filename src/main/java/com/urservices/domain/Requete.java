package com.urservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.urservices.domain.enumeration.StatutRequete;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Requete.
 */
@Entity
@Table(name = "requete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Requete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 4, max = 70)
    @Column(name = "objet", length = 70, nullable = false)
    private String objet;

    @NotNull
    @Size(min = 2)
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutRequete statut;

    @Column(name = "traiter")
    private Boolean traiter;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @ManyToOne
    @JsonIgnoreProperties(value = { "etudiant", "filiere", "anneeAcademique" }, allowSetters = true)
    private Inscription etudiant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Administrateur validateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requete id(Long id) {
        this.id = id;
        return this;
    }

    public String getObjet() {
        return this.objet;
    }

    public Requete objet(String objet) {
        this.objet = objet;
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDescription() {
        return this.description;
    }

    public Requete description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatutRequete getStatut() {
        return this.statut;
    }

    public Requete statut(StatutRequete statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(StatutRequete statut) {
        this.statut = statut;
    }

    public Boolean getTraiter() {
        return this.traiter;
    }

    public Requete traiter(Boolean traiter) {
        this.traiter = traiter;
        return this;
    }

    public void setTraiter(Boolean traiter) {
        this.traiter = traiter;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Requete dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public Requete dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public Inscription getEtudiant() {
        return this.etudiant;
    }

    public Requete etudiant(Inscription inscription) {
        this.setEtudiant(inscription);
        return this;
    }

    public void setEtudiant(Inscription inscription) {
        this.etudiant = inscription;
    }

    public Administrateur getValidateur() {
        return this.validateur;
    }

    public Requete validateur(Administrateur administrateur) {
        this.setValidateur(administrateur);
        return this;
    }

    public void setValidateur(Administrateur administrateur) {
        this.validateur = administrateur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requete)) {
            return false;
        }
        return id != null && id.equals(((Requete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Requete{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", description='" + getDescription() + "'" +
            ", statut='" + getStatut() + "'" +
            ", traiter='" + getTraiter() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}
