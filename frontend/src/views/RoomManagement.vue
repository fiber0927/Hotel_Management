<template>
  <div class="room-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">房间管理</h1>
        <p class="page-description">管理酒店房间信息</p>
      </div>
      <button class="btn btn--primary" @click="showAddDialog">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增房间
      </button>
    </div>

    <!-- Toolbar -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索房间号..."
          class="search-input"
          clearable
        >
          <template #prefix>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/>
              <line x1="21" y1="21" x2="16.65" y2="16.65"/>
            </svg>
          </template>
        </el-input>
        <el-select v-model="filterStatus" placeholder="筛选状态" clearable class="filter-select">
          <el-option label="全部" value="" />
          <el-option label="可用" value="available" />
          <el-option label="已预约" value="booked" />
          <el-option label="已占用" value="occupied" />
        </el-select>
      </div>
    </div>

    <!-- Data Table -->
    <div class="table-container">
      <el-table
        :data="filteredRooms"
        border
        stripe
        class="data-table"
        :header-cell-style="{ background: 'var(--color-muted)', color: 'var(--color-text-primary)', fontWeight: '600' }"
      >
        <el-table-column prop="roomNumber" label="房间号" width="100" />
        <el-table-column prop="roomType" label="房间类型" width="120">
          <template #default="{ row }">
            <span class="room-type-badge" :class="'room-type--' + row.roomType">
              {{ row.roomType === 'standard' ? '标准间' : '套房' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="floor" label="楼层" width="80" />
        <el-table-column prop="price" label="价格/晚" width="120">
          <template #default="{ row }">
            <span class="price-value">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <span class="status-badge" :class="'status--' + row.status">
              {{ getStatusText(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="160">
          <template #default="{ row }">
            <div class="action-buttons">
              <button class="btn btn--small" @click="showEditDialog(row)">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
                编辑
              </button>
              <button class="btn btn--small btn--danger" @click="handleDelete(row.id)">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="3,6 5,6 21,6"/>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                </svg>
                删除
              </button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- Empty State -->
      <div v-if="filteredRooms.length === 0" class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M3 21V7H7V21H3Z"/>
          <path d="M7 21V11H11V21H7Z"/>
          <path d="M11 21V9H15V21H11Z"/>
          <path d="M15 21V13H19V21H15Z"/>
        </svg>
        <p class="empty-text">暂无房间数据</p>
        <button class="btn btn--primary" @click="showAddDialog">新增第一个房间</button>
      </div>
    </div>

    <!-- Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑房间' : '新增房间'"
      width="500px"
      class="custom-dialog"
    >
      <el-form :model="roomForm" label-width="80px" class="dialog-form">
        <el-form-item label="房间号">
          <el-input v-model="roomForm.roomNumber" placeholder="例如：101" />
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="roomForm.roomType" class="full-width">
            <el-option label="标准间" value="standard" />
            <el-option label="套房" value="suite" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼层">
          <el-input-number v-model="roomForm.floor" :min="1" :max="4" class="full-width" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="roomForm.price" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="roomForm.status" class="full-width">
            <el-option label="可用" value="available" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn btn--secondary" @click="dialogVisible = false">取消</button>
        <button class="btn btn--primary" @click="handleSave">保存</button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'RoomManagement',
  data() {
    return {
      rooms: [],
      dialogVisible: false,
      isEdit: false,
      searchKeyword: '',
      filterStatus: '',
      roomForm: {
        id: null,
        roomNumber: '',
        roomType: 'standard',
        floor: 1,
        price: 0,
        status: 'available'
      }
    }
  },
  computed: {
    filteredRooms() {
      let result = this.rooms
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        result = result.filter(r => r.roomNumber.toLowerCase().includes(kw))
      }
      if (this.filterStatus) {
        result = result.filter(r => r.status === this.filterStatus)
      }
      return result
    }
  },
  mounted() {
    this.loadRooms()
  },
  methods: {
    loadRooms() {
      api.getRooms().then(res => { this.rooms = res.data })
    },
    showAddDialog() {
      this.isEdit = false
      this.roomForm = { roomType: 'standard', floor: 1, price: 0, status: 'available' }
      this.dialogVisible = true
    },
    showEditDialog(row) {
      this.isEdit = true
      this.roomForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      if (this.isEdit) {
        api.updateRoom(this.roomForm.id, this.roomForm).then(() => {
          ElMessage.success('更新成功')
          this.dialogVisible = false
          this.loadRooms()
        })
      } else {
        api.addRoom(this.roomForm).then(() => {
          ElMessage.success('添加成功')
          this.dialogVisible = false
          this.loadRooms()
        })
      }
    },
    handleDelete(id) {
      ElMessageBox.confirm('确认删除该房间？此操作不可撤销。', '删除确认', {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteRoom(id).then(() => {
          ElMessage.success('删除成功')
          this.loadRooms()
        })
      })
    },
    getStatusText(status) {
      const map = {
        available: '可用',
        booked: '已被预约',
        occupied: '已占用'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
/* ===== PAGE HEADER ===== */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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

/* ===== TOOLBAR ===== */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-6);
}

.toolbar-left {
  display: flex;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.search-input {
  width: 240px;
}

.filter-select {
  width: 140px;
}

/* ===== TABLE CONTAINER ===== */
.table-container {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  overflow: hidden;
}

:deep(.el-table) {
  --el-table-border-color: var(--color-border);
}

/* ===== BADGES ===== */
.room-type-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.room-type--standard {
  background: var(--color-muted);
  color: var(--color-text-primary);
}

.room-type--suite {
  background: #FEF3C7;
  color: #92400E;
}

.price-value {
  font-family: var(--font-mono);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.status-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.status--available {
  background: #D1FAE5;
  color: #065F46;
}

.status--booked {
  background: #FEF3C7;
  color: #92400E;
}

.status--occupied {
  background: #FEE2E2;
  color: #991B1B;
}

/* ===== ACTION BUTTONS ===== */
.action-buttons {
  display: flex;
  gap: var(--space-2);
}

/* ===== EMPTY STATE ===== */
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
  font-size: var(--text-base);
}

/* ===== BUTTONS ===== */
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

.btn--danger {
  background: var(--color-danger);
  color: white;
}

.btn--danger:hover {
  background: var(--color-danger-light);
}

.btn--small {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
}

.btn--small.btn--danger {
  background: transparent;
  color: var(--color-danger);
  border: 1px solid var(--color-danger);
}

.btn--small.btn--danger:hover {
  background: var(--color-danger);
  color: white;
}

/* ===== DIALOG ===== */
:deep(.custom-dialog) {
  --el-dialog-border-radius: var(--radius-lg);
}

:deep(.el-dialog__header) {
  padding: var(--space-5) var(--space-6);
  border-bottom: 1px solid var(--color-border);
}

:deep(.el-dialog__body) {
  padding: var(--space-6);
}

:deep(.el-dialog__footer) {
  padding: var(--space-4) var(--space-6);
  border-top: 1px solid var(--color-border);
}

.dialog-form :deep(.el-form-item__label) {
  font-weight: var(--font-medium);
}

.full-width {
  width: 100%;
}
</style>