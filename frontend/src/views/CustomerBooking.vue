<template>
  <div class="customer-booking">
    <h2>我的预订</h2>
    <el-table :data="bookings" border>
      <el-table-column prop="id" label="预订号" width="80" />
      <el-table-column prop="checkInDate" label="入住日期" width="120" />
      <el-table-column prop="checkOutDate" label="退房日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="specialRequests" label="备注" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button size="small" type="danger" v-if="row.status === 'confirmed'" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CustomerBooking',
  data() {
    return {
      bookings: []
    }
  },
  mounted() { this.loadBookings() },
  methods: {
    loadBookings() {
      const user = JSON.parse(sessionStorage.getItem('user') || '{}')
      if (user.id) {
        api.getBookingsByGuest(user.id).then(res => {
          this.bookings = res.data
        })
      }
    },
    handleDelete(row) {
      ElMessageBox.confirm('确定要取消该预订吗？取消后将恢复房间可用状态', '提示').then(() => {
        api.deleteBooking(row.id).then(() => {
          ElMessage.success('预订已取消')
          this.loadBookings()
        }).catch(err => {
          ElMessage.error('取消失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    getStatusType(status) {
      return status === 'confirmed' ? 'success' : status === 'cancelled' ? 'danger' : status === 'pending' ? 'warning' : 'info'
    },
    getStatusText(status) {
      return status === 'confirmed' ? '已确认' : status === 'cancelled' ? '已取消' : status === 'pending' ? '待处理' : '已完成'
    }
  }
}
</script>

<style scoped>
.customer-booking h2 { margin-bottom: 20px; }
</style>
