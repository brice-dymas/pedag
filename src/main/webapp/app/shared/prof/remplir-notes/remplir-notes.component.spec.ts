import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemplirNotesComponent } from './remplir-notes.component';

describe('RemplirNotesComponent', () => {
  let component: RemplirNotesComponent;
  let fixture: ComponentFixture<RemplirNotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RemplirNotesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RemplirNotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
