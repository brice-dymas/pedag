import { IUser } from 'app/entities/user/user.model';
import { Grade } from 'app/entities/enumerations/grade.model';
import { Statut } from 'app/entities/enumerations/statut.model';

export interface IEnseignant {
  id?: number;
  diplome?: string;
  nom?: string;
  prenom?: string | null;
  grade?: Grade;
  email?: string;
  telephone?: string;
  statut?: Statut;
  photoContentType?: string | null;
  photo?: string | null;
  user?: IUser | null;
}

export class Enseignant implements IEnseignant {
  constructor(
    public id?: number,
    public diplome?: string,
    public nom?: string,
    public prenom?: string | null,
    public grade?: Grade,
    public email?: string,
    public telephone?: string,
    public statut?: Statut,
    public photoContentType?: string | null,
    public photo?: string | null,
    public user?: IUser | null
  ) {}
}

export function getEnseignantIdentifier(enseignant: IEnseignant): number | undefined {
  return enseignant.id;
}
