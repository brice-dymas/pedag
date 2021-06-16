jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MatiereService } from '../service/matiere.service';
import { IMatiere, Matiere } from '../matiere.model';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';

import { MatiereUpdateComponent } from './matiere-update.component';

describe('Component Tests', () => {
  describe('Matiere Management Update Component', () => {
    let comp: MatiereUpdateComponent;
    let fixture: ComponentFixture<MatiereUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let matiereService: MatiereService;
    let moduleService: ModuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MatiereUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MatiereUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MatiereUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      matiereService = TestBed.inject(MatiereService);
      moduleService = TestBed.inject(ModuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Module query and add missing value', () => {
        const matiere: IMatiere = { id: 456 };
        const module: IModule = { id: 45232 };
        matiere.module = module;

        const moduleCollection: IModule[] = [{ id: 78753 }];
        spyOn(moduleService, 'query').and.returnValue(of(new HttpResponse({ body: moduleCollection })));
        const additionalModules = [module];
        const expectedCollection: IModule[] = [...additionalModules, ...moduleCollection];
        spyOn(moduleService, 'addModuleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ matiere });
        comp.ngOnInit();

        expect(moduleService.query).toHaveBeenCalled();
        expect(moduleService.addModuleToCollectionIfMissing).toHaveBeenCalledWith(moduleCollection, ...additionalModules);
        expect(comp.modulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const matiere: IMatiere = { id: 456 };
        const module: IModule = { id: 44713 };
        matiere.module = module;

        activatedRoute.data = of({ matiere });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(matiere));
        expect(comp.modulesSharedCollection).toContain(module);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const matiere = { id: 123 };
        spyOn(matiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ matiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: matiere }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(matiereService.update).toHaveBeenCalledWith(matiere);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const matiere = new Matiere();
        spyOn(matiereService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ matiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: matiere }));
        saveSubject.complete();

        // THEN
        expect(matiereService.create).toHaveBeenCalledWith(matiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const matiere = { id: 123 };
        spyOn(matiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ matiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(matiereService.update).toHaveBeenCalledWith(matiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackModuleById', () => {
        it('Should return tracked Module primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackModuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
