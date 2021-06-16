import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';
import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';
import { SessionExamenService } from 'app/entities/session-examen/service/session-examen.service';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { IExamen } from 'app/entities/examen/examen.model';
import { ExamenService } from 'app/entities/examen/service/examen.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  sessionExamenSharedCollection: ISessionExamen[] = [];
  inscriptionsSharedCollection: IInscription[] = [];
  examenSharedCollection: IExamen[] = [];
  matieresSharedCollection: IMatiere[] = [];
  enseignantsSharedCollection: IEnseignant[] = [];

  editForm = this.fb.group({
    id: [],
    moyenne: [null, [Validators.required]],
    observation: [],
    creditMatiere: [],
    creditObtenu: [],
    sessionExamen: [],
    etudiant: [],
    examen: [],
    matiere: [],
    enseignant: [],
  });

  constructor(
    protected noteService: NoteService,
    protected sessionExamenService: SessionExamenService,
    protected inscriptionService: InscriptionService,
    protected examenService: ExamenService,
    protected matiereService: MatiereService,
    protected enseignantService: EnseignantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const note = this.createFromForm();
    if (note.id !== undefined) {
      this.subscribeToSaveResponse(this.noteService.update(note));
    } else {
      this.subscribeToSaveResponse(this.noteService.create(note));
    }
  }

  trackSessionExamenById(index: number, item: ISessionExamen): number {
    return item.id!;
  }

  trackInscriptionById(index: number, item: IInscription): number {
    return item.id!;
  }

  trackExamenById(index: number, item: IExamen): number {
    return item.id!;
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  trackEnseignantById(index: number, item: IEnseignant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      moyenne: note.moyenne,
      observation: note.observation,
      creditMatiere: note.creditMatiere,
      creditObtenu: note.creditObtenu,
      sessionExamen: note.sessionExamen,
      etudiant: note.etudiant,
      examen: note.examen,
      matiere: note.matiere,
      enseignant: note.enseignant,
    });

    this.sessionExamenSharedCollection = this.sessionExamenService.addSessionExamenToCollectionIfMissing(
      this.sessionExamenSharedCollection,
      note.sessionExamen
    );
    this.inscriptionsSharedCollection = this.inscriptionService.addInscriptionToCollectionIfMissing(
      this.inscriptionsSharedCollection,
      note.etudiant
    );
    this.examenSharedCollection = this.examenService.addExamenToCollectionIfMissing(this.examenSharedCollection, note.examen);
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, note.matiere);
    this.enseignantsSharedCollection = this.enseignantService.addEnseignantToCollectionIfMissing(
      this.enseignantsSharedCollection,
      note.enseignant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionExamenService
      .query()
      .pipe(map((res: HttpResponse<ISessionExamen[]>) => res.body ?? []))
      .pipe(
        map((sessionExamen: ISessionExamen[]) =>
          this.sessionExamenService.addSessionExamenToCollectionIfMissing(sessionExamen, this.editForm.get('sessionExamen')!.value)
        )
      )
      .subscribe((sessionExamen: ISessionExamen[]) => (this.sessionExamenSharedCollection = sessionExamen));

    this.inscriptionService
      .query()
      .pipe(map((res: HttpResponse<IInscription[]>) => res.body ?? []))
      .pipe(
        map((inscriptions: IInscription[]) =>
          this.inscriptionService.addInscriptionToCollectionIfMissing(inscriptions, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((inscriptions: IInscription[]) => (this.inscriptionsSharedCollection = inscriptions));

    this.examenService
      .query()
      .pipe(map((res: HttpResponse<IExamen[]>) => res.body ?? []))
      .pipe(map((examen: IExamen[]) => this.examenService.addExamenToCollectionIfMissing(examen, this.editForm.get('examen')!.value)))
      .subscribe((examen: IExamen[]) => (this.examenSharedCollection = examen));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));

    this.enseignantService
      .query()
      .pipe(map((res: HttpResponse<IEnseignant[]>) => res.body ?? []))
      .pipe(
        map((enseignants: IEnseignant[]) =>
          this.enseignantService.addEnseignantToCollectionIfMissing(enseignants, this.editForm.get('enseignant')!.value)
        )
      )
      .subscribe((enseignants: IEnseignant[]) => (this.enseignantsSharedCollection = enseignants));
  }

  protected createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      moyenne: this.editForm.get(['moyenne'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      creditMatiere: this.editForm.get(['creditMatiere'])!.value,
      creditObtenu: this.editForm.get(['creditObtenu'])!.value,
      sessionExamen: this.editForm.get(['sessionExamen'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      examen: this.editForm.get(['examen'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
      enseignant: this.editForm.get(['enseignant'])!.value,
    };
  }
}
