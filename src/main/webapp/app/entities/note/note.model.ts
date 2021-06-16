import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { IExamen } from 'app/entities/examen/examen.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';

export interface INote {
  id?: number;
  moyenne?: number;
  observation?: string | null;
  creditMatiere?: number | null;
  creditObtenu?: number | null;
  sessionExamen?: ISessionExamen | null;
  etudiant?: IInscription | null;
  examen?: IExamen | null;
  matiere?: IMatiere | null;
  enseignant?: IEnseignant | null;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public moyenne?: number,
    public observation?: string | null,
    public creditMatiere?: number | null,
    public creditObtenu?: number | null,
    public sessionExamen?: ISessionExamen | null,
    public etudiant?: IInscription | null,
    public examen?: IExamen | null,
    public matiere?: IMatiere | null,
    public enseignant?: IEnseignant | null
  ) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
