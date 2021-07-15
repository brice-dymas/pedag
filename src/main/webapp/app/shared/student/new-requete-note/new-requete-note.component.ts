import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { IAdministrateur } from 'app/entities/administrateur/administrateur.model';
import { AdministrateurService } from 'app/entities/administrateur/service/administrateur.service';
import { StatutRequete } from 'app/entities/enumerations/statut-requete.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { INote } from 'app/entities/note/note.model';
import { NoteService } from 'app/entities/note/service/note.service';
import { IRequete, Requete } from 'app/entities/requete/requete.model';
import { RequeteService } from 'app/entities/requete/service/requete.service';
import { Observable, Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-new-requete-note',
  templateUrl: './new-requete-note.component.html',
  styleUrls: ['./new-requete-note.component.scss'],
})
export class NewRequeteNoteComponent implements OnInit, OnDestroy {
  isSaving = false;
  account: any | null = null;
  authSubscription?: Subscription;

  editForm = this.fb.group({
    id: [],
    objet: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(70)]],
    description: [null, [Validators.required, Validators.minLength(2)]],
    statut: [],
    traiter: [],
    dateCreation: [],
    dateModification: [],
    noteAttendue: [null, [Validators.required, Validators.min(0), Validators.max(20)]],
    noteObtenue: [],
    etudiant: [],
    validateur: [],
    note: [],
    session: [],
    typeExamen: [],
    userId: [],
    matiere: [],
  });

  constructor(
    private accountService: AccountService,
    protected requeteService: RequeteService,
    protected inscriptionService: InscriptionService,
    protected administrateurService: AdministrateurService,
    protected noteService: NoteService,
    protected activatedRoute: ActivatedRoute,
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
    // if (requete.id !== undefined) {
    //   this.subscribeToSaveResponse(this.requeteService.update(requete));
    // } else {
    this.subscribeToSaveResponse(this.requeteService.createForEtudiant(requete, this.account.id));
    // }
  }

  trackInscriptionById(index: number, item: IInscription): number {
    return item.id!;
  }

  trackAdministrateurById(index: number, item: IAdministrateur): number {
    return item.id!;
  }

  trackNoteById(index: number, item: INote): number {
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
    const session: string | null = requete.note?.sessionExamen?.libelle ?? null;
    // const obj : string = `Note ${String(requete.note?.sessionExamen?.type)}-${String(requete.note?.sessionExamen?.libelle)}`;
    this.editForm.patchValue({
      id: requete.id,
      objet: 'CONSTESTATION NOTE OBTENUE',
      description: requete.description,
      statut: requete.statut,
      traiter: requete.traiter,
      dateCreation: requete.dateCreation,
      dateModification: requete.dateModification,
      noteAttendue: requete.noteAttendue,
      noteObtenue: requete.note?.moyenne,
      etudiant: requete.etudiant,
      validateur: requete.validateur,
      note: requete.note,
      session: requete.note?.sessionExamen?.libelle,
      typeExamen: requete.note?.sessionExamen?.type,
      userId: null,
      matiere: requete.note?.matiere?.libelle,
    });
  }

  protected createFromForm(): IRequete {
    return {
      ...new Requete(),
      id: this.editForm.get(['id'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      description: this.editForm.get(['description'])!.value,
      statut: StatutRequete.EN_ATTENTE,
      traiter: this.editForm.get(['traiter'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      noteAttendue: this.editForm.get(['noteAttendue'])!.value,
      noteObtenue: this.editForm.get(['noteObtenue'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      validateur: this.editForm.get(['validateur'])!.value,
      note: this.editForm.get(['note'])!.value,
      userId: this.account.id,
    };
  }
}
