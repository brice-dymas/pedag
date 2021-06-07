import { IFiliere } from 'app/entities/filiere/filiere.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface ICoursFiliere {
  id?: number;
  quotaHoraire?: number | null;
  filiere?: IFiliere | null;
  matiere?: IMatiere | null;
}

export class CoursFiliere implements ICoursFiliere {
  constructor(
    public id?: number,
    public quotaHoraire?: number | null,
    public filiere?: IFiliere | null,
    public matiere?: IMatiere | null
  ) {}
}

export function getCoursFiliereIdentifier(coursFiliere: ICoursFiliere): number | undefined {
  return coursFiliere.id;
}
