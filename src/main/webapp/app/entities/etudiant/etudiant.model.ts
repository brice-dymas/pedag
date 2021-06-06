import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IEtudiant {
  id?: number;
  matricule?: string;
  nom?: string;
  dateNaissance?: dayjs.Dayjs;
  prenom?: string | null;
  email?: string;
  telephone?: string;
  photoContentType?: string | null;
  photo?: string | null;
  user?: IUser | null;
}

export class Etudiant implements IEtudiant {
  constructor(
    public id?: number,
    public matricule?: string,
    public nom?: string,
    public dateNaissance?: dayjs.Dayjs,
    public prenom?: string | null,
    public email?: string,
    public telephone?: string,
    public photoContentType?: string | null,
    public photo?: string | null,
    public user?: IUser | null
  ) {}
}

export function getEtudiantIdentifier(etudiant: IEtudiant): number | undefined {
  return etudiant.id;
}
