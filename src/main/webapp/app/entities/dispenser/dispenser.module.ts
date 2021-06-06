import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DispenserComponent } from './list/dispenser.component';
import { DispenserDetailComponent } from './detail/dispenser-detail.component';
import { DispenserUpdateComponent } from './update/dispenser-update.component';
import { DispenserDeleteDialogComponent } from './delete/dispenser-delete-dialog.component';
import { DispenserRoutingModule } from './route/dispenser-routing.module';

@NgModule({
  imports: [SharedModule, DispenserRoutingModule],
  declarations: [DispenserComponent, DispenserDetailComponent, DispenserUpdateComponent, DispenserDeleteDialogComponent],
  entryComponents: [DispenserDeleteDialogComponent],
})
export class DispenserModule {}
