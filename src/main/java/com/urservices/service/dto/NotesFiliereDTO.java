package com.urservices.service.dto;

import com.urservices.domain.*;
import java.util.List;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Thursday 17 of June
 */
public class NotesFiliereDTO {

    private Matiere matiere;
    private Filiere filiere;
    private List<Note> notes;
    private Dispenser dispenser;
    private Enseignant enseignant;
    private SessionExamen sessionExamen;
    private AnneeAcademique anneeAcademique;

    public NotesFiliereDTO(
        Matiere matiere,
        Filiere filiere,
        List<Note> notes,
        Dispenser dispenser,
        Enseignant enseignant,
        SessionExamen sessionExamen,
        AnneeAcademique anneeAcademique
    ) {
        this.matiere = matiere;
        this.filiere = filiere;
        this.notes = notes;
        this.dispenser = dispenser;
        this.enseignant = enseignant;
        this.sessionExamen = sessionExamen;
        this.anneeAcademique = anneeAcademique;
    }

    public NotesFiliereDTO() {}

    public Matiere getMatiere() {
        return this.matiere;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public Dispenser getDispenser() {
        return this.dispenser;
    }

    public Enseignant getEnseignant() {
        return this.enseignant;
    }

    public SessionExamen getSessionExamen() {
        return this.sessionExamen;
    }

    public AnneeAcademique getAnneeAcademique() {
        return this.anneeAcademique;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setDispenser(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public void setSessionExamen(SessionExamen sessionExamen) {
        this.sessionExamen = sessionExamen;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NotesFiliereDTO)) return false;
        final NotesFiliereDTO other = (NotesFiliereDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$matiere = this.getMatiere();
        final Object other$matiere = other.getMatiere();
        if (this$matiere == null ? other$matiere != null : !this$matiere.equals(other$matiere)) return false;
        final Object this$filiere = this.getFiliere();
        final Object other$filiere = other.getFiliere();
        if (this$filiere == null ? other$filiere != null : !this$filiere.equals(other$filiere)) return false;
        final Object this$notes = this.getNotes();
        final Object other$notes = other.getNotes();
        if (this$notes == null ? other$notes != null : !this$notes.equals(other$notes)) return false;
        final Object this$dispenser = this.getDispenser();
        final Object other$dispenser = other.getDispenser();
        if (this$dispenser == null ? other$dispenser != null : !this$dispenser.equals(other$dispenser)) return false;
        final Object this$enseignant = this.getEnseignant();
        final Object other$enseignant = other.getEnseignant();
        if (this$enseignant == null ? other$enseignant != null : !this$enseignant.equals(other$enseignant)) return false;
        final Object this$sessionExamen = this.getSessionExamen();
        final Object other$sessionExamen = other.getSessionExamen();
        if (this$sessionExamen == null ? other$sessionExamen != null : !this$sessionExamen.equals(other$sessionExamen)) return false;
        final Object this$anneeAcademique = this.getAnneeAcademique();
        final Object other$anneeAcademique = other.getAnneeAcademique();
        if (
            this$anneeAcademique == null ? other$anneeAcademique != null : !this$anneeAcademique.equals(other$anneeAcademique)
        ) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NotesFiliereDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $matiere = this.getMatiere();
        result = result * PRIME + ($matiere == null ? 43 : $matiere.hashCode());
        final Object $filiere = this.getFiliere();
        result = result * PRIME + ($filiere == null ? 43 : $filiere.hashCode());
        final Object $notes = this.getNotes();
        result = result * PRIME + ($notes == null ? 43 : $notes.hashCode());
        final Object $dispenser = this.getDispenser();
        result = result * PRIME + ($dispenser == null ? 43 : $dispenser.hashCode());
        final Object $enseignant = this.getEnseignant();
        result = result * PRIME + ($enseignant == null ? 43 : $enseignant.hashCode());
        final Object $sessionExamen = this.getSessionExamen();
        result = result * PRIME + ($sessionExamen == null ? 43 : $sessionExamen.hashCode());
        final Object $anneeAcademique = this.getAnneeAcademique();
        result = result * PRIME + ($anneeAcademique == null ? 43 : $anneeAcademique.hashCode());
        return result;
    }

    public String toString() {
        return (
            "NotesFiliereDTO(matiere=" +
            this.getMatiere() +
            ", filiere=" +
            this.getFiliere() +
            ", notes=" +
            this.getNotes() +
            ", dispenser=" +
            this.getDispenser() +
            ", enseignant=" +
            this.getEnseignant() +
            ", sessionExamen=" +
            this.getSessionExamen() +
            ", anneeAcademique=" +
            this.getAnneeAcademique() +
            ")"
        );
    }
}
