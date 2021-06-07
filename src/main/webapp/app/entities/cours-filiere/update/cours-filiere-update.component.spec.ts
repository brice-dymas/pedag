jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CoursFiliereService } from '../service/cours-filiere.service';
import { ICoursFiliere, CoursFiliere } from '../cours-filiere.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { CoursFiliereUpdateComponent } from './cours-filiere-update.component';

describe('Component Tests', () => {
  describe('CoursFiliere Management Update Component', () => {
    let comp: CoursFiliereUpdateComponent;
    let fixture: ComponentFixture<CoursFiliereUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let coursFiliereService: CoursFiliereService;
    let filiereService: FiliereService;
    let matiereService: MatiereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CoursFiliereUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CoursFiliereUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CoursFiliereUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      coursFiliereService = TestBed.inject(CoursFiliereService);
      filiereService = TestBed.inject(FiliereService);
      matiereService = TestBed.inject(MatiereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Filiere query and add missing value', () => {
        const coursFiliere: ICoursFiliere = { id: 456 };
        const filiere: IFiliere = { id: 75138 };
        coursFiliere.filiere = filiere;

        const filiereCollection: IFiliere[] = [{ id: 26551 }];
        spyOn(filiereService, 'query').and.returnValue(of(new HttpResponse({ body: filiereCollection })));
        const additionalFilieres = [filiere];
        const expectedCollection: IFiliere[] = [...additionalFilieres, ...filiereCollection];
        spyOn(filiereService, 'addFiliereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ coursFiliere });
        comp.ngOnInit();

        expect(filiereService.query).toHaveBeenCalled();
        expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, ...additionalFilieres);
        expect(comp.filieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Matiere query and add missing value', () => {
        const coursFiliere: ICoursFiliere = { id: 456 };
        const matiere: IMatiere = { id: 90590 };
        coursFiliere.matiere = matiere;

        const matiereCollection: IMatiere[] = [{ id: 61389 }];
        spyOn(matiereService, 'query').and.returnValue(of(new HttpResponse({ body: matiereCollection })));
        const additionalMatieres = [matiere];
        const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
        spyOn(matiereService, 'addMatiereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ coursFiliere });
        comp.ngOnInit();

        expect(matiereService.query).toHaveBeenCalled();
        expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
        expect(comp.matieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const coursFiliere: ICoursFiliere = { id: 456 };
        const filiere: IFiliere = { id: 84356 };
        coursFiliere.filiere = filiere;
        const matiere: IMatiere = { id: 4633 };
        coursFiliere.matiere = matiere;

        activatedRoute.data = of({ coursFiliere });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(coursFiliere));
        expect(comp.filieresSharedCollection).toContain(filiere);
        expect(comp.matieresSharedCollection).toContain(matiere);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const coursFiliere = { id: 123 };
        spyOn(coursFiliereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ coursFiliere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: coursFiliere }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(coursFiliereService.update).toHaveBeenCalledWith(coursFiliere);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const coursFiliere = new CoursFiliere();
        spyOn(coursFiliereService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ coursFiliere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: coursFiliere }));
        saveSubject.complete();

        // THEN
        expect(coursFiliereService.create).toHaveBeenCalledWith(coursFiliere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const coursFiliere = { id: 123 };
        spyOn(coursFiliereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ coursFiliere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(coursFiliereService.update).toHaveBeenCalledWith(coursFiliere);
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

      describe('trackMatiereById', () => {
        it('Should return tracked Matiere primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMatiereById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
