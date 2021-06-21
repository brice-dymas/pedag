import { MoisAnnee } from 'app/entities/enumerations/mois-annee.model';

export interface ISessionExamen {
  id?: number;
  libelle?: string | null;
  mois?: MoisAnnee;
  annee?: number;
  actif?: boolean | null;
}

export class SessionExamen implements ISessionExamen {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public mois?: MoisAnnee,
    public annee?: number,
    public actif?: boolean | null
  ) {
    this.actif = this.actif ?? false;
  }
}

export function getSessionExamenIdentifier(sessionExamen: ISessionExamen): number | undefined {
  return sessionExamen.id;
}
