import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

interface amenities {
  id?: string;
  name: string;
  mondayRate: number;
  tuesdayRate: number;
  wednesdayRate: number;
  thursdayRate: number;
  fridayRate: number;
  saturdayRate: number;
  sundayRate: number;
  amenitiesId: string;
}



@Injectable({
  providedIn: 'root'
})
export class AmenitiesService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addAmenities(amenities: amenities): Observable<amenities> {
    return this.http.post<amenities>(`${this.apiUrl}/amenities`, amenities);
  }

  editAmenities(amenities: amenities): Observable<amenities> {
    return this.http.put<amenities>(`${this.apiUrl}/amenities/${amenities.id}`, amenities);
  }

  getAllAmenities(): Observable<amenities[]> {
    return this.http.get<amenities[]>(`${this.apiUrl}/amenities/getAllWithValue`);
  }

  deleteAmenities(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/amenities/${id}`);
  }
}          
