import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesControleComponent } from './notes-controle.component';

describe('NotesControleComponent', () => {
  let component: NotesControleComponent;
  let fixture: ComponentFixture<NotesControleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotesControleComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotesControleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
