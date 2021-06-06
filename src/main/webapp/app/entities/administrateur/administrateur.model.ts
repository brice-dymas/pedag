import { IUser } from 'app/entities/user/user.model';
import { Grade } from 'app/entities/enumerations/grade.model';

export interface IAdministrateur {
  id?: number;
  nom?: string;
  prenom?: string | null;
  email?: string;
  grade?: Grade;
  user?: IUser | null;
}

export class Administrateur implements IAdministrateur {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string | null,
    public email?: string,
    public grade?: Grade,
    public user?: IUser | null
  ) {}
}

export function getAdministrateurIdentifier(administrateur: IAdministrateur): number | undefined {
  return administrateur.id;
}
