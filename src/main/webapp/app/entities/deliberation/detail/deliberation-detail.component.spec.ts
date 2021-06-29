import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliberationDetailComponent } from './deliberation-detail.component';

describe('Component Tests', () => {
  describe('Deliberation Management Detail Component', () => {
    let comp: DeliberationDetailComponent;
    let fixture: ComponentFixture<DeliberationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DeliberationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ deliberation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DeliberationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeliberationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load deliberation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.deliberation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
