<template>
  <div class="booking-management">
    <h2>预订管理</h2>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="待处理" name="pending">
        <el-table :data="pendingBookings" border>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="roomId" label="房间号" width="100">
            <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
          </el-table-column>
          <el-table-column prop="checkInDate" label="入住日期" width="120" />
          <el-table-column prop="checkOutDate" label="退房日期" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag type="warning">待处理</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button size="small" type="success" @click="handleConfirm(row.id)">确认</el-button>
              <el-button size="small" type="danger" @click="handleReject(row.id)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="已确认" name="confirmed">
        <el-table :data="confirmedBookings" border>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="roomId" label="房间号" width="100">
            <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
          </el-table-column>
          <el-table-column prop="checkInDate" label="入住日期" width="120" />
          <el-table-column prop="checkOutDate" label="退房日期" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag type="success">已确认</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="全部" name="all">
        <el-table :data="allBookings" border>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="roomId" label="房间号" width="100">
            <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
          </el-table-column>
          <el-table-column prop="checkInDate" label="入住日期" width="120" />
          <el-table-column prop="checkOutDate" label="退房日期" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'BookingManagement',
  data() {
    return {
      activeTab: 'pending',
      allBookings: [],
      rooms: []
    }
  },
  computed: {
    pendingBookings() {
      return this.allBookings.filter(b => b.status === 'pending')
    },
    confirmedBookings() {
      return this.allBookings.filter(b => b.status === 'confirmed')
    }
  },
  mounted() { this.loadData() },
  methods: {
    loadData() {
      api.getBookings().then(res => { this.allBookings = res.data })
      api.getRooms().then(res => { this.rooms = res.data })
    },
    getRoomNumber(roomId) {
      const room = this.rooms.find(r => r.id === roomId)
      return room ? room.roomNumber : roomId
    },
    handleConfirm(id) {
      ElMessageBox.confirm('确认该预订？', '提示').then(() => {
        api.confirmBooking(id).then(() => {
          ElMessage.success('已确认')
          this.loadData()
        }).catch(err => {
          console.error('确认失败:', err)
          ElMessage.error('确认失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    handleReject(id) {
      ElMessageBox.confirm('拒绝该预订？', '提示').then(() => {
        api.cancelBooking(id).then(() => {
          ElMessage.success('已拒绝')
          this.loadData()
        }).catch(err => {
          console.error('拒绝失败:', err)
          ElMessage.error('拒绝失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    getStatusType(status) {
      return status === 'pending' ? 'warning' : status === 'confirmed' ? 'success' : status === 'cancelled' ? 'danger' : 'info'
    },
    getStatusText(status) {
      return status === 'pending' ? '待处理' : status === 'confirmed' ? '已确认' : status === 'cancelled' ? '已拒绝' : '已完成'
    }
  }
}
</script>

<style scoped>
.booking-management h2 { margin-bottom: 20px; }
</style>