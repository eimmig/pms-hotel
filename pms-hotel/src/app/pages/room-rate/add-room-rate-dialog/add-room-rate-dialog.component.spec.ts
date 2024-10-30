import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRoomRateDialogComponent } from './add-room-rate-dialog.component';

describe('AddRoomRateDialogComponent', () => {
  let component: AddRoomRateDialogComponent;
  let fixture: ComponentFixture<AddRoomRateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRoomRateDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRoomRateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
