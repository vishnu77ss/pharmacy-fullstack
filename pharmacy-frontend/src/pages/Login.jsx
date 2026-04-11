import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

const ROLES = ['PATIENT', 'PHARMACIST', 'PHARMACY_MANAGER', 'ADMIN']

const ROLE_ROUTES = {
  PATIENT: '/patient',
  PHARMACIST: '/pharmacist',
  PHARMACY_MANAGER: '/manager',
  ADMIN: '/admin',
}

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()

  const [mode, setMode] = useState('login')
  const [role, setRole] = useState('PATIENT')
  const [users, setUsers] = useState([])
  const [selectedUserId, setSelectedUserId] = useState('')
  const [form, setForm] = useState({ name: '', email: '', licenseNumber: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function loadUsers(r) {
    setRole(r)
    setSelectedUserId('')
    try {
      const res = await api.get(`/users?role=${r}`)
      setUsers(res.data)
    } catch {
      setUsers([])
    }
  }

  async function handleLoginSubmit(e) {
    e.preventDefault()
    setError('')
    if (!selectedUserId) { setError('Please select a user'); return }
    const u = users.find(u => String(u.id) === String(selectedUserId))
    if (!u) { setError('User not found'); return }
    login(u)
    navigate(ROLE_ROUTES[u.role])
  }

  async function handleRegister(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await api.post('/users', { ...form, role })
      login(res.data)
      navigate(ROLE_ROUTES[res.data.role])
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded shadow w-full max-w-md">
        <h1 className="text-2xl font-bold mb-2 text-center">Pharmacy System</h1>
        <p className="text-center text-gray-500 mb-6 text-sm">Prescription Verification & Workflow</p>

        <div className="flex mb-6 border rounded overflow-hidden">
          <button
            className={`flex-1 py-2 text-sm font-medium ${mode === 'login' ? 'bg-blue-700 text-white' : 'bg-white text-gray-600'}`}
            onClick={() => setMode('login')}
          >Login</button>
          <button
            className={`flex-1 py-2 text-sm font-medium ${mode === 'register' ? 'bg-blue-700 text-white' : 'bg-white text-gray-600'}`}
            onClick={() => setMode('register')}
          >Register</button>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Select Role</label>
          <select
            className="w-full border px-3 py-2 rounded text-sm"
            value={role}
            onChange={e => loadUsers(e.target.value)}
          >
            {ROLES.map(r => <option key={r} value={r}>{r}</option>)}
          </select>
        </div>

        {error && <p className="text-red-600 text-sm mb-3">{error}</p>}

        {mode === 'login' ? (
          <form onSubmit={handleLoginSubmit}>
            <div className="mb-4">
              <label className="block text-sm font-medium mb-1">Select User</label>
              {users.length === 0 ? (
                <p className="text-sm text-gray-400 italic">No users found for this role. Register first.</p>
              ) : (
                <select
                  className="w-full border px-3 py-2 rounded text-sm"
                  value={selectedUserId}
                  onChange={e => setSelectedUserId(e.target.value)}
                >
                  <option value="">-- Select --</option>
                  {users.map(u => (
                    <option key={u.id} value={u.id}>{u.name} ({u.email})</option>
                  ))}
                </select>
              )}
            </div>
            <button type="submit" className="w-full bg-blue-700 text-white py-2 rounded hover:bg-blue-800 text-sm font-medium">
              Login
            </button>
          </form>
        ) : (
          <form onSubmit={handleRegister}>
            <div className="mb-3">
              <label className="block text-sm font-medium mb-1">Name</label>
              <input
                className="w-full border px-3 py-2 rounded text-sm"
                value={form.name}
                onChange={e => setForm({ ...form, name: e.target.value })}
                required
              />
            </div>
            <div className="mb-3">
              <label className="block text-sm font-medium mb-1">Email</label>
              <input
                type="email"
                className="w-full border px-3 py-2 rounded text-sm"
                value={form.email}
                onChange={e => setForm({ ...form, email: e.target.value })}
                required
              />
            </div>
            {(role === 'PHARMACIST' || role === 'PHARMACY_MANAGER') && (
              <div className="mb-3">
                <label className="block text-sm font-medium mb-1">License Number</label>
                <input
                  className="w-full border px-3 py-2 rounded text-sm"
                  value={form.licenseNumber}
                  onChange={e => setForm({ ...form, licenseNumber: e.target.value })}
                />
              </div>
            )}
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-700 text-white py-2 rounded hover:bg-blue-800 text-sm font-medium disabled:opacity-50"
            >
              {loading ? 'Registering...' : 'Register & Login'}
            </button>
          </form>
        )}
      </div>
    </div>
  )
}
