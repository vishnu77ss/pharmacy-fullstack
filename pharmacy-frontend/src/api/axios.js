import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
})

api.interceptors.request.use((config) => {
  const saved = localStorage.getItem('pharmacy_user')
  if (saved) {
    const user = JSON.parse(saved)
    config.headers['X-User-Role'] = user.role
    config.headers['X-User-Id'] = user.id
  }
  return config
})

export default api
