import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRequeteNoteComponent } from './new-requete-note.component';

describe('NewRequeteNoteComponent', () => {
  let component: NewRequeteNoteComponent;
  let fixture: ComponentFixture<NewRequeteNoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewRequeteNoteComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewRequeteNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
