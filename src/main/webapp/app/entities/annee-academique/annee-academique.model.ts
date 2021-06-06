export interface IAnneeAcademique {
  id?: number;
  libelle?: string;
  current?: boolean | null;
}

export class AnneeAcademique implements IAnneeAcademique {
  constructor(public id?: number, public libelle?: string, public current?: boolean | null) {
    this.current = this.current ?? false;
  }
}

export function getAnneeAcademiqueIdentifier(anneeAcademique: IAnneeAcademique): number | undefined {
  return anneeAcademique.id;
}
