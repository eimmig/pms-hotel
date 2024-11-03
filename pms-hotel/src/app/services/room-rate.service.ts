import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RoomRate } from '../models/roomRate';


@Injectable({
  providedIn: 'root'
})
export class RoomRateService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addRoomRate(roomRate: RoomRate): Observable<RoomRate> {
    return this.http.post<RoomRate>(`${this.apiUrl}/rate`, roomRate);
  }

  editRoomRate(roomRate: RoomRate): Observable<RoomRate> {
    return this.http.put<RoomRate>(`${this.apiUrl}/rate/${roomRate.id}`, roomRate);
  }

  getAllRoomRates(): Observable<RoomRate[]> {
    return this.http.get<RoomRate[]>(`${this.apiUrl}/rate/getAllWithValue`);
  }

  deleteRoomRate(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/rate/${id}`);
  }
}          
