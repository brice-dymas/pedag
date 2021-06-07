import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoursFiliere, CoursFiliere } from '../cours-filiere.model';
import { CoursFiliereService } from '../service/cours-filiere.service';

@Injectable({ providedIn: 'root' })
export class CoursFiliereRoutingResolveService implements Resolve<ICoursFiliere> {
  constructor(protected service: CoursFiliereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICoursFiliere> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((coursFiliere: HttpResponse<CoursFiliere>) => {
          if (coursFiliere.body) {
            return of(coursFiliere.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CoursFiliere());
  }
}
