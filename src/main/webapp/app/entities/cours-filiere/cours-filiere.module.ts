import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CoursFiliereComponent } from './list/cours-filiere.component';
import { CoursFiliereDetailComponent } from './detail/cours-filiere-detail.component';
import { CoursFiliereUpdateComponent } from './update/cours-filiere-update.component';
import { CoursFiliereDeleteDialogComponent } from './delete/cours-filiere-delete-dialog.component';
import { CoursFiliereRoutingModule } from './route/cours-filiere-routing.module';

@NgModule({
  imports: [SharedModule, CoursFiliereRoutingModule],
  declarations: [CoursFiliereComponent, CoursFiliereDetailComponent, CoursFiliereUpdateComponent, CoursFiliereDeleteDialogComponent],
  entryComponents: [CoursFiliereDeleteDialogComponent],
})
export class CoursFiliereModule {}
