<template>
  <div class="customer-room">
    <h2>浏览房间</h2>
    <el-row :gutter="20">
      <el-col :span="8" v-for="room in rooms" :key="room.id">
        <div class="room-card">
          <div class="room-header">
            <span class="room-number">{{ room.roomNumber }}</span>
            <el-tag :type="getRoomStatusType(room.status)">
              {{ getRoomStatusText(room.status) }}
            </el-tag>
          </div>
          <div class="room-body">
            <p>类型：{{ room.roomType === 'standard' ? '标准间' : '套房' }}</p>
            <p>楼层：{{ room.floor }}楼</p>
            <p class="price">￥{{ room.price }} / 晚</p>
          </div>
          <div class="room-footer">
            <el-button type="primary" size="small" :disabled="room.status !== 'available'" @click="handleBooking(room)">
              立即预订
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog v-model="bookingDialog" title="预订房间" width="500px">
      <el-form :model="bookingForm" label-width="100px">
        <el-form-item label="房间号">
          <span class="room-number-display">{{ bookingForm.roomNumber }}</span>
        </el-form-item>
        <el-form-item label="入住日期" required>
          <el-date-picker v-model="bookingForm.checkInDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" placeholder="选择入住日期" />
        </el-form-item>
        <el-form-item label="退房日期" required>
          <el-date-picker v-model="bookingForm.checkOutDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" placeholder="选择退房日期" />
        </el-form-item>
        <el-form-item label="联系电话" required>
          <el-input v-model="bookingForm.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bookingDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认预订</el-button>
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
        this.rooms = res.data
      }).catch(err => {
        console.error('加载房间失败:', err)
        ElMessage.error('加载房间列表失败')
      })
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
      api.addGuest({
        name: user.name || user.username || '顾客',
        phone: this.bookingForm.phone
      }).then(res => {
        const guestId = res.data.id
        api.addBooking({
          guestId: guestId,
          roomId: this.bookingForm.roomId,
          checkInDate: this.bookingForm.checkInDate,
          checkOutDate: this.bookingForm.checkOutDate,
          status: 'pending'
        }).then(() => {
          ElMessage.success('预订成功，请等待管理员确认')
          this.bookingDialog = false
          this.loadRooms()
        }).catch(err => {
          console.error('预订失败:', err)
          ElMessage.error('预订失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(err => {
        console.error('创建客户失败:', err)
        ElMessage.error('创建客户信息失败: ' + (err.response?.data?.message || '未知错误'))
      })
    },
    getRoomStatusType(status) {
      if (status === 'available') return 'success'
      if (status === 'booked') return 'warning'
      if (status === 'occupied') return 'danger'
      if (status === 'cleaning') return 'info'
      return 'info'
    },
    getRoomStatusText(status) {
      if (status === 'available') return '可预订'
      if (status === 'booked') return '已被预约'
      if (status === 'occupied') return '已入住'
      if (status === 'cleaning') return '清洁中'
      if (status === 'maintenance') return '维修中'
      return '不可预订'
    }
  }
}
</script>

<style scoped>
.customer-room h2 { margin-bottom: 20px; }

.room-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.room-number {
  font-size: 20px;
  font-weight: bold;
}

.room-number-display {
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
}

.room-body p {
  margin: 8px 0;
  color: #666;
}

.price {
  font-size: 24px;
  color: #e74c3c;
  font-weight: bold;
}

.room-footer {
  margin-top: 15px;
  text-align: right;
}
</style>
