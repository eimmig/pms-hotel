import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { RoomService } from '../../../services/room.service';
import { Room } from '../../../models/room';
import { RoomTypeIdName } from '../../../models/roomTypeIdName';

interface Status {
  id: string;
  name: string;
}

@Component({
  selector: 'app-add-room-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatDialogContent
  ],
  template: `
    <mat-dialog-content>
      <form [formGroup]="roomForm" (ngSubmit)="onSubmit()">
        <mat-form-field appearance="fill">
          <mat-label>Número</mat-label>
          <input matInput formControlName="number" required />
          <mat-error *ngIf="roomForm.get('number')?.hasError('required')">O número não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Status</mat-label>
          <mat-select formControlName="status" required>
            <mat-option *ngFor="let stats of status" [value]="stats.id">
              {{ stats.name }}
            </mat-option>
          </mat-select>
          <mat-error *ngIf="roomForm.get('status')?.hasError('required')">O status não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Categoria</mat-label>
          <mat-select formControlName="roomTypeId" required>
            <mat-option *ngFor="let roomType of roomTypes" [value]="roomType.id">
              {{ roomType.name }}
            </mat-option>
          </mat-select>
          <mat-error *ngIf="roomForm.get('roomTypeId')?.hasError('required')">A categoria não pode estar em branco.</mat-error>
        </mat-form-field>

        <div mat-dialog-actions class="div-btns">
          <button mat-button color="warn" (click)="onCancel()" class="btn-cancelar">Cancelar</button>
          <button mat-button class="btn-adicionar" type="submit" [disabled]="roomForm.invalid">Salvar</button>
        </div>
      </form>
    </mat-dialog-content>
  `,
  styles: [`
    mat-form-field {
      width: 100%; 
      margin-bottom: 16px;
    }

    button {
      min-width: 100px; 
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
    }
  `]
})
export class AddRoomDialogComponent implements OnInit {
  roomForm!: FormGroup;
  loading: boolean = false;
  roomTypes: RoomTypeIdName[] = [];
  status: Status[] = [
    { id: 'A', name: 'Disponível' },
    { id: 'O', name: 'Ocupado' },
    { id: 'M', name: 'Manutenção' },
    { id: 'I', name: 'Inativo' },
  ];

  constructor(
    public dialogRef: MatDialogRef<AddRoomDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Room,
    private fb: FormBuilder,
    private roomService: RoomService
  ) {}

  ngOnInit(): void {
    this.roomForm = this.fb.group({
      number: ['', Validators.required],
      roomTypeId: [null, Validators.required],
      status: ['', Validators.required]
    });

    this.loadRoomTypes();

    if (this.data) {
      this.roomForm.patchValue({
        number: this.data.number,
        roomTypeId: this.data.roomTypeId,
        status: this.data.status,
      });
    }
  }

  loadRoomTypes() {
    this.roomService.getRoomTypesSelect().subscribe(
      (roomTypes: RoomTypeIdName[]) => {
        this.roomTypes = roomTypes; 
      },
      (error) => {
        console.error('Erro ao carregar as categorias:', error);
        alert('Não foi possível carregar as categorias.');
      }
    );
  }

  onSubmit() {
    if (this.roomForm.invalid) return;
    const roomData = { ...this.roomForm.value, id: this.data?.id };

    const roomObservable: Observable<Room> = roomData.id
      ? this.roomService.editRoom(roomData)
      : this.roomService.addRoom(roomData);

    roomObservable.subscribe(
      (response) => {
        alert('Quarto salvo com sucesso!');
        window.location.reload();
        this.dialogRef.close(response);
      },
      (error) => {
        alert('Erro ao salvar quarto. Tente novamente.');
      }
    );
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
