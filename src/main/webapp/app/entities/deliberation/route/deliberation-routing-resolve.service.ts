import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliberation, Deliberation } from '../deliberation.model';
import { DeliberationService } from '../service/deliberation.service';

@Injectable({ providedIn: 'root' })
export class DeliberationRoutingResolveService implements Resolve<IDeliberation> {
  constructor(protected service: DeliberationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliberation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliberation: HttpResponse<Deliberation>) => {
          if (deliberation.body) {
            return of(deliberation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Deliberation());
  }
}
