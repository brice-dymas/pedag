import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SessionExamenComponent } from '../list/session-examen.component';
import { SessionExamenDetailComponent } from '../detail/session-examen-detail.component';
import { SessionExamenUpdateComponent } from '../update/session-examen-update.component';
import { SessionExamenRoutingResolveService } from './session-examen-routing-resolve.service';

const sessionExamenRoute: Routes = [
  {
    path: '',
    component: SessionExamenComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SessionExamenDetailComponent,
    resolve: {
      sessionExamen: SessionExamenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SessionExamenUpdateComponent,
    resolve: {
      sessionExamen: SessionExamenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SessionExamenUpdateComponent,
    resolve: {
      sessionExamen: SessionExamenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sessionExamenRoute)],
  exports: [RouterModule],
})
export class SessionExamenRoutingModule {}
