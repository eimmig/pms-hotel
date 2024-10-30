import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

interface Room {
  id?: string;
  number: string;
  roomTypeId: string;
  roomTypeName: number;
  status: string;
  statusDisplay: string;
}

interface RoomType {
  id: string
  name : string
}


@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addRoom(room: Room): Observable<Room> {
    return this.http.post<Room>(`${this.apiUrl}/room`, room);
  }

  editRoom(room: Room): Observable<Room> {
    return this.http.put<Room>(`${this.apiUrl}/room/${room.id}`, room);
  }

  getAllRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(`${this.apiUrl}/room`);
  }

  deleteRoom(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/room/${id}`);
  }

  getRoomTypesSelect(): Observable<RoomType[]> {
    return this.http.get<RoomType[]>(`${this.apiUrl}/room-types`);
  }
}
