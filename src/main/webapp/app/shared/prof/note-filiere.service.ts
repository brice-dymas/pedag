import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { INoteFiliere } from 'app/shared/prof/note-filiere.model';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

export type EntityResponseType = HttpResponse<INoteFiliere>;

@Injectable({ providedIn: 'root' })
export class NoteFiliereService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/notes-filled');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  saveNotes(note: INoteFiliere): Observable<EntityResponseType> | any {
    // eslint-disable-next-line no-console
    console.log('Note to save are : ', note);
    return this.http.post<INoteFiliere>(this.resourceUrl, note, { observe: 'response' });
    // return 'OK';
  }

  getFormulaireNoteByDispenserAndSession(idDispenser: number, idSession: number): Observable<EntityResponseType> {
    return this.http.get<INoteFiliere>(`${this.resourceUrl}/matiere/${idDispenser}/session/${idSession}`, { observe: 'response' });
  }
}
