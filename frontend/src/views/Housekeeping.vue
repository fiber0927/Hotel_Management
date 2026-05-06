<template>
  <div class="housekeeping">
    <h2>客房服务</h2>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">添加任务</el-button>
    </div>
    <el-table :data="tasks" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="roomId" label="房间ID" width="80" />
      <el-table-column prop="taskType" label="任务类型" width="100">
        <template #default="{ row }">
          {{ row.taskType === 'cleaning' ? '清洁' : '维修' }}
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="优先级" width="80">
        <template #default="{ row }">
          <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="notes" label="备注" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="success" v-if="row.status !== 'completed'" @click="handleComplete(row.id)">完成</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="添加任务" width="500px">
      <el-form :model="taskForm" label-width="100px">
        <el-form-item label="房间ID">
          <el-select v-model="taskForm.roomId" placeholder="选择房间">
            <el-option v-for="room in rooms" :key="room.id" :label="room.roomNumber" :value="room.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="taskForm.taskType">
            <el-option label="清洁" value="cleaning" />
            <el-option label="维修" value="repair" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="taskForm.priority">
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
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
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
      taskForm: { roomId: null, taskType: 'cleaning', priority: 'normal', status: 'pending', notes: '' }
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
      ElMessageBox.confirm('确认完成该任务？', '提示').then(() => {
        api.completeHousekeepingTask(id).then(() => {
          ElMessage.success('任务完成')
          this.loadData()
        })
      })
    },
    handleDelete(id) {
      ElMessageBox.confirm('确认删除？', '提示').then(() => {
        api.deleteHousekeepingTask(id).then(() => {
          ElMessage.success('删除成功')
          this.loadData()
        })
      })
    },
    getPriorityType(p) { return p === 'high' ? 'danger' : p === 'normal' ? 'warning' : 'info' },
    getPriorityText(p) { return p === 'high' ? '高' : p === 'normal' ? '普通' : '低' },
    getStatusType(s) { return s === 'pending' ? 'warning' : s === 'in_progress' ? 'primary' : 'success' },
    getStatusText(s) { return s === 'pending' ? '待处理' : s === 'in_progress' ? '进行中' : '已完成' }
  }
}
</script>

<style scoped>
.housekeeping h2 { margin-bottom: 20px; }
.toolbar { margin-bottom: 15px; }
</style>