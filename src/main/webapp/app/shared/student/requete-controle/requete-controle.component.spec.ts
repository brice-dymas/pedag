import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequeteControleComponent } from './requete-controle.component';

describe('RequeteControleComponent', () => {
  let component: RequeteControleComponent;
  let fixture: ComponentFixture<RequeteControleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequeteControleComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequeteControleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
