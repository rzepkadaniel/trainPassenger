import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrainPassengerTestModule } from '../../../test.module';
import { TrainDetailComponent } from 'app/entities/train/train-detail.component';
import { Train } from 'app/shared/model/train.model';

describe('Component Tests', () => {
  describe('Train Management Detail Component', () => {
    let comp: TrainDetailComponent;
    let fixture: ComponentFixture<TrainDetailComponent>;
    const route = ({ data: of({ train: new Train(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TrainPassengerTestModule],
        declarations: [TrainDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TrainDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrainDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load train on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.train).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
