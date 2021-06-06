import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourriel } from '../courriel.model';
import { CourrielService } from '../service/courriel.service';

@Component({
  templateUrl: './courriel-delete-dialog.component.html',
})
export class CourrielDeleteDialogComponent {
  courriel?: ICourriel;

  constructor(protected courrielService: CourrielService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courrielService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
