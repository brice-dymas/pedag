import { NgModule } from '@angular/core';
import { DashboardComponent } from 'app/entities/etudiant/dashboard/dashboard.component';
import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { RequeteEtudiantComponent } from './student/requete-etudiant/requete-etudiant.component';
import { MatieresEtudiantComponent } from './student/matieres-etudiant/matieres-etudiant.component';
import { NotesEtudiantComponent } from './student/notes-etudiant/notes-etudiant.component';
import { RouterModule } from '@angular/router';
import { ProfDashboardComponent } from './prof/prof-dashboard/prof-dashboard.component';
import { NotesProfComponent } from './prof/notes-prof/notes-prof.component';
import { MatieresProfComponent } from './prof/matieres-prof/matieres-prof.component';

@NgModule({
  imports: [SharedLibsModule, RouterModule],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    DashboardComponent,
    RequeteEtudiantComponent,
    MatieresEtudiantComponent,
    NotesEtudiantComponent,
    ProfDashboardComponent,
    NotesProfComponent,
    MatieresProfComponent,
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    DashboardComponent,
    RequeteEtudiantComponent,
    MatieresEtudiantComponent,
    NotesEtudiantComponent,
    ProfDashboardComponent,
    NotesProfComponent,
    MatieresProfComponent,
  ],
})
export class SharedModule {}
