<template>
  <div class="dashboard">
    <h2>首页</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="card">
          <div class="card-title">房间总数</div>
          <div class="card-value">{{ stats.totalRooms }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card">
          <div class="card-title">可用房间</div>
          <div class="card-value">{{ stats.availableRooms }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card">
          <div class="card-title">待处理预约</div>
          <div class="card-value">{{ stats.pendingBookings }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card">
          <div class="card-title">未结账单</div>
          <div class="card-value">{{ stats.unpaidBills }}</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalRooms: 0,
        availableRooms: 0,
        pendingBookings: 0,
        unpaidBills: 0
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    loadStats() {
      api.getRooms().then(res => {
        this.stats.totalRooms = res.data.length
        this.stats.availableRooms = res.data.filter(r => r.status === 'available').length
      })
      api.getBookingsByStatus('pending').then(res => {
        this.stats.pendingBookings = res.data.length
      })
      api.getBillsByPaid(0).then(res => {
        this.stats.unpaidBills = res.data.length
      })
    }
  }
}
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
}

.card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.card-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.card-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
}
</style>