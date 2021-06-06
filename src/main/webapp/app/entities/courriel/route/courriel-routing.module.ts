import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourrielComponent } from '../list/courriel.component';
import { CourrielDetailComponent } from '../detail/courriel-detail.component';
import { CourrielUpdateComponent } from '../update/courriel-update.component';
import { CourrielRoutingResolveService } from './courriel-routing-resolve.service';

const courrielRoute: Routes = [
  {
    path: '',
    component: CourrielComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourrielDetailComponent,
    resolve: {
      courriel: CourrielRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourrielUpdateComponent,
    resolve: {
      courriel: CourrielRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourrielUpdateComponent,
    resolve: {
      courriel: CourrielRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courrielRoute)],
  exports: [RouterModule],
})
export class CourrielRoutingModule {}
