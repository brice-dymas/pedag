import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DeliberationComponent } from './list/deliberation.component';
import { DeliberationDetailComponent } from './detail/deliberation-detail.component';
import { DeliberationUpdateComponent } from './update/deliberation-update.component';
import { DeliberationDeleteDialogComponent } from './delete/deliberation-delete-dialog.component';
import { DeliberationRoutingModule } from './route/deliberation-routing.module';

@NgModule({
  imports: [SharedModule, DeliberationRoutingModule],
  declarations: [DeliberationComponent, DeliberationDetailComponent, DeliberationUpdateComponent, DeliberationDeleteDialogComponent],
  entryComponents: [DeliberationDeleteDialogComponent],
})
export class DeliberationModule {}
