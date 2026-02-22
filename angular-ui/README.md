# Task Management System - Angular Frontend

A modern Angular application for managing users and tasks, built to work with the Spring Boot backend.

## Features

### User Management
- ✅ List all users
- ✅ Create new users
- ✅ Edit existing users
- ✅ Delete users
- ✅ Form validation

### Task Management
- ✅ List all tasks (card view)
- ✅ Create new tasks
- ✅ Edit existing tasks
- ✅ Delete tasks
- ✅ Assign tasks to users
- ✅ Update task status
- ✅ Filter tasks by user
- ✅ Filter tasks by status
- ✅ Form validation

### Task Statuses
- PENDING
- IN_PROGRESS
- COMPLETED
- CANCELLED

## Project Structure

```
src/
├── app/
│   ├── components/
│   │   ├── user-list/           # User list component
│   │   ├── user-form/           # User create/edit form
│   │   ├── task-list/           # Task list with filters
│   │   └── task-form/           # Task create/edit form
│   ├── models/
│   │   ├── user.model.ts        # User interface
│   │   └── task.model.ts        # Task interfaces & enums
│   ├── services/
│   │   ├── user.service.ts      # User API service
│   │   └── task.service.ts      # Task API service
│   ├── app.component.*          # Root component
│   ├── app.module.ts            # App module
│   └── app-routing.module.ts   # Routing configuration
├── environments/
│   ├── environment.ts           # Development config
│   └── environment.prod.ts      # Production config
├── index.html                   # HTML entry point
├── main.ts                      # TypeScript entry point
└── styles.css                   # Global styles
```

## Prerequisites

- Node.js (v18 or higher)
- npm (v9 or higher)
- Angular CLI (v17 or higher)

## Installation

### 1. Install Angular CLI globally
```bash
npm install -g @angular/cli
```

### 2. Install dependencies
```bash
npm install
```

### 3. Configure API URL

Update the API URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'  // Your Spring Boot backend URL
};
```

## Running the Application

### Development Server
```bash
ng serve
```

Navigate to `http://localhost:4200/`

The application will automatically reload when you change source files.

### Build for Production
```bash
ng build
```

Build artifacts will be stored in the `dist/` directory.

### Build with Production Configuration
```bash
ng build --configuration production
```

## Application Routes

| Route | Component | Description |
|-------|-----------|-------------|
| `/` | TaskListComponent | Redirects to /tasks |
| `/tasks` | TaskListComponent | View all tasks |
| `/tasks/new` | TaskFormComponent | Create new task |
| `/tasks/:id/edit` | TaskFormComponent | Edit task |
| `/users` | UserListComponent | View all users |
| `/users/new` | UserFormComponent | Create new user |
| `/users/:id/edit` | UserFormComponent | Edit user |

## API Integration

The application communicates with the Spring Boot backend through the following endpoints:

### User Endpoints
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Task Endpoints
- `GET /api/tasks` - Get all tasks (with optional filters)
- `GET /api/tasks?userId={id}` - Filter tasks by user
- `GET /api/tasks?status={status}` - Filter tasks by status
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create task
- `PUT /api/tasks/{id}` - Update task
- `PATCH /api/tasks/{id}/assign/{userId}` - Assign task
- `PATCH /api/tasks/{id}/unassign` - Unassign task
- `PATCH /api/tasks/{id}/status?status={status}` - Update status
- `DELETE /api/tasks/{id}` - Delete task

## CORS Configuration

Ensure your Spring Boot backend has CORS enabled. Add this to your Spring Boot application:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## Component Details

### UserListComponent
- Displays all users in a table
- Actions: View, Edit, Delete
- Create new user button

### UserFormComponent
- Reactive form with validation
- Fields: Name (required), Email (required, valid email)
- Works for both create and edit modes

### TaskListComponent
- Displays tasks in a card grid layout
- Filter by user (dropdown)
- Filter by status (dropdown)
- Quick status update from card
- Actions: View, Edit, Delete
- Visual status badges (color-coded)

### TaskFormComponent
- Reactive form with validation
- Fields: Title (required), Description, Status (required), Assigned User
- Works for both create and edit modes

## Styling

The application uses:
- Custom CSS (no external UI library)
- Responsive design
- Color-coded status badges
- Hover effects and transitions
- Clean, modern interface

## Error Handling

- Form validation errors displayed inline
- API error messages shown in alerts
- Loading states for async operations
- Confirmation dialogs for delete operations

## Development Tips

### Generate new component
```bash
ng generate component components/component-name
```

### Generate new service
```bash
ng generate service services/service-name
```

### Run tests
```bash
ng test
```

### Lint code
```bash
ng lint
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### CORS Issues
If you see CORS errors in the browser console:
1. Verify backend CORS configuration
2. Check that API URL in environment.ts is correct
3. Ensure backend is running on the correct port

### API Connection Issues
If API calls fail:
1. Verify backend is running (`http://localhost:8080`)
2. Check environment.ts has correct API URL
3. Open browser DevTools → Network tab to see failed requests

### Build Issues
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

## Production Deployment

### 1. Update environment.prod.ts
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://your-production-api.com/api'
};
```

### 2. Build for production
```bash
ng build --configuration production
```

### 3. Deploy dist/ folder
Deploy the contents of `dist/task-management-frontend/` to your web server:
- Apache
- Nginx
- Firebase Hosting
- Netlify
- Vercel
- AWS S3 + CloudFront

### 4. Configure server for Angular routing
Add URL rewrite rules to redirect all routes to index.html

**Nginx example:**
```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

## Future Enhancements

Potential improvements:
- [ ] Authentication & Authorization
- [ ] User profiles
- [ ] Task comments
- [ ] Task attachments
- [ ] Task due dates
- [ ] Email notifications
- [ ] Real-time updates (WebSocket)
- [ ] Task search functionality
- [ ] Pagination for large datasets
- [ ] Dark mode
- [ ] Export tasks to CSV/PDF
- [ ] Task categories/tags
- [ ] Dashboard with statistics

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues or questions:
- Create an issue in the repository
- Check the backend API documentation
- Review browser console for errors
