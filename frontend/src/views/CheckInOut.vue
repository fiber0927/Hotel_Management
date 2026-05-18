<template>
  <div class="checkinout">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="page-title">入住退房</h1>
      <p class="page-description">办理客户入住和退房手续</p>
    </div>

    <!-- Tabs -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" class="custom-tabs">
        <el-tab-pane label="入住管理" name="checkin">
          <div class="action-bar">
            <button class="btn btn--primary" @click="showCheckInDialog">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                <circle cx="8.5" cy="7" r="4"/>
                <line x1="20" y1="8" x2="20" y2="14"/>
                <line x1="23" y1="11" x2="17" y2="11"/>
              </svg>
              办理入住
            </button>
          </div>

          <div class="table-wrapper">
            <el-table :data="checkInRecords" border stripe :header-cell-style="headerStyle">
              <el-table-column prop="id" label="ID" width="60" />
              <el-table-column prop="bookingId" label="预订ID" width="80" />
              <el-table-column prop="roomId" label="房间ID" width="80" />
              <el-table-column prop="checkInTime" label="入住时间" width="160" />
              <el-table-column prop="checkOutTime" label="退房时间" width="160" />
              <el-table-column prop="status" label="状态" width="120">
                <template #default="{ row }">
                  <span class="status-badge" :class="'status--' + row.status">
                    {{ getStatusText(row.status) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <button
                    v-if="row.status === 'checked_in'"
                    class="btn btn--small btn--warning"
                    @click="handleCheckOut(row.id)"
                  >
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                      <polyline points="16,17 21,12 16,7"/>
                      <line x1="21" y1="12" x2="9" y2="12"/>
                    </svg>
                    办理退房
                  </button>
                  <button
                    v-if="row.status === 'checked_out'"
                    class="btn btn--small btn--success"
                    @click="handleConfirmCheckout(row.id)"
                  >
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <polyline points="20,6 9,17 4,12"/>
                    </svg>
                    确认退房
                  </button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" title="办理入住" width="500px" class="custom-dialog">
      <el-form :model="checkInForm" label-width="100px" class="dialog-form">
        <el-form-item label="预订ID">
          <el-input-number v-model="checkInForm.bookingId" :min="1" class="full-width" />
        </el-form-item>
        <el-form-item label="房间ID">
          <el-select v-model="checkInForm.roomId" placeholder="选择房间" class="full-width">
            <el-option v-for="room in availableRooms" :key="room.id" :label="room.roomNumber" :value="room.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn btn--secondary" @click="dialogVisible = false">取消</button>
        <button class="btn btn--primary" @click="handleCheckIn">确认入住</button>
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
      checkInForm: { bookingId: null, roomId: null },
      headerStyle: {
        background: 'var(--color-muted)',
        color: 'var(--color-text-primary)',
        fontWeight: '600'
      }
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
      ElMessageBox.confirm('确认办理退房？退房后房间进入清洁流程', '确认').then(() => {
        api.checkOut(id).then(() => {
          ElMessage.success('退房成功，房间清洁中')
          this.loadData()
        }).catch(err => {
          ElMessage.error('退房失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    handleConfirmCheckout(id) {
      ElMessageBox.confirm('确认房间已清洁完成并退房？房间将恢复为可用', '确认').then(() => {
        api.confirmCheckout(id).then(() => {
          ElMessage.success('退房已确认，房间已恢复可用')
          this.loadData()
        }).catch(err => {
          ElMessage.error('确认失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    getStatusText(status) {
      const map = {
        checked_in: '已入住',
        checked_out: '已退房(待确认)',
        completed: '已完成'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.page-header {
  margin-bottom: var(--space-6);
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

.tabs-container {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  padding: var(--space-5);
}

:deep(.custom-tabs) {
  --el-tabs-header-height: 48px;
}

:deep(.el-tabs__item) {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}

.action-bar {
  margin-bottom: var(--space-4);
}

.table-wrapper {
  margin-top: var(--space-4);
}

.status-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.status--checked_in { background: #D1FAE5; color: #065F46; }
.status--checked_out { background: #FEF3C7; color: #92400E; }
.status--completed { background: var(--color-muted); color: var(--color-text-secondary); }

.btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
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

.btn--primary:hover {
  background: var(--color-primary-light);
}

.btn--secondary {
  background: var(--color-muted);
  color: var(--color-text-primary);
}

.btn--secondary:hover {
  background: var(--color-border);
}

.btn--success {
  background: var(--color-success);
  color: white;
}

.btn--warning {
  background: var(--color-warning);
  color: white;
}

.btn--small {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
}

:deep(.custom-dialog) {
  --el-dialog-border-radius: var(--radius-lg);
}

.dialog-form :deep(.el-form-item__label) {
  font-weight: var(--font-medium);
}

.full-width {
  width: 100%;
}
</style>