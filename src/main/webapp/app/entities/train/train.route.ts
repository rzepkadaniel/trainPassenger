import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITrain, Train } from 'app/shared/model/train.model';
import { TrainService } from './train.service';
import { TrainComponent } from './train.component';
import { TrainDetailComponent } from './train-detail.component';
import { TrainUpdateComponent } from './train-update.component';

@Injectable({ providedIn: 'root' })
export class TrainResolve implements Resolve<ITrain> {
  constructor(private service: TrainService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((train: HttpResponse<Train>) => {
          if (train.body) {
            return of(train.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Train());
  }
}

export const trainRoute: Routes = [
  {
    path: '',
    component: TrainComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'trainPassengerApp.train.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TrainDetailComponent,
    resolve: {
      train: TrainResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'trainPassengerApp.train.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TrainUpdateComponent,
    resolve: {
      train: TrainResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'trainPassengerApp.train.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TrainUpdateComponent,
    resolve: {
      train: TrainResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'trainPassengerApp.train.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
