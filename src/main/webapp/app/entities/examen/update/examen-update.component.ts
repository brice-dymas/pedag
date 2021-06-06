import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IExamen, Examen } from '../examen.model';
import { ExamenService } from '../service/examen.service';

@Component({
  selector: 'jhi-examen-update',
  templateUrl: './examen-update.component.html',
})
export class ExamenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dateExamen: [null, [Validators.required]],
    type: [null, [Validators.required]],
    semestre: [null, [Validators.required]],
  });

  constructor(protected examenService: ExamenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ examen }) => {
      this.updateForm(examen);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const examen = this.createFromForm();
    if (examen.id !== undefined) {
      this.subscribeToSaveResponse(this.examenService.update(examen));
    } else {
      this.subscribeToSaveResponse(this.examenService.create(examen));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExamen>>): void {
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

  protected updateForm(examen: IExamen): void {
    this.editForm.patchValue({
      id: examen.id,
      dateExamen: examen.dateExamen,
      type: examen.type,
      semestre: examen.semestre,
    });
  }

  protected createFromForm(): IExamen {
    return {
      ...new Examen(),
      id: this.editForm.get(['id'])!.value,
      dateExamen: this.editForm.get(['dateExamen'])!.value,
      type: this.editForm.get(['type'])!.value,
      semestre: this.editForm.get(['semestre'])!.value,
    };
  }
}
