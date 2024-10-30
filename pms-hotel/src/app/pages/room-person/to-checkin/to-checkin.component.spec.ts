import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToCheckinComponent } from './to-checkin.component';

describe('ToCheckinComponent', () => {
  let component: ToCheckinComponent;
  let fixture: ComponentFixture<ToCheckinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToCheckinComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ToCheckinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
