package com.urservices.domain;

import com.urservices.domain.enumeration.ConditionAppliquer;
import com.urservices.domain.enumeration.ConditionSelection;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deliberation.
 */
@Entity
@Table(name = "deliberation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deliberation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "critere_selection", nullable = false)
    private ConditionSelection critereSelection;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valeur_selection_debut", nullable = false)
    private Float valeurSelectionDebut;

    @Column(name = "valeur_selection_fin")
    private Float valeurSelectionFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "critere_appliquer", nullable = false)
    private ConditionAppliquer critereAppliquer;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valeur_appliquer", nullable = false)
    private Float valeurAppliquer;

    @Column(name = "date_deliberation")
    private LocalDate dateDeliberation;

    @ManyToOne(optional = false)
    @NotNull
    private SessionExamen sessionExamen;

    @ManyToOne
    private Filiere filiere;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Deliberation id(Long id) {
        this.id = id;
        return this;
    }

    public ConditionSelection getCritereSelection() {
        return this.critereSelection;
    }

    public Deliberation critereSelection(ConditionSelection critereSelection) {
        this.critereSelection = critereSelection;
        return this;
    }

    public void setCritereSelection(ConditionSelection critereSelection) {
        this.critereSelection = critereSelection;
    }

    public Float getValeurSelectionDebut() {
        return this.valeurSelectionDebut;
    }

    public Deliberation valeurSelectionDebut(Float valeurSelectionDebut) {
        this.valeurSelectionDebut = valeurSelectionDebut;
        return this;
    }

    public void setValeurSelectionDebut(Float valeurSelectionDebut) {
        this.valeurSelectionDebut = valeurSelectionDebut;
    }

    public Float getValeurSelectionFin() {
        return this.valeurSelectionFin;
    }

    public Deliberation valeurSelectionFin(Float valeurSelectionFin) {
        this.valeurSelectionFin = valeurSelectionFin;
        return this;
    }

    public void setValeurSelectionFin(Float valeurSelectionFin) {
        this.valeurSelectionFin = valeurSelectionFin;
    }

    public ConditionAppliquer getCritereAppliquer() {
        return this.critereAppliquer;
    }

    public Deliberation critereAppliquer(ConditionAppliquer critereAppliquer) {
        this.critereAppliquer = critereAppliquer;
        return this;
    }

    public void setCritereAppliquer(ConditionAppliquer critereAppliquer) {
        this.critereAppliquer = critereAppliquer;
    }

    public Float getValeurAppliquer() {
        return this.valeurAppliquer;
    }

    public Deliberation valeurAppliquer(Float valeurAppliquer) {
        this.valeurAppliquer = valeurAppliquer;
        return this;
    }

    public void setValeurAppliquer(Float valeurAppliquer) {
        this.valeurAppliquer = valeurAppliquer;
    }

    public LocalDate getDateDeliberation() {
        return this.dateDeliberation;
    }

    public Deliberation dateDeliberation(LocalDate dateDeliberation) {
        this.dateDeliberation = dateDeliberation;
        return this;
    }

    public void setDateDeliberation(LocalDate dateDeliberation) {
        this.dateDeliberation = dateDeliberation;
    }

    public SessionExamen getSessionExamen() {
        return this.sessionExamen;
    }

    public Deliberation sessionExamen(SessionExamen sessionExamen) {
        this.setSessionExamen(sessionExamen);
        return this;
    }

    public void setSessionExamen(SessionExamen sessionExamen) {
        this.sessionExamen = sessionExamen;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public Deliberation filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deliberation)) {
            return false;
        }
        return id != null && id.equals(((Deliberation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deliberation{" +
            "id=" + getId() +
            ", critereSelection='" + getCritereSelection() + "'" +
            ", valeurSelectionDebut=" + getValeurSelectionDebut() +
            ", valeurSelectionFin=" + getValeurSelectionFin() +
            ", critereAppliquer='" + getCritereAppliquer() + "'" +
            ", valeurAppliquer=" + getValeurAppliquer() +
            ", dateDeliberation='" + getDateDeliberation() + "'" +
            "}";
    }
}
