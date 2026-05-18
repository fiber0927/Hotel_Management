import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

api.interceptors.response.use(
  response => response,
  error => {
    // Only intercept 401 for non-login endpoints
    if (error.response && error.response.status === 401) {
      const url = error.config.url || ''
      if (!url.includes('/login')) {
        sessionStorage.removeItem('user')
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default {
  // Customer
  customerLogin(username, password) {
    return api.post('/customer/login', { username, password })
  },
  customerRegister(username, password, phone) {
    return api.post('/customer/register', { username, password, phone })
  },

  // Admin
  login(username, password) {
    return api.post('/admin/login', { username, password })
  },

  // Room
  getRooms() { return api.get('/rooms') },
  getRoom(id) { return api.get(`/rooms/${id}`) },
  getAvailableRooms() { return api.get('/rooms/available') },
  getRoomsByFloor(floor) { return api.get(`/rooms/floor/${floor}`) },
  getRoomsByType(roomType) { return api.get(`/rooms/type/${roomType}`) },
  getRoomsByFloorAndType(floor, roomType) { return api.get(`/rooms/floor/${floor}/type/${roomType}`) },
  getRoomStats() { return api.get('/rooms/stats') },
  addRoom(room) { return api.post('/rooms', room) },
  updateRoom(id, room) { return api.put(`/rooms/${id}`, room) },
  updateRoomStatus(id, status) { return api.patch(`/rooms/${id}/status`, { status }) },
  deleteRoom(id) { return api.delete(`/rooms/${id}`) },

  // Guest
  getGuests() { return api.get('/guests') },
  getGuest(id) { return api.get(`/guests/${id}`) },
  addGuest(guest) { return api.post('/guests', guest) },
  updateGuest(id, guest) { return api.put(`/guests/${id}`, guest) },
  deleteGuest(id) { return api.delete(`/guests/${id}`) },

  // Booking
  getBookings() { return api.get('/bookings') },
  getBooking(id) { return api.get(`/bookings/${id}`) },
  getBookingsByGuest(customerId) { return api.get(`/bookings/guest/${customerId}`) },
  getBookingsByStatus(status) { return api.get(`/bookings/status/${status}`) },
  addBooking(booking) { return api.post('/bookings', booking) },
  updateBooking(id, booking) { return api.put(`/bookings/${id}`, booking) },
  confirmBooking(id) { return api.patch(`/bookings/${id}/confirm`) },
  cancelBooking(id) { return api.patch(`/bookings/${id}/cancel`) },
  rejectBooking(id) { return api.patch(`/bookings/${id}/reject`) },
  deleteBooking(id) { return api.delete(`/bookings/${id}`) },

  // CheckIn
  getCheckIns() { return api.get('/checkin') },
  getCheckIn(id) { return api.get(`/checkin/${id}`) },
  getCheckInsByStatus(status) { return api.get(`/checkin/status/${status}`) },
  checkIn(record) { return api.post('/checkin', record) },
  checkOut(id) { return api.post(`/checkin/${id}/checkout`) },
  confirmCheckout(id) { return api.post(`/checkin/${id}/confirm-checkout`) },

  // Housekeeping
  getHousekeepingTasks() { return api.get('/housekeeping') },
  getHousekeepingTask(id) { return api.get(`/housekeeping/${id}`) },
  getTasksByRoom(roomId) { return api.get(`/housekeeping/room/${roomId}`) },
  getTasksByStatus(status) { return api.get(`/housekeeping/status/${status}`) },
  addHousekeepingTask(task) { return api.post('/housekeeping', task) },
  updateHousekeepingTask(id, task) { return api.put(`/housekeeping/${id}`, task) },
  completeHousekeepingTask(id) { return api.patch(`/housekeeping/${id}/complete`) },
  deleteHousekeepingTask(id) { return api.delete(`/housekeeping/${id}`) },

  // Bill
  getBills() { return api.get('/bills') },
  getBill(id) { return api.get(`/bills/${id}`) },
  getBillByCheckIn(checkInId) { return api.get(`/bills/checkin/${checkInId}`) },
  getBillsByPaid(isPaid) { return api.get(`/bills/paid/${isPaid}`) },
  createBill(bill) { return api.post('/bills', bill) },
  updateBill(id, bill) { return api.put(`/bills/${id}`, bill) },
  payBill(id, bill) { return api.patch(`/bills/${id}/pay`, bill) }
}