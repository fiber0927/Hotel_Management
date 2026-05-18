<template>
  <div class="guest-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">客人管理</h1>
        <p class="page-description">管理客户信息</p>
      </div>
      <button class="btn btn--primary" @click="showAddDialog">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增客人
      </button>
    </div>

    <!-- Table -->
    <div class="table-container">
      <el-table :data="guests" border stripe :header-cell-style="headerStyle">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
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
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑客人' : '新增客人'" width="500px" class="custom-dialog">
      <el-form :model="guestForm" label-width="100px" class="dialog-form">
        <el-form-item label="姓名">
          <el-input v-model="guestForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="guestForm.phone" placeholder="请输入电话" />
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
  name: 'GuestManagement',
  data() {
    return {
      guests: [],
      dialogVisible: false,
      isEdit: false,
      guestForm: { id: null, name: '', phone: '' },
      headerStyle: {
        background: 'var(--color-muted)',
        color: 'var(--color-text-primary)',
        fontWeight: '600'
      }
    }
  },
  mounted() { this.loadGuests() },
  methods: {
    loadGuests() { api.getGuests().then(res => { this.guests = res.data }) },
    showAddDialog() {
      this.isEdit = false
      this.guestForm = { name: '', phone: '' }
      this.dialogVisible = true
    },
    showEditDialog(row) {
      this.isEdit = true
      this.guestForm = { ...row }
      this.dialogVisible = true
    },
    handleSave() {
      if (this.isEdit) {
        api.updateGuest(this.guestForm.id, this.guestForm).then(() => {
          ElMessage.success('更新成功')
          this.dialogVisible = false
          this.loadGuests()
        })
      } else {
        api.addGuest(this.guestForm).then(() => {
          ElMessage.success('添加成功')
          this.dialogVisible = false
          this.loadGuests()
        })
      }
    },
    handleDelete(id) {
      ElMessageBox.confirm('确认删除该客人信息？', '确认删除').then(() => {
        api.deleteGuest(id).then(() => {
          ElMessage.success('删除成功')
          this.loadGuests()
        })
      })
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
</style>