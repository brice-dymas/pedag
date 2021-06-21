import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourrielEtudiantComponent } from './courriel-etudiant.component';

describe('CourrielEtudiantComponent', () => {
  let component: CourrielEtudiantComponent;
  let fixture: ComponentFixture<CourrielEtudiantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CourrielEtudiantComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourrielEtudiantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
