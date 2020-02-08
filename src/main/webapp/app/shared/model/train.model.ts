import { IPassenger } from 'app/shared/model/passenger.model';

export interface ITrain {
  id?: number;
  model?: string;
  name?: string;
  connection?: string;
  passengers?: IPassenger[];
}

export class Train implements ITrain {
  constructor(
    public id?: number,
    public model?: string,
    public name?: string,
    public connection?: string,
    public passengers?: IPassenger[]
  ) {}
}
