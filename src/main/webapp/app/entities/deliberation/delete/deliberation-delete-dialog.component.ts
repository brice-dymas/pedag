import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliberation } from '../deliberation.model';
import { DeliberationService } from '../service/deliberation.service';

@Component({
  templateUrl: './deliberation-delete-dialog.component.html',
})
export class DeliberationDeleteDialogComponent {
  deliberation?: IDeliberation;

  constructor(protected deliberationService: DeliberationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliberationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
