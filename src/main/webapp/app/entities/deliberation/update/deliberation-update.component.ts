import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeliberation, Deliberation } from '../deliberation.model';
import { DeliberationService } from '../service/deliberation.service';
import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';
import { SessionExamenService } from 'app/entities/session-examen/service/session-examen.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';

@Component({
  selector: 'jhi-deliberation-update',
  templateUrl: './deliberation-update.component.html',
})
export class DeliberationUpdateComponent implements OnInit {
  isSaving = false;

  sessionExamenSharedCollection: ISessionExamen[] = [];
  filieresSharedCollection: IFiliere[] = [];

  editForm = this.fb.group({
    id: [],
    critereSelection: [null, [Validators.required]],
    valeurSelectionDebut: [null, [Validators.required, Validators.min(0)]],
    valeurSelectionFin: [],
    critereAppliquer: [null, [Validators.required]],
    valeurAppliquer: [null, [Validators.required, Validators.min(0)]],
    dateDeliberation: [],
    sessionExamen: [null, Validators.required],
    filiere: [],
  });

  constructor(
    protected deliberationService: DeliberationService,
    protected sessionExamenService: SessionExamenService,
    protected filiereService: FiliereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliberation }) => {
      this.updateForm(deliberation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliberation = this.createFromForm();
    if (deliberation.id !== undefined) {
      this.subscribeToSaveResponse(this.deliberationService.update(deliberation));
    } else {
      this.subscribeToSaveResponse(this.deliberationService.create(deliberation));
    }
  }

  trackSessionExamenById(index: number, item: ISessionExamen): number {
    return item.id!;
  }

  trackFiliereById(index: number, item: IFiliere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliberation>>): void {
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

  protected updateForm(deliberation: IDeliberation): void {
    this.editForm.patchValue({
      id: deliberation.id,
      critereSelection: deliberation.critereSelection,
      valeurSelectionDebut: deliberation.valeurSelectionDebut,
      valeurSelectionFin: deliberation.valeurSelectionFin,
      critereAppliquer: deliberation.critereAppliquer,
      valeurAppliquer: deliberation.valeurAppliquer,
      dateDeliberation: deliberation.dateDeliberation,
      sessionExamen: deliberation.sessionExamen,
      filiere: deliberation.filiere,
    });

    this.sessionExamenSharedCollection = this.sessionExamenService.addSessionExamenToCollectionIfMissing(
      this.sessionExamenSharedCollection,
      deliberation.sessionExamen
    );
    this.filieresSharedCollection = this.filiereService.addFiliereToCollectionIfMissing(
      this.filieresSharedCollection,
      deliberation.filiere
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionExamenService
      .queryDeliberation()
      .pipe(map((res: HttpResponse<ISessionExamen[]>) => res.body ?? []))
      .pipe(
        map((sessionExamen: ISessionExamen[]) =>
          this.sessionExamenService.addSessionExamenToCollectionIfMissing(sessionExamen, this.editForm.get('sessionExamen')!.value)
        )
      )
      .subscribe((sessionExamen: ISessionExamen[]) => (this.sessionExamenSharedCollection = sessionExamen));

    this.filiereService
      .query()
      .pipe(map((res: HttpResponse<IFiliere[]>) => res.body ?? []))
      .pipe(
        map((filieres: IFiliere[]) => this.filiereService.addFiliereToCollectionIfMissing(filieres, this.editForm.get('filiere')!.value))
      )
      .subscribe((filieres: IFiliere[]) => (this.filieresSharedCollection = filieres));
  }

  protected createFromForm(): IDeliberation {
    return {
      ...new Deliberation(),
      id: this.editForm.get(['id'])!.value,
      critereSelection: this.editForm.get(['critereSelection'])!.value,
      valeurSelectionDebut: this.editForm.get(['valeurSelectionDebut'])!.value,
      valeurSelectionFin: this.editForm.get(['valeurSelectionFin'])!.value,
      critereAppliquer: this.editForm.get(['critereAppliquer'])!.value,
      valeurAppliquer: this.editForm.get(['valeurAppliquer'])!.value,
      dateDeliberation: this.editForm.get(['dateDeliberation'])!.value,
      sessionExamen: this.editForm.get(['sessionExamen'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
    };
  }
}
