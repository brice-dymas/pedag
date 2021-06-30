import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { StatutRequete } from 'app/entities/enumerations/statut-requete.model';
import { IRequete, Requete } from 'app/entities/requete/requete.model';
import { RequeteService } from 'app/entities/requete/service/requete.service';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-rejeter-requete-dialog',
  templateUrl: './rejeter-requete-dialog.component.html',
  styleUrls: ['./rejeter-requete-dialog.component.scss'],
})
export class RejeterRequeteDialogComponent {
  requete?: IRequete;

  constructor(protected requeteService: RequeteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmRejet(item: any): void {
    const req = this.getRequeteForValidation(item);
    req.statut = StatutRequete.NON_FONDE;
    this.requeteService.partialUpdate(req).subscribe(() => this.activeModal.close('deleted'));
  }

  getRequeteForValidation(item: IRequete): IRequete {
    return {
      ...new Requete(),
      id: item.id,
      traiter: true,
      dateModification: dayjs(),
    };
  }
}
