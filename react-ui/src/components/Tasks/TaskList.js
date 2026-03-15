import React, { useState, useEffect } from 'react';
import { taskAPI, userAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import SubtaskList from './SubtaskList';
import './TaskList.css';

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [selectedUserId, setSelectedUserId] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');
  const [newTaskTitle, setNewTaskTitle] = useState('');
  const [expandedTasks, setExpandedTasks] = useState(new Set());

  const { user, logout } = useAuth();
  const taskStatuses = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

  useEffect(() => {
    loadUsers();
    loadTasks();
  }, [selectedUserId, selectedStatus]);

  const loadUsers = async () => {
    try {
      const response = await userAPI.getAllUsers();
      setUsers(response.data);
    } catch (err) {
      console.error('Error loading users:', err);
    }
  };

  const loadTasks = async () => {
    setLoading(true);
    setError('');

    try {
      const response = await taskAPI.getAllTasks(selectedUserId, selectedStatus);
      setTasks(response.data);
    } catch (err) {
      setError('Failed to load tasks');
      console.error('Error loading tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddTask = async (e) => {
    e.preventDefault();
    if (!newTaskTitle.trim()) return;

    try {
      await taskAPI.createTask({
        title: newTaskTitle,
        status: 'PENDING',
      });
      setNewTaskTitle('');
      loadTasks();
    } catch (err) {
      setError('Failed to create task');
      console.error('Error creating task:', err);
    }
  };

  const handleToggleComplete = async (taskId) => {
    try {
      await taskAPI.toggleTaskComplete(taskId);
      loadTasks();
    } catch (err) {
      setError('Failed to update task');
      console.error('Error updating task:', err);
    }
  };

  const handleDeleteTask = async (taskId) => {
    if (!window.confirm('Are you sure you want to delete this task?')) return;

    try {
      await taskAPI.deleteTask(taskId);
      loadTasks();
    } catch (err) {
      setError('Failed to delete task');
      console.error('Error deleting task:', err);
    }
  };

  const toggleTaskExpanded = (taskId) => {
    const newExpanded = new Set(expandedTasks);
    if (newExpanded.has(taskId)) {
      newExpanded.delete(taskId);
    } else {
      newExpanded.add(taskId);
    }
    setExpandedTasks(newExpanded);
  };

  const getSubtaskProgress = (subtasks) => {
    if (!subtasks || subtasks.length === 0) {
      return { completed: 0, total: 0, percentage: 0 };
    }
    const total = subtasks.length;
    const completed = subtasks.filter(s => s.completed).length;
    const percentage = Math.round((completed / total) * 100);
    return { completed, total, percentage };
  };

  const getStatusClass = (status) => {
    const classes = {
      'PENDING': 'status-pending',
      'IN_PROGRESS': 'status-in-progress',
      'COMPLETED': 'status-completed',
      'CANCELLED': 'status-cancelled',
    };
    return classes[status] || '';
  };

  return (
    <div className="task-list-container">
      {/* Header */}
      <header className="app-header">
        <div className="header-content">
          <h1>📝 My Tasks</h1>
          <div className="user-info">
            <span className="welcome">Welcome, {user?.name}!</span>
            <button className="btn btn-secondary" onClick={logout}>
              Logout
            </button>
          </div>
        </div>
      </header>

      <div className="container">
        {/* Error Alert */}
        {error && (
          <div className="alert alert-error">
            {error}
            <button className="alert-close" onClick={() => setError('')}>×</button>
          </div>
        )}

        {/* New Task Form */}
        <div className="new-task-section">
          <form onSubmit={handleAddTask} className="new-task-form">
            <input
              type="text"
              value={newTaskTitle}
              onChange={(e) => setNewTaskTitle(e.target.value)}
              placeholder="What needs to be done?"
              className="new-task-input"
            />
            <button type="submit" className="btn btn-primary">
              Add Task
            </button>
          </form>
        </div>

        {/* Filters */}
        <div className="filters">
          <div className="filter-group">
            <label>Filter by User:</label>
            <select
              value={selectedUserId}
              onChange={(e) => setSelectedUserId(e.target.value)}
              className="form-control"
            >
              <option value="">All Users</option>
              {users.map(u => (
                <option key={u.id} value={u.id}>{u.name}</option>
              ))}
            </select>
          </div>

          <div className="filter-group">
            <label>Filter by Status:</label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="form-control"
            >
              <option value="">All Statuses</option>
              {taskStatuses.map(status => (
                <option key={status} value={status}>{status}</option>
              ))}
            </select>
          </div>

          <button
            className="btn btn-secondary"
            onClick={() => {
              setSelectedUserId('');
              setSelectedStatus('');
            }}
          >
            Clear Filters
          </button>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="loading">
            <div className="spinner"></div>
            Loading tasks...
          </div>
        )}

        {/* Empty State */}
        {!loading && tasks.length === 0 && (
          <div className="empty-state">
            <p>🎉 No tasks yet! Add one above to get started.</p>
          </div>
        )}

        {/* Task Grid */}
        {!loading && tasks.length > 0 && (
          <div className="task-grid">
            {tasks.map(task => (
              <div key={task.id} className={`task-card ${task.status === 'COMPLETED' ? 'completed' : ''}`}>
                {/* Task Header */}
                <div className="task-header">
                  <div className="task-title-section">
                    <h3>{task.title}</h3>
                    <span className={`status-badge ${getStatusClass(task.status)}`}>
                      {task.status}
                    </span>
                  </div>
                  <div className="task-actions">
                    <button
                      className="btn-icon"
                      onClick={() => handleDeleteTask(task.id)}
                      title="Delete"
                    >
                      🗑️
                    </button>
                  </div>
                </div>

                {/* Task Description */}
                {task.description && (
                  <p className="task-description">{task.description}</p>
                )}

                {/* Task Meta */}
                {task.assignedTo && (
                  <div className="task-meta">
                    <strong>Assigned to:</strong> {task.assignedTo.name}
                  </div>
                )}

                {/* Complete Checkbox */}
                <div className="task-complete-section">
                  <label className="checkbox-container">
                    <input
                      type="checkbox"
                      checked={task.status === 'COMPLETED'}
                      onChange={() => handleToggleComplete(task.id)}
                    />
                    <span className="checkmark"></span>
                    <span className="checkbox-label">Mark as Complete</span>
                  </label>
                </div>

                {/* Subtasks Section */}
                {task.subtasks && task.subtasks.length > 0 && (
                  <div className="subtasks-preview">
                    <div
                      className="subtasks-header"
                      onClick={() => toggleTaskExpanded(task.id)}
                    >
                      <div className="subtasks-title">
                        <span className="toggle-icon">
                          {expandedTasks.has(task.id) ? '▼' : '▶'}
                        </span>
                        <span>Subtasks ({task.subtasks.length})</span>
                      </div>
                      <div className="progress-info">
                        <span className="progress-text">
                          {getSubtaskProgress(task.subtasks).completed}/{getSubtaskProgress(task.subtasks).total}
                        </span>
                        <div className="progress-bar-mini">
                          <div
                            className="progress-fill"
                            style={{ width: `${getSubtaskProgress(task.subtasks).percentage}%` }}
                          ></div>
                        </div>
                      </div>
                    </div>

                    {/* Subtask List Component */}
                    {expandedTasks.has(task.id) && (
                      <SubtaskList
                        taskId={task.id}
                        subtasks={task.subtasks}
                        onUpdate={loadTasks}
                      />
                    )}
                  </div>
                )}

                {/* Add Subtask (always visible) */}
                <SubtaskList
                  taskId={task.id}
                  subtasks={task.subtasks || []}
                  onUpdate={loadTasks}
                  showOnlyInput={!task.subtasks || task.subtasks.length === 0}
                />
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default TaskList;
