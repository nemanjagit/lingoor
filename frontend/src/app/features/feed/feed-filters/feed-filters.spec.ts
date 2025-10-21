import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedFilters } from './feed-filters';

describe('FeedFilters', () => {
  let component: FeedFilters;
  let fixture: ComponentFixture<FeedFilters>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedFilters]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedFilters);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
