import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DispenserComponent } from '../list/dispenser.component';
import { DispenserDetailComponent } from '../detail/dispenser-detail.component';
import { DispenserUpdateComponent } from '../update/dispenser-update.component';
import { DispenserRoutingResolveService } from './dispenser-routing-resolve.service';

const dispenserRoute: Routes = [
  {
    path: '',
    component: DispenserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DispenserDetailComponent,
    resolve: {
      dispenser: DispenserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DispenserUpdateComponent,
    resolve: {
      dispenser: DispenserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DispenserUpdateComponent,
    resolve: {
      dispenser: DispenserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dispenserRoute)],
  exports: [RouterModule],
})
export class DispenserRoutingModule {}
