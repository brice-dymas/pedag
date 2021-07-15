import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesSemestreComponent } from './notes-semestre.component';

describe('NotesSemestreComponent', () => {
  let component: NotesSemestreComponent;
  let fixture: ComponentFixture<NotesSemestreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotesSemestreComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotesSemestreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
