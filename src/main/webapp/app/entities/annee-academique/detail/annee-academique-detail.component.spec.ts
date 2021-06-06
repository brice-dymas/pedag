import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnneeAcademiqueDetailComponent } from './annee-academique-detail.component';

describe('Component Tests', () => {
  describe('AnneeAcademique Management Detail Component', () => {
    let comp: AnneeAcademiqueDetailComponent;
    let fixture: ComponentFixture<AnneeAcademiqueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnneeAcademiqueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ anneeAcademique: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AnneeAcademiqueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnneeAcademiqueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load anneeAcademique on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.anneeAcademique).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
