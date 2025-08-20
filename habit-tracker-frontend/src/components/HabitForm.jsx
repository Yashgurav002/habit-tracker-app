import { useState, useEffect } from 'react'
import { X } from 'lucide-react'

const HabitForm = ({ habit, onSubmit, onCancel, loading = false }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    frequencyType: 'DAILY',
    targetCount: 1,
    reminderTime: ''
  })

  const [errors, setErrors] = useState({})

  // Populate form with habit data when editing
  useEffect(() => {
    if (habit) {
      setFormData({
        name: habit.name || '',
        description: habit.description || '',
        frequencyType: habit.frequencyType || 'DAILY',
        targetCount: habit.targetCount || 1,
        reminderTime: habit.reminderTime || ''
      })
    }
  }, [habit])

  const validate = () => {
    const newErrors = {}

    if (!formData.name.trim()) {
      newErrors.name = 'Habit name is required'
    } else if (formData.name.trim().length < 3) {
      newErrors.name = 'Habit name must be at least 3 characters'
    }

    if (formData.targetCount < 1) {
      newErrors.targetCount = 'Target count must be at least 1'
    }

    if (!['DAILY', 'WEEKLY', 'MONTHLY'].includes(formData.frequencyType)) {
      newErrors.frequencyType = 'Please select a valid frequency'
    }

    return newErrors
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    
    const validationErrors = validate()
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      return
    }

    setErrors({})
    onSubmit({
      ...formData,
      targetCount: Number(formData.targetCount)
    })
  }

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
    
    // Clear error for this field
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: ''
      }))
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {/* Name Field */}
      <div>
        <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
          Habit Name *
        </label>
        <input
          type="text"
          id="name"
          value={formData.name}
          onChange={(e) => handleChange('name', e.target.value)}
          placeholder="e.g., Daily Exercise"
          className={`input-field ${errors.name ? 'border-red-500 focus:border-red-500' : ''}`}
          disabled={loading}
        />
        {errors.name && (
          <p className="mt-1 text-sm text-red-600">{errors.name}</p>
        )}
      </div>

      {/* Description Field */}
      <div>
        <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
          Description
        </label>
        <textarea
          id="description"
          value={formData.description}
          onChange={(e) => handleChange('description', e.target.value)}
          placeholder="Optional description for your habit..."
          rows="3"
          className="input-field"
          disabled={loading}
        />
      </div>

      {/* Frequency Type Field */}
      <div>
        <label htmlFor="frequencyType" className="block text-sm font-medium text-gray-700 mb-1">
          Frequency *
        </label>
        <select
          id="frequencyType"
          value={formData.frequencyType}
          onChange={(e) => handleChange('frequencyType', e.target.value)}
          className={`input-field ${errors.frequencyType ? 'border-red-500 focus:border-red-500' : ''}`}
          disabled={loading}
        >
          <option value="DAILY">Daily</option>
          <option value="WEEKLY">Weekly</option>
          <option value="MONTHLY">Monthly</option>
        </select>
        {errors.frequencyType && (
          <p className="mt-1 text-sm text-red-600">{errors.frequencyType}</p>
        )}
      </div>

      {/* Target Count Field */}
      <div>
        <label htmlFor="targetCount" className="block text-sm font-medium text-gray-700 mb-1">
          Target Count *
        </label>
        <input
          type="number"
          id="targetCount"
          value={formData.targetCount}
          onChange={(e) => handleChange('targetCount', e.target.value)}
          min="1"
          className={`input-field ${errors.targetCount ? 'border-red-500 focus:border-red-500' : ''}`}
          disabled={loading}
        />
        {errors.targetCount && (
          <p className="mt-1 text-sm text-red-600">{errors.targetCount}</p>
        )}
      </div>

      {/* Reminder Time Field */}
      <div>
        <label htmlFor="reminderTime" className="block text-sm font-medium text-gray-700 mb-1">
          Reminder Time (Optional)
        </label>
        <input
          type="time"
          id="reminderTime"
          value={formData.reminderTime}
          onChange={(e) => handleChange('reminderTime', e.target.value)}
          className="input-field"
          disabled={loading}
        />
      </div>

      {/* Action Buttons */}
      <div className="flex justify-end gap-3 pt-4">
        <button
          type="button"
          onClick={onCancel}
          disabled={loading}
          className="btn-secondary"
        >
          Cancel
        </button>
        <button
          type="submit"
          disabled={loading}
          className="btn-primary flex items-center gap-2"
        >
          {loading ? (
            <>
              <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
              {habit ? 'Updating...' : 'Creating...'}
            </>
          ) : (
            habit ? 'Update Habit' : 'Create Habit'
          )}
        </button>
      </div>
    </form>
  )
}

export default HabitForm
