import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatieresProfComponent } from 'app/shared/prof/matieres-prof/matieres-prof.component';
import { NotesProfComponent } from 'app/shared/prof/notes-prof/notes-prof.component';
import { EnseignantComponent } from '../list/enseignant.component';
import { EnseignantDetailComponent } from '../detail/enseignant-detail.component';
import { EnseignantUpdateComponent } from '../update/enseignant-update.component';
import { EnseignantRoutingResolveService } from './enseignant-routing-resolve.service';

const enseignantRoute: Routes = [
  {
    path: '',
    component: EnseignantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnseignantDetailComponent,
    resolve: {
      enseignant: EnseignantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnseignantUpdateComponent,
    resolve: {
      enseignant: EnseignantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnseignantUpdateComponent,
    resolve: {
      enseignant: EnseignantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'mes-notes',
    component: NotesProfComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'mes-cours',
    component: MatieresProfComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(enseignantRoute)],
  exports: [RouterModule],
})
export class EnseignantRoutingModule {}
