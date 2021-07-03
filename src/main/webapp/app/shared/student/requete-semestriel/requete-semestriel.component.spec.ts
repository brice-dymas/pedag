import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequeteSemestrielComponent } from './requete-semestriel.component';

describe('RequeteSemestrielComponent', () => {
  let component: RequeteSemestrielComponent;
  let fixture: ComponentFixture<RequeteSemestrielComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequeteSemestrielComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequeteSemestrielComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
