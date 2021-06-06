export interface IMatiere {
  id?: number;
  libelle?: string;
  code?: string;
}

export class Matiere implements IMatiere {
  constructor(public id?: number, public libelle?: string, public code?: string) {}
}

export function getMatiereIdentifier(matiere: IMatiere): number | undefined {
  return matiere.id;
}
