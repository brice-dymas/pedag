import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePjMatiereComponent } from './update-pj-matiere.component';

describe('UpdatePjMatiereComponent', () => {
  let component: UpdatePjMatiereComponent;
  let fixture: ComponentFixture<UpdatePjMatiereComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdatePjMatiereComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdatePjMatiereComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
