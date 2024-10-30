import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToCheckoutComponent } from './to-checkout.component';

describe('ToCheckoutComponent', () => {
  let component: ToCheckoutComponent;
  let fixture: ComponentFixture<ToCheckoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToCheckoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ToCheckoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
