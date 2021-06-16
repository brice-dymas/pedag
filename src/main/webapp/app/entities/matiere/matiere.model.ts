export interface IMatiere {
  id?: number;
  libelle?: string;
  code?: string;
  credit?: number | null;
}

export class Matiere implements IMatiere {
  constructor(public id?: number, public libelle?: string, public code?: string, public credit?: number | null) {}
}

export function getMatiereIdentifier(matiere: IMatiere): number | undefined {
  return matiere.id;
}
