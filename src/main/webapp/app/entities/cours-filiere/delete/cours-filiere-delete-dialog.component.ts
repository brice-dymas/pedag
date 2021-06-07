import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoursFiliere } from '../cours-filiere.model';
import { CoursFiliereService } from '../service/cours-filiere.service';

@Component({
  templateUrl: './cours-filiere-delete-dialog.component.html',
})
export class CoursFiliereDeleteDialogComponent {
  coursFiliere?: ICoursFiliere;

  constructor(protected coursFiliereService: CoursFiliereService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coursFiliereService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
