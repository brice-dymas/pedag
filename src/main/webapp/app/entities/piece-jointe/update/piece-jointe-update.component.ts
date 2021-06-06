import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPieceJointe, PieceJointe } from '../piece-jointe.model';
import { PieceJointeService } from '../service/piece-jointe.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-piece-jointe-update',
  templateUrl: './piece-jointe-update.component.html',
})
export class PieceJointeUpdateComponent implements OnInit {
  isSaving = false;

  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    contenu: [null, [Validators.required]],
    contenuContentType: [],
    dateCreation: [],
    matiere: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pieceJointeService: PieceJointeService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pieceJointe }) => {
      this.updateForm(pieceJointe);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('pedagApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pieceJointe = this.createFromForm();
    if (pieceJointe.id !== undefined) {
      this.subscribeToSaveResponse(this.pieceJointeService.update(pieceJointe));
    } else {
      this.subscribeToSaveResponse(this.pieceJointeService.create(pieceJointe));
    }
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPieceJointe>>): void {
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

  protected updateForm(pieceJointe: IPieceJointe): void {
    this.editForm.patchValue({
      id: pieceJointe.id,
      libelle: pieceJointe.libelle,
      contenu: pieceJointe.contenu,
      contenuContentType: pieceJointe.contenuContentType,
      dateCreation: pieceJointe.dateCreation,
      matiere: pieceJointe.matiere,
    });

    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, pieceJointe.matiere);
  }

  protected loadRelationshipsOptions(): void {
    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): IPieceJointe {
    return {
      ...new PieceJointe(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      contenuContentType: this.editForm.get(['contenuContentType'])!.value,
      contenu: this.editForm.get(['contenu'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
    };
  }
}
