import { useState, useEffect } from 'react'
import api from '../api/axios'

export default function Reports() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/reports').then(res => {
      setData(res.data)
      setLoading(false)
    }).catch(() => setLoading(false))
  }, [])

  if (loading) return <div className="p-6 text-gray-500">Loading reports...</div>
  if (!data) return <div className="p-6 text-red-500">Failed to load reports.</div>

  const statusStats = data.prescriptionsByStatus || []

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-6">Reports & Analytics</h2>

      <div className="grid grid-cols-3 gap-4 mb-8">
        <div className="border rounded p-4 text-center">
          <p className="text-3xl font-bold text-blue-700">{data.totalDispensing ?? 0}</p>
          <p className="text-sm text-gray-500 mt-1">Total Dispensing Records</p>
        </div>
        <div className="border rounded p-4 text-center">
          <p className="text-3xl font-bold text-green-700">{data.totalRefills ?? 0}</p>
          <p className="text-sm text-gray-500 mt-1">Total Refills Processed</p>
        </div>
        <div className="border rounded p-4 text-center">
          <p className="text-3xl font-bold text-orange-600">{data.totalInteractions ?? 0}</p>
          <p className="text-sm text-gray-500 mt-1">Drug Interactions Logged</p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="border rounded p-4">
          <h3 className="font-semibold mb-3 text-sm">Prescriptions by Status</h3>
          {statusStats.length === 0
            ? <p className="text-sm text-gray-400">No data.</p>
            : (
              <table className="w-full text-sm">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-3 py-2 text-left">Status</th>
                    <th className="px-3 py-2 text-left">Count</th>
                  </tr>
                </thead>
                <tbody>
                  {statusStats.map((row, i) => (
                    <tr key={i} className="border-t">
                      <td className="px-3 py-2">{String(row[1])}</td>
                      <td className="px-3 py-2 font-medium">{String(row[0])}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
        </div>

        <div className="border rounded p-4">
          <h3 className="font-semibold mb-3 text-sm">Monthly Dispensing</h3>
          {(data.monthlyDispensing || []).length === 0
            ? <p className="text-sm text-gray-400">No dispensing data yet.</p>
            : (
              <table className="w-full text-sm">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-3 py-2 text-left">Month</th>
                    <th className="px-3 py-2 text-left">Count</th>
                  </tr>
                </thead>
                <tbody>
                  {(data.monthlyDispensing || []).map((row, i) => (
                    <tr key={i} className="border-t">
                      <td className="px-3 py-2">Month {String(row[1])}</td>
                      <td className="px-3 py-2 font-medium">{String(row[0])}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
        </div>
      </div>
    </div>
  )
}
