import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICoursFiliere, CoursFiliere } from '../cours-filiere.model';
import { CoursFiliereService } from '../service/cours-filiere.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-cours-filiere-update',
  templateUrl: './cours-filiere-update.component.html',
})
export class CoursFiliereUpdateComponent implements OnInit {
  isSaving = false;

  filieresSharedCollection: IFiliere[] = [];
  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    quotaHoraire: [],
    filiere: [],
    matiere: [],
  });

  constructor(
    protected coursFiliereService: CoursFiliereService,
    protected filiereService: FiliereService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coursFiliere }) => {
      this.updateForm(coursFiliere);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coursFiliere = this.createFromForm();
    if (coursFiliere.id !== undefined) {
      this.subscribeToSaveResponse(this.coursFiliereService.update(coursFiliere));
    } else {
      this.subscribeToSaveResponse(this.coursFiliereService.create(coursFiliere));
    }
  }

  trackFiliereById(index: number, item: IFiliere): number {
    return item.id!;
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoursFiliere>>): void {
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

  protected updateForm(coursFiliere: ICoursFiliere): void {
    this.editForm.patchValue({
      id: coursFiliere.id,
      quotaHoraire: coursFiliere.quotaHoraire,
      filiere: coursFiliere.filiere,
      matiere: coursFiliere.matiere,
    });

    this.filieresSharedCollection = this.filiereService.addFiliereToCollectionIfMissing(
      this.filieresSharedCollection,
      coursFiliere.filiere
    );
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(
      this.matieresSharedCollection,
      coursFiliere.matiere
    );
  }

  protected loadRelationshipsOptions(): void {
    this.filiereService
      .query()
      .pipe(map((res: HttpResponse<IFiliere[]>) => res.body ?? []))
      .pipe(
        map((filieres: IFiliere[]) => this.filiereService.addFiliereToCollectionIfMissing(filieres, this.editForm.get('filiere')!.value))
      )
      .subscribe((filieres: IFiliere[]) => (this.filieresSharedCollection = filieres));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): ICoursFiliere {
    return {
      ...new CoursFiliere(),
      id: this.editForm.get(['id'])!.value,
      quotaHoraire: this.editForm.get(['quotaHoraire'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
    };
  }
}
