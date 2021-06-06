import * as dayjs from 'dayjs';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface IPieceJointe {
  id?: number;
  libelle?: string;
  contenuContentType?: string;
  contenu?: string;
  dateCreation?: dayjs.Dayjs | null;
  matiere?: IMatiere | null;
}

export class PieceJointe implements IPieceJointe {
  constructor(
    public id?: number,
    public libelle?: string,
    public contenuContentType?: string,
    public contenu?: string,
    public dateCreation?: dayjs.Dayjs | null,
    public matiere?: IMatiere | null
  ) {}
}

export function getPieceJointeIdentifier(pieceJointe: IPieceJointe): number | undefined {
  return pieceJointe.id;
}
