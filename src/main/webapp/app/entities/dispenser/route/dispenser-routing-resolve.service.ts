import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDispenser, Dispenser } from '../dispenser.model';
import { DispenserService } from '../service/dispenser.service';

@Injectable({ providedIn: 'root' })
export class DispenserRoutingResolveService implements Resolve<IDispenser> {
  constructor(protected service: DispenserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDispenser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dispenser: HttpResponse<Dispenser>) => {
          if (dispenser.body) {
            return of(dispenser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dispenser());
  }
}
