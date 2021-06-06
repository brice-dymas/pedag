export interface IFiliere {
  id?: number;
  libelle?: string;
  sigle?: string;
}

export class Filiere implements IFiliere {
  constructor(public id?: number, public libelle?: string, public sigle?: string) {}
}

export function getFiliereIdentifier(filiere: IFiliere): number | undefined {
  return filiere.id;
}
