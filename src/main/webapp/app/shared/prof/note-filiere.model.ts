import { IAnneeAcademique } from 'app/entities/annee-academique/annee-academique.model';
import { IDispenser } from 'app/entities/dispenser/dispenser.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { INote } from 'app/entities/note/note.model';
import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';

export interface INoteFiliere {
  matiere?: IMatiere | null;
  filiere?: IFiliere | null;
  notes?: INote[] | [];
  dispenser?: IDispenser | null;
  enseignant?: IEnseignant | null;
  sessionExamen?: ISessionExamen | null;
  anneeAcademique?: IAnneeAcademique | null;
}

export class NoteFiliere implements INoteFiliere {
  constructor(
    public matiere?: IMatiere | null,
    public filiere?: IFiliere | null,
    public notes?: INote[],
    public dispenser?: IDispenser | null,
    public enseignant?: IEnseignant | null,
    public sessionExamen?: ISessionExamen | null,
    public anneeAcademique?: IAnneeAcademique | null
  ) {}
}
