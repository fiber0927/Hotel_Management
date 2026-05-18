<template>
  <div class="dashboard">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="page-title">数据概览</h1>
      <p class="page-description">实时了解酒店运营状态</p>
    </div>

    <!-- KPI Cards -->
    <el-row :gutter="24" class="kpi-grid">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-card--rooms">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 21V7H7V21H3Z"/>
              <path d="M7 21V11H11V21H7Z"/>
              <path d="M11 21V9H15V21H11Z"/>
              <path d="M15 21V13H19V21H15Z"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-label">房间总数</span>
            <span class="kpi-value">{{ stats.totalRooms }}</span>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-card--available">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22,4 12,14.01 9,11.01"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-label">可用房间</span>
            <span class="kpi-value">{{ stats.availableRooms }}</span>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-card--pending">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12,6 12,12 16,14"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-label">待处理预约</span>
            <span class="kpi-value">{{ stats.pendingBookings }}</span>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-card--bills">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14,2 14,8 20,8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
              <polyline points="10,9 9,9 8,9"/>
            </svg>
          </div>
          <div class="kpi-content">
            <span class="kpi-label">未结账单</span>
            <span class="kpi-value">{{ stats.unpaidBills }}</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Quick Actions Section -->
    <div class="quick-actions">
      <h2 class="section-title">快捷操作</h2>
      <div class="actions-grid">
        <router-link to="/main/rooms" class="action-card">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="12" y1="5" x2="12" y2="19"/>
              <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
          </div>
          <span class="action-label">新增房间</span>
        </router-link>

        <router-link to="/main/bookings" class="action-card">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
              <line x1="16" y1="2" x2="16" y2="6"/>
              <line x1="8" y1="2" x2="8" y2="6"/>
              <line x1="3" y1="10" x2="21" y2="10"/>
            </svg>
          </div>
          <span class="action-label">处理预订</span>
        </router-link>

        <router-link to="/main/checkin" class="action-card">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
              <circle cx="8.5" cy="7" r="4"/>
              <polyline points="17,11 19,13 23,9"/>
            </svg>
          </div>
          <span class="action-label">办理入住</span>
        </router-link>

        <router-link to="/main/bills" class="action-card">
          <div class="action-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="12" y1="1" x2="12" y2="23"/>
              <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
            </svg>
          </div>
          <span class="action-label">账单管理</span>
        </router-link>
      </div>
    </div>

    <!-- Room Stats Section -->
    <div class="room-stats-section">
      <h2 class="section-title">房间统计</h2>
      <el-table :data="roomStatsTable" border style="width: 100%">
        <el-table-column prop="floor" label="楼层" width="100" />
        <el-table-column prop="standard" label="标准间" align="center" />
        <el-table-column prop="suite" label="套房" align="center" />
        <el-table-column prop="total" label="小计" align="center" />
      </el-table>
    </div>
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
      },
      roomStatsTable: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadRoomStats()
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
    },
    loadRoomStats() {
      api.getRoomStats().then(res => {
        const byFloor = res.data.byFloor
        this.roomStatsTable = [
          { floor: '1楼', standard: byFloor['1'].standard, suite: byFloor['1'].suite, total: byFloor['1'].total },
          { floor: '2楼', standard: byFloor['2'].standard, suite: byFloor['2'].suite, total: byFloor['2'].total },
          { floor: '3楼', standard: byFloor['3'].standard, suite: byFloor['3'].suite, total: byFloor['3'].total },
          { floor: '4楼', standard: byFloor['4'].standard, suite: byFloor['4'].suite, total: byFloor['4'].total }
        ]
      })
    }
  }
}
</script>

<style scoped>
/* ===== PAGE HEADER ===== */
.page-header {
  margin-bottom: var(--space-8);
}

.page-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.page-description {
  font-size: var(--text-base);
  color: var(--color-text-muted);
  margin: 0;
}

/* ===== KPI GRID ===== */
.kpi-grid {
  margin-bottom: var(--space-10);
}

:deep(.el-col) {
  margin-bottom: var(--space-6);
}

/* ===== KPI CARD ===== */
.kpi-card {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-5);
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
  cursor: default;
}

.kpi-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.kpi-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

/* KPI Card Variants */
.kpi-card--rooms .kpi-icon {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-secondary) 100%);
  color: white;
}

.kpi-card--available .kpi-icon {
  background: linear-gradient(135deg, var(--color-success) 0%, var(--color-success-light) 100%);
  color: white;
}

.kpi-card--pending .kpi-icon {
  background: linear-gradient(135deg, var(--color-warning) 0%, var(--color-warning-light) 100%);
  color: white;
}

.kpi-card--bills .kpi-icon {
  background: linear-gradient(135deg, var(--color-danger) 0%, var(--color-danger-light) 100%);
  color: white;
}

.kpi-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.kpi-label {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
  font-weight: var(--font-medium);
}

.kpi-value {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  font-family: var(--font-mono);
  line-height: 1;
}

/* ===== QUICK ACTIONS ===== */
.quick-actions {
  margin-top: var(--space-8);
}

.section-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-4);
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: var(--space-4);
}

.action-card {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.action-card:hover {
  border-color: var(--color-accent);
  background: var(--color-muted);
}

.action-icon {
  width: 40px;
  height: 40px;
  background: var(--color-muted);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-accent);
  transition: all var(--transition-fast);
}

.action-card:hover .action-icon {
  background: var(--color-accent);
  color: white;
}

.action-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
}

/* ===== ROOM STATS ===== */
.room-stats-section {
  margin-top: var(--space-8);
}
</style>