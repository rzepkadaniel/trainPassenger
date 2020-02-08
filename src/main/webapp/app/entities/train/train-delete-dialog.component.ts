import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrain } from 'app/shared/model/train.model';
import { TrainService } from './train.service';

@Component({
  templateUrl: './train-delete-dialog.component.html'
})
export class TrainDeleteDialogComponent {
  train?: ITrain;

  constructor(protected trainService: TrainService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trainService.delete(id).subscribe(() => {
      this.eventManager.broadcast('trainListModification');
      this.activeModal.close();
    });
  }
}
