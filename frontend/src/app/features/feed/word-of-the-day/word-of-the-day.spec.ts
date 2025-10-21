import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WordOfTheDay } from './word-of-the-day';

describe('WordOfTheDay', () => {
  let component: WordOfTheDay;
  let fixture: ComponentFixture<WordOfTheDay>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WordOfTheDay]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WordOfTheDay);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
