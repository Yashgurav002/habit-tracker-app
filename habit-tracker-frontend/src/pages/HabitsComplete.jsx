import { useState, useEffect } from 'react'
import { Plus, Edit, Trash2, Target, CheckCircle } from 'lucide-react'
import api from '../services/api'
import LoadingSpinner from '../components/LoadingSpinner'
import HabitForm from '../components/HabitForm'
import ConfirmDialog from '../components/ConfirmDialog'
import toast from 'react-hot-toast'

const Habits = () => {
  const [loading, setLoading] = useState(true)
  const [habits, setHabits] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [editingHabit, setEditingHabit] = useState(null)
  const [formLoading, setFormLoading] = useState(false)
  const [deleteDialog, setDeleteDialog] = useState({ isOpen: false, habit: null })
  const [deleteLoading, setDeleteLoading] = useState(false)

  useEffect(() => {
    loadHabits()
  }, [])

  const loadHabits = async () => {
    try {
      setLoading(true)
      const response = await api.get('/habits')
      
      if (response.data.success) {
        setHabits(response.data.data)
      }
    } catch (error) {
      console.error('Error loading habits:', error)
      toast.error('Failed to load habits')
    } finally {
      setLoading(false)
    }
  }

  const handleCreateHabit = () => {
    setEditingHabit(null)
    setShowForm(true)
  }

  const handleEditHabit = (habit) => {
    setEditingHabit(habit)
    setShowForm(true)
  }

  const handleFormSubmit = async (formData) => {
    try {
      setFormLoading(true)
      
      let response
      if (editingHabit) {
        // Update existing habit
        response = await api.put(`/habits/${editingHabit.id}`, formData)
      } else {
        // Create new habit
        response = await api.post('/habits', formData)
      }

      if (response.data.success) {
        toast.success(editingHabit ? 'Habit updated successfully!' : 'Habit created successfully!')
        setShowForm(false)
        setEditingHabit(null)
        loadHabits() // Refresh the list
      }
    } catch (error) {
      console.error('Error saving habit:', error)
      const message = error.response?.data?.message || 'Failed to save habit'
      toast.error(message)
    } finally {
      setFormLoading(false)
    }
  }

  const handleFormCancel = () => {
    setShowForm(false)
    setEditingHabit(null)
  }

  const handleDeleteClick = (habit) => {
    setDeleteDialog({ isOpen: true, habit })
  }

  const handleDeleteConfirm = async () => {
    try {
      setDeleteLoading(true)
      
      const response = await api.delete(`/habits/${deleteDialog.habit.id}`)
      
      if (response.data.success) {
        toast.success('Habit deleted successfully!')
        setDeleteDialog({ isOpen: false, habit: null })
        loadHabits() // Refresh the list
      }
    } catch (error) {
      console.error('Error deleting habit:', error)
      const message = error.response?.data?.message || 'Failed to delete habit'
      toast.error(message)
    } finally {
      setDeleteLoading(false)
    }
  }

  const handleDeleteCancel = () => {
    setDeleteDialog({ isOpen: false, habit: null })
  }

  const handleLogProgress = async (habit) => {
    try {
      const today = new Date().toISOString().split('T')[0] // YYYY-MM-DD format
      
      const entryData = {
        entryDate: today,
        completionCount: 1,
        isCompleted: true,
        notes: ''
      }

      const response = await api.post(`/habits/${habit.id}/entries`, entryData)
      
      if (response.data.success) {
        toast.success('Progress logged successfully!')
        loadHabits() // Refresh to update streak counts
      }
    } catch (error) {
      console.error('Error logging progress:', error)
      const message = error.response?.data?.message || 'Failed to log progress'
      toast.error(message)
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <LoadingSpinner />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">My Habits</h1>
          <p className="text-gray-600">Manage your daily, weekly, and monthly habits</p>
        </div>
        <button 
          onClick={handleCreateHabit}
          className="btn-primary flex items-center gap-2"
        >
          <Plus className="w-4 h-4" />
          Add New Habit
        </button>
      </div>

      {/* Habits Grid */}
      {habits.length === 0 ? (
        <div className="card text-center py-12">
          <Target className="w-16 h-16 text-gray-400 mx-auto mb-4" />
          <h2 className="text-xl font-medium text-gray-900 mb-2">No habits yet</h2>
          <p className="text-gray-600 mb-6">Start building positive habits by creating your first one!</p>
          <button 
            onClick={handleCreateHabit}
            className="btn-primary flex items-center gap-2 mx-auto"
          >
            <Plus className="w-4 h-4" />
            Create Your First Habit
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {habits.map((habit) => (
            <div key={habit.id} className="card">
              <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">{habit.name}</h3>
                  {habit.description && (
                    <p className="text-sm text-gray-600 mb-3">{habit.description}</p>
                  )}
                </div>
                <div className="flex gap-2 ml-4">
                  <button 
                    onClick={() => handleEditHabit(habit)}
                    className="p-2 text-gray-400 hover:text-primary-600 transition-colors"
                    title="Edit habit"
                  >
                    <Edit className="w-4 h-4" />
                  </button>
                  <button 
                    onClick={() => handleDeleteClick(habit)}
                    className="p-2 text-gray-400 hover:text-red-600 transition-colors"
                    title="Delete habit"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div className="space-y-3">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Frequency:</span>
                  <span className="font-medium text-gray-900">{habit.frequencyType}</span>
                </div>
                
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Target:</span>
                  <span className="font-medium text-gray-900">{habit.targetCount}</span>
                </div>

                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Current Streak:</span>
                  <span className="font-medium text-orange-600">{habit.streakCount} days</span>
                </div>

                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Best Streak:</span>
                  <span className="font-medium text-purple-600">{habit.bestStreak} days</span>
                </div>

                <div className="pt-3 border-t border-gray-200">
                  <button 
                    onClick={() => handleLogProgress(habit)}
                    className="btn-primary w-full text-sm flex items-center justify-center gap-2"
                  >
                    <CheckCircle className="w-4 h-4" />
                    Log Progress
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Habit Form Modal */}
      {showForm && (
        <HabitForm
          habit={editingHabit}
          onSubmit={handleFormSubmit}
          onCancel={handleFormCancel}
          loading={formLoading}
        />
      )}

      {/* Delete Confirmation Dialog */}
      <ConfirmDialog
        isOpen={deleteDialog.isOpen}
        title="Delete Habit"
        message={`Are you sure you want to delete "${deleteDialog.habit?.name}"? This action cannot be undone.`}
        confirmText="Delete"
        cancelText="Cancel"
        onConfirm={handleDeleteConfirm}
        onCancel={handleDeleteCancel}
        loading={deleteLoading}
        dangerous={true}
      />
    </div>
  )
}

export default Habits
