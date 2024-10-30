import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCheckoutDialogComponent } from './add-checkout-dialog.component';

describe('AddCheckoutDialogComponent', () => {
  let component: AddCheckoutDialogComponent;
  let fixture: ComponentFixture<AddCheckoutDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCheckoutDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCheckoutDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
