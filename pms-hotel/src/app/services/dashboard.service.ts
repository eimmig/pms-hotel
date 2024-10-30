import { Injectable, signal } from '@angular/core';
import { Widget } from '../models/dashboard';
import { CheckinsComponent } from '../pages/dashboard/widgets/checkins.component';
import { CheckoutsComponent } from '../pages/dashboard/widgets/checkouts.component';
import { TicketValueComponent } from '../pages/dashboard/widgets/ticketvalue.component';
import { OccupationComponent } from '../pages/dashboard/widgets/occupation.component';
import { RoomTypeChartComponent } from '../pages/dashboard/widgets/roomtype.component';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InterativeChart } from '../models/interativeChart';
import { StaticChart } from '../models/staticChart';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly baseUrl = environment.apiUrl;


  constructor(private http: HttpClient) {
  }

  getRoomTypeData(): Observable<InterativeChart> {
    return this.http.get<InterativeChart>(`${this.baseUrl}/booking/roomtypechart`);
  }

  getOccupancyData(): Observable<InterativeChart> {
    return this.http.get<InterativeChart>(`${this.baseUrl}/booking/occupancychart`);
  }

  getRevenueData(): Observable<StaticChart> {
    return this.http.get<StaticChart>(`${this.baseUrl}/booking/revenuechart`);
  }

  getCheckinData(): Observable<StaticChart> {
    return this.http.get<StaticChart>(`${this.baseUrl}/booking/checkinschart`);
  }

  getCheckoutData(): Observable<StaticChart> {
    return this.http.get<StaticChart>(`${this.baseUrl}/booking/checkoutschart`);
  }

  
  widgets = signal<Widget[]>([
    {
      id: 1,
      label: 'Checkins',
      content: CheckinsComponent,
      rows: 1,
      cols: 1,
      backgroudColor: '#003f5c',
      color: 'whitesmoke'
    },
    {
      id: 2,
      label: 'Checkouts',
      content: CheckoutsComponent,
      rows: 1,
      cols: 1,
      backgroudColor: '#003f5c',
      color: 'whitesmoke'
    },
    {
      id: 3,
      label: 'Valor médio',
      content: TicketValueComponent,
      rows: 1,
      cols: 1,
      backgroudColor: '#003f5c',
      color: 'whitesmoke'
    },
    {
      id: 4,
      label: 'Ocupação',
      content: OccupationComponent,
      rows: 2,
      cols: 2,
      backgroudColor: '#003f5c',
      color: 'whitesmoke'
    },
    {
      id: 5,
      label: 'Categorias Ocupadas',
      content: RoomTypeChartComponent,
      rows: 2,
      cols: 1,
      backgroudColor: '#003f5c',
      color: 'whitesmoke'
    }

  ]);

}
