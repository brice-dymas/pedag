import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatiere, Matiere } from '../matiere.model';
import { MatiereService } from '../service/matiere.service';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';

@Component({
  selector: 'jhi-matiere-update',
  templateUrl: './matiere-update.component.html',
})
export class MatiereUpdateComponent implements OnInit {
  isSaving = false;

  modulesSharedCollection: IModule[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    code: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],
    credit: [null, [Validators.min(1)]],
    module: [null, Validators.required],
  });

  constructor(
    protected matiereService: MatiereService,
    protected moduleService: ModuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.updateForm(matiere);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matiere = this.createFromForm();
    if (matiere.id !== undefined) {
      this.subscribeToSaveResponse(this.matiereService.update(matiere));
    } else {
      this.subscribeToSaveResponse(this.matiereService.create(matiere));
    }
  }

  trackModuleById(index: number, item: IModule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatiere>>): void {
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

  protected updateForm(matiere: IMatiere): void {
    this.editForm.patchValue({
      id: matiere.id,
      libelle: matiere.libelle,
      code: matiere.code,
      credit: matiere.credit,
      module: matiere.module,
    });

    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing(this.modulesSharedCollection, matiere.module);
  }

  protected loadRelationshipsOptions(): void {
    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(map((modules: IModule[]) => this.moduleService.addModuleToCollectionIfMissing(modules, this.editForm.get('module')!.value)))
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));
  }

  protected createFromForm(): IMatiere {
    return {
      ...new Matiere(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      code: this.editForm.get(['code'])!.value,
      credit: this.editForm.get(['credit'])!.value,
      module: this.editForm.get(['module'])!.value,
    };
  }
}
