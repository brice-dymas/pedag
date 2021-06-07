import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CoursFiliereComponent } from '../list/cours-filiere.component';
import { CoursFiliereDetailComponent } from '../detail/cours-filiere-detail.component';
import { CoursFiliereUpdateComponent } from '../update/cours-filiere-update.component';
import { CoursFiliereRoutingResolveService } from './cours-filiere-routing-resolve.service';

const coursFiliereRoute: Routes = [
  {
    path: '',
    component: CoursFiliereComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CoursFiliereDetailComponent,
    resolve: {
      coursFiliere: CoursFiliereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CoursFiliereUpdateComponent,
    resolve: {
      coursFiliere: CoursFiliereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CoursFiliereUpdateComponent,
    resolve: {
      coursFiliere: CoursFiliereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(coursFiliereRoute)],
  exports: [RouterModule],
})
export class CoursFiliereRoutingModule {}
