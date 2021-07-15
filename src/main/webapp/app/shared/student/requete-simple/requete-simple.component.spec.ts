import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequeteSimpleComponent } from './requete-simple.component';

describe('RequeteSimpleComponent', () => {
  let component: RequeteSimpleComponent;
  let fixture: ComponentFixture<RequeteSimpleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequeteSimpleComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequeteSimpleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
