<template>
  <div class="customer-home">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="page-title">欢迎回来</h1>
      <p class="page-description">查看您的预订信息和账户概览</p>
    </div>

    <!-- Quick Actions -->
    <div class="quick-actions">
      <div class="action-card" @click="$router.push('/customer/rooms')">
        <div class="action-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 21V7H7V21H3Z"/>
            <path d="M7 21V11H11V21H7Z"/>
            <path d="M11 21V9H15V21H11Z"/>
            <path d="M15 21V13H19V21H15Z"/>
          </svg>
        </div>
        <div class="action-content">
          <span class="action-title">浏览房间</span>
          <span class="action-desc">查看可预约房间并进行预订</span>
        </div>
        <div class="action-arrow">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9,18 15,12 9,6"/>
          </svg>
        </div>
      </div>

      <div class="action-card" @click="$router.push('/customer/bookings')">
        <div class="action-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14,2 14,8 20,8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
          </svg>
        </div>
        <div class="action-content">
          <span class="action-title">我的预约</span>
          <span class="action-desc">查看预约记录和状态</span>
        </div>
        <div class="action-arrow">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9,18 15,12 9,6"/>
          </svg>
        </div>
      </div>
    </div>

    <!-- Bookings Section -->
    <div class="bookings-section">
      <h2 class="section-title">我的预约记录</h2>
      <div class="table-container">
        <el-table :data="myBookings" border stripe :header-cell-style="headerStyle">
          <el-table-column prop="id" label="预约号" width="80" />
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
        </el-table>

        <div v-if="myBookings.length === 0" class="empty-state">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14,2 14,8 20,8"/>
          </svg>
          <p class="empty-text">暂无预约记录</p>
          <button class="btn btn--primary" @click="$router.push('/customer/rooms')">浏览房间</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'CustomerHome',
  data() {
    return {
      myBookings: [],
      headerStyle: {
        background: 'var(--color-muted)',
        color: 'var(--color-text-primary)',
        fontWeight: '600'
      }
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
    getStatusText(status) {
      const map = {
        pending: '待处理',
        confirmed: '已确认',
        cancelled: '已取消',
        completed: '已完成'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.page-header {
  margin-bottom: var(--space-8);
}

.page-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-1);
}

.page-description {
  font-size: var(--text-base);
  color: var(--color-text-muted);
  margin: 0;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--space-4);
  margin-bottom: var(--space-10);
}

.action-card {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-5);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.action-card:hover {
  border-color: var(--color-accent);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.action-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.action-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.action-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.action-desc {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
}

.action-arrow {
  color: var(--color-text-muted);
  transition: transform var(--transition-fast);
}

.action-card:hover .action-arrow {
  transform: translateX(4px);
  color: var(--color-accent);
}

.bookings-section {
  margin-top: var(--space-8);
}

.section-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
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
</style>