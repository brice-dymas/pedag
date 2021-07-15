package com.urservices.domain;

import com.urservices.domain.enumeration.MoisAnnee;
import com.urservices.domain.enumeration.TypeExamen;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SessionExamen.
 */
@Entity
@Table(name = "session_examen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SessionExamen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mois", nullable = false)
    private MoisAnnee mois;

    @NotNull
    @Min(value = 2000)
    @Column(name = "annee", nullable = false)
    private Integer annee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeExamen type;

    @Column(name = "actif")
    private Boolean actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessionExamen id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public SessionExamen libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public MoisAnnee getMois() {
        return this.mois;
    }

    public SessionExamen mois(MoisAnnee mois) {
        this.mois = mois;
        return this;
    }

    public void setMois(MoisAnnee mois) {
        this.mois = mois;
    }

    public Integer getAnnee() {
        return this.annee;
    }

    public SessionExamen annee(Integer annee) {
        this.annee = annee;
        return this;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public TypeExamen getType() {
        return this.type;
    }

    public SessionExamen type(TypeExamen type) {
        this.type = type;
        return this;
    }

    public void setType(TypeExamen type) {
        this.type = type;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public SessionExamen actif(Boolean actif) {
        this.actif = actif;
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionExamen)) {
            return false;
        }
        return id != null && id.equals(((SessionExamen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionExamen{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", mois='" + getMois() + "'" +
            ", annee=" + getAnnee() +
            ", type='" + getType() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
