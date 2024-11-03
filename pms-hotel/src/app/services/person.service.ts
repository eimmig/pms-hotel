import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Person } from '../models/person';


@Injectable({
  providedIn: 'root'
})
export class PersonService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  addPerson(person: Person): Observable<Person> {
    return this.http.post<Person>(`${this.apiUrl}/person`, person);
  }

  editPerson(person: Person): Observable<Person> {
    return this.http.put<Person>(`${this.apiUrl}/person/${person.id}`, person);
  }

  getAllPersons(): Observable<Person[]> {
    return this.http.get<Person[]>(`${this.apiUrl}/person`);
  }

  getForCheckin(): Observable<Person[]> {
    return this.http.get<Person[]>(`${this.apiUrl}/person/getForCheckin`);
  }

  getForCheckout(): Observable<Person[]> {
    return this.http.get<Person[]>(`${this.apiUrl}/person/getForCheckout`);
  }

  deletePerson(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/person/${id}`);
  }

  getApiUrl(): string {
    return this.apiUrl;
  }
}
