import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SessionExamenComponent } from './list/session-examen.component';
import { SessionExamenDetailComponent } from './detail/session-examen-detail.component';
import { SessionExamenUpdateComponent } from './update/session-examen-update.component';
import { SessionExamenDeleteDialogComponent } from './delete/session-examen-delete-dialog.component';
import { SessionExamenRoutingModule } from './route/session-examen-routing.module';

@NgModule({
  imports: [SharedModule, SessionExamenRoutingModule],
  declarations: [SessionExamenComponent, SessionExamenDetailComponent, SessionExamenUpdateComponent, SessionExamenDeleteDialogComponent],
  entryComponents: [SessionExamenDeleteDialogComponent],
})
export class SessionExamenModule {}
