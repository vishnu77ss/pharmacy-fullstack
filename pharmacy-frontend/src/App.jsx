import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import Navbar from './components/Navbar'

import Login from './pages/Login'
import PatientDashboard from './pages/PatientDashboard'
import PharmacistDashboard from './pages/PharmacistDashboard'
import ManagerDashboard from './pages/ManagerDashboard'
import AdminDashboard from './pages/AdminDashboard'
import PrescriptionDetail from './pages/PrescriptionDetail'
import Reports from './pages/Reports'

function Layout({ children }) {
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <main>{children}</main>
    </div>
  )
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Navigate to="/login" replace />} />

          <Route path="/patient" element={
            <ProtectedRoute roles={['PATIENT']}>
              <Layout><PatientDashboard /></Layout>
            </ProtectedRoute>
          } />

          <Route path="/pharmacist" element={
            <ProtectedRoute roles={['PHARMACIST']}>
              <Layout><PharmacistDashboard /></Layout>
            </ProtectedRoute>
          } />

          <Route path="/manager" element={
            <ProtectedRoute roles={['PHARMACY_MANAGER']}>
              <Layout><ManagerDashboard /></Layout>
            </ProtectedRoute>
          } />

          <Route path="/admin" element={
            <ProtectedRoute roles={['ADMIN']}>
              <Layout><AdminDashboard /></Layout>
            </ProtectedRoute>
          } />

          <Route path="/prescription/:id" element={
            <ProtectedRoute>
              <Layout><PrescriptionDetail /></Layout>
            </ProtectedRoute>
          } />

          <Route path="/reports" element={
            <ProtectedRoute roles={['PHARMACY_MANAGER', 'ADMIN']}>
              <Layout><Reports /></Layout>
            </ProtectedRoute>
          } />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
