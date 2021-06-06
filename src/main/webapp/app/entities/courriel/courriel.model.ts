import * as dayjs from 'dayjs';
import { IInscription } from 'app/entities/inscription/inscription.model';

export interface ICourriel {
  id?: number;
  sender?: string;
  receiver?: string;
  objet?: string;
  message?: string;
  dateCreation?: dayjs.Dayjs | null;
  etudiant?: IInscription | null;
}

export class Courriel implements ICourriel {
  constructor(
    public id?: number,
    public sender?: string,
    public receiver?: string,
    public objet?: string,
    public message?: string,
    public dateCreation?: dayjs.Dayjs | null,
    public etudiant?: IInscription | null
  ) {}
}

export function getCourrielIdentifier(courriel: ICourriel): number | undefined {
  return courriel.id;
}
