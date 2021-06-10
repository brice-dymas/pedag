import { Injectable } from '@angular/core';
import { ROLE_PROF, ROLE_STUDENT, TK_ENS, TK_INS } from 'app/app.constants';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';

@Injectable({ providedIn: 'root' })
export class TokenService {
  constructor(private inscriptionService: InscriptionService, private enseignantService: EnseignantService) {}

  storeInscription(ins: any, remember?: boolean): void {
    if (remember) {
      localStorage.setItem(TK_INS, JSON.stringify(ins));
    } else {
      sessionStorage.setItem(TK_INS, JSON.stringify(ins));
    }
  }

  storeEnseignant(ins: any, remember?: boolean): void {
    if (remember) {
      localStorage.setItem(TK_ENS, JSON.stringify(ins));
    } else {
      sessionStorage.setItem(TK_ENS, JSON.stringify(ins));
    }
  }

  getInscription(): unknown {
    const xs: unknown = JSON.parse(<string>sessionStorage.getItem(TK_INS)) || JSON.parse(<string>localStorage.getItem(TK_INS));
    return xs;
  }

  getEnseignant(): unknown {
    const rs: unknown = JSON.parse(<string>sessionStorage.getItem(TK_ENS)) || JSON.parse(<string>localStorage.getItem(TK_ENS));
    return rs;
  }

  accountStorageProceeed(id: number, rememberMe: boolean, roles: string[]): any {
    if (roles.includes(ROLE_PROF)) {
      this.enseignantService.findByUserId(id).subscribe(ens => {
        this.storeEnseignant(ens.body, rememberMe);
        return ens;
      });
    }
    if (roles.includes(ROLE_STUDENT)) {
      this.inscriptionService.findbyUserId(id).subscribe(ens => {
        this.storeInscription(ens.body, rememberMe);
        return ens;
      });
    }
  }
}
