import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from 'app/core/auth/token.service';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourriel, Courriel } from '../courriel.model';
import { CourrielService } from '../service/courriel.service';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';

@Component({
  selector: 'jhi-courriel-update',
  templateUrl: './courriel-update.component.html',
})
export class CourrielUpdateComponent implements OnInit {
  isSaving = false;

  inscriptionsSharedCollection: IInscription[] = [];
  ins!: any;

  editForm = this.fb.group({
    id: [],
    sender: [null],
    receiver: [null, [Validators.required]],
    objet: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(70)]],
    message: [null, [Validators.required, Validators.minLength(2)]],
    dateCreation: [],
    etudiant: [],
  });

  constructor(
    protected courrielService: CourrielService,
    protected inscriptionService: InscriptionService,
    protected activatedRoute: ActivatedRoute,
    protected tokenService: TokenService,
    protected fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courriel }) => {
      this.updateForm(courriel);
      this.ins = this.tokenService.getInscription();
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courriel = this.createFromForm();
    // eslint-disable-next-line no-console
    console.log('courriel', courriel);
    // this.isSaving = false;
    // if (courriel.id !== undefined) {
    //   this.subscribeToSaveResponse(this.courrielService.update(courriel));
    // } else {
    this.subscribeToSaveResponse(this.courrielService.create(courriel));
    // }
  }

  trackInscriptionById(index: number, item: IInscription): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourriel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/etudiant/mes-courriels']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(courriel: ICourriel): void {
    this.editForm.patchValue({
      id: courriel.id,
      sender: courriel.sender,
      receiver: courriel.receiver,
      objet: courriel.objet,
      message: courriel.message,
      dateCreation: courriel.dateCreation,
      etudiant: courriel.etudiant,
    });

    this.inscriptionsSharedCollection = this.inscriptionService.addInscriptionToCollectionIfMissing(
      this.inscriptionsSharedCollection,
      courriel.etudiant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inscriptionService
      .query()
      .pipe(map((res: HttpResponse<IInscription[]>) => res.body ?? []))
      .pipe(
        map((inscriptions: IInscription[]) =>
          this.inscriptionService.addInscriptionToCollectionIfMissing(inscriptions, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((inscriptions: IInscription[]) => (this.inscriptionsSharedCollection = inscriptions));
  }

  protected createFromForm(): ICourriel {
    return {
      ...new Courriel(),
      id: this.editForm.get(['id'])!.value,
      sender: this.ins.etudiant.email,
      receiver: this.editForm.get(['receiver'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      message: this.editForm.get(['message'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      etudiant: this.ins,
    };
  }
}
