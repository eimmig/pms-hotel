import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { RoomTypeService } from '../../../services/room-type.service';
import { RoomType } from '../../../models/roomType';
import { RateIdName } from '../../../models/rateIdName';

@Component({
  selector: 'app-add-room-type-dialog',
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
      <form [formGroup]="roomTypeForm" (ngSubmit)="onSubmit()">
        <mat-form-field appearance="fill">
          <mat-label>Nome</mat-label>
          <input matInput formControlName="name" required />
          <mat-error *ngIf="roomTypeForm.get('name')?.hasError('required')">O nome não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Abreviação</mat-label>
          <input matInput formControlName="abbreviation" required />
          <mat-error *ngIf="roomTypeForm.get('abbreviation')?.hasError('required')">A abreviação não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Tarifa</mat-label>
          <mat-select formControlName="rateId" required>
            <mat-option *ngFor="let rate of rates" [value]="rate.id">
              {{ rate.name }}
            </mat-option>
          </mat-select>
          <mat-error *ngIf="roomTypeForm.get('rateId')?.hasError('required')">A tarifa não pdoe estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Máximo de Ocupantes</mat-label>
          <input matInput formControlName="maxPersons" required />
          <mat-error *ngIf="roomTypeForm.get('maxPersons')?.hasError('required')">O número de ocupantes não pode estar em branco.</mat-error>
        </mat-form-field>

        <div mat-dialog-actions class="div-btns">
          <button mat-button color="warn" (click)="onCancel()" class="btn-cancelar">Cancelar</button>
          <button mat-button class="btn-adicionar" type="submit" [disabled]="loading || roomTypeForm.invalid">Salvar</button>
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
export class AddRoomTypeDialogComponent implements OnInit {
  roomTypeForm!: FormGroup;
  loading: boolean = false;
  rates: RateIdName[] = [];

  constructor(
    public dialogRef: MatDialogRef<AddRoomTypeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RoomType,
    private fb: FormBuilder,
    private roomTypeService: RoomTypeService
  ) {}

  ngOnInit(): void {
    this.roomTypeForm = this.fb.group({
      name: ['', Validators.required],
      abbreviation: ['', Validators.required],
      rateId: [null, Validators.required],
      maxPersons: ['', Validators.required]
    });

    this.loadRates();

    if (this.data) {
      this.roomTypeForm.patchValue({
        name: this.data.name,
        abbreviation: this.data.abbreviation,
        rateId: this.data.rateId,
        maxPersons: this.data.maxPersons
      });
    }
  }

  loadRates() {
    this.roomTypeService.getRatesSelect().subscribe(
      (rates: RateIdName[]) => {
        this.rates = rates; 
      },
      (error) => {
        console.error('Erro ao carregar as tarifas:', error);
        alert('Não foi possível carregar as tarifas.');
      }
    );
  }

  onSubmit() {
    if (this.roomTypeForm.invalid) return;
    const roomTypeData = { ...this.roomTypeForm.value, id: this.data?.id };

    const roomTypeObservable: Observable<RoomType> = roomTypeData.id
      ? this.roomTypeService.editRoomType(roomTypeData)
      : this.roomTypeService.addRoomType(roomTypeData);

    roomTypeObservable.subscribe(
      (response) => {
        alert('Categoria salva com sucesso!');
        this.dialogRef.close(response);
      },
      (error) => {
        alert('Erro ao salvar categoria. Tente novamente.');

      },
      () => {
      }
    );
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
