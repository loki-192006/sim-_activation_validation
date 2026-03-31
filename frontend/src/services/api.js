import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
})

// Response interceptor – unwrap ApiResponse wrapper
api.interceptors.response.use(
  (res) => res,
  (err) => {
    const message =
      err.response?.data?.message ||
      err.response?.data?.error ||
      'Something went wrong. Please try again.'
    return Promise.reject(new Error(message))
  }
)

export const simAPI = {
  validate: (simNumber) => api.get(`/sim/validate/${simNumber}`),
  activate: (payload) => api.post('/sim/activate', payload),
}

export const customerAPI = {
  validate: (payload) => api.post('/customer/validate', payload),
  updateAddress: (payload) => api.put('/customer/update-address', payload),
}

export const offerAPI = {
  getAll: () => api.get('/offers'),
}

export default api
