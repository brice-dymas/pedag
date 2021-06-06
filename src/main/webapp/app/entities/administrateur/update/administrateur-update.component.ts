import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAdministrateur, Administrateur } from '../administrateur.model';
import { AdministrateurService } from '../service/administrateur.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-administrateur-update',
  templateUrl: './administrateur-update.component.html',
})
export class AdministrateurUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required, Validators.minLength(2)]],
    prenom: [],
    email: [null, [Validators.required]],
    grade: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected administrateurService: AdministrateurService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ administrateur }) => {
      this.updateForm(administrateur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const administrateur = this.createFromForm();
    if (administrateur.id !== undefined) {
      this.subscribeToSaveResponse(this.administrateurService.update(administrateur));
    } else {
      this.subscribeToSaveResponse(this.administrateurService.create(administrateur));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdministrateur>>): void {
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

  protected updateForm(administrateur: IAdministrateur): void {
    this.editForm.patchValue({
      id: administrateur.id,
      nom: administrateur.nom,
      prenom: administrateur.prenom,
      email: administrateur.email,
      grade: administrateur.grade,
      user: administrateur.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, administrateur.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IAdministrateur {
    return {
      ...new Administrateur(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
