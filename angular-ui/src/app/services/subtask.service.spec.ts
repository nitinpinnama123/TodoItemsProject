import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SubtaskService } from './subtask.service';
import { AuthService } from './auth.service';
import { Subtask, SubtaskRequest } from '../models/task.model';
import { environment } from '../../environments/environment';

describe('SubtaskService', () => {
  let service: SubtaskService;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  const mockUserId = 1;
  const mockTaskId = 10;
  const mockSubtaskId = 100;
  const apiUrl = `${environment.apiUrl}/items`;

  beforeEach(() => {
    const authSpy = jasmine.createSpyObj('AuthService', ['getUserId']);
    authSpy.getUserId.and.returnValue(mockUserId);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SubtaskService,
        { provide: AuthService, useValue: authSpy }
      ]
    });

    service = TestBed.inject(SubtaskService);
    httpMock = TestBed.inject(HttpTestingController);
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getSubtasks', () => {
    it('should get all subtasks for a task', () => {
      const mockSubtasks: Subtask[] = [
        { id: 1, title: 'Subtask 1', completed: false },
        { id: 2, title: 'Subtask 2', completed: true }
      ];

      service.getSubtasks(mockTaskId).subscribe(subtasks => {
        expect(subtasks).toEqual(mockSubtasks);
        expect(subtasks.length).toBe(2);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks`);
      expect(req.request.method).toBe('GET');
      expect(req.request.headers.get('X-User-Id')).toBe(mockUserId.toString());
      req.flush(mockSubtasks);
    });
  });

  describe('getSubtaskById', () => {
    it('should get a specific subtask', () => {
      const mockSubtask: Subtask = {
        id: mockSubtaskId,
        title: 'Test Subtask',
        completed: false
      };

      service.getSubtaskById(mockTaskId, mockSubtaskId).subscribe(subtask => {
        expect(subtask).toEqual(mockSubtask);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/${mockSubtaskId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSubtask);
    });
  });

  describe('createSubtask', () => {
    it('should create a new subtask', () => {
      const request: SubtaskRequest = {
        title: 'New Subtask',
        completed: false
      };
      const mockResponse: Subtask = {
        id: 101,
        title: 'New Subtask',
        completed: false
      };

      service.createSubtask(mockTaskId, request).subscribe(subtask => {
        expect(subtask).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(request);
      expect(req.request.headers.get('Content-Type')).toBe('application/json');
      req.flush(mockResponse);
    });
  });

  describe('updateSubtask', () => {
    it('should update an existing subtask', () => {
      const request: SubtaskRequest = {
        title: 'Updated Subtask',
        completed: true
      };
      const mockResponse: Subtask = {
        id: mockSubtaskId,
        title: 'Updated Subtask',
        completed: true
      };

      service.updateSubtask(mockTaskId, mockSubtaskId, request).subscribe(subtask => {
        expect(subtask).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/${mockSubtaskId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(request);
      req.flush(mockResponse);
    });
  });

  describe('toggleSubtaskComplete', () => {
    it('should toggle subtask completion status', () => {
      const mockResponse: Subtask = {
        id: mockSubtaskId,
        title: 'Test Subtask',
        completed: true
      };

      service.toggleSubtaskComplete(mockTaskId, mockSubtaskId).subscribe(subtask => {
        expect(subtask).toEqual(mockResponse);
        expect(subtask.completed).toBe(true);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/${mockSubtaskId}/toggle`);
      expect(req.request.method).toBe('PATCH');
      expect(req.request.body).toEqual({});
      req.flush(mockResponse);
    });
  });

  describe('deleteSubtask', () => {
    it('should delete a subtask', () => {
      service.deleteSubtask(mockTaskId, mockSubtaskId).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/${mockSubtaskId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('createSubtasksBulk', () => {
    it('should create multiple subtasks at once', () => {
      const requests: SubtaskRequest[] = [
        { title: 'Subtask 1', completed: false },
        { title: 'Subtask 2', completed: false }
      ];
      const mockResponse: Subtask[] = [
        { id: 101, title: 'Subtask 1', completed: false },
        { id: 102, title: 'Subtask 2', completed: false }
      ];

      service.createSubtasksBulk(mockTaskId, requests).subscribe(subtasks => {
        expect(subtasks).toEqual(mockResponse);
        expect(subtasks.length).toBe(2);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/bulk`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(requests);
      req.flush(mockResponse);
    });
  });

  describe('completeAllSubtasks', () => {
    it('should mark all subtasks as complete', () => {
      const mockResponse: Subtask[] = [
        { id: 1, title: 'Subtask 1', completed: true },
        { id: 2, title: 'Subtask 2', completed: true }
      ];

      service.completeAllSubtasks(mockTaskId).subscribe(subtasks => {
        expect(subtasks).toEqual(mockResponse);
        expect(subtasks.every(s => s.completed)).toBe(true);
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/complete-all`);
      expect(req.request.method).toBe('PATCH');
      req.flush(mockResponse);
    });
  });

  describe('reorderSubtasks', () => {
    it('should reorder subtasks', () => {
      const subtaskIds = [3, 1, 2];

      service.reorderSubtasks(mockTaskId, subtaskIds).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/reorder`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({ subtaskIds });
      req.flush(null);
    });
  });

  describe('error handling', () => {
    it('should handle HTTP errors', () => {
      const errorMessage = 'Subtask not found';

      service.getSubtaskById(mockTaskId, mockSubtaskId).subscribe(
        () => fail('should have failed'),
        (error) => {
          expect(error.status).toBe(404);
          expect(error.error).toBe(errorMessage);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/${mockTaskId}/subtasks/${mockSubtaskId}`);
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    });
  });
});
