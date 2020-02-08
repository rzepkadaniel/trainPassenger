import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPassenger } from 'app/shared/model/passenger.model';
import { PassengerService } from './passenger.service';
import { PassengerDeleteDialogComponent } from './passenger-delete-dialog.component';

@Component({
  selector: 'jhi-passenger',
  templateUrl: './passenger.component.html'
})
export class PassengerComponent implements OnInit, OnDestroy {
  passengers?: IPassenger[];
  eventSubscriber?: Subscription;

  constructor(protected passengerService: PassengerService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.passengerService.query().subscribe((res: HttpResponse<IPassenger[]>) => (this.passengers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPassengers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPassenger): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPassengers(): void {
    this.eventSubscriber = this.eventManager.subscribe('passengerListModification', () => this.loadAll());
  }

  delete(passenger: IPassenger): void {
    const modalRef = this.modalService.open(PassengerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.passenger = passenger;
  }
}
