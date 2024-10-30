import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

interface RoomType {
  id?: number;
  name: string;
  abbreviation: string;
  maxPersons: number;
  rateId: string;
  rateName: string;
}


interface Rate {
  id: string
  name : string
}


@Injectable({
  providedIn: 'root'
})
export class RoomTypeService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addRoomType(roomType: RoomType): Observable<RoomType> {
    return this.http.post<RoomType>(`${this.apiUrl}/room-types`, roomType);
  }

  editRoomType(roomType: RoomType): Observable<RoomType> {
    return this.http.put<RoomType>(`${this.apiUrl}/room-types/${roomType.id}`, roomType);
  }

  getAllRoomTypes(): Observable<RoomType[]> {
    return this.http.get<RoomType[]>(`${this.apiUrl}/room-types`);
  }

  deleteRoomType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/room-types/${id}`);
  }

  getRatesSelect(): Observable<Rate[]> {
    return this.http.get<Rate[]>(`${this.apiUrl}/rate`);
  }
}          
