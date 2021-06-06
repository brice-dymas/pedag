import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
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
export class RequeteUpdateComponent implements OnInit {
  isSaving = false;

  inscriptionsSharedCollection: IInscription[] = [];
  administrateursSharedCollection: IAdministrateur[] = [];

  editForm = this.fb.group({
    id: [],
    objet: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(70)]],
    description: [null, [Validators.required, Validators.minLength(2)]],
    statut: [],
    traiter: [],
    dateCreation: [],
    dateModification: [],
    etudiant: [],
    validateur: [],
  });

  constructor(
    protected requeteService: RequeteService,
    protected inscriptionService: InscriptionService,
    protected administrateurService: AdministrateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requete }) => {
      this.updateForm(requete);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requete = this.createFromForm();
    if (requete.id !== undefined) {
      this.subscribeToSaveResponse(this.requeteService.update(requete));
    } else {
      this.subscribeToSaveResponse(this.requeteService.create(requete));
    }
  }

  trackInscriptionById(index: number, item: IInscription): number {
    return item.id!;
  }

  trackAdministrateurById(index: number, item: IAdministrateur): number {
    return item.id!;
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
      statut: requete.statut,
      traiter: requete.traiter,
      dateCreation: requete.dateCreation,
      dateModification: requete.dateModification,
      etudiant: requete.etudiant,
      validateur: requete.validateur,
    });

    this.inscriptionsSharedCollection = this.inscriptionService.addInscriptionToCollectionIfMissing(
      this.inscriptionsSharedCollection,
      requete.etudiant
    );
    this.administrateursSharedCollection = this.administrateurService.addAdministrateurToCollectionIfMissing(
      this.administrateursSharedCollection,
      requete.validateur
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

    this.administrateurService
      .query()
      .pipe(map((res: HttpResponse<IAdministrateur[]>) => res.body ?? []))
      .pipe(
        map((administrateurs: IAdministrateur[]) =>
          this.administrateurService.addAdministrateurToCollectionIfMissing(administrateurs, this.editForm.get('validateur')!.value)
        )
      )
      .subscribe((administrateurs: IAdministrateur[]) => (this.administrateursSharedCollection = administrateurs));
  }

  protected createFromForm(): IRequete {
    return {
      ...new Requete(),
      id: this.editForm.get(['id'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      description: this.editForm.get(['description'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      traiter: this.editForm.get(['traiter'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      validateur: this.editForm.get(['validateur'])!.value,
    };
  }
}
