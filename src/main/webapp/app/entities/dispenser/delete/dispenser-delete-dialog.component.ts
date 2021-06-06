import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDispenser } from '../dispenser.model';
import { DispenserService } from '../service/dispenser.service';

@Component({
  templateUrl: './dispenser-delete-dialog.component.html',
})
export class DispenserDeleteDialogComponent {
  dispenser?: IDispenser;

  constructor(protected dispenserService: DispenserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dispenserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
