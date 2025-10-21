import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'shortDefinition'
})
export class ShortDefinitionPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}
