<template>
  <div class="guest-management">
    <h2>客人管理</h2>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">新增客人</el-button>
    </div>
    <el-table :data="guests" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑客人' : '新增客人'" width="500px">
      <el-form :model="guestForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="guestForm.name" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="guestForm.phone" />
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
  name: 'GuestManagement',
  data() {
    return {
      guests: [],
      dialogVisible: false,
      isEdit: false,
      guestForm: { id: null, name: '', phone: '' }
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
      ElMessageBox.confirm('确认删除？', '提示').then(() => {
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
.guest-management h2 { margin-bottom: 20px; }
.toolbar { margin-bottom: 15px; }
</style>