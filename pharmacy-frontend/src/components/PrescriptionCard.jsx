import { useNavigate } from 'react-router-dom'
import StatusBadge from './StatusBadge'

export default function PrescriptionCard({ prescription }) {
  const navigate = useNavigate()
  const med = prescription.medication
  const patient = prescription.patient

  return (
    <tr className="border-b hover:bg-gray-50">
      <td className="px-4 py-2 text-sm">{prescription.id}</td>
      <td className="px-4 py-2 text-sm">{patient?.name || '-'}</td>
      <td className="px-4 py-2 text-sm">{med?.medicationName || '-'}</td>
      <td className="px-4 py-2 text-sm">{prescription.doctorName}</td>
      <td className="px-4 py-2 text-sm">{prescription.dosage}</td>
      <td className="px-4 py-2 text-sm">{prescription.remainingRefills}</td>
      <td className="px-4 py-2 text-sm">
        <StatusBadge status={prescription.status} />
      </td>
      <td className="px-4 py-2 text-sm">
        <button
          onClick={() => navigate(`/prescription/${prescription.id}`)}
          className="text-blue-600 hover:underline"
        >
          View
        </button>
      </td>
    </tr>
  )
}
