import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdministrateurComponent } from '../list/administrateur.component';
import { AdministrateurDetailComponent } from '../detail/administrateur-detail.component';
import { AdministrateurUpdateComponent } from '../update/administrateur-update.component';
import { AdministrateurRoutingResolveService } from './administrateur-routing-resolve.service';

const administrateurRoute: Routes = [
  {
    path: '',
    component: AdministrateurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdministrateurDetailComponent,
    resolve: {
      administrateur: AdministrateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdministrateurUpdateComponent,
    resolve: {
      administrateur: AdministrateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdministrateurUpdateComponent,
    resolve: {
      administrateur: AdministrateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(administrateurRoute)],
  exports: [RouterModule],
})
export class AdministrateurRoutingModule {}
