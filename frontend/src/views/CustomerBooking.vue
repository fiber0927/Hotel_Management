<template>
  <div class="customer-booking">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="page-title">我的预订</h1>
      <p class="page-description">查看和管理您的预订记录</p>
    </div>

    <!-- Table -->
    <div class="table-container">
      <el-table :data="bookings" border stripe :header-cell-style="headerStyle">
        <el-table-column prop="id" label="预订号" width="80" />
        <el-table-column prop="checkInDate" label="入住日期" width="120" />
        <el-table-column prop="checkOutDate" label="退房日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span class="status-badge" :class="'status--' + row.status">
              {{ getStatusText(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="specialRequests" label="备注" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <button
              v-if="row.status === 'confirmed' || row.status === 'rejected'"
              class="btn btn--small btn--danger"
              @click="handleCancel(row)"
            >
              删除记录
            </button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Empty State -->
      <div v-if="bookings.length === 0" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
          <polyline points="14,2 14,8 20,8"/>
        </svg>
        <p class="empty-text">暂无预订记录</p>
        <button class="btn btn--primary" @click="$router.push('/customer/rooms')">浏览房间</button>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CustomerBooking',
  data() {
    return {
      bookings: [],
      headerStyle: {
        background: 'var(--color-muted)',
        color: 'var(--color-text-primary)',
        fontWeight: '600'
      }
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
    handleCancel(row) {
      const isRejected = row.status === 'rejected'
      const message = isRejected
        ? '确定要删除该记录吗？'
        : '确定要取消该预订吗？取消后将恢复房间可用状态'
      ElMessageBox.confirm(message, isRejected ? '删除记录' : '取消预订').then(() => {
        api.deleteBooking(row.id).then(() => {
          ElMessage.success(isRejected ? '记录已删除' : '预订已取消')
          this.loadBookings()
        }).catch(err => {
          ElMessage.error('操作失败: ' + (err.response?.data?.message || '未知错误'))
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

.table-container {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  overflow: hidden;
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

.empty-state {
  padding: var(--space-12);
  text-align: center;
  color: var(--color-text-muted);
}

.empty-state svg {
  margin-bottom: var(--space-4);
  opacity: 0.5;
}

.empty-text {
  margin-bottom: var(--space-4);
}

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

.btn--small {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
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