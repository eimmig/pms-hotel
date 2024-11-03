import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Booking } from '../models/booking';
import { BookingRecive } from '../models/bookingRecive';
import { AmenityListRecive } from '../models/amenityListRecive';
import { RoomListRecive } from '../models/roomListRecive';
import { PersonListRecive } from '../models/personListRecive';


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
    return this.http.get<BookingRecive[]>(`${this.apiUrl}/booking/getAllBookingCheckin`);
  }

  getAllBookingCheckout(): Observable<BookingRecive[]> {
    return this.http.get<BookingRecive[]>(`${this.apiUrl}/booking/getAllBookingCheckout`);
  }

  deleteBooking(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/booking/${id}`);
  }

  getAmenities(): Observable<AmenityListRecive[]> {
    return this.http.get<AmenityListRecive[]>(`${this.apiUrl}/amenities/toBooking`);
  }

  getAvailableRooms(startDate: string, endDate: string, bookingId: string): Observable<RoomListRecive[]> {
    const params = { startDate, endDate, bookingId };
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
