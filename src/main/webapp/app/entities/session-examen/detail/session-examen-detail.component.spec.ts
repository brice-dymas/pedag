import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SessionExamenDetailComponent } from './session-examen-detail.component';

describe('Component Tests', () => {
  describe('SessionExamen Management Detail Component', () => {
    let comp: SessionExamenDetailComponent;
    let fixture: ComponentFixture<SessionExamenDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SessionExamenDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sessionExamen: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SessionExamenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SessionExamenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sessionExamen on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sessionExamen).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
