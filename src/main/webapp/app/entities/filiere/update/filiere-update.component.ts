import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFiliere, Filiere } from '../filiere.model';
import { FiliereService } from '../service/filiere.service';

@Component({
  selector: 'jhi-filiere-update',
  templateUrl: './filiere-update.component.html',
})
export class FiliereUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required, Validators.minLength(7), Validators.maxLength(50)]],
    sigle: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(4)]],
  });

  constructor(protected filiereService: FiliereService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiere }) => {
      this.updateForm(filiere);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiere = this.createFromForm();
    if (filiere.id !== undefined) {
      this.subscribeToSaveResponse(this.filiereService.update(filiere));
    } else {
      this.subscribeToSaveResponse(this.filiereService.create(filiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliere>>): void {
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

  protected updateForm(filiere: IFiliere): void {
    this.editForm.patchValue({
      id: filiere.id,
      libelle: filiere.libelle,
      sigle: filiere.sigle,
    });
  }

  protected createFromForm(): IFiliere {
    return {
      ...new Filiere(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      sigle: this.editForm.get(['sigle'])!.value,
    };
  }
}
