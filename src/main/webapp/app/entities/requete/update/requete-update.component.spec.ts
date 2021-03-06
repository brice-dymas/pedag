jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RequeteService } from '../service/requete.service';
import { IRequete, Requete } from '../requete.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';
import { IAdministrateur } from 'app/entities/administrateur/administrateur.model';
import { AdministrateurService } from 'app/entities/administrateur/service/administrateur.service';
import { INote } from 'app/entities/note/note.model';
import { NoteService } from 'app/entities/note/service/note.service';

import { RequeteUpdateComponent } from './requete-update.component';

describe('Component Tests', () => {
  describe('Requete Management Update Component', () => {
    let comp: RequeteUpdateComponent;
    let fixture: ComponentFixture<RequeteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let requeteService: RequeteService;
    let inscriptionService: InscriptionService;
    let administrateurService: AdministrateurService;
    let noteService: NoteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RequeteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RequeteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RequeteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      requeteService = TestBed.inject(RequeteService);
      inscriptionService = TestBed.inject(InscriptionService);
      administrateurService = TestBed.inject(AdministrateurService);
      noteService = TestBed.inject(NoteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Inscription query and add missing value', () => {
        const requete: IRequete = { id: 456 };
        const etudiant: IInscription = { id: 98867 };
        requete.etudiant = etudiant;

        const inscriptionCollection: IInscription[] = [{ id: 40112 }];
        spyOn(inscriptionService, 'query').and.returnValue(of(new HttpResponse({ body: inscriptionCollection })));
        const additionalInscriptions = [etudiant];
        const expectedCollection: IInscription[] = [...additionalInscriptions, ...inscriptionCollection];
        spyOn(inscriptionService, 'addInscriptionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        expect(inscriptionService.query).toHaveBeenCalled();
        expect(inscriptionService.addInscriptionToCollectionIfMissing).toHaveBeenCalledWith(
          inscriptionCollection,
          ...additionalInscriptions
        );
        expect(comp.inscriptionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Administrateur query and add missing value', () => {
        const requete: IRequete = { id: 456 };
        const validateur: IAdministrateur = { id: 49525 };
        requete.validateur = validateur;

        const administrateurCollection: IAdministrateur[] = [{ id: 78279 }];
        spyOn(administrateurService, 'query').and.returnValue(of(new HttpResponse({ body: administrateurCollection })));
        const additionalAdministrateurs = [validateur];
        const expectedCollection: IAdministrateur[] = [...additionalAdministrateurs, ...administrateurCollection];
        spyOn(administrateurService, 'addAdministrateurToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        expect(administrateurService.query).toHaveBeenCalled();
        expect(administrateurService.addAdministrateurToCollectionIfMissing).toHaveBeenCalledWith(
          administrateurCollection,
          ...additionalAdministrateurs
        );
        expect(comp.administrateursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Note query and add missing value', () => {
        const requete: IRequete = { id: 456 };
        const note: INote = { id: 42577 };
        requete.note = note;

        const noteCollection: INote[] = [{ id: 96993 }];
        spyOn(noteService, 'query').and.returnValue(of(new HttpResponse({ body: noteCollection })));
        const additionalNotes = [note];
        const expectedCollection: INote[] = [...additionalNotes, ...noteCollection];
        spyOn(noteService, 'addNoteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        expect(noteService.query).toHaveBeenCalled();
        expect(noteService.addNoteToCollectionIfMissing).toHaveBeenCalledWith(noteCollection, ...additionalNotes);
        expect(comp.notesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const requete: IRequete = { id: 456 };
        const etudiant: IInscription = { id: 52069 };
        requete.etudiant = etudiant;
        const validateur: IAdministrateur = { id: 52468 };
        requete.validateur = validateur;
        const note: INote = { id: 45704 };
        requete.note = note;

        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(requete));
        expect(comp.inscriptionsSharedCollection).toContain(etudiant);
        expect(comp.administrateursSharedCollection).toContain(validateur);
        expect(comp.notesSharedCollection).toContain(note);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const requete = { id: 123 };
        spyOn(requeteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: requete }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(requeteService.update).toHaveBeenCalledWith(requete);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const requete = new Requete();
        spyOn(requeteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: requete }));
        saveSubject.complete();

        // THEN
        expect(requeteService.create).toHaveBeenCalledWith(requete);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const requete = { id: 123 };
        spyOn(requeteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(requeteService.update).toHaveBeenCalledWith(requete);
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

      describe('trackAdministrateurById', () => {
        it('Should return tracked Administrateur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAdministrateurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackNoteById', () => {
        it('Should return tracked Note primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackNoteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
