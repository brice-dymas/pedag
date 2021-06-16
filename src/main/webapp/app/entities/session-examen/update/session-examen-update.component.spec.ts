jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SessionExamenService } from '../service/session-examen.service';
import { ISessionExamen, SessionExamen } from '../session-examen.model';

import { SessionExamenUpdateComponent } from './session-examen-update.component';

describe('Component Tests', () => {
  describe('SessionExamen Management Update Component', () => {
    let comp: SessionExamenUpdateComponent;
    let fixture: ComponentFixture<SessionExamenUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sessionExamenService: SessionExamenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SessionExamenUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SessionExamenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SessionExamenUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sessionExamenService = TestBed.inject(SessionExamenService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const sessionExamen: ISessionExamen = { id: 456 };

        activatedRoute.data = of({ sessionExamen });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sessionExamen));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sessionExamen = { id: 123 };
        spyOn(sessionExamenService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sessionExamen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sessionExamen }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sessionExamenService.update).toHaveBeenCalledWith(sessionExamen);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sessionExamen = new SessionExamen();
        spyOn(sessionExamenService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sessionExamen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sessionExamen }));
        saveSubject.complete();

        // THEN
        expect(sessionExamenService.create).toHaveBeenCalledWith(sessionExamen);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sessionExamen = { id: 123 };
        spyOn(sessionExamenService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sessionExamen });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sessionExamenService.update).toHaveBeenCalledWith(sessionExamen);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
