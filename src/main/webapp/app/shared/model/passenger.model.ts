import { ITrain } from 'app/shared/model/train.model';

export interface IPassenger {
  id?: number;
  name?: string;
  lastName?: string;
  cardId?: number;
  trains?: ITrain[];
}

export class Passenger implements IPassenger {
  constructor(public id?: number, public name?: string, public lastName?: string, public cardId?: number, public trains?: ITrain[]) {}
}
