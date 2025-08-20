import { useState, useEffect } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar, PieChart, Pie, Cell } from 'recharts'
import api from '../services/api'
import LoadingSpinner from '../components/LoadingSpinner'
import toast from 'react-hot-toast'

const Analytics = () => {
  const [loading, setLoading] = useState(true)
  const [analytics, setAnalytics] = useState(null)

  useEffect(() => {
    loadAnalytics()
  }, [])

  const loadAnalytics = async () => {
    try {
      setLoading(true)
      const response = await api.get('/analytics')
      
      if (response.data.success) {
        setAnalytics(response.data.data)
      }
    } catch (error) {
      console.error('Error loading analytics:', error)
      toast.error('Failed to load analytics')
    } finally {
      setLoading(false)
    }
  }

  // Sample data for charts (replace with real data from analytics)
  const streakData = [
    { date: '2024-01-01', streak: 5 },
    { date: '2024-01-02', streak: 6 },
    { date: '2024-01-03', streak: 7 },
    { date: '2024-01-04', streak: 8 },
    { date: '2024-01-05', streak: 0 },
    { date: '2024-01-06', streak: 1 },
    { date: '2024-01-07', streak: 2 },
  ]

  const habitData = [
    { habit: 'Exercise', completed: 20, missed: 5 },
    { habit: 'Reading', completed: 18, missed: 7 },
    { habit: 'Meditation', completed: 22, missed: 3 },
    { habit: 'Water Intake', completed: 24, missed: 1 },
  ]

  const completionData = [
    { name: 'Completed', value: 85, color: '#10B981' },
    { name: 'Missed', value: 15, color: '#EF4444' },
  ]

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
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Analytics</h1>
        <p className="text-gray-600">Track your progress and insights</p>
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card">
          <div className="text-2xl font-bold text-gray-900">{analytics?.totalHabits || 0}</div>
          <div className="text-sm text-gray-600">Total Habits</div>
        </div>
        <div className="card">
          <div className="text-2xl font-bold text-green-600">{analytics?.weeklyCompletionRate?.toFixed(1) || 0}%</div>
          <div className="text-sm text-gray-600">Weekly Completion</div>
        </div>
        <div className="card">
          <div className="text-2xl font-bold text-orange-600">{analytics?.currentStreak || 0}</div>
          <div className="text-sm text-gray-600">Current Streak</div>
        </div>
        <div className="card">
          <div className="text-2xl font-bold text-purple-600">{analytics?.longestStreak || 0}</div>
          <div className="text-sm text-gray-600">Best Streak</div>
        </div>
      </div>

      {/* Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Streak Progress Chart */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Streak Progress</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={streakData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="streak" stroke="#0ea5e9" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Completion Rate Pie Chart */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Overall Completion Rate</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={completionData}
                cx="50%"
                cy="50%"
                innerRadius={60}
                outerRadius={120}
                paddingAngle={5}
                dataKey="value"
              >
                {completionData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={entry.color} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        {/* Habit Performance Bar Chart */}
        <div className="card lg:col-span-2">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Habit Performance</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={habitData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="habit" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="completed" fill="#10B981" name="Completed" />
              <Bar dataKey="missed" fill="#EF4444" name="Missed" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Habit Streaks Table */}
      {analytics?.habitStreaks && (
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Habit Streaks</h3>
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-3 font-medium text-gray-900">Habit</th>
                  <th className="px-4 py-3 font-medium text-gray-900">Current Streak</th>
                  <th className="px-4 py-3 font-medium text-gray-900">Best Streak</th>
                  <th className="px-4 py-3 font-medium text-gray-900">Progress</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {analytics.habitStreaks.map((habit) => (
                  <tr key={habit.habitId}>
                    <td className="px-4 py-4 font-medium text-gray-900">{habit.habitName}</td>
                    <td className="px-4 py-4 text-orange-600">{habit.currentStreak} days</td>
                    <td className="px-4 py-4 text-purple-600">{habit.bestStreak} days</td>
                    <td className="px-4 py-4">
                      <div className="w-full bg-gray-200 rounded-full h-2">
                        <div
                          className="bg-primary-600 h-2 rounded-full"
                          style={{
                            width: `${Math.min((habit.currentStreak / habit.bestStreak) * 100, 100)}%`
                          }}
                        />
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}

export default Analytics
