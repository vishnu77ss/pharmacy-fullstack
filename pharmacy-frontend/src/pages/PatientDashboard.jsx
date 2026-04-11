import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'
import StatusBadge from '../components/StatusBadge'

export default function PatientDashboard() {
  const { user } = useAuth()
  const [prescriptions, setPrescriptions] = useState([])
  const [medications, setMedications] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({
    medicationId: '',
    doctorName: '',
    doctorRegistrationNumber: '',
    dosage: '',
    frequency: '',
    duration: '',
    refillsRequested: 0,
    prescriptionImageUrl: '',
  })
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    loadPrescriptions()
    loadMedications()
  }, [])

  async function loadPrescriptions() {
    try {
      const res = await api.get(`/prescriptions/patient/${user.id}`)
      setPrescriptions(res.data)
    } catch { }
  }

  async function loadMedications() {
    try {
      const res = await api.get('/medications')
      setMedications(res.data)
    } catch { }
  }

  async function handleUpload(e) {
    e.preventDefault()
    setError('')
    setMsg('')
    try {
      await api.post('/prescriptions/upload', { ...form, patientId: user.id })
      setMsg('Prescription uploaded successfully!')
      setShowForm(false)
      setForm({ medicationId: '', doctorName: '', doctorRegistrationNumber: '', dosage: '', frequency: '', duration: '', refillsRequested: 0, prescriptionImageUrl: '' })
      loadPrescriptions()
    } catch (err) {
      setError(err.response?.data?.error || 'Upload failed')
    }
  }

  async function handleRefillRequest(prescriptionId) {
    setError('')
    setMsg('')
    try {
      await api.post('/dispensing/refill', { prescriptionId, pharmacistId: user.id, quantityDispensed: 1 })
      setMsg('Refill requested successfully!')
      loadPrescriptions()
    } catch (err) {
      setError(err.response?.data?.error || 'Refill request failed')
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold">My Prescriptions</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-700 text-white px-4 py-2 rounded text-sm hover:bg-blue-800"
        >
          {showForm ? 'Cancel' : '+ Upload Prescription'}
        </button>
      </div>

      {msg && <p className="mb-4 text-green-700 bg-green-50 border border-green-200 rounded px-3 py-2 text-sm">{msg}</p>}
      {error && <p className="mb-4 text-red-700 bg-red-50 border border-red-200 rounded px-3 py-2 text-sm">{error}</p>}

      {showForm && (
        <div className="border rounded p-4 mb-6 bg-gray-50">
          <h3 className="font-semibold mb-4 text-sm">Upload New Prescription</h3>
          <form onSubmit={handleUpload} className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs font-medium mb-1">Medication</label>
              <select
                className="w-full border px-2 py-1.5 rounded text-sm"
                value={form.medicationId}
                onChange={e => setForm({ ...form, medicationId: e.target.value })}
                required
              >
                <option value="">-- Select --</option>
                {medications.map(m => (
                  <option key={m.id} value={m.id}>{m.medicationName}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Doctor Name</label>
              <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.doctorName}
                onChange={e => setForm({ ...form, doctorName: e.target.value })} required />
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Doctor Reg. No.</label>
              <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.doctorRegistrationNumber}
                onChange={e => setForm({ ...form, doctorRegistrationNumber: e.target.value })} required />
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Dosage</label>
              <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.dosage}
                onChange={e => setForm({ ...form, dosage: e.target.value })} />
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Frequency</label>
              <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.frequency}
                onChange={e => setForm({ ...form, frequency: e.target.value })} />
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Duration</label>
              <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.duration}
                onChange={e => setForm({ ...form, duration: e.target.value })} />
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Refills Requested</label>
              <input type="number" min="0" className="w-full border px-2 py-1.5 rounded text-sm" value={form.refillsRequested}
                onChange={e => setForm({ ...form, refillsRequested: parseInt(e.target.value) })} />
            </div>
            <div>
              <label className="block text-xs font-medium mb-1">Prescription Image URL</label>
              <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.prescriptionImageUrl}
                onChange={e => setForm({ ...form, prescriptionImageUrl: e.target.value })} />
            </div>
            <div className="col-span-2">
              <button type="submit" className="bg-blue-700 text-white px-4 py-2 rounded text-sm hover:bg-blue-800">
                Submit Prescription
              </button>
            </div>
          </form>
        </div>
      )}

      <table className="w-full border text-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-4 py-2 text-left">ID</th>
            <th className="px-4 py-2 text-left">Medication</th>
            <th className="px-4 py-2 text-left">Doctor</th>
            <th className="px-4 py-2 text-left">Dosage</th>
            <th className="px-4 py-2 text-left">Refills Left</th>
            <th className="px-4 py-2 text-left">Status</th>
            <th className="px-4 py-2 text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {prescriptions.length === 0 && (
            <tr><td colSpan={7} className="px-4 py-4 text-center text-gray-400">No prescriptions found.</td></tr>
          )}
          {prescriptions.map(p => (
            <tr key={p.id} className="border-t hover:bg-gray-50">
              <td className="px-4 py-2">{p.id}</td>
              <td className="px-4 py-2">{p.medication?.medicationName}</td>
              <td className="px-4 py-2">{p.doctorName}</td>
              <td className="px-4 py-2">{p.dosage}</td>
              <td className="px-4 py-2">{p.remainingRefills}</td>
              <td className="px-4 py-2"><StatusBadge status={p.status} /></td>
              <td className="px-4 py-2">
                {p.status === 'DISPENSED' && p.remainingRefills > 0 && (
                  <button
                    onClick={() => handleRefillRequest(p.id)}
                    className="text-sm bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                  >
                    Request Refill
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
