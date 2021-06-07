import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'administrateur',
        data: { pageTitle: 'pedagApp.administrateur.home.title' },
        loadChildren: () => import('./administrateur/administrateur.module').then(m => m.AdministrateurModule),
      },
      {
        path: 'etudiant',
        data: { pageTitle: 'pedagApp.etudiant.home.title' },
        loadChildren: () => import('./etudiant/etudiant.module').then(m => m.EtudiantModule),
      },
      {
        path: 'enseignant',
        data: { pageTitle: 'pedagApp.enseignant.home.title' },
        loadChildren: () => import('./enseignant/enseignant.module').then(m => m.EnseignantModule),
      },
      {
        path: 'filiere',
        data: { pageTitle: 'pedagApp.filiere.home.title' },
        loadChildren: () => import('./filiere/filiere.module').then(m => m.FiliereModule),
      },
      {
        path: 'matiere',
        data: { pageTitle: 'pedagApp.matiere.home.title' },
        loadChildren: () => import('./matiere/matiere.module').then(m => m.MatiereModule),
      },
      {
        path: 'piece-jointe',
        data: { pageTitle: 'pedagApp.pieceJointe.home.title' },
        loadChildren: () => import('./piece-jointe/piece-jointe.module').then(m => m.PieceJointeModule),
      },
      {
        path: 'examen',
        data: { pageTitle: 'pedagApp.examen.home.title' },
        loadChildren: () => import('./examen/examen.module').then(m => m.ExamenModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'pedagApp.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      {
        path: 'requete',
        data: { pageTitle: 'pedagApp.requete.home.title' },
        loadChildren: () => import('./requete/requete.module').then(m => m.RequeteModule),
      },
      {
        path: 'courriel',
        data: { pageTitle: 'pedagApp.courriel.home.title' },
        loadChildren: () => import('./courriel/courriel.module').then(m => m.CourrielModule),
      },
      {
        path: 'dispenser',
        data: { pageTitle: 'pedagApp.dispenser.home.title' },
        loadChildren: () => import('./dispenser/dispenser.module').then(m => m.DispenserModule),
      },
      {
        path: 'inscription',
        data: { pageTitle: 'pedagApp.inscription.home.title' },
        loadChildren: () => import('./inscription/inscription.module').then(m => m.InscriptionModule),
      },
      {
        path: 'annee-academique',
        data: { pageTitle: 'pedagApp.anneeAcademique.home.title' },
        loadChildren: () => import('./annee-academique/annee-academique.module').then(m => m.AnneeAcademiqueModule),
      },
      {
        path: 'cours-filiere',
        data: { pageTitle: 'pedagApp.coursFiliere.home.title' },
        loadChildren: () => import('./cours-filiere/cours-filiere.module').then(m => m.CoursFiliereModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
