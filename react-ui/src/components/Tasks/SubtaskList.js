import React, { useState } from 'react';
import { subtaskAPI } from '../../services/api';

const SubtaskList = ({ taskId, subtasks, onUpdate, showOnlyInput }) => {
  const [newSubtaskTitle, setNewSubtaskTitle] = useState('');
  const [loading, setLoading] = useState(false);

  const handleAddSubtask = async (e) => {
    e.preventDefault();
    if (!newSubtaskTitle.trim()) return;

    setLoading(true);
    try {
      await subtaskAPI.createSubtask(taskId, {
        title: newSubtaskTitle,
        completed: false,
      });
      setNewSubtaskTitle('');
      onUpdate();
    } catch (err) {
      console.error('Error creating subtask:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleSubtask = async (subtaskId) => {
    try {
      await subtaskAPI.toggleSubtaskComplete(taskId, subtaskId);
      onUpdate();
    } catch (err) {
      console.error('Error toggling subtask:', err);
    }
  };

  const handleDeleteSubtask = async (subtaskId) => {
    if (!window.confirm('Delete this subtask?')) return;

    try {
      await subtaskAPI.deleteSubtask(taskId, subtaskId);
      onUpdate();
    } catch (err) {
      console.error('Error deleting subtask:', err);
    }
  };

  return (
    <div className="subtasks-container">
      {!showOnlyInput && subtasks && subtasks.length > 0 && (
        <div className="subtasks-list">
          {subtasks.map(subtask => (
            <div key={subtask.id} className="subtask-item">
              <label className="subtask-checkbox-container">
                <input
                  type="checkbox"
                  checked={subtask.completed}
                  onChange={() => handleToggleSubtask(subtask.id)}
                />
                <span className="subtask-checkmark"></span>
              </label>
              <span className={`subtask-title ${subtask.completed ? 'completed' : ''}`}>
                {subtask.title}
              </span>
              <button
                className="btn-icon-sm"
                onClick={() => handleDeleteSubtask(subtask.id)}
                title="Delete"
              >
                ×
              </button>
            </div>
          ))}
        </div>
      )}

      <form onSubmit={handleAddSubtask} className="add-subtask-form">
        <input
          type="text"
          value={newSubtaskTitle}
          onChange={(e) => setNewSubtaskTitle(e.target.value)}
          placeholder="+ Add subtask"
          className="add-subtask-input"
          disabled={loading}
        />
        {newSubtaskTitle.trim() && (
          <button type="submit" className="btn btn-sm btn-primary" disabled={loading}>
            Add
          </button>
        )}
      </form>
    </div>
  );
};

export default SubtaskList;
