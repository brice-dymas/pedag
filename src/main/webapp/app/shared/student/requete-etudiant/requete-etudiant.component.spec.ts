import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequeteEtudiantComponent } from './requete-etudiant.component';

describe('RequeteEtudiantComponent', () => {
  let component: RequeteEtudiantComponent;
  let fixture: ComponentFixture<RequeteEtudiantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequeteEtudiantComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequeteEtudiantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
