const colors = {
  UPLOADED: 'bg-gray-200 text-gray-800',
  UNDER_VERIFICATION: 'bg-yellow-100 text-yellow-800',
  VERIFIED: 'bg-blue-100 text-blue-800',
  REJECTED: 'bg-red-100 text-red-800',
  APPROVED: 'bg-green-100 text-green-800',
  DISPENSED: 'bg-purple-100 text-purple-800',
  EXPIRED: 'bg-orange-100 text-orange-800',
}

export default function StatusBadge({ status }) {
  return (
    <span className={`px-2 py-0.5 rounded text-xs font-medium ${colors[status] || 'bg-gray-100'}`}>
      {status}
    </span>
  )
}
