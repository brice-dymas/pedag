import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDispenser, Dispenser } from '../dispenser.model';
import { DispenserService } from '../service/dispenser.service';
import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/annee-academique/service/annee-academique.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-dispenser-update',
  templateUrl: './dispenser-update.component.html',
})
export class DispenserUpdateComponent implements OnInit {
  isSaving = false;

  anneeAcademiquesSharedCollection: IAnneeAcademique[] = [];
  enseignantsSharedCollection: IEnseignant[] = [];
  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    semestre: [null, [Validators.required]],
    anneeAcademique: [],
    enseignant: [],
    matiere: [],
  });

  constructor(
    protected dispenserService: DispenserService,
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected enseignantService: EnseignantService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispenser }) => {
      this.updateForm(dispenser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dispenser = this.createFromForm();
    if (dispenser.id !== undefined) {
      this.subscribeToSaveResponse(this.dispenserService.update(dispenser));
    } else {
      this.subscribeToSaveResponse(this.dispenserService.create(dispenser));
    }
  }

  trackAnneeAcademiqueById(index: number, item: IAnneeAcademique): number {
    return item.id!;
  }

  trackEnseignantById(index: number, item: IEnseignant): number {
    return item.id!;
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDispenser>>): void {
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

  protected updateForm(dispenser: IDispenser): void {
    this.editForm.patchValue({
      id: dispenser.id,
      semestre: dispenser.semestre,
      anneeAcademique: dispenser.anneeAcademique,
      enseignant: dispenser.enseignant,
      matiere: dispenser.matiere,
    });

    this.anneeAcademiquesSharedCollection = this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing(
      this.anneeAcademiquesSharedCollection,
      dispenser.anneeAcademique
    );
    this.enseignantsSharedCollection = this.enseignantService.addEnseignantToCollectionIfMissing(
      this.enseignantsSharedCollection,
      dispenser.enseignant
    );
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, dispenser.matiere);
  }

  protected loadRelationshipsOptions(): void {
    this.anneeAcademiqueService
      .query()
      .pipe(map((res: HttpResponse<IAnneeAcademique[]>) => res.body ?? []))
      .pipe(
        map((anneeAcademiques: IAnneeAcademique[]) =>
          this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiques, this.editForm.get('anneeAcademique')!.value)
        )
      )
      .subscribe((anneeAcademiques: IAnneeAcademique[]) => (this.anneeAcademiquesSharedCollection = anneeAcademiques));

    this.enseignantService
      .query()
      .pipe(map((res: HttpResponse<IEnseignant[]>) => res.body ?? []))
      .pipe(
        map((enseignants: IEnseignant[]) =>
          this.enseignantService.addEnseignantToCollectionIfMissing(enseignants, this.editForm.get('enseignant')!.value)
        )
      )
      .subscribe((enseignants: IEnseignant[]) => (this.enseignantsSharedCollection = enseignants));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): IDispenser {
    return {
      ...new Dispenser(),
      id: this.editForm.get(['id'])!.value,
      semestre: this.editForm.get(['semestre'])!.value,
      anneeAcademique: this.editForm.get(['anneeAcademique'])!.value,
      enseignant: this.editForm.get(['enseignant'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
    };
  }
}
