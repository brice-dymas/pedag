jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PieceJointeService } from '../service/piece-jointe.service';
import { IPieceJointe, PieceJointe } from '../piece-jointe.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { PieceJointeUpdateComponent } from './piece-jointe-update.component';

describe('Component Tests', () => {
  describe('PieceJointe Management Update Component', () => {
    let comp: PieceJointeUpdateComponent;
    let fixture: ComponentFixture<PieceJointeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pieceJointeService: PieceJointeService;
    let matiereService: MatiereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PieceJointeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PieceJointeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PieceJointeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pieceJointeService = TestBed.inject(PieceJointeService);
      matiereService = TestBed.inject(MatiereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Matiere query and add missing value', () => {
        const pieceJointe: IPieceJointe = { id: 456 };
        const matiere: IMatiere = { id: 28803 };
        pieceJointe.matiere = matiere;

        const matiereCollection: IMatiere[] = [{ id: 79240 }];
        spyOn(matiereService, 'query').and.returnValue(of(new HttpResponse({ body: matiereCollection })));
        const additionalMatieres = [matiere];
        const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
        spyOn(matiereService, 'addMatiereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pieceJointe });
        comp.ngOnInit();

        expect(matiereService.query).toHaveBeenCalled();
        expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
        expect(comp.matieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pieceJointe: IPieceJointe = { id: 456 };
        const matiere: IMatiere = { id: 97588 };
        pieceJointe.matiere = matiere;

        activatedRoute.data = of({ pieceJointe });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pieceJointe));
        expect(comp.matieresSharedCollection).toContain(matiere);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pieceJointe = { id: 123 };
        spyOn(pieceJointeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pieceJointe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pieceJointe }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pieceJointeService.update).toHaveBeenCalledWith(pieceJointe);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pieceJointe = new PieceJointe();
        spyOn(pieceJointeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pieceJointe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pieceJointe }));
        saveSubject.complete();

        // THEN
        expect(pieceJointeService.create).toHaveBeenCalledWith(pieceJointe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pieceJointe = { id: 123 };
        spyOn(pieceJointeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pieceJointe });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pieceJointeService.update).toHaveBeenCalledWith(pieceJointe);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
