import { useState, useEffect } from 'react'
import api from '../api/axios'

const SCHEDULES = ['II', 'III', 'IV', 'V']

export default function AdminDashboard() {
  const [medications, setMedications] = useState([])
  const [users, setUsers] = useState([])
  const [tab, setTab] = useState('medications')
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({
    medicationName: '', genericName: '', drugClass: '',
    isControlledSubstance: false, controlledSchedule: '',
    maxRefillsAllowed: 0, requiresPrescription: true, contraindications: ''
  })
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    loadMedications()
    loadUsers()
  }, [])

  async function loadMedications() {
    try {
      const res = await api.get('/medications')
      setMedications(res.data)
    } catch { }
  }

  async function loadUsers() {
    try {
      const res = await api.get('/users')
      setUsers(res.data)
    } catch { }
  }

  async function handleAddMedication(e) {
    e.preventDefault()
    setMsg(''); setError('')
    try {
      const payload = { ...form }
      if (!payload.isControlledSubstance) payload.controlledSchedule = null
      await api.post('/medications', payload)
      setMsg('Medication added!')
      setShowForm(false)
      setForm({ medicationName: '', genericName: '', drugClass: '', isControlledSubstance: false, controlledSchedule: '', maxRefillsAllowed: 0, requiresPrescription: true, contraindications: '' })
      loadMedications()
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to add medication')
    }
  }

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-6">Admin Dashboard</h2>

      {msg && <p className="mb-4 text-green-700 bg-green-50 border border-green-200 rounded px-3 py-2 text-sm">{msg}</p>}
      {error && <p className="mb-4 text-red-700 bg-red-50 border border-red-200 rounded px-3 py-2 text-sm">{error}</p>}

      <div className="flex gap-2 mb-4 border-b">
        <button onClick={() => setTab('medications')} className={`px-4 py-2 text-sm ${tab === 'medications' ? 'border-b-2 border-blue-700 font-medium' : 'text-gray-500'}`}>
          Medications ({medications.length})
        </button>
        <button onClick={() => setTab('users')} className={`px-4 py-2 text-sm ${tab === 'users' ? 'border-b-2 border-blue-700 font-medium' : 'text-gray-500'}`}>
          Users ({users.length})
        </button>
      </div>

      {tab === 'medications' && (
        <>
          <div className="mb-4">
            <button onClick={() => setShowForm(!showForm)} className="bg-blue-700 text-white px-4 py-2 rounded text-sm hover:bg-blue-800">
              {showForm ? 'Cancel' : '+ Add Medication'}
            </button>
          </div>

          {showForm && (
            <div className="border rounded p-4 mb-6 bg-gray-50">
              <h3 className="font-semibold mb-4 text-sm">Add New Medication</h3>
              <form onSubmit={handleAddMedication} className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium mb-1">Medication Name *</label>
                  <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.medicationName}
                    onChange={e => setForm({ ...form, medicationName: e.target.value })} required />
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1">Generic Name</label>
                  <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.genericName}
                    onChange={e => setForm({ ...form, genericName: e.target.value })} />
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1">Drug Class</label>
                  <input className="w-full border px-2 py-1.5 rounded text-sm" value={form.drugClass}
                    onChange={e => setForm({ ...form, drugClass: e.target.value })} />
                </div>
                <div>
                  <label className="block text-xs font-medium mb-1">Max Refills Allowed</label>
                  <input type="number" min="0" className="w-full border px-2 py-1.5 rounded text-sm" value={form.maxRefillsAllowed}
                    onChange={e => setForm({ ...form, maxRefillsAllowed: parseInt(e.target.value) })} />
                </div>
                <div className="flex items-center gap-3">
                  <input type="checkbox" id="controlled" checked={form.isControlledSubstance}
                    onChange={e => setForm({ ...form, isControlledSubstance: e.target.checked })} />
                  <label htmlFor="controlled" className="text-sm">Controlled Substance</label>
                </div>
                <div className="flex items-center gap-3">
                  <input type="checkbox" id="reqPrescription" checked={form.requiresPrescription}
                    onChange={e => setForm({ ...form, requiresPrescription: e.target.checked })} />
                  <label htmlFor="reqPrescription" className="text-sm">Requires Prescription</label>
                </div>
                {form.isControlledSubstance && (
                  <div>
                    <label className="block text-xs font-medium mb-1">Schedule</label>
                    <select className="w-full border px-2 py-1.5 rounded text-sm" value={form.controlledSchedule}
                      onChange={e => setForm({ ...form, controlledSchedule: e.target.value })}>
                      <option value="">-- Select --</option>
                      {SCHEDULES.map(s => <option key={s} value={s}>Schedule {s}</option>)}
                    </select>
                  </div>
                )}
                <div className="col-span-2">
                  <label className="block text-xs font-medium mb-1">Contraindications</label>
                  <textarea className="w-full border px-2 py-1.5 rounded text-sm" rows={2} value={form.contraindications}
                    onChange={e => setForm({ ...form, contraindications: e.target.value })} />
                </div>
                <div className="col-span-2">
                  <button type="submit" className="bg-blue-700 text-white px-4 py-2 rounded text-sm hover:bg-blue-800">
                    Add Medication
                  </button>
                </div>
              </form>
            </div>
          )}

          <table className="w-full border text-sm">
            <thead className="bg-gray-100">
              <tr>
                <th className="px-3 py-2 text-left">ID</th>
                <th className="px-3 py-2 text-left">Name</th>
                <th className="px-3 py-2 text-left">Generic Name</th>
                <th className="px-3 py-2 text-left">Drug Class</th>
                <th className="px-3 py-2 text-left">Controlled</th>
                <th className="px-3 py-2 text-left">Schedule</th>
                <th className="px-3 py-2 text-left">Max Refills</th>
                <th className="px-3 py-2 text-left">Req. Prescription</th>
              </tr>
            </thead>
            <tbody>
              {medications.length === 0 && (
                <tr><td colSpan={8} className="px-4 py-4 text-center text-gray-400">No medications found.</td></tr>
              )}
              {medications.map(m => (
                <tr key={m.id} className="border-t hover:bg-gray-50">
                  <td className="px-3 py-2">{m.id}</td>
                  <td className="px-3 py-2 font-medium">{m.medicationName}</td>
                  <td className="px-3 py-2">{m.genericName || '-'}</td>
                  <td className="px-3 py-2">{m.drugClass || '-'}</td>
                  <td className="px-3 py-2">
                    {m.controlledSubstance
                      ? <span className="text-red-600 font-medium">Yes</span>
                      : <span className="text-gray-500">No</span>}
                  </td>
                  <td className="px-3 py-2">{m.controlledSchedule || '-'}</td>
                  <td className="px-3 py-2">{m.maxRefillsAllowed}</td>
                  <td className="px-3 py-2">{m.requiresPrescription ? 'Yes' : 'No'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}

      {tab === 'users' && (
        <table className="w-full border text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="px-3 py-2 text-left">ID</th>
              <th className="px-3 py-2 text-left">Name</th>
              <th className="px-3 py-2 text-left">Email</th>
              <th className="px-3 py-2 text-left">Role</th>
              <th className="px-3 py-2 text-left">License No.</th>
              <th className="px-3 py-2 text-left">Created At</th>
            </tr>
          </thead>
          <tbody>
            {users.length === 0 && (
              <tr><td colSpan={6} className="px-4 py-4 text-center text-gray-400">No users found.</td></tr>
            )}
            {users.map(u => (
              <tr key={u.id} className="border-t hover:bg-gray-50">
                <td className="px-3 py-2">{u.id}</td>
                <td className="px-3 py-2">{u.name}</td>
                <td className="px-3 py-2">{u.email}</td>
                <td className="px-3 py-2">
                  <span className="bg-blue-100 text-blue-800 px-2 py-0.5 rounded text-xs">{u.role}</span>
                </td>
                <td className="px-3 py-2">{u.licenseNumber || '-'}</td>
                <td className="px-3 py-2">{u.createdAt ? new Date(u.createdAt).toLocaleDateString() : '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
