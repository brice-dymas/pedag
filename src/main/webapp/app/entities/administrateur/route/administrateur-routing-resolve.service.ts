import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdministrateur, Administrateur } from '../administrateur.model';
import { AdministrateurService } from '../service/administrateur.service';

@Injectable({ providedIn: 'root' })
export class AdministrateurRoutingResolveService implements Resolve<IAdministrateur> {
  constructor(protected service: AdministrateurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdministrateur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((administrateur: HttpResponse<Administrateur>) => {
          if (administrateur.body) {
            return of(administrateur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Administrateur());
  }
}
