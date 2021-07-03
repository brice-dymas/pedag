import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesRattrapageComponent } from './notes-rattrapage.component';

describe('NotesRattrapageComponent', () => {
  let component: NotesRattrapageComponent;
  let fixture: ComponentFixture<NotesRattrapageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotesRattrapageComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotesRattrapageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
