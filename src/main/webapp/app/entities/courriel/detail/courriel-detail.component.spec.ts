import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourrielDetailComponent } from './courriel-detail.component';

describe('Component Tests', () => {
  describe('Courriel Management Detail Component', () => {
    let comp: CourrielDetailComponent;
    let fixture: ComponentFixture<CourrielDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourrielDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courriel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourrielDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourrielDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courriel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courriel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
