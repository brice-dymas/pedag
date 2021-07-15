import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliberationComponent } from '../list/deliberation.component';
import { DeliberationDetailComponent } from '../detail/deliberation-detail.component';
import { DeliberationUpdateComponent } from '../update/deliberation-update.component';
import { DeliberationRoutingResolveService } from './deliberation-routing-resolve.service';

const deliberationRoute: Routes = [
  {
    path: '',
    component: DeliberationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliberationDetailComponent,
    resolve: {
      deliberation: DeliberationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliberationUpdateComponent,
    resolve: {
      deliberation: DeliberationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliberationUpdateComponent,
    resolve: {
      deliberation: DeliberationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliberationRoute)],
  exports: [RouterModule],
})
export class DeliberationRoutingModule {}
