<template>
  <div class="housekeeping">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">客房服务</h1>
        <p class="page-description">管理清洁和维修任务</p>
      </div>
      <button class="btn btn--primary" @click="showAddDialog">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        添加任务
      </button>
    </div>

    <!-- Table -->
    <div class="table-container">
      <el-table :data="tasks" border stripe :header-cell-style="headerStyle">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="roomId" label="房间ID" width="80" />
        <el-table-column prop="taskType" label="任务类型" width="100">
          <template #default="{ row }">
            <span class="type-badge" :class="'type--' + row.taskType">
              {{ row.taskType === 'cleaning' ? '清洁' : '维修' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <span class="priority-badge" :class="'priority--' + row.priority">
              {{ getPriorityText(row.priority) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span class="status-badge" :class="'status--' + row.status">
              {{ getStatusText(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <button
              v-if="row.status !== 'completed'"
              class="btn btn--small btn--success"
              @click="handleComplete(row.id)"
            >
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="20,6 9,17 4,12"/>
              </svg>
              完成
            </button>
            <button class="btn btn--small btn--danger" @click="handleDelete(row.id)">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="3,6 5,6 21,6"/>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
              </svg>
              删除
            </button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" title="添加任务" width="500px" class="custom-dialog">
      <el-form :model="taskForm" label-width="100px" class="dialog-form">
        <el-form-item label="房间ID">
          <el-select v-model="taskForm.roomId" placeholder="选择房间" class="full-width">
            <el-option v-for="room in rooms" :key="room.id" :label="room.roomNumber" :value="room.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="taskForm.taskType" class="full-width">
            <el-option label="清洁" value="cleaning" />
            <el-option label="维修" value="repair" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="taskForm.priority" class="full-width">
            <el-option label="高" value="high" />
            <el-option label="普通" value="normal" />
            <el-option label="低" value="low" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="taskForm.notes" type="textarea" />
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
  name: 'Housekeeping',
  data() {
    return {
      tasks: [],
      rooms: [],
      dialogVisible: false,
      taskForm: { roomId: null, taskType: 'cleaning', priority: 'normal', status: 'pending', notes: '' },
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
      api.getHousekeepingTasks().then(res => { this.tasks = res.data })
      api.getRooms().then(res => { this.rooms = res.data })
    },
    showAddDialog() {
      this.taskForm = { taskType: 'cleaning', priority: 'normal', status: 'pending', notes: '' }
      this.dialogVisible = true
    },
    handleSave() {
      api.addHousekeepingTask(this.taskForm).then(() => {
        ElMessage.success('添加成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    handleComplete(id) {
      ElMessageBox.confirm('确认完成该任务？', '确认').then(() => {
        api.completeHousekeepingTask(id).then(() => {
          ElMessage.success('任务完成')
          this.loadData()
        })
      })
    },
    handleDelete(id) {
      ElMessageBox.confirm('确认删除？此操作不可撤销。', '删除确认').then(() => {
        api.deleteHousekeepingTask(id).then(() => {
          ElMessage.success('删除成功')
          this.loadData()
        })
      })
    },
    getPriorityText(p) {
      const map = { high: '高', normal: '普通', low: '低' }
      return map[p] || p
    },
    getStatusText(s) {
      const map = { pending: '待处理', in_progress: '进行中', completed: '已完成' }
      return map[s] || s
    }
  }
}
</script>

<style scoped>
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

.table-container {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  overflow: hidden;
}

.type-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.type--cleaning { background: #D1FAE5; color: #065F46; }
.type--repair { background: #DBEAFE; color: #1E40AF; }

.priority-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.priority--high { background: #FEE2E2; color: #991B1B; }
.priority--normal { background: #FEF3C7; color: #92400E; }
.priority--low { background: var(--color-muted); color: var(--color-text-secondary); }

.status-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.status--pending { background: #FEF3C7; color: #92400E; }
.status--in_progress { background: #DBEAFE; color: #1E40AF; }
.status--completed { background: #D1FAE5; color: #065F46; }

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

.btn--small {
  padding: var(--space-1) var(--space-3);
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