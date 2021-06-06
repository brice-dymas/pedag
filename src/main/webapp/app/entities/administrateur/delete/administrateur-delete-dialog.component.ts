import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdministrateur } from '../administrateur.model';
import { AdministrateurService } from '../service/administrateur.service';

@Component({
  templateUrl: './administrateur-delete-dialog.component.html',
})
export class AdministrateurDeleteDialogComponent {
  administrateur?: IAdministrateur;

  constructor(protected administrateurService: AdministrateurService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.administrateurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
