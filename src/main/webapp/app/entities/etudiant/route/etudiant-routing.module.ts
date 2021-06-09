import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatieresEtudiantComponent } from 'app/shared/student/matieres-etudiant/matieres-etudiant.component';
import { RequeteEtudiantComponent } from 'app/shared/student/requete-etudiant/requete-etudiant.component';
import { EtudiantComponent } from '../list/etudiant.component';
import { EtudiantDetailComponent } from '../detail/etudiant-detail.component';
import { EtudiantUpdateComponent } from '../update/etudiant-update.component';
import { EtudiantRoutingResolveService } from './etudiant-routing-resolve.service';

const etudiantRoute: Routes = [
  {
    path: '',
    component: EtudiantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'mes-requetes',
    component: RequeteEtudiantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'mes-cours',
    component: MatieresEtudiantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EtudiantDetailComponent,
    resolve: {
      etudiant: EtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EtudiantUpdateComponent,
    resolve: {
      etudiant: EtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EtudiantUpdateComponent,
    resolve: {
      etudiant: EtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(etudiantRoute)],
  exports: [RouterModule],
})
export class EtudiantRoutingModule {}
