import { MoisAnnee } from 'app/entities/enumerations/mois-annee.model';

export interface ISessionExamen {
  id?: number;
  libelle?: string | null;
  mois?: MoisAnnee;
  annee?: number;
}

export class SessionExamen implements ISessionExamen {
  constructor(public id?: number, public libelle?: string | null, public mois?: MoisAnnee, public annee?: number) {}
}

export function getSessionExamenIdentifier(sessionExamen: ISessionExamen): number | undefined {
  return sessionExamen.id;
}
