import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { INote, Note } from 'app/entities/note/note.model';
import { NoteService } from 'app/entities/note/service/note.service';
import { IRequete, Requete } from 'app/entities/requete/requete.model';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class RequeteNoteResolveService implements Resolve<IRequete> {
  constructor(protected service: NoteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRequete> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((notes: HttpResponse<Note>) => {
          if (notes.body) {
            return of({ ...new Requete(), note: notes.body });
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Requete());
  }
}
