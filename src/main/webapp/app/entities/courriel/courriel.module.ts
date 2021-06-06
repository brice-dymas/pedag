import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourrielComponent } from './list/courriel.component';
import { CourrielDetailComponent } from './detail/courriel-detail.component';
import { CourrielUpdateComponent } from './update/courriel-update.component';
import { CourrielDeleteDialogComponent } from './delete/courriel-delete-dialog.component';
import { CourrielRoutingModule } from './route/courriel-routing.module';

@NgModule({
  imports: [SharedModule, CourrielRoutingModule],
  declarations: [CourrielComponent, CourrielDetailComponent, CourrielUpdateComponent, CourrielDeleteDialogComponent],
  entryComponents: [CourrielDeleteDialogComponent],
})
export class CourrielModule {}
