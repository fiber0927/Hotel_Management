<template>
  <div class="checkinout">
    <h2>入住退房</h2>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="入住" name="checkin">
        <el-button type="primary" @click="showCheckInDialog" style="margin-bottom:15px">办理入住</el-button>
        <el-table :data="checkInRecords" border>
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="bookingId" label="预订ID" width="80" />
          <el-table-column prop="roomId" label="房间ID" width="80" />
          <el-table-column prop="checkInTime" label="入住时间" width="160" />
          <el-table-column prop="checkOutTime" label="退房时间" width="160" />
          <el-table-column prop="status" label="状态" width="120">
            <template #default="{ row }">
              <el-tag v-if="row.status === 'checked_in'" type="success">已入住</el-tag>
              <el-tag v-else-if="row.status === 'checked_out'" type="warning">已退房(待确认)</el-tag>
              <el-tag v-else type="info">已完成</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button size="small" type="warning" v-if="row.status === 'checked_in'" @click="handleCheckOut(row.id)">办理退房</el-button>
              <el-button size="small" type="success" v-if="row.status === 'checked_out'" @click="handleConfirmCheckout(row.id)">确认退房</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" title="办理入住" width="500px">
      <el-form :model="checkInForm" label-width="100px">
        <el-form-item label="预订ID">
          <el-input-number v-model="checkInForm.bookingId" :min="1" />
        </el-form-item>
        <el-form-item label="房间ID">
          <el-select v-model="checkInForm.roomId" placeholder="选择房间">
            <el-option v-for="room in availableRooms" :key="room.id" :label="room.roomNumber" :value="room.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCheckIn">确认入住</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CheckInOut',
  data() {
    return {
      activeTab: 'checkin',
      checkInRecords: [],
      availableRooms: [],
      dialogVisible: false,
      checkInForm: { bookingId: null, roomId: null }
    }
  },
  mounted() { this.loadData() },
  methods: {
    loadData() {
      api.getCheckIns().then(res => { this.checkInRecords = res.data })
      api.getAvailableRooms().then(res => { this.availableRooms = res.data })
    },
    showCheckInDialog() {
      this.checkInForm = { bookingId: null, roomId: null }
      this.dialogVisible = true
    },
    handleCheckIn() {
      api.checkIn(this.checkInForm).then(() => {
        ElMessage.success('入住成功')
        this.dialogVisible = false
        this.loadData()
      }).catch(err => {
        ElMessage.error('入住失败: ' + (err.response?.data?.message || '未知错误'))
      })
    },
    handleCheckOut(id) {
      ElMessageBox.confirm('确认办理退房？退房后房间进入清洁流程', '提示').then(() => {
        api.checkOut(id).then(() => {
          ElMessage.success('退房成功，房间清洁中')
          this.loadData()
        }).catch(err => {
          ElMessage.error('退房失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    handleConfirmCheckout(id) {
      ElMessageBox.confirm('确认房间已清洁完成并退房？房间将恢复为可用', '提示').then(() => {
        api.confirmCheckout(id).then(() => {
          ElMessage.success('退房已确认，房间已恢复可用')
          this.loadData()
        }).catch(err => {
          ElMessage.error('确认失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.checkinout h2 { margin-bottom: 20px; }
</style>
