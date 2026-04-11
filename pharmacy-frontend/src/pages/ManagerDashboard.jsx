import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'
import StatusBadge from '../components/StatusBadge'

export default function ManagerDashboard() {
  const { user } = useAuth()
  const [prescriptions, setPrescriptions] = useState([])
  const [pendingLogs, setPendingLogs] = useState([])
  const [tab, setTab] = useState('prescriptions')
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    loadPrescriptions()
    loadPendingLogs()
  }, [])

  async function loadPrescriptions() {
    try {
      const res = await api.get('/prescriptions')
      setPrescriptions(res.data.filter(p => p.status === 'VERIFIED'))
    } catch { }
  }

  async function loadPendingLogs() {
    try {
      const res = await api.get('/controlled-logs/pending-approval')
      setPendingLogs(res.data)
    } catch { }
  }

  async function approve(id) {
    setMsg(''); setError('')
    try {
      await api.put(`/prescriptions/${id}/approve`, { managerId: user.id })
      setMsg(`Prescription #${id} approved.`)
      loadPrescriptions()
      loadPendingLogs()
    } catch (err) {
      setError(err.response?.data?.error || 'Approve failed')
    }
  }

  async function reject(id) {
    const reason = prompt('Rejection reason:')
    if (!reason) return
    setMsg(''); setError('')
    try {
      await api.put(`/prescriptions/${id}/reject`, { pharmacistId: user.id, rejectionReason: reason })
      setMsg(`Prescription #${id} rejected.`)
      loadPrescriptions()
    } catch (err) {
      setError(err.response?.data?.error || 'Reject failed')
    }
  }

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-6">Manager Dashboard</h2>

      {msg && <p className="mb-4 text-green-700 bg-green-50 border border-green-200 rounded px-3 py-2 text-sm">{msg}</p>}
      {error && <p className="mb-4 text-red-700 bg-red-50 border border-red-200 rounded px-3 py-2 text-sm">{error}</p>}

      <div className="flex gap-2 mb-4 border-b">
        <button
          onClick={() => setTab('prescriptions')}
          className={`px-4 py-2 text-sm ${tab === 'prescriptions' ? 'border-b-2 border-blue-700 font-medium' : 'text-gray-500'}`}
        >Verified Prescriptions ({prescriptions.length})</button>
        <button
          onClick={() => setTab('controlled')}
          className={`px-4 py-2 text-sm ${tab === 'controlled' ? 'border-b-2 border-blue-700 font-medium' : 'text-gray-500'}`}
        >Pending Controlled Approvals ({pendingLogs.length})</button>
      </div>

      {tab === 'prescriptions' && (
        <table className="w-full border text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-3 py-2 text-left">ID</th>
              <th className="px-3 py-2 text-left">Patient</th>
              <th className="px-3 py-2 text-left">Medication</th>
              <th className="px-3 py-2 text-left">Controlled</th>
              <th className="px-3 py-2 text-left">Doctor</th>
              <th className="px-3 py-2 text-left">Status</th>
              <th className="px-3 py-2 text-left">Actions</th>
            </tr>
          </thead>
          <tbody>
            {prescriptions.length === 0 && (
              <tr><td colSpan={7} className="px-4 py-4 text-center text-gray-400">No verified prescriptions pending approval.</td></tr>
            )}
            {prescriptions.map(p => (
              <tr key={p.id} className="border-t hover:bg-gray-50">
                <td className="px-3 py-2">{p.id}</td>
                <td className="px-3 py-2">{p.patient?.name}</td>
                <td className="px-3 py-2">{p.medication?.medicationName}</td>
                <td className="px-3 py-2">
                  {p.medication?.controlledSubstance
                    ? <span className="text-red-600 font-medium">Yes (Sched {p.medication.controlledSchedule})</span>
                    : 'No'}
                </td>
                <td className="px-3 py-2">{p.doctorName}</td>
                <td className="px-3 py-2"><StatusBadge status={p.status} /></td>
                <td className="px-3 py-2 flex gap-1">
                  <button onClick={() => approve(p.id)} className="bg-green-600 text-white px-2 py-1 rounded text-xs hover:bg-green-700">Approve</button>
                  <button onClick={() => reject(p.id)} className="bg-red-500 text-white px-2 py-1 rounded text-xs hover:bg-red-600">Reject</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {tab === 'controlled' && (
        <table className="w-full border text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-3 py-2 text-left">Log ID</th>
              <th className="px-3 py-2 text-left">Prescription ID</th>
              <th className="px-3 py-2 text-left">Patient</th>
              <th className="px-3 py-2 text-left">Prescribing Doctor</th>
              <th className="px-3 py-2 text-left">Pending</th>
            </tr>
          </thead>
          <tbody>
            {pendingLogs.length === 0 && (
              <tr><td colSpan={5} className="px-4 py-4 text-center text-gray-400">No pending controlled substance approvals.</td></tr>
            )}
            {pendingLogs.map(l => (
              <tr key={l.id} className="border-t hover:bg-gray-50">
                <td className="px-3 py-2">{l.id}</td>
                <td className="px-3 py-2">{l.prescriptionUpload?.id}</td>
                <td className="px-3 py-2">{l.prescriptionUpload?.patient?.name}</td>
                <td className="px-3 py-2">{l.prescribingDoctor}</td>
                <td className="px-3 py-2">
                  <span className="text-orange-600 font-medium">Pending</span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
