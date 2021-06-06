import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInscription, Inscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/annee-academique/service/annee-academique.service';

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;

  etudiantsSharedCollection: IEtudiant[] = [];
  filieresSharedCollection: IFiliere[] = [];
  anneeAcademiquesSharedCollection: IAnneeAcademique[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    etudiant: [],
    filiere: [],
    anneeAcademique: [],
  });

  constructor(
    protected inscriptionService: InscriptionService,
    protected etudiantService: EtudiantService,
    protected filiereService: FiliereService,
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.updateForm(inscription);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscription = this.createFromForm();
    if (inscription.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  trackEtudiantById(index: number, item: IEtudiant): number {
    return item.id!;
  }

  trackFiliereById(index: number, item: IFiliere): number {
    return item.id!;
  }

  trackAnneeAcademiqueById(index: number, item: IAnneeAcademique): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
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

  protected updateForm(inscription: IInscription): void {
    this.editForm.patchValue({
      id: inscription.id,
      date: inscription.date,
      etudiant: inscription.etudiant,
      filiere: inscription.filiere,
      anneeAcademique: inscription.anneeAcademique,
    });

    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      inscription.etudiant
    );
    this.filieresSharedCollection = this.filiereService.addFiliereToCollectionIfMissing(this.filieresSharedCollection, inscription.filiere);
    this.anneeAcademiquesSharedCollection = this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing(
      this.anneeAcademiquesSharedCollection,
      inscription.anneeAcademique
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));

    this.filiereService
      .query()
      .pipe(map((res: HttpResponse<IFiliere[]>) => res.body ?? []))
      .pipe(
        map((filieres: IFiliere[]) => this.filiereService.addFiliereToCollectionIfMissing(filieres, this.editForm.get('filiere')!.value))
      )
      .subscribe((filieres: IFiliere[]) => (this.filieresSharedCollection = filieres));

    this.anneeAcademiqueService
      .query()
      .pipe(map((res: HttpResponse<IAnneeAcademique[]>) => res.body ?? []))
      .pipe(
        map((anneeAcademiques: IAnneeAcademique[]) =>
          this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiques, this.editForm.get('anneeAcademique')!.value)
        )
      )
      .subscribe((anneeAcademiques: IAnneeAcademique[]) => (this.anneeAcademiquesSharedCollection = anneeAcademiques));
  }

  protected createFromForm(): IInscription {
    return {
      ...new Inscription(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      anneeAcademique: this.editForm.get(['anneeAcademique'])!.value,
    };
  }
}
