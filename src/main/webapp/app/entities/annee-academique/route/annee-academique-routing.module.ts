import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnneeAcademiqueComponent } from '../list/annee-academique.component';
import { AnneeAcademiqueDetailComponent } from '../detail/annee-academique-detail.component';
import { AnneeAcademiqueUpdateComponent } from '../update/annee-academique-update.component';
import { AnneeAcademiqueRoutingResolveService } from './annee-academique-routing-resolve.service';

const anneeAcademiqueRoute: Routes = [
  {
    path: '',
    component: AnneeAcademiqueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnneeAcademiqueDetailComponent,
    resolve: {
      anneeAcademique: AnneeAcademiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnneeAcademiqueUpdateComponent,
    resolve: {
      anneeAcademique: AnneeAcademiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnneeAcademiqueUpdateComponent,
    resolve: {
      anneeAcademique: AnneeAcademiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(anneeAcademiqueRoute)],
  exports: [RouterModule],
})
export class AnneeAcademiqueRoutingModule {}
