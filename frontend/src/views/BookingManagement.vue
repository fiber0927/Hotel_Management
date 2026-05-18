<template>
  <div class="booking-management">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="page-title">预订管理</h1>
      <p class="page-description">处理客户预订请求</p>
    </div>

    <!-- Tabs -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" class="custom-tabs">
        <el-tab-pane label="待处理" name="pending">
          <div class="table-wrapper">
            <el-table :data="pendingBookings" border stripe :header-cell-style="headerStyle">
              <el-table-column prop="id" label="ID" width="60" />
              <el-table-column prop="roomId" label="房间号" width="100">
                <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
              </el-table-column>
              <el-table-column prop="checkInDate" label="入住日期" width="120" />
              <el-table-column prop="checkOutDate" label="退房日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <span class="status-badge status--pending">待处理</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <button class="btn btn--small btn--success" @click="handleConfirm(row.id)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <polyline points="20,6 9,17 4,12"/>
                    </svg>
                    确认
                  </button>
                  <button class="btn btn--small btn--danger" @click="handleReject(row.id)">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <line x1="18" y1="6" x2="6" y2="18"/>
                      <line x1="6" y1="6" x2="18" y2="18"/>
                    </svg>
                    拒绝
                  </button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="已确认" name="confirmed">
          <div class="table-wrapper">
            <el-table :data="confirmedBookings" border stripe :header-cell-style="headerStyle">
              <el-table-column prop="id" label="ID" width="60" />
              <el-table-column prop="roomId" label="房间号" width="100">
                <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
              </el-table-column>
              <el-table-column prop="checkInDate" label="入住日期" width="120" />
              <el-table-column prop="checkOutDate" label="退房日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <span class="status-badge status--confirmed">已确认</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="已拒绝" name="rejected">
          <div class="table-wrapper">
            <el-table :data="rejectedBookings" border stripe :header-cell-style="headerStyle">
              <el-table-column prop="id" label="ID" width="60" />
              <el-table-column prop="roomId" label="房间号" width="100">
                <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
              </el-table-column>
              <el-table-column prop="checkInDate" label="入住日期" width="120" />
              <el-table-column prop="checkOutDate" label="退房日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <span class="status-badge status--rejected">被拒绝</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="全部" name="all">
          <div class="table-wrapper">
            <el-table :data="allBookings" border stripe :header-cell-style="headerStyle">
              <el-table-column prop="id" label="ID" width="60" />
              <el-table-column prop="roomId" label="房间号" width="100">
                <template #default="{ row }">{{ getRoomNumber(row.roomId) }}</template>
              </el-table-column>
              <el-table-column prop="checkInDate" label="入住日期" width="120" />
              <el-table-column prop="checkOutDate" label="退房日期" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <span class="status-badge" :class="'status--' + row.status">{{ getStatusText(row.status) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
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
      rooms: [],
      headerStyle: {
        background: 'var(--color-muted)',
        color: 'var(--color-text-primary)',
        fontWeight: '600'
      }
    }
  },
  computed: {
    pendingBookings() {
      return this.allBookings.filter(b => b.status === 'pending')
    },
    confirmedBookings() {
      return this.allBookings.filter(b => b.status === 'confirmed')
    },
    rejectedBookings() {
      return this.allBookings.filter(b => b.status === 'rejected')
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
      ElMessageBox.confirm('确认该预订？', '确认').then(() => {
        api.confirmBooking(id).then(() => {
          ElMessage.success('已确认')
          this.loadData()
        }).catch(err => {
          ElMessage.error('确认失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    handleReject(id) {
      ElMessageBox.confirm('拒绝该预订？', '确认').then(() => {
        api.rejectBooking(id).then(() => {
          ElMessage.success('已拒绝')
          this.loadData()
        }).catch(err => {
          ElMessage.error('拒绝失败: ' + (err.response?.data?.message || '未知错误'))
        })
      }).catch(() => {})
    },
    getStatusText(status) {
      const map = {
        pending: '待处理',
        confirmed: '已确认',
        cancelled: '已取消',
        completed: '已完成',
        rejected: '被拒绝'
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
  color: var(--color-text-muted);
  padding: 0 var(--space-5);
}

:deep(.el-tabs__item.is-active) {
  color: var(--color-accent);
}

:deep(.el-tabs__active-bar) {
  background-color: var(--color-accent);
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

.status--pending { background: #FEF3C7; color: #92400E; }
.status--confirmed { background: #D1FAE5; color: #065F46; }
.status--cancelled { background: #FEE2E2; color: #991B1B; }
.status--completed { background: var(--color-muted); color: var(--color-text-secondary); }
.status--rejected { background: #FEE2E2; color: #991B1B; }

.btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-md);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  border: none;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn--small {
  padding: var(--space-1) var(--space-3);
}

.btn--success {
  background: var(--color-success);
  color: white;
}

.btn--success:hover {
  background: var(--color-success-light);
}

.btn--danger {
  background: transparent;
  color: var(--color-danger);
  border: 1px solid var(--color-danger);
}

.btn--danger:hover {
  background: var(--color-danger);
  color: white;
}
</style>