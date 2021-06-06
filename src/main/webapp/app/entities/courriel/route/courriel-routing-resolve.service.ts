import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourriel, Courriel } from '../courriel.model';
import { CourrielService } from '../service/courriel.service';

@Injectable({ providedIn: 'root' })
export class CourrielRoutingResolveService implements Resolve<ICourriel> {
  constructor(protected service: CourrielService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourriel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courriel: HttpResponse<Courriel>) => {
          if (courriel.body) {
            return of(courriel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Courriel());
  }
}
