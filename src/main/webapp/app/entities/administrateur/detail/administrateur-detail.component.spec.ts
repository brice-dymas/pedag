import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdministrateurDetailComponent } from './administrateur-detail.component';

describe('Component Tests', () => {
  describe('Administrateur Management Detail Component', () => {
    let comp: AdministrateurDetailComponent;
    let fixture: ComponentFixture<AdministrateurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AdministrateurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ administrateur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AdministrateurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdministrateurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load administrateur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.administrateur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
