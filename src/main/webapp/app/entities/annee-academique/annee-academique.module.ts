import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnneeAcademiqueComponent } from './list/annee-academique.component';
import { AnneeAcademiqueDetailComponent } from './detail/annee-academique-detail.component';
import { AnneeAcademiqueUpdateComponent } from './update/annee-academique-update.component';
import { AnneeAcademiqueDeleteDialogComponent } from './delete/annee-academique-delete-dialog.component';
import { AnneeAcademiqueRoutingModule } from './route/annee-academique-routing.module';

@NgModule({
  imports: [SharedModule, AnneeAcademiqueRoutingModule],
  declarations: [
    AnneeAcademiqueComponent,
    AnneeAcademiqueDetailComponent,
    AnneeAcademiqueUpdateComponent,
    AnneeAcademiqueDeleteDialogComponent,
  ],
  entryComponents: [AnneeAcademiqueDeleteDialogComponent],
})
export class AnneeAcademiqueModule {}
