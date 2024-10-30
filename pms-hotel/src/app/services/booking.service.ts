import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';


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

interface PersonRecive {
  personId: string; 
  personName: string;
}

interface RoomRecive {
  roomId: string;
  roomNumer: string;
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

interface AmenityListRecive {
  amenitieId: string; 
  amenitieName: string;
}

interface RoomListRecive {
  roomId: string;
  roomNumber: string;
}

interface PersonListRecive {
  personId: string;
  personName: string;
}

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addBooking(booking: Booking): Observable<BookingRecive> {
    return this.http.post<BookingRecive>(`${this.apiUrl}/booking`, booking);
  }

  editBooking(booking: Booking): Observable<BookingRecive> {
    return this.http.put<BookingRecive>(`${this.apiUrl}/booking/${booking.id}`, booking);
  }

  getAllBooking(): Observable<BookingRecive[]> {
    return this.http.get<BookingRecive[]>(`${this.apiUrl}/booking/getAllBooking`);
  }

  getAllBookingCheckin(): Observable<BookingRecive[]> {
    return this.http.get<BookingRecive[]>(`${this.apiUrl}/booking/getAllBookingCheckout`);
  }

  getAllBookingCheckout(): Observable<BookingRecive[]> {
    return this.http.get<BookingRecive[]>(`${this.apiUrl}/booking/getAllBookingCheckin`);
  }

  deleteBooking(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/booking/${id}`);
  }

  getAmenities(): Observable<AmenityListRecive[]> {
    return this.http.get<AmenityListRecive[]>(`${this.apiUrl}/amenities/toBooking`);
  }

  getAvailableRooms(startDate: string, endDate: string): Observable<RoomListRecive[]> {
    const params = { startDate, endDate };
    return this.http.get<RoomListRecive[]>(`${this.apiUrl}/room/available`, { params });
  }

  getPeople(): Observable<PersonListRecive[]> {
    return this.http.get<PersonListRecive[]>(`${this.apiUrl}/person/toBooking`);
  }

  checkinBooking(bookingId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/booking/checkin/${bookingId}`, {}); 
  }

  getCheckoutValue(bookingId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/rate/getTotalValueByBooking/${bookingId}`);
  }

  checkoutBooking(bookingId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/booking/checkout/${bookingId}`, {});
  }
}          
