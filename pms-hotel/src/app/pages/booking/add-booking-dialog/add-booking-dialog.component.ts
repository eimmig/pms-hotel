import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTabsModule } from '@angular/material/tabs';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { BookingService } from '../../../services/booking.service';

interface Amenity {
  amenitieId: string; 
}

interface Room {
  roomId: string;
  amenities: Amenity[];
}

interface Booking {
  id?: string;
  startDate: string;
  endDate: string;
  personId: string; 
  status: string;
  roomList: Room[];
}

interface AmenityRecive {
  amenitieId: string; 
  amenitieName: string;
}

interface RoomRecive {
  roomId: string;
  roomNumber: string;
}

interface PersonRecive {
  personId: string;
  personName: string;
}

@Component({
  selector: 'app-add-booking-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatCheckboxModule,
    MatTabsModule,
    MatDialogContent
  ],
  template: `
    <mat-dialog-content class="dialog-content">
      <form [formGroup]="bookingForm" (ngSubmit)="onSubmit()" class="form-full">
        <mat-tab-group class="full-width-tab-group">
          
          <!-- Aba para Informações Básicas -->
          <mat-tab label="Informações Básicas">
            <div class="tab-content">
              <mat-form-field appearance="fill">
                <mat-label>Data Inicial</mat-label>
                <input matInput type="date" formControlName="startDate" required />
                <mat-error *ngIf="bookingForm.get('startDate')?.hasError('required')">
                  A data inicial é obrigatória.
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="fill">
                <mat-label>Data Final</mat-label>
                <input matInput type="date" formControlName="endDate" required />
                <mat-error *ngIf="bookingForm.get('endDate')?.hasError('required')">
                  A data final é obrigatória.
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="fill">
                <mat-label>Pessoa</mat-label>
                <mat-select formControlName="personId" required>
                  <mat-option *ngFor="let person of personList" [value]="person.personId">
                    {{ person.personName }}
                  </mat-option>
                </mat-select>
                <mat-error *ngIf="bookingForm.get('personId')?.hasError('required')">
                  A seleção de pessoa é obrigatória.
                </mat-error>
              </mat-form-field>
            </div>
          </mat-tab>

          <mat-tab label="Quartos e Extras">
            <div class="tab-content">
              <mat-form-field appearance="fill">
                <mat-label>Selecione o Quarto</mat-label>
                <mat-select formControlName="roomId" required>
                  <mat-option *ngFor="let room of roomList" [value]="room.roomId">
                    {{ room.roomNumber }}
                  </mat-option>
                </mat-select>
                <mat-error *ngIf="bookingForm.get('roomId')?.hasError('required')">
                  A seleção de quarto é obrigatória.
                </mat-error>
              </mat-form-field>

              <!-- Seleção de Extras -->
              <mat-form-field appearance="fill">
                <mat-label>Selecione o Extra</mat-label>
                <mat-select formControlName="amenities" required>
                  <mat-option value="none">Sem extra</mat-option>
                  <mat-option *ngFor="let amenity of amenityList" [value]="amenity.amenitieId">
                    {{ amenity.amenitieName }}
                  </mat-option>
                </mat-select>
                <mat-error *ngIf="bookingForm.get('amenities')?.hasError('required')">
                  A seleção de extra é obrigatória.
                </mat-error>
              </mat-form-field>
            </div>
          </mat-tab>
        </mat-tab-group>

        <div mat-dialog-actions class="div-btns">
          <button mat-button color="warn" (click)="onCancel()" class="btn-cancelar">Cancelar</button>
          <button mat-button class="btn-adicionar" type="submit" [disabled]="loading || bookingForm.invalid">
            <ng-container *ngIf="loading; else saveText">Salvando...</ng-container>
            <ng-template #saveText>Salvar</ng-template>
          </button>
        </div>
      </form>
    </mat-dialog-content>
  `,
  styles: [`
    .dialog-content {
      min-height: 400px; 
      max-height: 90vh; 
      overflow: hidden;  
      display: flex;
      flex-direction: column;
      align-items: stretch;
      padding: 20px;
    }

    .form-full {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      justify-content: space-between; 
    }

    .full-width-tab-group {
      flex-grow: 1;
    }

    .tab-content {
      display: flex;
      flex-direction: column;
      gap: 16px; 
      flex-grow: 1; 
      overflow: hidden;
    }

    mat-tab-body {
      display: flex;
      flex-direction: column;
      flex-grow: 1; 
      overflow: hidden;
    }

    mat-form-field {
      width: 100%;
    }

    .btn-cancelar {
      background-color: red;
      padding: 5px;
      color: white;
      border-radius: 5px;
      font-size: 15px;
      border: none;
      transition: background-color 0.3s, color 0.3s;
    }
    .btn-cancelar:hover {
      background-color: #ff3333;
    }

    .btn-adicionar {
      background-color: green;
      padding: 5px;
      color: white;
      border-radius: 5px;
      font-size: 15px;
      border: none;
      transition: background-color 0.3s, color 0.3s;
    }
    .btn-adicionar:hover {
      background-color: #00cc00;
    }

    .div-btns {
      display: flex;
      justify-content: space-between;
      margin-top: 16px;
    }
  `]
})
export class AddBookingDialogComponent implements OnInit {
  bookingForm!: FormGroup;
  loading: boolean = false;
  
  personList: PersonRecive[] = [];
  roomList: RoomRecive[] = [];
  amenityList: AmenityRecive[] = [];

  constructor(
    public dialogRef: MatDialogRef<AddBookingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Booking,
    private fb: FormBuilder,
    private bookingService: BookingService
  ) {}

  ngOnInit(): void {
    this.bookingForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      personId: ['', Validators.required],
      roomId: ['', Validators.required],
      amenities: ['none']
    });

    if (this.data) {
      this.bookingForm.patchValue(this.data);
    }

    this.loadPeople();
    this.loadAmenities();

    this.bookingForm.get('startDate')?.valueChanges.subscribe(() => {
      this.loadRooms();
    });
    this.bookingForm.get('endDate')?.valueChanges.subscribe(() => {
      this.loadRooms();
    });
  }

  loadPeople(): void {
    this.bookingService.getPeople().subscribe((people: PersonRecive[]) => {
      this.personList = people;
    });
  }

  loadRooms(): void {
    const { startDate, endDate } = this.bookingForm.value;
  
    const start = new Date(startDate);
    const end = new Date(endDate);
  
    if (!startDate || !endDate) {
      return;
    }
  
    if (isNaN(start.getTime()) || isNaN(end.getTime())) {
      return;
    }
  
    if (start >= end) {
      return;
    }
  
    this.bookingService.getAvailableRooms(startDate, endDate).subscribe((rooms: RoomRecive[]) => {
      this.roomList = rooms;
    });
  }
  

  loadAmenities(): void {
    this.bookingService.getAmenities().subscribe((amenities: AmenityRecive[]) => {
      this.amenityList = amenities;
    });
  }

  onSubmit() {
    if (this.bookingForm.invalid) return;
  
    const bookingData = { ...this.bookingForm.value, id: this.data?.id };
  
    const transformedObject = {
      startDate: this.setTime(new Date(bookingData.startDate), 14, 0), 
      endDate: this.setTime(new Date(bookingData.endDate), 12, 0), 
      personId: bookingData.personId,
      status: "P",
      roomList: [
        {
          roomId: bookingData.roomId,
          amenities: bookingData.amenities !== "none" ? [
            {
              amenitieId: bookingData.amenities 
            }
          ] : [] 
        }
      ]
    };
  
    this.loading = true;
  
    const bookingObservable: Observable<Booking> = bookingData.id
      ? this.bookingService.editBooking(transformedObject)
      : this.bookingService.addBooking(transformedObject);
  
    bookingObservable.subscribe(
      (response) => {
        alert('Reserva salva com sucesso!');
        window.location.reload();
        this.dialogRef.close(response);
      },
      (error) => {
        alert('Erro ao salvar reserva. Tente novamente.');
        this.loading = false;
      }
    );
  }
  
  setTime(date: Date, hours: number, minutes: number): string {
    date.setHours(hours, minutes, 0, 0); 
    return date.toISOString(); 
  }
  
  

  onCancel(): void {
    this.dialogRef.close();
  }
}
