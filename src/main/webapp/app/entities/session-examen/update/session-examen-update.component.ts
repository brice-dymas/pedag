import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISessionExamen, SessionExamen } from '../session-examen.model';
import { SessionExamenService } from '../service/session-examen.service';

@Component({
  selector: 'jhi-session-examen-update',
  templateUrl: './session-examen-update.component.html',
})
export class SessionExamenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [],
    mois: [null, [Validators.required]],
    annee: [null, [Validators.required, Validators.min(2000)]],
    actif: [],
  });

  constructor(protected sessionExamenService: SessionExamenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionExamen }) => {
      this.updateForm(sessionExamen);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sessionExamen = this.createFromForm();
    if (sessionExamen.id !== undefined) {
      this.subscribeToSaveResponse(this.sessionExamenService.update(sessionExamen));
    } else {
      this.subscribeToSaveResponse(this.sessionExamenService.create(sessionExamen));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionExamen>>): void {
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

  protected updateForm(sessionExamen: ISessionExamen): void {
    this.editForm.patchValue({
      id: sessionExamen.id,
      libelle: sessionExamen.libelle,
      mois: sessionExamen.mois,
      annee: sessionExamen.annee,
      actif: sessionExamen.actif,
    });
  }

  protected createFromForm(): ISessionExamen {
    return {
      ...new SessionExamen(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      mois: this.editForm.get(['mois'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      actif: this.editForm.get(['actif'])!.value,
    };
  }
}
