package com.urservices.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PieceJointe.
 */
@Entity
@Table(name = "piece_jointe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PieceJointe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "libelle", length = 50, nullable = false)
    private String libelle;

    @Lob
    @Column(name = "contenu", nullable = false)
    private byte[] contenu;

    @Column(name = "contenu_content_type", nullable = false)
    private String contenuContentType;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    private Matiere matiere;

    public PieceJointe(
        Long id,
        @NotNull @Size(min = 3, max = 50) String libelle,
        byte[] contenu,
        String contenuContentType,
        LocalDate dateCreation,
        Matiere matiere
    ) {
        this.id = id;
        this.libelle = libelle;
        this.contenu = contenu;
        this.contenuContentType = contenuContentType;
        this.dateCreation = dateCreation;
        this.matiere = matiere;
    }

    public PieceJointe() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PieceJointe id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public PieceJointe libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public byte[] getContenu() {
        return this.contenu;
    }

    public PieceJointe contenu(byte[] contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public String getContenuContentType() {
        return this.contenuContentType;
    }

    public PieceJointe contenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
        return this;
    }

    public void setContenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public PieceJointe dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public PieceJointe matiere(Matiere matiere) {
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
        if (!(o instanceof PieceJointe)) {
            return false;
        }
        return id != null && id.equals(((PieceJointe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointe{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", contenuContentType='" + getContenuContentType() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
