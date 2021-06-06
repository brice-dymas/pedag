jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExamenService } from '../service/examen.service';
import { IExamen, Examen } from '../examen.model';

import { ExamenUpdateComponent } from './examen-update.component';

describe('Component Tests', () => {
  describe('Examen Management Update Component', () => {
    let comp: ExamenUpdateComponent;
    let fixture: ComponentFixture<ExamenUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let examenService: ExamenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExamenUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExamenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExamenUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      examenService = TestBed.inject(ExamenService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const examen: IExamen = { id: 456 };

        activatedRoute.data = of({ examen });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(examen));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const examen = { id: 123 };
        spyOn(examenService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ examen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: examen }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(examenService.update).toHaveBeenCalledWith(examen);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const examen = new Examen();
        spyOn(examenService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ examen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: examen }));
        saveSubject.complete();

        // THEN
        expect(examenService.create).toHaveBeenCalledWith(examen);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const examen = { id: 123 };
        spyOn(examenService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ examen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(examenService.update).toHaveBeenCalledWith(examen);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
