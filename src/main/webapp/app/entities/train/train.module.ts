import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TrainPassengerSharedModule } from 'app/shared/shared.module';
import { TrainComponent } from './train.component';
import { TrainDetailComponent } from './train-detail.component';
import { TrainUpdateComponent } from './train-update.component';
import { TrainDeleteDialogComponent } from './train-delete-dialog.component';
import { trainRoute } from './train.route';

@NgModule({
  imports: [TrainPassengerSharedModule, RouterModule.forChild(trainRoute)],
  declarations: [TrainComponent, TrainDetailComponent, TrainUpdateComponent, TrainDeleteDialogComponent],
  entryComponents: [TrainDeleteDialogComponent]
})
export class TrainPassengerTrainModule {}
