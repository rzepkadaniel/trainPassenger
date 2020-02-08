import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrain } from 'app/shared/model/train.model';
import { TrainService } from './train.service';
import { TrainDeleteDialogComponent } from './train-delete-dialog.component';

@Component({
  selector: 'jhi-train',
  templateUrl: './train.component.html'
})
export class TrainComponent implements OnInit, OnDestroy {
  trains?: ITrain[];
  eventSubscriber?: Subscription;

  constructor(protected trainService: TrainService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.trainService.query().subscribe((res: HttpResponse<ITrain[]>) => (this.trains = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTrains();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITrain): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTrains(): void {
    this.eventSubscriber = this.eventManager.subscribe('trainListModification', () => this.loadAll());
  }

  delete(train: ITrain): void {
    const modalRef = this.modalService.open(TrainDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.train = train;
  }
}
