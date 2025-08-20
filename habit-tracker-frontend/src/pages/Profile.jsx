import { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { User, Mail, Calendar, Save } from 'lucide-react'
import toast from 'react-hot-toast'
import api from '../services/api'

import LoadingSpinner from '../components/LoadingSpinner'


const Profile = () => {
  const { user, logout } = useAuth()
  const [loading, setLoading] = useState(false)
  const [statsLoading, setStatsLoading] = useState(true)

  const [formData, setFormData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    username: user?.username || '',
  })
  const [stats, setStats] = useState({
    totalHabits: 0,
    daysActive: 0,
    bestStreak: 0,
  })

  useEffect(() => {
    loadStats()
  }, [])

  const loadStats = async () => {
    try {
      setStatsLoading(true)
      const response = await api.get('/analytics')
      if (response.data.success) {
        const analytics = response.data.data
        setStats({
          totalHabits: analytics.totalHabits || 0,
          daysActive: analytics.totalActiveDays || 0, // assuming backend sends this
          bestStreak: analytics.longestStreak || 0,
        })
      }
    } catch (error) {
      console.error('Error loading profile stats:', error)
      toast.error('Failed to load account stats')
    } finally {
      setStatsLoading(false)
    }
  }
  

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      // TODO: Implement profile update API call
      toast.success('Profile updated successfully!')
    } catch (error) {
      toast.error('Failed to update profile')
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    logout()
    toast.success('Logged out successfully')
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Profile Settings</h1>
        <p className="text-gray-600">Manage your account settings and preferences</p>
      </div>

      {/* Profile Card */}
      <div className="card">
        <div className="flex items-center mb-6">
          <div className="w-20 h-20 bg-primary-100 rounded-full flex items-center justify-center">
            <User className="w-10 h-10 text-primary-600" />
          </div>
          <div className="ml-6">
            <h2 className="text-xl font-semibold text-gray-900">
              {user?.firstName} {user?.lastName}
            </h2>
            <p className="text-gray-600">{user?.email}</p>
            <p className="text-sm text-gray-500">Member since {new Date().toLocaleDateString()}</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
                First Name
              </label>
              <div className="mt-1 relative">
                <input
                  id="firstName"
                  name="firstName"
                  type="text"
                  required
                  className="input-field pl-10"
                  value={formData.firstName}
                  onChange={handleChange}
                  disabled={loading}
                />
                <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              </div>
            </div>

            <div>
              <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
                Last Name
              </label>
              <div className="mt-1 relative">
                <input
                  id="lastName"
                  name="lastName"
                  type="text"
                  required
                  className="input-field pl-10"
                  value={formData.lastName}
                  onChange={handleChange}
                  disabled={loading}
                />
                <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
              </div>
            </div>
          </div>

          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700">
              Username
            </label>
            <div className="mt-1 relative">
              <input
                id="username"
                name="username"
                type="text"
                required
                className="input-field pl-10"
                value={formData.username}
                onChange={handleChange}
                disabled={loading}
              />
              <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            </div>
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email Address
            </label>
            <div className="mt-1 relative">
              <input
                id="email"
                name="email"
                type="email"
                required
                className="input-field pl-10"
                value={formData.email}
                onChange={handleChange}
                disabled={loading}
              />
              <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            </div>
          </div>

          <div className="flex justify-between pt-6 border-t border-gray-200">
            <button
              type="button"
              onClick={handleLogout}
              className="btn-danger"
            >
              Sign Out
            </button>
            
            <button
              type="submit"
              disabled={loading}
              className="btn-primary flex items-center gap-2"
            >
              <Save className="w-4 h-4" />
              Save Changes
            </button>
          </div>
        </form>
      </div>

      {/* Account Stats */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Account Statistics</h3>
        
        {statsLoading ? (
          <div className="flex items-center justify-center h-24">
            <LoadingSpinner />
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center">
              <div className="text-2xl font-bold text-primary-600">
                {stats.totalHabits}
              </div>
              <div className="text-sm text-gray-600">Total Habits</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-green-600">
                {stats.daysActive}
              </div>
              <div className="text-sm text-gray-600">Days Active</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-orange-600">
                {stats.bestStreak}
              </div>
              <div className="text-sm text-gray-600">Best Streak</div>
            </div>
          </div>
        )}
      </div>

      {/* Preferences */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Preferences</h3>
        
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <div className="font-medium text-gray-900">Email Notifications</div>
              <div className="text-sm text-gray-600">Receive daily reminders and progress updates</div>
            </div>
            <input
              type="checkbox"
              className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
              defaultChecked
            />
          </div>
          
          <div className="flex items-center justify-between">
            <div>
              <div className="font-medium text-gray-900">Weekly Reports</div>
              <div className="text-sm text-gray-600">Get weekly progress summaries</div>
            </div>
            <input
              type="checkbox"
              className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
              defaultChecked
            />
          </div>
        </div>
      </div>
    </div>
  )
}

export default Profile
