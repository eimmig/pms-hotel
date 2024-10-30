import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRoomTypeDialogComponent } from './add-room-type-dialog.component';

describe('AddRoomTypeDialogComponent', () => {
  let component: AddRoomTypeDialogComponent;
  let fixture: ComponentFixture<AddRoomTypeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRoomTypeDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRoomTypeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
