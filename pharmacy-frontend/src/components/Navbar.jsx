import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  const links = {
    PATIENT: [{ to: '/patient', label: 'My Prescriptions' }],
    PHARMACIST: [{ to: '/pharmacist', label: 'Prescriptions' }],
    PHARMACY_MANAGER: [
      { to: '/manager', label: 'Approvals' },
      { to: '/reports', label: 'Reports' },
    ],
    ADMIN: [
      { to: '/admin', label: 'Medications' },
      { to: '/reports', label: 'Reports' },
    ],
  }

  const roleLinks = user ? (links[user.role] || []) : []

  return (
    <nav className="bg-blue-700 text-white px-6 py-3 flex items-center justify-between">
      <div className="flex items-center gap-6">
        <span className="font-bold text-lg">PharmaSys</span>
        {roleLinks.map((l) => (
          <Link key={l.to} to={l.to} className="hover:underline text-sm">
            {l.label}
          </Link>
        ))}
      </div>
      {user && (
        <div className="flex items-center gap-4 text-sm">
          <span>{user.name} ({user.role})</span>
          <button onClick={handleLogout} className="bg-white text-blue-700 px-3 py-1 rounded hover:bg-gray-100">
            Logout
          </button>
        </div>
      )}
    </nav>
  )
}
