import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEtudiant, Etudiant } from '../etudiant.model';
import { EtudiantService } from '../service/etudiant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-etudiant-update',
  templateUrl: './etudiant-update.component.html',
})
export class EtudiantUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    matricule: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(12)]],
    nom: [null, [Validators.required, Validators.minLength(2)]],
    dateNaissance: [null, [Validators.required]],
    prenom: [],
    email: [null, [Validators.required]],
    telephone: [null, [Validators.required, Validators.minLength(9), Validators.maxLength(15)]],
    photo: [],
    photoContentType: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected etudiantService: EtudiantService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etudiant }) => {
      this.updateForm(etudiant);

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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etudiant = this.createFromForm();
    if (etudiant.id !== undefined) {
      this.subscribeToSaveResponse(this.etudiantService.update(etudiant));
    } else {
      this.subscribeToSaveResponse(this.etudiantService.create(etudiant));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtudiant>>): void {
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

  protected updateForm(etudiant: IEtudiant): void {
    this.editForm.patchValue({
      id: etudiant.id,
      matricule: etudiant.matricule,
      nom: etudiant.nom,
      dateNaissance: etudiant.dateNaissance,
      prenom: etudiant.prenom,
      email: etudiant.email,
      telephone: etudiant.telephone,
      photo: etudiant.photo,
      photoContentType: etudiant.photoContentType,
      user: etudiant.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, etudiant.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IEtudiant {
    return {
      ...new Etudiant(),
      id: this.editForm.get(['id'])!.value,
      matricule: this.editForm.get(['matricule'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
