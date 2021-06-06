jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FiliereService } from '../service/filiere.service';
import { IFiliere, Filiere } from '../filiere.model';

import { FiliereUpdateComponent } from './filiere-update.component';

describe('Component Tests', () => {
  describe('Filiere Management Update Component', () => {
    let comp: FiliereUpdateComponent;
    let fixture: ComponentFixture<FiliereUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let filiereService: FiliereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FiliereUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FiliereUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FiliereUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      filiereService = TestBed.inject(FiliereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const filiere: IFiliere = { id: 456 };

        activatedRoute.data = of({ filiere });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(filiere));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filiere = { id: 123 };
        spyOn(filiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: filiere }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(filiereService.update).toHaveBeenCalledWith(filiere);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filiere = new Filiere();
        spyOn(filiereService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: filiere }));
        saveSubject.complete();

        // THEN
        expect(filiereService.create).toHaveBeenCalledWith(filiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filiere = { id: 123 };
        spyOn(filiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(filiereService.update).toHaveBeenCalledWith(filiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
