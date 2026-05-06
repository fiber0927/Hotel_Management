import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Main from '../views/Main.vue'
import Dashboard from '../views/Dashboard.vue'
import RoomManagement from '../views/RoomManagement.vue'
import BookingManagement from '../views/BookingManagement.vue'
import CheckInOut from '../views/CheckInOut.vue'
import Housekeeping from '../views/Housekeeping.vue'
import Billing from '../views/Billing.vue'
import GuestManagement from '../views/GuestManagement.vue'
import CustomerMain from '../views/CustomerMain.vue'
import CustomerHome from '../views/CustomerHome.vue'
import CustomerRoom from '../views/CustomerRoom.vue'
import CustomerBooking from '../views/CustomerBooking.vue'

// 路由配置
const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  {
    path: '/main',
    component: Main,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'rooms', component: RoomManagement },
      { path: 'bookings', component: BookingManagement },
      { path: 'checkin', component: CheckInOut },
      { path: 'housekeeping', component: Housekeeping },
      { path: 'bills', component: Billing },
      { path: 'guests', component: GuestManagement }
    ]
  },
  {
    path: '/customer',
    component: CustomerMain,
    children: [
      { path: 'home', component: CustomerHome },
      { path: 'rooms', component: CustomerRoom },
      { path: 'bookings', component: CustomerBooking }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from) => {
  const userStr = sessionStorage.getItem('user')

  // 登录页和注册页直接放行
  if (to.path === '/login' || to.path === '/register') {
    return true
  }

  // 无session，直接去登录页
  if (!userStr) {
    return { path: '/login' }
  }

  // 解析用户数据
  let userData
  try {
    userData = JSON.parse(userStr)
  } catch (e) {
    sessionStorage.removeItem('user')
    return { path: '/login' }
  }

  // 管理员访问管理端路由 -> 放行
  if (to.path.startsWith('/main') && userData.role === 'admin') {
    return true
  }

  // 顾客访问顾客端路由 -> 放行
  if (to.path.startsWith('/customer') && userData.role === 'customer') {
    return true
  }

  // 角色不匹配，重定向
  if (userData.role === 'admin') {
    return { path: '/main/dashboard' }
  }

  if (userData.role === 'customer') {
    return { path: '/customer/home' }
  }

  // 未知角色，去登录
  sessionStorage.removeItem('user')
  return { path: '/login' }
})

export default router
