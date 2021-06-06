jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourrielService } from '../service/courriel.service';
import { ICourriel, Courriel } from '../courriel.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';

import { CourrielUpdateComponent } from './courriel-update.component';

describe('Component Tests', () => {
  describe('Courriel Management Update Component', () => {
    let comp: CourrielUpdateComponent;
    let fixture: ComponentFixture<CourrielUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courrielService: CourrielService;
    let inscriptionService: InscriptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourrielUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourrielUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourrielUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courrielService = TestBed.inject(CourrielService);
      inscriptionService = TestBed.inject(InscriptionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Inscription query and add missing value', () => {
        const courriel: ICourriel = { id: 456 };
        const etudiant: IInscription = { id: 30155 };
        courriel.etudiant = etudiant;

        const inscriptionCollection: IInscription[] = [{ id: 83415 }];
        spyOn(inscriptionService, 'query').and.returnValue(of(new HttpResponse({ body: inscriptionCollection })));
        const additionalInscriptions = [etudiant];
        const expectedCollection: IInscription[] = [...additionalInscriptions, ...inscriptionCollection];
        spyOn(inscriptionService, 'addInscriptionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ courriel });
        comp.ngOnInit();

        expect(inscriptionService.query).toHaveBeenCalled();
        expect(inscriptionService.addInscriptionToCollectionIfMissing).toHaveBeenCalledWith(
          inscriptionCollection,
          ...additionalInscriptions
        );
        expect(comp.inscriptionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const courriel: ICourriel = { id: 456 };
        const etudiant: IInscription = { id: 74751 };
        courriel.etudiant = etudiant;

        activatedRoute.data = of({ courriel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(courriel));
        expect(comp.inscriptionsSharedCollection).toContain(etudiant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courriel = { id: 123 };
        spyOn(courrielService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courriel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courriel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courrielService.update).toHaveBeenCalledWith(courriel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courriel = new Courriel();
        spyOn(courrielService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courriel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: courriel }));
        saveSubject.complete();

        // THEN
        expect(courrielService.create).toHaveBeenCalledWith(courriel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const courriel = { id: 123 };
        spyOn(courrielService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ courriel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courrielService.update).toHaveBeenCalledWith(courriel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInscriptionById', () => {
        it('Should return tracked Inscription primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInscriptionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
