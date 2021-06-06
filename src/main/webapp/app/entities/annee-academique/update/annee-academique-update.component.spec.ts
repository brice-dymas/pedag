jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnneeAcademiqueService } from '../service/annee-academique.service';
import { IAnneeAcademique, AnneeAcademique } from '../annee-academique.model';

import { AnneeAcademiqueUpdateComponent } from './annee-academique-update.component';

describe('Component Tests', () => {
  describe('AnneeAcademique Management Update Component', () => {
    let comp: AnneeAcademiqueUpdateComponent;
    let fixture: ComponentFixture<AnneeAcademiqueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let anneeAcademiqueService: AnneeAcademiqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnneeAcademiqueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnneeAcademiqueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnneeAcademiqueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const anneeAcademique: IAnneeAcademique = { id: 456 };

        activatedRoute.data = of({ anneeAcademique });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(anneeAcademique));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const anneeAcademique = { id: 123 };
        spyOn(anneeAcademiqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ anneeAcademique });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: anneeAcademique }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(anneeAcademiqueService.update).toHaveBeenCalledWith(anneeAcademique);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const anneeAcademique = new AnneeAcademique();
        spyOn(anneeAcademiqueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ anneeAcademique });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: anneeAcademique }));
        saveSubject.complete();

        // THEN
        expect(anneeAcademiqueService.create).toHaveBeenCalledWith(anneeAcademique);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const anneeAcademique = { id: 123 };
        spyOn(anneeAcademiqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ anneeAcademique });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(anneeAcademiqueService.update).toHaveBeenCalledWith(anneeAcademique);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
