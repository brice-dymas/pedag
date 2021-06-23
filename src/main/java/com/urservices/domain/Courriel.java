package com.urservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Courriel.
 */
@Entity
@Table(name = "courriel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Courriel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "sender", nullable = false)
    private String sender;

    @NotNull
    @Column(name = "receiver", nullable = false)
    private String receiver;

    @NotNull
    @Size(min = 4, max = 70)
    @Column(name = "objet", length = 70, nullable = false)
    private String objet;

    @NotNull
    @Size(min = 2)
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "filiere", "anneeAcademique" }, allowSetters = true)
    private Inscription etudiant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Courriel id(Long id) {
        this.id = id;
        return this;
    }

    public String getSender() {
        return this.sender;
    }

    public Courriel sender(String sender) {
        this.sender = sender;
        return this;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public Courriel receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getObjet() {
        return this.objet;
    }

    public Courriel objet(String objet) {
        this.objet = objet;
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return this.message;
    }

    public Courriel message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Courriel dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Inscription getEtudiant() {
        return this.etudiant;
    }

    public Courriel etudiant(Inscription inscription) {
        this.setEtudiant(inscription);
        return this;
    }

    public void setEtudiant(Inscription inscription) {
        this.etudiant = inscription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Courriel)) {
            return false;
        }
        return id != null && id.equals(((Courriel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Courriel{" +
            "id=" + getId() +
            ", sender='" + getSender() + "'" +
            ", receiver='" + getReceiver() + "'" +
            ", objet='" + getObjet() + "'" +
            ", message='" + getMessage() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
