import * as dayjs from 'dayjs';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';

export interface IInscription {
  id?: number;
  date?: dayjs.Dayjs;
  etudiant?: IEtudiant | null;
  filiere?: IFiliere | null;
  anneeAcademique?: IAnneeAcademique | null;
}

export class Inscription implements IInscription {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public etudiant?: IEtudiant | null,
    public filiere?: IFiliere | null,
    public anneeAcademique?: IAnneeAcademique | null
  ) {}
}

export function getInscriptionIdentifier(inscription: IInscription): number | undefined {
  return inscription.id;
}
