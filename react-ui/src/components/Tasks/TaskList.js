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
  const [newTaskDescription, setNewTaskDescription] = useState('');
  const [newTaskAssignedTo, setNewTaskAssignedTo] = useState('');
  const [expandedTasks, setExpandedTasks] = useState(new Set());
  const [editingTaskId, setEditingTaskId] = useState(null);
  const [editAssignedTo, setEditAssignedTo] = useState('');


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
      const taskData = {
        title: newTaskTitle,
        description: newTaskDescription || undefined,
        status: 'PENDING',
      };

      // Add assignedToId if a user is selected
      if (newTaskAssignedTo) {
        taskData.assignedToId = parseInt(newTaskAssignedTo);
      }

      await taskAPI.createTask(taskData);

      // Reset form
      setNewTaskTitle('');
      setNewTaskDescription('');
      setNewTaskAssignedTo('');

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

  const handleAssignUser = async (taskId, userId) => {
    try {
      const task = tasks.find(t => t.id === taskId);

      const updatedTask = {
        title: task.title,
        description: task.description || '',
        status: task.status,
        assignedToId: userId ? parseInt(userId) : null,
      };

      await taskAPI.updateTask(taskId, updatedTask);
      setEditingTaskId(null);
      setEditAssignedTo('');
      loadTasks();
    } catch (err) {
      setError('Failed to assign user');
      console.error('Error assigning user:', err);
    }
  };

  const startEditingAssignment = (taskId, currentAssignedToId) => {
    setEditingTaskId(taskId);
    setEditAssignedTo(currentAssignedToId ? currentAssignedToId.toString() : '');
  };

  const cancelEditingAssignment = () => {
    setEditingTaskId(null);
    setEditAssignedTo('');
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
           <div className="new-task-inputs">
                <input
                  type="text"
                  value={newTaskTitle}
                  onChange={(e) => setNewTaskTitle(e.target.value)}
                  placeholder="What needs to be done?"
                  className="new-task-input"
                  required
                />
                <input
                  type="text"
                  value={newTaskDescription}
                  onChange={(e) => setNewTaskDescription(e.target.value)}
                  placeholder="Description (optional)"
                  className="new-task-input"
                />
                <select
                  value={newTaskAssignedTo}
                  onChange={(e) => setNewTaskAssignedTo(e.target.value)}
                  className="new-task-select"
                >
                  <option value="">Assign to user (optional)</option>
                  {users.map(u => (
                    <option key={u.id} value={u.id}>{u.username}</option>
                  ))}
                </select>
            </div>
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
                <option key={u.id} value={u.id}>{u.username}</option>
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
            {tasks.map(task => {

              const progress = getSubtaskProgress(task.subtasks);
              const isEditing = editingTaskId === task.id;

              return (
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
                <div className="task-assignment">
                    {!isEditing ? (
                      <div className="assignment-display">
                        <div className="assignment-info">
                          <strong>👤 Assigned to:</strong>{' '}
                          {task.assignedTo ? (
                            <span className="assigned-user">{task.assignedTo.username}</span>
                          ) : (
                            <span className="unassigned">Unassigned</span>
                          )}
                        </div>
                        <button
                          className="btn btn-sm btn-outline"
                          onClick={() => startEditingAssignment(task.id, task.assignedTo?.id)}
                        >
                          {task.assignedTo ? 'Reassign' : 'Assign'}
                        </button>
                      </div>
                    ) : (
                      <div className="assignment-edit">
                        <select
                          value={editAssignedTo}
                          onChange={(e) => setEditAssignedTo(e.target.value)}
                          className="form-control-sm"
                          autoFocus
                        >
                          <option value="">Unassigned</option>
                          {users.map(u => (
                            <option key={u.id} value={u.id}>{u.username}</option>
                          ))}
                        </select>
                        <div className="assignment-actions">
                          <button
                            className="btn btn-sm btn-primary"
                            onClick={() => handleAssignUser(task.id, editAssignedTo)}
                          >
                            Save
                          </button>
                          <button
                            className="btn btn-sm btn-secondary"
                            onClick={cancelEditingAssignment}
                          >
                            Cancel
                          </button>
                        </div>
                      </div>
                    )}
                  </div>


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
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default TaskList;
