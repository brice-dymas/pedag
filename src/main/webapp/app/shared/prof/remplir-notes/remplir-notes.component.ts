import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { INoteFiliere } from 'app/shared/prof/note-filiere.model';
import { NoteFiliereService } from 'app/shared/prof/note-filiere.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-remplir-notes',
  templateUrl: './remplir-notes.component.html',
  styleUrls: ['./remplir-notes.component.scss'],
})
export class RemplirNotesComponent implements OnInit {
  isSaving = false;
  noteFiliere: INoteFiliere | undefined;

  notes = this.fb.array([
    {
      id: new FormControl(),
      moyenne: new FormControl(0),
      observation: new FormControl(),
      creditMatiere: new FormControl(),
      creditObtenu: new FormControl(),
      sessionExamen: new FormControl(),
      etudiant: new FormControl(),
      examen: new FormControl(),
      matiere: new FormControl(''),
      enseignant: new FormControl(''),
    },
  ]);

  editForm = this.fb.group({
    matiere: [],
    filiere: [],
    notes: this.fb.array([]),
    dispenser: [],
    enseignant: [],
    sessionExamen: [],
    anneeAcademique: [],
  });

  constructor(protected activatedRoute: ActivatedRoute, protected noteFiliereService: NoteFiliereService, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.addManyNotes(note.notes.length);
      this.noteFiliere = note;
      this.updateForm(note);
    });
  }

  previousState(): void {
    window.history.back();
  }

  addManyNotes(size: number): void {
    for (let i = 0; i < size; i++) {
      this.notes = this.editForm.get('notes') as FormArray;
      this.notes.push(this.initNotes());
    }
  }

  initNotes(): FormGroup {
    return this.fb.group({
      id: new FormControl(),
      moyenne: new FormControl(0),
      observation: new FormControl(),
      creditMatiere: new FormControl(),
      creditObtenu: new FormControl(),
      sessionExamen: new FormControl(),
      etudiant: this.fb.group({
        id: [],
        date: [null],
        etudiant: this.fb.group({
          id: [],
          matricule: [null],
          nom: [null],
          dateNaissance: [null],
          prenom: [],
          email: [null],
          telephone: [null],
          photo: [],
          photoContentType: [],
          user: [],
        }),
        filiere: this.fb.group({
          id: [],
          libelle: [null],
          sigle: [null],
        }),
        anneeAcademique: [],
      }),
      examen: new FormControl(),
      matiere: new FormControl(''),
      enseignant: new FormControl(''),
    });
  }

  updateForm(noteFiliere: INoteFiliere): void {
    this.editForm.patchValue({
      matiere: noteFiliere.matiere,
      filiere: noteFiliere.filiere,
      notes: noteFiliere.notes,
      dispenser: noteFiliere.dispenser,
      enseignant: noteFiliere.enseignant,
      sessionExamen: noteFiliere.sessionExamen,
      anneeAcademique: noteFiliere.anneeAcademique,
    });
  }

  /* getObs(moyenne: number): string {
    return moyenne <= 9 ? 'NV' : 'VA';
  }

  onValueMoyenneChange(pos: number): void {
    this.notes = this.editForm.get('notes') as FormArray;
    this.notes
      .at(pos)
      .get('moyenne')!
      .valueChanges.subscribe(moyenne => {
        const obs = this.getObs(moyenne);
        this.notes.at(pos).get('observation')!.setValue(obs);
      });
  }*/

  save(): void {
    const formHeader = this.editForm.value;
    this.subscribeToSaveResponse(this.noteFiliereService.saveNotes(formHeader));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INoteFiliere>>): void {
    this.isSaving = false;
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    // result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
    //   () => this.onSaveSuccess(),
    //   () => this.onSaveError()
    // );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
}
