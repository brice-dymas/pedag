jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NoteService } from '../service/note.service';
import { INote, Note } from '../note.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { IExamen } from 'app/entities/examen/examen.model';
import { ExamenService } from 'app/entities/examen/service/examen.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';

import { NoteUpdateComponent } from './note-update.component';

describe('Component Tests', () => {
  describe('Note Management Update Component', () => {
    let comp: NoteUpdateComponent;
    let fixture: ComponentFixture<NoteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let noteService: NoteService;
    let inscriptionService: InscriptionService;
    let examenService: ExamenService;
    let matiereService: MatiereService;
    let enseignantService: EnseignantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NoteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NoteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NoteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      noteService = TestBed.inject(NoteService);
      inscriptionService = TestBed.inject(InscriptionService);
      examenService = TestBed.inject(ExamenService);
      matiereService = TestBed.inject(MatiereService);
      enseignantService = TestBed.inject(EnseignantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Inscription query and add missing value', () => {
        const note: INote = { id: 456 };
        const etudiant: IInscription = { id: 98867 };
        note.etudiant = etudiant;

        const inscriptionCollection: IInscription[] = [{ id: 40112 }];
        spyOn(inscriptionService, 'query').and.returnValue(of(new HttpResponse({ body: inscriptionCollection })));
        const additionalInscriptions = [etudiant];
        const expectedCollection: IInscription[] = [...additionalInscriptions, ...inscriptionCollection];
        spyOn(inscriptionService, 'addInscriptionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(inscriptionService.query).toHaveBeenCalled();
        expect(inscriptionService.addInscriptionToCollectionIfMissing).toHaveBeenCalledWith(
          inscriptionCollection,
          ...additionalInscriptions
        );
        expect(comp.inscriptionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Examen query and add missing value', () => {
        const note: INote = { id: 456 };
        const examen: IExamen = { id: 48818 };
        note.examen = examen;

        const examenCollection: IExamen[] = [{ id: 34658 }];
        spyOn(examenService, 'query').and.returnValue(of(new HttpResponse({ body: examenCollection })));
        const additionalExamen = [examen];
        const expectedCollection: IExamen[] = [...additionalExamen, ...examenCollection];
        spyOn(examenService, 'addExamenToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(examenService.query).toHaveBeenCalled();
        expect(examenService.addExamenToCollectionIfMissing).toHaveBeenCalledWith(examenCollection, ...additionalExamen);
        expect(comp.examenSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Matiere query and add missing value', () => {
        const note: INote = { id: 456 };
        const matiere: IMatiere = { id: 94282 };
        note.matiere = matiere;

        const matiereCollection: IMatiere[] = [{ id: 84595 }];
        spyOn(matiereService, 'query').and.returnValue(of(new HttpResponse({ body: matiereCollection })));
        const additionalMatieres = [matiere];
        const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
        spyOn(matiereService, 'addMatiereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(matiereService.query).toHaveBeenCalled();
        expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
        expect(comp.matieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Enseignant query and add missing value', () => {
        const note: INote = { id: 456 };
        const enseignant: IEnseignant = { id: 9241 };
        note.enseignant = enseignant;

        const enseignantCollection: IEnseignant[] = [{ id: 31391 }];
        spyOn(enseignantService, 'query').and.returnValue(of(new HttpResponse({ body: enseignantCollection })));
        const additionalEnseignants = [enseignant];
        const expectedCollection: IEnseignant[] = [...additionalEnseignants, ...enseignantCollection];
        spyOn(enseignantService, 'addEnseignantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(enseignantService.query).toHaveBeenCalled();
        expect(enseignantService.addEnseignantToCollectionIfMissing).toHaveBeenCalledWith(enseignantCollection, ...additionalEnseignants);
        expect(comp.enseignantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const note: INote = { id: 456 };
        const etudiant: IInscription = { id: 52069 };
        note.etudiant = etudiant;
        const examen: IExamen = { id: 75424 };
        note.examen = examen;
        const matiere: IMatiere = { id: 43014 };
        note.matiere = matiere;
        const enseignant: IEnseignant = { id: 84209 };
        note.enseignant = enseignant;

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(note));
        expect(comp.inscriptionsSharedCollection).toContain(etudiant);
        expect(comp.examenSharedCollection).toContain(examen);
        expect(comp.matieresSharedCollection).toContain(matiere);
        expect(comp.enseignantsSharedCollection).toContain(enseignant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const note = { id: 123 };
        spyOn(noteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ note });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: note }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(noteService.update).toHaveBeenCalledWith(note);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const note = new Note();
        spyOn(noteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ note });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: note }));
        saveSubject.complete();

        // THEN
        expect(noteService.create).toHaveBeenCalledWith(note);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const note = { id: 123 };
        spyOn(noteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ note });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(noteService.update).toHaveBeenCalledWith(note);
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

      describe('trackExamenById', () => {
        it('Should return tracked Examen primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExamenById(0, entity);
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

      describe('trackEnseignantById', () => {
        it('Should return tracked Enseignant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEnseignantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
