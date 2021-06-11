import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatieresProfComponent } from './matieres-prof.component';

describe('MatieresProfComponent', () => {
  let component: MatieresProfComponent;
  let fixture: ComponentFixture<MatieresProfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MatieresProfComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatieresProfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
