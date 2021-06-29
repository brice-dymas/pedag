jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeliberationService } from '../service/deliberation.service';
import { IDeliberation, Deliberation } from '../deliberation.model';
import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';
import { SessionExamenService } from 'app/entities/session-examen/service/session-examen.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';

import { DeliberationUpdateComponent } from './deliberation-update.component';

describe('Component Tests', () => {
  describe('Deliberation Management Update Component', () => {
    let comp: DeliberationUpdateComponent;
    let fixture: ComponentFixture<DeliberationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let deliberationService: DeliberationService;
    let sessionExamenService: SessionExamenService;
    let filiereService: FiliereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeliberationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeliberationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeliberationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      deliberationService = TestBed.inject(DeliberationService);
      sessionExamenService = TestBed.inject(SessionExamenService);
      filiereService = TestBed.inject(FiliereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SessionExamen query and add missing value', () => {
        const deliberation: IDeliberation = { id: 456 };
        const sessionExamen: ISessionExamen = { id: 86188 };
        deliberation.sessionExamen = sessionExamen;

        const sessionExamenCollection: ISessionExamen[] = [{ id: 422 }];
        spyOn(sessionExamenService, 'query').and.returnValue(of(new HttpResponse({ body: sessionExamenCollection })));
        const additionalSessionExamen = [sessionExamen];
        const expectedCollection: ISessionExamen[] = [...additionalSessionExamen, ...sessionExamenCollection];
        spyOn(sessionExamenService, 'addSessionExamenToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ deliberation });
        comp.ngOnInit();

        expect(sessionExamenService.query).toHaveBeenCalled();
        expect(sessionExamenService.addSessionExamenToCollectionIfMissing).toHaveBeenCalledWith(
          sessionExamenCollection,
          ...additionalSessionExamen
        );
        expect(comp.sessionExamenSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Filiere query and add missing value', () => {
        const deliberation: IDeliberation = { id: 456 };
        const filiere: IFiliere = { id: 61210 };
        deliberation.filiere = filiere;

        const filiereCollection: IFiliere[] = [{ id: 35848 }];
        spyOn(filiereService, 'query').and.returnValue(of(new HttpResponse({ body: filiereCollection })));
        const additionalFilieres = [filiere];
        const expectedCollection: IFiliere[] = [...additionalFilieres, ...filiereCollection];
        spyOn(filiereService, 'addFiliereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ deliberation });
        comp.ngOnInit();

        expect(filiereService.query).toHaveBeenCalled();
        expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, ...additionalFilieres);
        expect(comp.filieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const deliberation: IDeliberation = { id: 456 };
        const sessionExamen: ISessionExamen = { id: 22426 };
        deliberation.sessionExamen = sessionExamen;
        const filiere: IFiliere = { id: 84635 };
        deliberation.filiere = filiere;

        activatedRoute.data = of({ deliberation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(deliberation));
        expect(comp.sessionExamenSharedCollection).toContain(sessionExamen);
        expect(comp.filieresSharedCollection).toContain(filiere);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const deliberation = { id: 123 };
        spyOn(deliberationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ deliberation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: deliberation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(deliberationService.update).toHaveBeenCalledWith(deliberation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const deliberation = new Deliberation();
        spyOn(deliberationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ deliberation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: deliberation }));
        saveSubject.complete();

        // THEN
        expect(deliberationService.create).toHaveBeenCalledWith(deliberation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const deliberation = { id: 123 };
        spyOn(deliberationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ deliberation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(deliberationService.update).toHaveBeenCalledWith(deliberation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSessionExamenById', () => {
        it('Should return tracked SessionExamen primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSessionExamenById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
