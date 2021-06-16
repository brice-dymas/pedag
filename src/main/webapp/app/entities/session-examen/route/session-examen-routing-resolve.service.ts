import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISessionExamen, SessionExamen } from '../session-examen.model';
import { SessionExamenService } from '../service/session-examen.service';

@Injectable({ providedIn: 'root' })
export class SessionExamenRoutingResolveService implements Resolve<ISessionExamen> {
  constructor(protected service: SessionExamenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISessionExamen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sessionExamen: HttpResponse<SessionExamen>) => {
          if (sessionExamen.body) {
            return of(sessionExamen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SessionExamen());
  }
}
