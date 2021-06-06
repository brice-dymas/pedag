jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InscriptionService } from '../service/inscription.service';
import { IInscription, Inscription } from '../inscription.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/annee-academique/service/annee-academique.service';

import { InscriptionUpdateComponent } from './inscription-update.component';

describe('Component Tests', () => {
  describe('Inscription Management Update Component', () => {
    let comp: InscriptionUpdateComponent;
    let fixture: ComponentFixture<InscriptionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let inscriptionService: InscriptionService;
    let etudiantService: EtudiantService;
    let filiereService: FiliereService;
    let anneeAcademiqueService: AnneeAcademiqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InscriptionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InscriptionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InscriptionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      inscriptionService = TestBed.inject(InscriptionService);
      etudiantService = TestBed.inject(EtudiantService);
      filiereService = TestBed.inject(FiliereService);
      anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Etudiant query and add missing value', () => {
        const inscription: IInscription = { id: 456 };
        const etudiant: IEtudiant = { id: 43176 };
        inscription.etudiant = etudiant;

        const etudiantCollection: IEtudiant[] = [{ id: 74627 }];
        spyOn(etudiantService, 'query').and.returnValue(of(new HttpResponse({ body: etudiantCollection })));
        const additionalEtudiants = [etudiant];
        const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
        spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        expect(etudiantService.query).toHaveBeenCalled();
        expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
        expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Filiere query and add missing value', () => {
        const inscription: IInscription = { id: 456 };
        const filiere: IFiliere = { id: 87917 };
        inscription.filiere = filiere;

        const filiereCollection: IFiliere[] = [{ id: 53851 }];
        spyOn(filiereService, 'query').and.returnValue(of(new HttpResponse({ body: filiereCollection })));
        const additionalFilieres = [filiere];
        const expectedCollection: IFiliere[] = [...additionalFilieres, ...filiereCollection];
        spyOn(filiereService, 'addFiliereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        expect(filiereService.query).toHaveBeenCalled();
        expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, ...additionalFilieres);
        expect(comp.filieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AnneeAcademique query and add missing value', () => {
        const inscription: IInscription = { id: 456 };
        const anneeAcademique: IAnneeAcademique = { id: 43731 };
        inscription.anneeAcademique = anneeAcademique;

        const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 2440 }];
        spyOn(anneeAcademiqueService, 'query').and.returnValue(of(new HttpResponse({ body: anneeAcademiqueCollection })));
        const additionalAnneeAcademiques = [anneeAcademique];
        const expectedCollection: IAnneeAcademique[] = [...additionalAnneeAcademiques, ...anneeAcademiqueCollection];
        spyOn(anneeAcademiqueService, 'addAnneeAcademiqueToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        expect(anneeAcademiqueService.query).toHaveBeenCalled();
        expect(anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing).toHaveBeenCalledWith(
          anneeAcademiqueCollection,
          ...additionalAnneeAcademiques
        );
        expect(comp.anneeAcademiquesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const inscription: IInscription = { id: 456 };
        const etudiant: IEtudiant = { id: 67837 };
        inscription.etudiant = etudiant;
        const filiere: IFiliere = { id: 27528 };
        inscription.filiere = filiere;
        const anneeAcademique: IAnneeAcademique = { id: 76466 };
        inscription.anneeAcademique = anneeAcademique;

        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(inscription));
        expect(comp.etudiantsSharedCollection).toContain(etudiant);
        expect(comp.filieresSharedCollection).toContain(filiere);
        expect(comp.anneeAcademiquesSharedCollection).toContain(anneeAcademique);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const inscription = { id: 123 };
        spyOn(inscriptionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: inscription }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(inscriptionService.update).toHaveBeenCalledWith(inscription);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const inscription = new Inscription();
        spyOn(inscriptionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: inscription }));
        saveSubject.complete();

        // THEN
        expect(inscriptionService.create).toHaveBeenCalledWith(inscription);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const inscription = { id: 123 };
        spyOn(inscriptionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ inscription });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(inscriptionService.update).toHaveBeenCalledWith(inscription);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEtudiantById', () => {
        it('Should return tracked Etudiant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEtudiantById(0, entity);
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

      describe('trackAnneeAcademiqueById', () => {
        it('Should return tracked AnneeAcademique primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAnneeAcademiqueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
