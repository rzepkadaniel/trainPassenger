import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TrainPassengerTestModule } from '../../../test.module';
import { PassengerComponent } from 'app/entities/passenger/passenger.component';
import { PassengerService } from 'app/entities/passenger/passenger.service';
import { Passenger } from 'app/shared/model/passenger.model';

describe('Component Tests', () => {
  describe('Passenger Management Component', () => {
    let comp: PassengerComponent;
    let fixture: ComponentFixture<PassengerComponent>;
    let service: PassengerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TrainPassengerTestModule],
        declarations: [PassengerComponent]
      })
        .overrideTemplate(PassengerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PassengerComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PassengerService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Passenger(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.passengers && comp.passengers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
