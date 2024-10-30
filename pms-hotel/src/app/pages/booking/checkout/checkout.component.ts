import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator'; 
import { MatSort } from '@angular/material/sort';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AddCheckoutDialogComponent } from './add-checkout-dialog/add-checkout-dialog.component';
import { MatDialog } from '@angular/material/dialog';
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
  amenities: AmenityRecive[];
}

interface BookingRecive {
  id?: string;
  startDate: string;
  endDate: string;
  personId: string; 
  personName: string;
  status: string;
  statusName: string;
  roomList: RoomRecive[];
}

@Component({
  selector: 'app-Checkout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    MatTableModule,
    MatInputModule,
    MatButtonModule,
    MatSortModule,
    FormsModule,
    MatIconModule,
    MatPaginatorModule,
  ],
  template: `
    <h1>Reservas com Checkout</h1>
    <div class="container mat-elevation-z3">
      <div class="filter-button-container">
        <mat-form-field>
          <mat-label>Filtrar</mat-label>
          <input matInput (keyup)="applyFilter($event)" placeholder="Filtrar Reserva" />
        </mat-form-field>
      </div>
      
      <table mat-table [dataSource]="dataSource" matSort>
        <!-- Nome do Hóspede -->
        <ng-container matColumnDef="personName">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Nome do Hóspede</th>
          <td mat-cell *matCellDef="let booking">{{ booking.personName }}</td>
        </ng-container>

        <!-- Status da Reserva -->
        <ng-container matColumnDef="statusName">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Status</th>
          <td mat-cell *matCellDef="let booking">{{ booking.statusName }}</td>
        </ng-container>

        <!-- Datas da Reserva -->
        <ng-container matColumnDef="startDate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Data de Início</th>
          <td mat-cell *matCellDef="let booking">{{ booking.startDate }}</td>
        </ng-container>

        <ng-container matColumnDef="endDate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Data de Término</th>
          <td mat-cell *matCellDef="let booking">{{ booking.endDate }}</td>
        </ng-container>

        <!-- Quartos -->
        <ng-container matColumnDef="rooms">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Quartos</th>
          <td mat-cell *matCellDef="let booking">
            {{ getRoomNumbers(booking) }}
          </td>
        </ng-container>

        <!-- Comodidades -->
        <ng-container matColumnDef="amenities">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Comodidades</th>
          <td mat-cell *matCellDef="let booking">
            {{ getAmenities(booking) }}
          </td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef class="text-center" style="width: 180px;">Ações</th>
          <td mat-cell *matCellDef="let booking" class="text-center" style="width: 180px;">
            <button mat-button (click)="openAddCheckout(booking)" [disabled]="booking.status === 'E'">Checkout</button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>

      <mat-paginator [pageSizeOptions]="[10, 15, 20]" showFirstLastButtons></mat-paginator>
    </div>
  `,
  styles: [`
    .container {
      margin: 20px;
      border: 2px solid #ccc;
    }
    table {
      width: 100%;
    }
    th.mat-header-cell, td.mat-cell {
      padding: 8px; 
      margin: 0; 
    }
    .text-center {
      text-align: center; 
    }
    .filter-button-container {
      display: flex;              
      justify-content: space-between; 
      align-items: center; 
      margin-bottom: 16px; 
      > button { 
        margin-right: 16px; 
        padding: 15px;
      }
    }
  `]
})
export class CheckoutComponent {
  displayedColumns: string[] = ['personName', 'statusName', 'startDate', 'endDate', 'rooms', 'amenities', 'actions'];

  originalData: BookingRecive[] = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<BookingRecive>(this.originalData);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public dialog: MatDialog, private bookingService: BookingService) {}

  ngOnInit(): void {
    this.fetchBooking();
  }

  fetchBooking() {
    this.bookingService.getAllBookingCheckout().subscribe(
      (response) => {
        this.originalData = response.map((item: any) => {
          return {
            id: item.id,
            startDate: item.startDate,
            endDate: item.endDate,
            personId: item.personId,
            personName: item.personName,
            status: item.status,
            statusName: item.statusName,
            roomList: item.roomList.map((room: any) => ({
              roomId: room.roomId,
              roomNumber: room.roomNumber,
              amenities: room.amenities.map((amenity: any) => ({
                amenitieId: amenity.amenitieId,
                amenitieName: amenity.amenitieName,
              })),
            })),
          } as BookingRecive;
        });
    
        this.dataSource.data = this.originalData;
      },
      (error) => {
        console.error('Erro ao buscar reservas:', error);
        alert('Não foi possível carregar a lista de reservas.');
      },
      () => {
        console.log('Busca de reservas concluída.');
      }
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  openAddCheckout(booking: BookingRecive) {
    const dialogRef = this.dialog.open(AddCheckoutDialogComponent, {
      width: '400px',
      data: booking // Passa a reserva para o diálogo
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.originalData.push(result);
        this.dataSource.data = this.originalData;  
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getRoomNumbers(booking: BookingRecive): string {
    return booking.roomList.map(room => room.roomNumber).join(',');
  }

  getAmenities(booking: BookingRecive): string {
    return booking.roomList
      .map(room => room.amenities.map(amenity => amenity.amenitieName).join(', '))
      .join('; ');
  }
}
