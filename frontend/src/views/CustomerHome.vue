<template>
  <div class="customer-home">
    <h2>顾客端首页</h2>
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card" @click="$router.push('/customer/rooms')">
          <div class="card-icon">🏨</div>
          <div class="card-title">浏览房间</div>
          <div class="card-desc">查看可预约房间并进行预订</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card" @click="$router.push('/customer/bookings')">
          <div class="card-icon">📋</div>
          <div class="card-title">我的预约</div>
          <div class="card-desc">查看我的预约记录和状态</div>
        </div>
      </el-col>
    </el-row>

    <h3 style="margin-top: 40px;">我的预约记录</h3>
    <el-table :data="myBookings" border style="margin-top: 15px;">
      <el-table-column prop="id" label="预约号" width="80" />
      <el-table-column prop="checkInDate" label="入住日期" width="120" />
      <el-table-column prop="checkOutDate" label="退房日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'CustomerHome',
  data() {
    return {
      myBookings: []
    }
  },
  mounted() {
    this.loadMyBookings()
  },
  methods: {
    loadMyBookings() {
      const user = JSON.parse(sessionStorage.getItem('user') || '{}')
      if (user.id) {
        api.getBookingsByGuest(user.id).then(res => {
          this.myBookings = res.data
        })
      }
    },
    getStatusType(status) {
      if (status === 'pending') return 'warning'
      if (status === 'confirmed') return 'success'
      if (status === 'cancelled') return 'danger'
      return 'info'
    },
    getStatusText(status) {
      if (status === 'pending') return '待处理'
      if (status === 'confirmed') return '已确认'
      if (status === 'cancelled') return '已取消'
      return status
    }
  }
}
</script>

<style scoped>
.customer-home h2 { margin-bottom: 30px; }

.card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.card-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.card-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.card-desc {
  font-size: 14px;
  color: #999;
}
</style>