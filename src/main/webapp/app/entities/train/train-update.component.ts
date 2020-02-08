import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITrain, Train } from 'app/shared/model/train.model';
import { TrainService } from './train.service';
import { IPassenger } from 'app/shared/model/passenger.model';
import { PassengerService } from 'app/entities/passenger/passenger.service';

@Component({
  selector: 'jhi-train-update',
  templateUrl: './train-update.component.html'
})
export class TrainUpdateComponent implements OnInit {
  isSaving = false;
  passengers: IPassenger[] = [];

  editForm = this.fb.group({
    id: [],
    model: [],
    name: [],
    connection: [],
    passengers: []
  });

  constructor(
    protected trainService: TrainService,
    protected passengerService: PassengerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ train }) => {
      this.updateForm(train);

      this.passengerService.query().subscribe((res: HttpResponse<IPassenger[]>) => (this.passengers = res.body || []));
    });
  }

  updateForm(train: ITrain): void {
    this.editForm.patchValue({
      id: train.id,
      model: train.model,
      name: train.name,
      connection: train.connection,
      passengers: train.passengers
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const train = this.createFromForm();
    if (train.id !== undefined) {
      this.subscribeToSaveResponse(this.trainService.update(train));
    } else {
      this.subscribeToSaveResponse(this.trainService.create(train));
    }
  }

  private createFromForm(): ITrain {
    return {
      ...new Train(),
      id: this.editForm.get(['id'])!.value,
      model: this.editForm.get(['model'])!.value,
      name: this.editForm.get(['name'])!.value,
      connection: this.editForm.get(['connection'])!.value,
      passengers: this.editForm.get(['passengers'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrain>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPassenger): any {
    return item.id;
  }

  getSelected(selectedVals: IPassenger[], option: IPassenger): IPassenger {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
