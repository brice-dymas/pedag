import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAnneeAcademique, AnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';

@Component({
  selector: 'jhi-annee-academique-update',
  templateUrl: './annee-academique-update.component.html',
})
export class AnneeAcademiqueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required]],
    current: [],
  });

  constructor(
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anneeAcademique }) => {
      this.updateForm(anneeAcademique);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const anneeAcademique = this.createFromForm();
    if (anneeAcademique.id !== undefined) {
      this.subscribeToSaveResponse(this.anneeAcademiqueService.update(anneeAcademique));
    } else {
      this.subscribeToSaveResponse(this.anneeAcademiqueService.create(anneeAcademique));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnneeAcademique>>): void {
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

  protected updateForm(anneeAcademique: IAnneeAcademique): void {
    this.editForm.patchValue({
      id: anneeAcademique.id,
      libelle: anneeAcademique.libelle,
      current: anneeAcademique.current,
    });
  }

  protected createFromForm(): IAnneeAcademique {
    return {
      ...new AnneeAcademique(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      current: this.editForm.get(['current'])!.value,
    };
  }
}
