jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ModuleService } from '../service/module.service';
import { IModule, Module } from '../module.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';

import { ModuleUpdateComponent } from './module-update.component';

describe('Component Tests', () => {
  describe('Module Management Update Component', () => {
    let comp: ModuleUpdateComponent;
    let fixture: ComponentFixture<ModuleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let moduleService: ModuleService;
    let filiereService: FiliereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ModuleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ModuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ModuleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      moduleService = TestBed.inject(ModuleService);
      filiereService = TestBed.inject(FiliereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Filiere query and add missing value', () => {
        const module: IModule = { id: 456 };
        const filiere: IFiliere = { id: 71737 };
        module.filiere = filiere;

        const filiereCollection: IFiliere[] = [{ id: 10020 }];
        spyOn(filiereService, 'query').and.returnValue(of(new HttpResponse({ body: filiereCollection })));
        const additionalFilieres = [filiere];
        const expectedCollection: IFiliere[] = [...additionalFilieres, ...filiereCollection];
        spyOn(filiereService, 'addFiliereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ module });
        comp.ngOnInit();

        expect(filiereService.query).toHaveBeenCalled();
        expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, ...additionalFilieres);
        expect(comp.filieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const module: IModule = { id: 456 };
        const filiere: IFiliere = { id: 99464 };
        module.filiere = filiere;

        activatedRoute.data = of({ module });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(module));
        expect(comp.filieresSharedCollection).toContain(filiere);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const module = { id: 123 };
        spyOn(moduleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ module });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: module }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(moduleService.update).toHaveBeenCalledWith(module);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const module = new Module();
        spyOn(moduleService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ module });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: module }));
        saveSubject.complete();

        // THEN
        expect(moduleService.create).toHaveBeenCalledWith(module);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const module = { id: 123 };
        spyOn(moduleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ module });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(moduleService.update).toHaveBeenCalledWith(module);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFiliereById', () => {
        it('Should return tracked Filiere primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFiliereById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
