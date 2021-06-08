import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { Observable, Subscription } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRequete, Requete } from '../requete.model';
import { RequeteService } from '../service/requete.service';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { IAdministrateur } from 'app/entities/administrateur/administrateur.model';
import { AdministrateurService } from 'app/entities/administrateur/service/administrateur.service';

@Component({
  selector: 'jhi-requete-update',
  templateUrl: './requete-update.component.html',
})
export class RequeteUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  account: any;
  authSubscription?: Subscription;

  editForm = this.fb.group({
    id: [],
    objet: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(70)]],
    description: [null, [Validators.required, Validators.minLength(2)]],
    userId: [],
  });

  constructor(
    protected requeteService: RequeteService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.activatedRoute.data.subscribe(({ requete }) => {
      this.updateForm(requete);
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requete = this.createFromForm();
    if (requete.id !== undefined) {
      this.subscribeToSaveResponse(this.requeteService.partialUpdate(requete));
    } else {
      this.subscribeToSaveResponse(this.requeteService.createSimple(requete));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequete>>): void {
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

  protected updateForm(requete: IRequete): void {
    this.editForm.patchValue({
      id: requete.id,
      objet: requete.objet,
      description: requete.description,
    });
  }

  protected createFromForm(): IRequete {
    return {
      ...new Requete(),
      id: this.editForm.get(['id'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      description: this.editForm.get(['description'])!.value,
      userId: this.account.id,
    };
  }
}
