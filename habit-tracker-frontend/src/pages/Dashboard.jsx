import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { Target, Calendar, TrendingUp, Award } from 'lucide-react'
import api from '../services/api'
import LoadingSpinner from '../components/LoadingSpinner'
import toast from 'react-hot-toast'

const Dashboard = () => {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState({
    totalHabits: 0,
    completedToday: 0,
    currentStreak: 0,
    bestStreak: 0,
  })
  const [habits, setHabits] = useState([])

  useEffect(() => {
    loadDashboardData()
  }, [])

  const loadDashboardData = async () => {
    try {
      setLoading(true)
      
      // Load habits and analytics data
      const [habitsResponse, analyticsResponse] = await Promise.all([
        api.get('/habits'),
        api.get('/analytics')
      ])

      if (habitsResponse.data.success) {
        setHabits(habitsResponse.data.data)
      }

      if (analyticsResponse.data.success) {
        const analytics = analyticsResponse.data.data
        setStats({
          totalHabits: analytics.totalHabits || 0,
          completedToday: analytics.completedEntriesToday || 0,
          currentStreak: analytics.currentStreak || 0,
          bestStreak: analytics.longestStreak || 0,
        })
      }
    } catch (error) {
      console.error('Error loading dashboard:', error)
      toast.error('Failed to load dashboard data')
    } finally {
      setLoading(false)
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
      {/* Welcome Section */}
      <div className="bg-gradient-to-r from-primary-600 to-primary-700 rounded-lg p-6 text-white">
        <h1 className="text-2xl font-bold mb-2">
          Welcome back, {user?.firstName || 'User'}! 👋
        </h1>
        <p className="text-primary-100">
          Ready to continue your habit journey? Let's see how you're doing today.
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card flex items-center">
          <div className="p-3 bg-primary-100 rounded-lg">
            <Target className="w-6 h-6 text-primary-600" />
          </div>
          <div className="ml-4">
            <p className="text-2xl font-bold text-gray-900">{stats.totalHabits}</p>
            <p className="text-sm text-gray-600">Total Habits</p>
          </div>
        </div>

        <div className="card flex items-center">
          <div className="p-3 bg-green-100 rounded-lg">
            <Calendar className="w-6 h-6 text-green-600" />
          </div>
          <div className="ml-4">
            <p className="text-2xl font-bold text-gray-900">{stats.completedToday}</p>
            <p className="text-sm text-gray-600">Completed Today</p>
          </div>
        </div>

        <div className="card flex items-center">
          <div className="p-3 bg-orange-100 rounded-lg">
            <TrendingUp className="w-6 h-6 text-orange-600" />
          </div>
          <div className="ml-4">
            <p className="text-2xl font-bold text-gray-900">{stats.currentStreak}</p>
            <p className="text-sm text-gray-600">Current Streak</p>
          </div>
        </div>

        <div className="card flex items-center">
          <div className="p-3 bg-purple-100 rounded-lg">
            <Award className="w-6 h-6 text-purple-600" />
          </div>
          <div className="ml-4">
            <p className="text-2xl font-bold text-gray-900">{stats.bestStreak}</p>
            <p className="text-sm text-gray-600">Best Streak</p>
          </div>
        </div>
      </div>

      {/* Recent Habits */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-bold text-gray-900">Your Habits</h2>
          <button 
            onClick={() => navigate('/habits')}
            className="btn-primary text-sm"
          >
            View All
          </button>
        </div>
        
        {habits.length === 0 ? (
          <div className="text-center py-8">
            <Target className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-600 mb-4">No habits yet. Start building your first habit!</p>
            <button 
              onClick={() => navigate('/habits')}
              className="btn-primary"
            >
              Create Your First Habit
            </button>
          </div>
        ) : (
          <div className="space-y-3">
            {habits.slice(0, 5).map((habit) => (
              <div key={habit.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div>
                  <h3 className="font-medium text-gray-900">{habit.name}</h3>
                  <p className="text-sm text-gray-600">
                    {habit.frequencyType} • Target: {habit.targetCount}
                  </p>
                </div>
                <div className="text-right">
                  <p className="text-sm font-medium text-gray-900">
                    Streak: {habit.streakCount}
                  </p>
                  <p className="text-sm text-gray-600">
                    Best: {habit.bestStreak}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default Dashboard
