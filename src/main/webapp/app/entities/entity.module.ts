import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'passenger',
        loadChildren: () => import('./passenger/passenger.module').then(m => m.TrainPassengerPassengerModule)
      },
      {
        path: 'train',
        loadChildren: () => import('./train/train.module').then(m => m.TrainPassengerTrainModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TrainPassengerEntityModule {}
