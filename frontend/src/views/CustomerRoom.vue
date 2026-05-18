<template>
  <div class="customer-room">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="page-title">浏览房间</h1>
      <p class="page-description">选择心仪的房间进行预订</p>
    </div>

    <!-- Floor Filter -->
    <div class="floor-filter">
      <span class="filter-label">选择楼层：</span>
      <el-radio-group v-model="selectedFloor" @change="handleFloorChange">
        <el-radio-button :value="0">全部</el-radio-button>
        <el-radio-button :value="1">1楼</el-radio-button>
        <el-radio-button :value="2">2楼</el-radio-button>
        <el-radio-button :value="3">3楼</el-radio-button>
        <el-radio-button :value="4">4楼</el-radio-button>
      </el-radio-group>
    </div>

    <!-- Room Grid -->
    <div class="room-grid">
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :lg="8" v-for="room in rooms" :key="room.id">
          <div class="room-card">
            <div class="room-header">
              <span class="room-number">{{ room.roomNumber }}</span>
              <span class="status-badge" :class="'status--' + room.status">
                {{ getRoomStatusText(room.status) }}
              </span>
            </div>
            <div class="room-body">
              <div class="room-info">
                <span class="info-label">类型</span>
                <span class="info-value">{{ room.roomType === 'standard' ? '标准间' : '套房' }}</span>
              </div>
              <div class="room-info">
                <span class="info-label">楼层</span>
                <span class="info-value">{{ room.floor }}楼</span>
              </div>
              <div class="room-price">
                <span class="price-label">每晚</span>
                <span class="price-value">¥{{ room.price }}</span>
              </div>
            </div>
            <div class="room-footer">
              <button
                class="btn btn--primary btn--block"
                :disabled="room.status !== 'available'"
                @click="handleBooking(room)"
              >
                {{ room.status === 'available' ? '立即预订' : '暂不可预订' }}
              </button>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- Empty State -->
      <div v-if="rooms.length === 0" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M3 21V7H7V21H3Z"/>
          <path d="M7 21V11H11V21H7Z"/>
          <path d="M11 21V9H15V21H11Z"/>
          <path d="M15 21V13H19V21H15Z"/>
        </svg>
        <p class="empty-text">暂无可用房间</p>
      </div>
    </div>

    <!-- Booking Dialog -->
    <el-dialog v-model="bookingDialog" title="预订房间" width="500px" class="custom-dialog">
      <el-form :model="bookingForm" label-width="100px" class="dialog-form">
        <el-form-item label="房间号">
          <span class="room-number-display">{{ bookingForm.roomNumber }}</span>
        </el-form-item>
        <el-form-item label="入住日期" required>
          <el-date-picker
            v-model="bookingForm.checkInDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
            placeholder="选择入住日期"
          />
        </el-form-item>
        <el-form-item label="退房日期" required>
          <el-date-picker
            v-model="bookingForm.checkOutDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
            placeholder="选择退房日期"
          />
        </el-form-item>
        <el-form-item label="联系电话" required>
          <el-input v-model="bookingForm.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn btn--secondary" @click="bookingDialog = false">取消</button>
        <button class="btn btn--primary" @click="handleSubmit">确认预订</button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage } from 'element-plus'

export default {
  name: 'CustomerRoom',
  data() {
    return {
      rooms: [],
      allRooms: [],
      selectedFloor: 0,
      bookingDialog: false,
      bookingForm: {
        roomId: null,
        roomNumber: '',
        checkInDate: '',
        checkOutDate: '',
        phone: ''
      }
    }
  },
  mounted() {
    this.loadRooms()
  },
  methods: {
    loadRooms() {
      api.getRooms().then(res => {
        this.allRooms = res.data
        this.rooms = res.data
      }).catch(err => {
        console.error('加载房间失败:', err)
        ElMessage.error('加载房间列表失败')
      })
    },
    handleFloorChange(floor) {
      if (floor === 0) {
        this.rooms = this.allRooms
      } else {
        this.rooms = this.allRooms.filter(room => room.floor === floor)
      }
    },
    handleBooking(room) {
      const user = JSON.parse(sessionStorage.getItem('user') || '{}')
      this.bookingForm = {
        roomId: room.id,
        roomNumber: room.roomNumber,
        checkInDate: '',
        checkOutDate: '',
        phone: user.phone || ''
      }
      this.bookingDialog = true
    },
    handleSubmit() {
      if (!this.bookingForm.roomId) {
        ElMessage.warning('请选择房间')
        return
      }
      if (!this.bookingForm.checkInDate) {
        ElMessage.warning('请选择入住日期')
        return
      }
      if (!this.bookingForm.checkOutDate) {
        ElMessage.warning('请选择退房日期')
        return
      }
      if (!this.bookingForm.phone) {
        ElMessage.warning('请输入联系电话')
        return
      }
      if (!/^1[3-9]\d{9}$/.test(this.bookingForm.phone)) {
        ElMessage.warning('手机号格式不正确')
        return
      }
      if (this.bookingForm.checkOutDate <= this.bookingForm.checkInDate) {
        ElMessage.warning('退房日期必须晚于入住日期')
        return
      }

      const user = JSON.parse(sessionStorage.getItem('user') || '{}')
      // 使用用户的 customer ID（从 sessionStorage 获取）直接创建预订
      api.addBooking({
        customerId: user.id,  // user.id 是 customer 表的 ID
        roomId: this.bookingForm.roomId,
        checkInDate: this.bookingForm.checkInDate,
        checkOutDate: this.bookingForm.checkOutDate,
        status: 'pending'
      }).then(() => {
        ElMessage.success('预订成功，请等待管理员确认')
        this.bookingDialog = false
        this.loadRooms()
      }).catch(err => {
        ElMessage.error('预订失败: ' + (err.response?.data?.message || '未知错误'))
      })
    },
    getRoomStatusText(status) {
      const map = {
        available: '可预订',
        booked: '已被预约',
        occupied: '已入住',
        cleaning: '清洁中',
        maintenance: '维修中'
      }
      return map[status] || '不可预订'
    }
  }
}
</script>

<style scoped>
.page-header {
  margin-bottom: var(--space-8);
}

.page-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.page-description {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
  margin: 0;
}

.floor-filter {
  margin-bottom: var(--space-6);
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.filter-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-secondary);
}

.room-grid {
  margin-bottom: var(--space-8);
}

:deep(.el-col) {
  margin-bottom: var(--space-6);
}

.room-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: all var(--transition-normal);
}

.room-card:hover {
  border-color: var(--color-accent);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-4) var(--space-5);
  background: var(--color-muted);
  border-bottom: 1px solid var(--color-border);
}

.room-number {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
}

.status-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.status--available { background: #D1FAE5; color: #065F46; }
.status--booked { background: #FEF3C7; color: #92400E; }
.status--occupied { background: #FEE2E2; color: #991B1B; }
.status--cleaning { background: var(--color-muted); color: var(--color-text-secondary); }
.status--maintenance { background: #DBEAFE; color: #1E40AF; }

.room-body {
  padding: var(--space-5);
}

.room-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-3);
}

.info-label {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
}

.info-value {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
}

.room-price {
  display: flex;
  align-items: baseline;
  gap: var(--space-2);
  margin-top: var(--space-4);
  padding-top: var(--space-4);
  border-top: 1px solid var(--color-border);
}

.price-label {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
}

.price-value {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-danger);
  font-family: var(--font-mono);
}

.room-footer {
  padding: var(--space-4) var(--space-5);
  border-top: 1px solid var(--color-border);
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-5);
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  border: none;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn--primary {
  background: var(--color-primary);
  color: white;
}

.btn--primary:hover:not(:disabled) {
  background: var(--color-primary-light);
}

.btn--primary:disabled {
  background: var(--color-muted);
  color: var(--color-text-muted);
  cursor: not-allowed;
}

.btn--block {
  width: 100%;
}

.btn--secondary {
  background: var(--color-muted);
  color: var(--color-text-primary);
}

.btn--secondary:hover {
  background: var(--color-border);
}

.empty-state {
  padding: var(--space-12);
  text-align: center;
  color: var(--color-text-muted);
}

.empty-state svg {
  margin-bottom: var(--space-4);
  opacity: 0.5;
}

.room-number-display {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--color-accent);
}

:deep(.custom-dialog) {
  --el-dialog-border-radius: var(--radius-lg);
}

.dialog-form :deep(.el-form-item__label) {
  font-weight: var(--font-medium);
}
</style>