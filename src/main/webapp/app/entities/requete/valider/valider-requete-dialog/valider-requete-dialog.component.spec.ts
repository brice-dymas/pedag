import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValiderRequeteDialogComponent } from './valider-requete-dialog.component';

describe('ValiderRequeteDialogComponent', () => {
  let component: ValiderRequeteDialogComponent;
  let fixture: ComponentFixture<ValiderRequeteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ValiderRequeteDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ValiderRequeteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
