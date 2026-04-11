import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import api from '../api/axios'
import StatusBadge from '../components/StatusBadge'

export default function PrescriptionDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [prescription, setPrescription] = useState(null)
  const [dispensing, setDispensing] = useState([])
  const [interactions, setInteractions] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadAll()
  }, [id])

  async function loadAll() {
    setLoading(true)
    try {
      const [pRes, dRes, iRes] = await Promise.all([
        api.get(`/prescriptions/${id}`),
        api.get(`/dispensing/prescription/${id}`),
        api.get(`/interactions/check/${id}`),
      ])
      setPrescription(pRes.data)
      setDispensing(dRes.data)
      setInteractions(iRes.data)
    } catch { }
    setLoading(false)
  }

  if (loading) return <div className="p-6 text-gray-500">Loading...</div>
  if (!prescription) return <div className="p-6 text-red-500">Prescription not found.</div>

  const med = prescription.medication
  const patient = prescription.patient

  const severityColor = { MILD: 'text-yellow-600', MODERATE: 'text-orange-600', SEVERE: 'text-red-600', CONTRAINDICATED: 'text-red-800 font-bold' }

  return (
    <div className="p-6 max-w-4xl">
      <div className="flex items-center gap-3 mb-6">
        <button onClick={() => navigate(-1)} className="text-blue-600 hover:underline text-sm">← Back</button>
        <h2 className="text-xl font-bold">Prescription #{prescription.id}</h2>
        <StatusBadge status={prescription.status} />
      </div>

      <div className="grid grid-cols-2 gap-6 mb-6">
        <div className="border rounded p-4">
          <h3 className="font-semibold mb-3 text-sm text-gray-700 uppercase tracking-wide">Patient Info</h3>
          <table className="w-full text-sm">
            <tbody>
              <tr><td className="py-1 text-gray-500 w-1/2">Name</td><td>{patient?.name}</td></tr>
              <tr><td className="py-1 text-gray-500">Email</td><td>{patient?.email}</td></tr>
              <tr><td className="py-1 text-gray-500">Role</td><td>{patient?.role}</td></tr>
            </tbody>
          </table>
        </div>

        <div className="border rounded p-4">
          <h3 className="font-semibold mb-3 text-sm text-gray-700 uppercase tracking-wide">Medication Info</h3>
          <table className="w-full text-sm">
            <tbody>
              <tr><td className="py-1 text-gray-500 w-1/2">Name</td><td>{med?.medicationName}</td></tr>
              <tr><td className="py-1 text-gray-500">Generic</td><td>{med?.genericName || '-'}</td></tr>
              <tr><td className="py-1 text-gray-500">Drug Class</td><td>{med?.drugClass || '-'}</td></tr>
              <tr><td className="py-1 text-gray-500">Controlled</td>
                <td>{med?.controlledSubstance ? <span className="text-red-600 font-medium">Yes (Sched {med.controlledSchedule})</span> : 'No'}</td>
              </tr>
              <tr><td className="py-1 text-gray-500">Max Refills</td><td>{med?.maxRefillsAllowed}</td></tr>
            </tbody>
          </table>
        </div>

        <div className="border rounded p-4">
          <h3 className="font-semibold mb-3 text-sm text-gray-700 uppercase tracking-wide">Prescription Details</h3>
          <table className="w-full text-sm">
            <tbody>
              <tr><td className="py-1 text-gray-500 w-1/2">Doctor</td><td>{prescription.doctorName}</td></tr>
              <tr><td className="py-1 text-gray-500">Reg. No.</td><td>{prescription.doctorRegistrationNumber}</td></tr>
              <tr><td className="py-1 text-gray-500">Dosage</td><td>{prescription.dosage || '-'}</td></tr>
              <tr><td className="py-1 text-gray-500">Frequency</td><td>{prescription.frequency || '-'}</td></tr>
              <tr><td className="py-1 text-gray-500">Duration</td><td>{prescription.duration || '-'}</td></tr>
              <tr><td className="py-1 text-gray-500">Refills Left</td><td>{prescription.remainingRefills}</td></tr>
              <tr><td className="py-1 text-gray-500">Uploaded</td><td>{new Date(prescription.uploadedAt).toLocaleString()}</td></tr>
            </tbody>
          </table>
        </div>

        <div className="border rounded p-4">
          <h3 className="font-semibold mb-3 text-sm text-gray-700 uppercase tracking-wide">Drug Interactions ({interactions.length})</h3>
          {interactions.length === 0
            ? <p className="text-sm text-green-600">No interactions on record.</p>
            : interactions.map(i => (
              <div key={i.id} className={`mb-2 text-sm ${severityColor[i.severity] || ''}`}>
                <strong>{i.interactingMedication}</strong> — {i.severity}
                <p className="text-gray-600 text-xs mt-0.5">{i.recommendation}</p>
              </div>
            ))}
        </div>
      </div>

      <div className="border rounded p-4">
        <h3 className="font-semibold mb-3 text-sm text-gray-700 uppercase tracking-wide">Dispensing History ({dispensing.length})</h3>
        {dispensing.length === 0
          ? <p className="text-sm text-gray-400">No dispensing records yet.</p>
          : (
            <table className="w-full text-sm">
              <thead className="bg-gray-100">
                <tr>
                  <th className="px-3 py-2 text-left">Refill #</th>
                  <th className="px-3 py-2 text-left">Dispensed By</th>
                  <th className="px-3 py-2 text-left">Quantity</th>
                  <th className="px-3 py-2 text-left">Date</th>
                  <th className="px-3 py-2 text-left">Expiry</th>
                </tr>
              </thead>
              <tbody>
                {dispensing.map(d => (
                  <tr key={d.id} className="border-t">
                    <td className="px-3 py-2">{d.refillNumber}</td>
                    <td className="px-3 py-2">{d.dispensedBy?.name}</td>
                    <td className="px-3 py-2">{d.quantityDispensed}</td>
                    <td className="px-3 py-2">{new Date(d.dispensingDate).toLocaleString()}</td>
                    <td className="px-3 py-2">{d.expiryDate}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
      </div>
    </div>
  )
}
