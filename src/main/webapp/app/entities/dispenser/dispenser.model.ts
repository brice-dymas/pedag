import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { Semestre } from 'app/entities/enumerations/semestre.model';

export interface IDispenser {
  id?: number;
  semestre?: Semestre;
  actif?: boolean | null;
  anneeAcademique?: IAnneeAcademique | null;
  enseignant?: IEnseignant | null;
  matiere?: IMatiere | null;
}

export class Dispenser implements IDispenser {
  constructor(
    public id?: number,
    public semestre?: Semestre,
    public actif?: boolean | null,
    public anneeAcademique?: IAnneeAcademique | null,
    public enseignant?: IEnseignant | null,
    public matiere?: IMatiere | null
  ) {
    this.actif = this.actif ?? false;
  }
}

export function getDispenserIdentifier(dispenser: IDispenser): number | undefined {
  return dispenser.id;
}
