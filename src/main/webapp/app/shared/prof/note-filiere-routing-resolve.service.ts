import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { INoteFiliere, NoteFiliere } from 'app/shared/prof/note-filiere.model';
import { NoteFiliereService } from 'app/shared/prof/note-filiere.service';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class NoteFiliereRoutingResolveService implements Resolve<INoteFiliere> {
  constructor(protected service: NoteFiliereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INoteFiliere> | Observable<never> {
    const idMatiere = route.params['idm'];
    const idSession = route.params['ids'];
    if (idSession && idMatiere) {
      return this.service.getFormulaireNoteByDispenserAndSession(idMatiere, idSession).pipe(
        mergeMap((note: HttpResponse<INoteFiliere>) => {
          if (note.body) {
            return of(note.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NoteFiliere());
  }
}
