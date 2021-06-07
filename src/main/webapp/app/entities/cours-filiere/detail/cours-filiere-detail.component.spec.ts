import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoursFiliereDetailComponent } from './cours-filiere-detail.component';

describe('Component Tests', () => {
  describe('CoursFiliere Management Detail Component', () => {
    let comp: CoursFiliereDetailComponent;
    let fixture: ComponentFixture<CoursFiliereDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CoursFiliereDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ coursFiliere: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CoursFiliereDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CoursFiliereDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load coursFiliere on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.coursFiliere).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
