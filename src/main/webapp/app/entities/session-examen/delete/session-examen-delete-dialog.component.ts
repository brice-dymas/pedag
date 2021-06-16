import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISessionExamen } from '../session-examen.model';
import { SessionExamenService } from '../service/session-examen.service';

@Component({
  templateUrl: './session-examen-delete-dialog.component.html',
})
export class SessionExamenDeleteDialogComponent {
  sessionExamen?: ISessionExamen;

  constructor(protected sessionExamenService: SessionExamenService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sessionExamenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
