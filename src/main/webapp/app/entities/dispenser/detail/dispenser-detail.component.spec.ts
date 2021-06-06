import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DispenserDetailComponent } from './dispenser-detail.component';

describe('Component Tests', () => {
  describe('Dispenser Management Detail Component', () => {
    let comp: DispenserDetailComponent;
    let fixture: ComponentFixture<DispenserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DispenserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dispenser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DispenserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DispenserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dispenser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dispenser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
