import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesProfComponent } from './notes-prof.component';

describe('NotesProfComponent', () => {
  let component: NotesProfComponent;
  let fixture: ComponentFixture<NotesProfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotesProfComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotesProfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
