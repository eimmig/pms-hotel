import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PersonService } from './person.service';
import { Person } from '../models/person';

describe('PersonService', () => {
  let service: PersonService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PersonService],
    });
    service = TestBed.inject(PersonService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should add a person', () => {
    const person: Person = {
      name: 'John Doe',
      document: '12345678901',
      phoneNumber: '123456789',
      email: 'john@example.com',
      birthDate: '1990-01-01',
      documentTypeId: '1',
    };

    service.addPerson(person).subscribe((response) => {
      expect(response).toBeDefined();
      expect(response.id).toBeDefined();
      expect(response.name).toBe(person.name);
      expect(response.document).toBe(person.document);
      expect(response.phoneNumber).toBe(person.phoneNumber);
      expect(response.email).toBe(person.email);
      expect(response.birthDate).toBe(person.birthDate);
      expect(response.documentTypeId).toBe(person.documentTypeId);
    });

    const req = httpMock.expectOne(`${service.getApiUrl()}/person`);
    expect(req.request.method).toBe('POST');
    req.flush({ id: 'generated-uuid', ...person });
  });

  it('should get all persons', () => {
    const mockPersons: Person[] = [
      {
        name: 'John Doe',
        document: '12345678901',
        phoneNumber: '123456789',
        email: 'john@example.com',
        birthDate: '1990-01-01',
        documentTypeId: '1',
      },
      {
        name: 'Jane Doe',
        document: '10987654321',
        phoneNumber: '987654321',
        email: 'jane@example.com',
        birthDate: '1992-02-02',
        documentTypeId: '1',
      },
    ];

    service.getAllPersons().subscribe((response) => {
      expect(response).toEqual(mockPersons);
    });

    const req = httpMock.expectOne(`${service.getApiUrl()}/person`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPersons);
  });
});
