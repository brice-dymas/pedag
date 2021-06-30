import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejeterRequeteDialogComponent } from './rejeter-requete-dialog.component';

describe('RejeterRequeteDialogComponent', () => {
  let component: RejeterRequeteDialogComponent;
  let fixture: ComponentFixture<RejeterRequeteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RejeterRequeteDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejeterRequeteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
