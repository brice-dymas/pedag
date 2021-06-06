import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';

@Component({
  templateUrl: './annee-academique-delete-dialog.component.html',
})
export class AnneeAcademiqueDeleteDialogComponent {
  anneeAcademique?: IAnneeAcademique;

  constructor(protected anneeAcademiqueService: AnneeAcademiqueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.anneeAcademiqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
