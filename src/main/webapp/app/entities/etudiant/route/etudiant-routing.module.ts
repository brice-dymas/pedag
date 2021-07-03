import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourrielEtudiantComponent } from 'app/shared/student/courriel-etudiant/courriel-etudiant.component';
import { MatieresEtudiantComponent } from 'app/shared/student/matieres-etudiant/matieres-etudiant.component';
import { NewRequeteNoteComponent } from 'app/shared/student/new-requete-note/new-requete-note.component';
import { NotesControleComponent } from 'app/shared/student/notes-controle/notes-controle.component';
import { NotesEtudiantComponent } from 'app/shared/student/notes-etudiant/notes-etudiant.component';
import { NotesRattrapageComponent } from 'app/shared/student/notes-rattrapage/notes-rattrapage.component';
import { NotesSemestreComponent } from 'app/shared/student/notes-semestre/notes-semestre.component';
import { RequeteEtudiantComponent } from 'app/shared/student/requete-etudiant/requete-etudiant.component';
import { RequeteNoteResolveService } from 'app/shared/student/requete-note-resolve.service';
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
  {
    path: 'mes-notes',
    component: NotesEtudiantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'notes-controle',
    component: NotesControleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'notes-semestre',
    component: NotesSemestreComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'notes-rattrapage',
    component: NotesRattrapageComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'mes-notes-controle',
    component: NotesControleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'mes-courriels',
    component: CourrielEtudiantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/new-requete-note',
    component: NewRequeteNoteComponent,
    resolve: {
      requete: RequeteNoteResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(etudiantRoute)],
  exports: [RouterModule],
})
export class EtudiantRoutingModule {}
