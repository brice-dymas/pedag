import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatieresEtudiantComponent } from './matieres-etudiant.component';

describe('MatieresEtudiantComponent', () => {
  let component: MatieresEtudiantComponent;
  let fixture: ComponentFixture<MatieresEtudiantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MatieresEtudiantComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatieresEtudiantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
