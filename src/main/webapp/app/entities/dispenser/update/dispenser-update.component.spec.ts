jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DispenserService } from '../service/dispenser.service';
import { IDispenser, Dispenser } from '../dispenser.model';
import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/annee-academique/service/annee-academique.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { DispenserUpdateComponent } from './dispenser-update.component';

describe('Component Tests', () => {
  describe('Dispenser Management Update Component', () => {
    let comp: DispenserUpdateComponent;
    let fixture: ComponentFixture<DispenserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dispenserService: DispenserService;
    let anneeAcademiqueService: AnneeAcademiqueService;
    let enseignantService: EnseignantService;
    let matiereService: MatiereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DispenserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DispenserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DispenserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dispenserService = TestBed.inject(DispenserService);
      anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);
      enseignantService = TestBed.inject(EnseignantService);
      matiereService = TestBed.inject(MatiereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AnneeAcademique query and add missing value', () => {
        const dispenser: IDispenser = { id: 456 };
        const anneeAcademique: IAnneeAcademique = { id: 1310 };
        dispenser.anneeAcademique = anneeAcademique;

        const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 29959 }];
        spyOn(anneeAcademiqueService, 'query').and.returnValue(of(new HttpResponse({ body: anneeAcademiqueCollection })));
        const additionalAnneeAcademiques = [anneeAcademique];
        const expectedCollection: IAnneeAcademique[] = [...additionalAnneeAcademiques, ...anneeAcademiqueCollection];
        spyOn(anneeAcademiqueService, 'addAnneeAcademiqueToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        expect(anneeAcademiqueService.query).toHaveBeenCalled();
        expect(anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing).toHaveBeenCalledWith(
          anneeAcademiqueCollection,
          ...additionalAnneeAcademiques
        );
        expect(comp.anneeAcademiquesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Enseignant query and add missing value', () => {
        const dispenser: IDispenser = { id: 456 };
        const enseignant: IEnseignant = { id: 36645 };
        dispenser.enseignant = enseignant;

        const enseignantCollection: IEnseignant[] = [{ id: 17586 }];
        spyOn(enseignantService, 'query').and.returnValue(of(new HttpResponse({ body: enseignantCollection })));
        const additionalEnseignants = [enseignant];
        const expectedCollection: IEnseignant[] = [...additionalEnseignants, ...enseignantCollection];
        spyOn(enseignantService, 'addEnseignantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        expect(enseignantService.query).toHaveBeenCalled();
        expect(enseignantService.addEnseignantToCollectionIfMissing).toHaveBeenCalledWith(enseignantCollection, ...additionalEnseignants);
        expect(comp.enseignantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Matiere query and add missing value', () => {
        const dispenser: IDispenser = { id: 456 };
        const matiere: IMatiere = { id: 7271 };
        dispenser.matiere = matiere;

        const matiereCollection: IMatiere[] = [{ id: 29330 }];
        spyOn(matiereService, 'query').and.returnValue(of(new HttpResponse({ body: matiereCollection })));
        const additionalMatieres = [matiere];
        const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
        spyOn(matiereService, 'addMatiereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        expect(matiereService.query).toHaveBeenCalled();
        expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
        expect(comp.matieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dispenser: IDispenser = { id: 456 };
        const anneeAcademique: IAnneeAcademique = { id: 66801 };
        dispenser.anneeAcademique = anneeAcademique;
        const enseignant: IEnseignant = { id: 36155 };
        dispenser.enseignant = enseignant;
        const matiere: IMatiere = { id: 54446 };
        dispenser.matiere = matiere;

        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dispenser));
        expect(comp.anneeAcademiquesSharedCollection).toContain(anneeAcademique);
        expect(comp.enseignantsSharedCollection).toContain(enseignant);
        expect(comp.matieresSharedCollection).toContain(matiere);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dispenser = { id: 123 };
        spyOn(dispenserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dispenser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dispenserService.update).toHaveBeenCalledWith(dispenser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dispenser = new Dispenser();
        spyOn(dispenserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dispenser }));
        saveSubject.complete();

        // THEN
        expect(dispenserService.create).toHaveBeenCalledWith(dispenser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dispenser = { id: 123 };
        spyOn(dispenserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dispenser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dispenserService.update).toHaveBeenCalledWith(dispenser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAnneeAcademiqueById', () => {
        it('Should return tracked AnneeAcademique primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAnneeAcademiqueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEnseignantById', () => {
        it('Should return tracked Enseignant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEnseignantById(0, entity);
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
