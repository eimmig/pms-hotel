import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BookingService } from '../../../../services/booking.service'; 
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

interface AmenityRecive {
  amenitieId: string; 
  amenitieName: string;
}

interface RoomRecive {
  roomId: string;
  roomNumber: string;
  amenities: AmenityRecive[];
}

interface BookingRecive {
  id: string;
  startDate: string;
  endDate: string;
  personId: string; 
  personName: string;
  status: string;
  statusName: string;
  roomList: RoomRecive[];
}

interface TotalValueDTO {
  bookingRoom: number;
  amenities: number;
}

@Component({
  imports: [FormsModule, MatFormFieldModule, MatInputModule],  
  standalone: true,
  template: `
    <div class="dialog-content">
      <h1 mat-dialog-title>Checkout</h1>
      <div mat-dialog-content>
        <p><strong>Nome do Hóspede:</strong> {{ data.personName }}</p>
        <p><strong>Quarto:</strong> {{ getRoomNumbers(data) }}</p>
        <p><strong>Data de Início:</strong> {{ data.startDate }}</p>
        <p><strong>Data de Término:</strong> {{ data.endDate }}</p>
        
        <mat-form-field appearance="fill">
          <mat-label>Valor do Quarto</mat-label>
          <input matInput [(ngModel)]="totalValue.bookingRoom" disabled />
        </mat-form-field>
        
        <mat-form-field appearance="fill">
          <mat-label>Valor das Comodidades</mat-label>
          <input matInput [(ngModel)]="totalValue.amenities" disabled />
        </mat-form-field>
        
        <mat-form-field appearance="fill">
          <mat-label>Valor Total</mat-label>
          <input matInput [value]="totalValue.bookingRoom + totalValue.amenities" disabled />
        </mat-form-field>
      </div>
      <div class="dialog-actions">
        <button mat-button class="btn-cancelar" (click)="onClose()">Fechar</button>
        <button mat-button class="btn-adicionar" (click)="onCheckout()">Checkout</button>
      </div>
    </div>
  `,
  styles: [`
    .dialog-content {
      min-width: 400px; 
      max-width: 600px; 
      padding: 20px; 
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .dialog-actions {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
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
  `]
})
export class AddCheckoutDialogComponent implements OnInit {
  totalValue: TotalValueDTO = { bookingRoom: 0, amenities: 0 };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: BookingRecive,
    private bookingService: BookingService,
    private dialogRef: MatDialogRef<AddCheckoutDialogComponent>
  ) {}

  ngOnInit(): void {
    this.fetchCheckoutValue(this.data.id);
  }

  fetchCheckoutValue(bookingId: string) {
    this.bookingService.getCheckoutValue(bookingId).subscribe(
      (response: TotalValueDTO) => {
        this.totalValue = response; // Atualiza o totalValue com a resposta
      },
      (error) => {
        console.error('Erro ao buscar valor de checkout:', error);
      }
    );
  }

  onCheckout() {
    this.bookingService.checkoutBooking(this.data.id).subscribe(
      (response) => {
        alert('Checkout realizado com sucesso!');
        this.dialogRef.close(response);
      },
      (error) => {
        window.location.reload();
        console.error('Erro ao realizar checkout:', error);
      }
    );
  }

  onClose() {
    this.dialogRef.close();
  }

  getRoomNumbers(booking: BookingRecive): string {
    return booking.roomList.map(room => room.roomNumber).join(', ');
  }
}
