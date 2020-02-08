import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TrainPassengerTestModule } from '../../../test.module';
import { TrainUpdateComponent } from 'app/entities/train/train-update.component';
import { TrainService } from 'app/entities/train/train.service';
import { Train } from 'app/shared/model/train.model';

describe('Component Tests', () => {
  describe('Train Management Update Component', () => {
    let comp: TrainUpdateComponent;
    let fixture: ComponentFixture<TrainUpdateComponent>;
    let service: TrainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TrainPassengerTestModule],
        declarations: [TrainUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TrainUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrainUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TrainService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Train(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Train();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
