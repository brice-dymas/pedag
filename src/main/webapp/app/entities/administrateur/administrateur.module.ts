import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AdministrateurComponent } from './list/administrateur.component';
import { AdministrateurDetailComponent } from './detail/administrateur-detail.component';
import { AdministrateurUpdateComponent } from './update/administrateur-update.component';
import { AdministrateurDeleteDialogComponent } from './delete/administrateur-delete-dialog.component';
import { AdministrateurRoutingModule } from './route/administrateur-routing.module';

@NgModule({
  imports: [SharedModule, AdministrateurRoutingModule],
  declarations: [
    AdministrateurComponent,
    AdministrateurDetailComponent,
    AdministrateurUpdateComponent,
    AdministrateurDeleteDialogComponent,
  ],
  entryComponents: [AdministrateurDeleteDialogComponent],
})
export class AdministrateurModule {}
