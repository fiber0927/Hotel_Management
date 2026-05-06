<template>
  <div class="room-management">
    <h2>房间管理</h2>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">新增房间</el-button>
    </div>
    <el-table :data="rooms" border>
      <el-table-column prop="roomNumber" label="房间号" width="100" />
      <el-table-column prop="roomType" label="房间类型" width="120">
        <template #default="{ row }">
          {{ row.roomType === 'standard' ? '标准间' : '套房' }}
        </template>
      </el-table-column>
      <el-table-column prop="floor" label="楼层" width="80" />
      <el-table-column prop="price" label="价格/晚" width="100" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑房间' : '新增房间'" width="500px">
      <el-form :model="roomForm" label-width="100px">
        <el-form-item label="房间号">
          <el-input v-model="roomForm.roomNumber" />
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="roomForm.roomType">
            <el-option label="标准间" value="standard" />
            <el-option label="套房" value="suite" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼层">
          <el-input-number v-model="roomForm.floor" :min="1" :max="4" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="roomForm.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="roomForm.status">
            <el-option label="可用" value="available" />
          </el-select>
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
  name: 'RoomManagement',
  data() {
    return {
      rooms: [],
      dialogVisible: false,
      isEdit: false,
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
      ElMessageBox.confirm('确认删除该房间？', '提示').then(() => {
        api.deleteRoom(id).then(() => {
          ElMessage.success('删除成功')
          this.loadRooms()
        })
      })
    },
    getStatusType(status) {
      if (status === 'available') return 'success'
      if (status === 'booked') return 'warning'
      if (status === 'occupied') return 'danger'
      return 'info'
    },
    getStatusText(status) {
      if (status === 'available') return '可用'
      if (status === 'booked') return '已被预约'
      if (status === 'occupied') return '已占用'
      return status
    }
  }
}
</script>

<style scoped>
.room-management h2 { margin-bottom: 20px; }
.toolbar { margin-bottom: 15px; }
</style>
