import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnneeAcademique, AnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';

@Injectable({ providedIn: 'root' })
export class AnneeAcademiqueRoutingResolveService implements Resolve<IAnneeAcademique> {
  constructor(protected service: AnneeAcademiqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnneeAcademique> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((anneeAcademique: HttpResponse<AnneeAcademique>) => {
          if (anneeAcademique.body) {
            return of(anneeAcademique.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnneeAcademique());
  }
}
