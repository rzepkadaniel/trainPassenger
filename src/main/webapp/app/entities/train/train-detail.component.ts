import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrain } from 'app/shared/model/train.model';

@Component({
  selector: 'jhi-train-detail',
  templateUrl: './train-detail.component.html'
})
export class TrainDetailComponent implements OnInit {
  train: ITrain | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ train }) => (this.train = train));
  }

  previousState(): void {
    window.history.back();
  }
}
