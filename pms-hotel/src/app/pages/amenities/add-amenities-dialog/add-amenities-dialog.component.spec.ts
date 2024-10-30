import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAmenitiesDialogComponent } from './add-amenities-dialog.component';

describe('AddAmenitiesDialogComponent', () => {
  let component: AddAmenitiesDialogComponent;
  let fixture: ComponentFixture<AddAmenitiesDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddAmenitiesDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddAmenitiesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
