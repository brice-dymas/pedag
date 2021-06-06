import * as dayjs from 'dayjs';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { IAdministrateur } from 'app/entities/administrateur/administrateur.model';
import { StatutRequete } from 'app/entities/enumerations/statut-requete.model';

export interface IRequete {
  id?: number;
  objet?: string;
  description?: string;
  statut?: StatutRequete | null;
  traiter?: boolean | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  etudiant?: IInscription | null;
  validateur?: IAdministrateur | null;
}

export class Requete implements IRequete {
  constructor(
    public id?: number,
    public objet?: string,
    public description?: string,
    public statut?: StatutRequete | null,
    public traiter?: boolean | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public etudiant?: IInscription | null,
    public validateur?: IAdministrateur | null
  ) {
    this.traiter = this.traiter ?? false;
  }
}

export function getRequeteIdentifier(requete: IRequete): number | undefined {
  return requete.id;
}