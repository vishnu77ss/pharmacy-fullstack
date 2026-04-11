import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'
import StatusBadge from '../components/StatusBadge'

export default function PharmacistDashboard() {
  const { user } = useAuth()
  const [prescriptions, setPrescriptions] = useState([])
  const [interactions, setInteractions] = useState({})
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')

  useEffect(() => { loadPrescriptions() }, [])

  async function loadPrescriptions() {
    try {
      const res = await api.get('/prescriptions')
      setPrescriptions(res.data)
    } catch { }
  }

  async function verify(id) {
    setMsg(''); setError('')
    try {
      await api.put(`/prescriptions/${id}/verify`, { pharmacistId: user.id })
      setMsg(`Prescription #${id} verified.`)
      loadPrescriptions()
    } catch (err) {
      setError(err.response?.data?.error || 'Verify failed')
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

  async function dispense(id) {
    const qty = prompt('Quantity to dispense:', '1')
    if (!qty) return
    setMsg(''); setError('')
    try {
      await api.post('/dispensing', { prescriptionId: id, pharmacistId: user.id, quantityDispensed: parseInt(qty) })
      setMsg(`Prescription #${id} dispensed.`)
      loadPrescriptions()
    } catch (err) {
      setError(err.response?.data?.error || 'Dispense failed')
    }
  }

  async function checkInteractions(id) {
    try {
      const res = await api.get(`/interactions/check/${id}`)
      setInteractions(prev => ({ ...prev, [id]: res.data }))
    } catch {
      setInteractions(prev => ({ ...prev, [id]: [] }))
    }
  }

  const severityColor = { MILD: 'text-yellow-600', MODERATE: 'text-orange-600', SEVERE: 'text-red-600', CONTRAINDICATED: 'text-red-800 font-bold' }

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-6">Pharmacist Dashboard</h2>

      {msg && <p className="mb-4 text-green-700 bg-green-50 border border-green-200 rounded px-3 py-2 text-sm">{msg}</p>}
      {error && <p className="mb-4 text-red-700 bg-red-50 border border-red-200 rounded px-3 py-2 text-sm">{error}</p>}

      <table className="w-full border text-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-3 py-2 text-left">ID</th>
            <th className="px-3 py-2 text-left">Patient</th>
            <th className="px-3 py-2 text-left">Medication</th>
            <th className="px-3 py-2 text-left">Doctor</th>
            <th className="px-3 py-2 text-left">Controlled</th>
            <th className="px-3 py-2 text-left">Status</th>
            <th className="px-3 py-2 text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {prescriptions.length === 0 && (
            <tr><td colSpan={7} className="px-4 py-4 text-center text-gray-400">No prescriptions.</td></tr>
          )}
          {prescriptions.map(p => (
            <>
              <tr key={p.id} className="border-t hover:bg-gray-50">
                <td className="px-3 py-2">{p.id}</td>
                <td className="px-3 py-2">{p.patient?.name}</td>
                <td className="px-3 py-2">{p.medication?.medicationName}</td>
                <td className="px-3 py-2">{p.doctorName}</td>
                <td className="px-3 py-2">
                  {p.medication?.controlledSubstance
                    ? <span className="text-red-600 font-medium">Yes ({p.medication.controlledSchedule})</span>
                    : 'No'}
                </td>
                <td className="px-3 py-2"><StatusBadge status={p.status} /></td>
                <td className="px-3 py-2 flex flex-wrap gap-1">
                  {(p.status === 'UPLOADED' || p.status === 'UNDER_VERIFICATION') && (
                    <>
                      <button onClick={() => verify(p.id)} className="bg-blue-600 text-white px-2 py-1 rounded text-xs hover:bg-blue-700">Verify</button>
                      <button onClick={() => reject(p.id)} className="bg-red-500 text-white px-2 py-1 rounded text-xs hover:bg-red-600">Reject</button>
                    </>
                  )}
                  {p.status === 'APPROVED' && (
                    <button onClick={() => dispense(p.id)} className="bg-green-600 text-white px-2 py-1 rounded text-xs hover:bg-green-700">Dispense</button>
                  )}
                  <button onClick={() => checkInteractions(p.id)} className="bg-yellow-500 text-white px-2 py-1 rounded text-xs hover:bg-yellow-600">
                    Check Interactions
                  </button>
                </td>
              </tr>
              {interactions[p.id] !== undefined && (
                <tr className="bg-yellow-50">
                  <td colSpan={7} className="px-6 py-2 text-xs">
                    {interactions[p.id].length === 0
                      ? <span className="text-green-600">No interactions found.</span>
                      : interactions[p.id].map(i => (
                        <div key={i.id} className={`mb-1 ${severityColor[i.severity] || ''}`}>
                          ⚠ {i.interactingMedication} — {i.severity}: {i.recommendation}
                        </div>
                      ))
                    }
                  </td>
                </tr>
              )}
            </>
          ))}
        </tbody>
      </table>
    </div>
  )
}
