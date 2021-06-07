package com.urservices.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoursFiliere.
 */
@Entity
@Table(name = "cours_filiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoursFiliere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "quota_horaire")
    private Integer quotaHoraire;

    @ManyToOne
    private Filiere filiere;

    @ManyToOne
    private Matiere matiere;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoursFiliere id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuotaHoraire() {
        return this.quotaHoraire;
    }

    public CoursFiliere quotaHoraire(Integer quotaHoraire) {
        this.quotaHoraire = quotaHoraire;
        return this;
    }

    public void setQuotaHoraire(Integer quotaHoraire) {
        this.quotaHoraire = quotaHoraire;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public CoursFiliere filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public CoursFiliere matiere(Matiere matiere) {
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
        if (!(o instanceof CoursFiliere)) {
            return false;
        }
        return id != null && id.equals(((CoursFiliere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoursFiliere{" +
            "id=" + getId() +
            ", quotaHoraire=" + getQuotaHoraire() +
            "}";
    }
}
