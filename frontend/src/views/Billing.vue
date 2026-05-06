<template>
  <div class="billing">
    <h2>账单管理</h2>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">创建账单</el-button>
    </div>
    <el-table :data="bills" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="checkInId" label="入住记录ID" width="120" />
      <el-table-column prop="roomCharge" label="房费" width="100" />
      <el-table-column prop="otherCharges" label="其他费用" width="100" />
      <el-table-column prop="totalAmount" label="总费用" width="100" />
      <el-table-column prop="isPaid" label="支付状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isPaid ? 'success' : 'danger'">{{ row.isPaid ? '已支付' : '未支付' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="paymentMethod" label="支付方式" width="100" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="primary" v-if="!row.isPaid" @click="handlePay(row)">支付</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="创建账单" width="500px">
      <el-form :model="billForm" label-width="100px">
        <el-form-item label="入住记录ID">
          <el-input-number v-model="billForm.checkInId" :min="1" />
        </el-form-item>
        <el-form-item label="房费">
          <el-input-number v-model="billForm.roomCharge" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="其他费用">
          <el-input-number v-model="billForm.otherCharges" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="billForm.notes" type="textarea" />
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
import { ElMessage } from 'element-plus'

export default {
  name: 'Billing',
  data() {
    return {
      bills: [],
      dialogVisible: false,
      billForm: { checkInId: null, roomCharge: 0, otherCharges: 0, totalAmount: 0, notes: '' }
    }
  },
  mounted() { this.loadBills() },
  methods: {
    loadBills() { api.getBills().then(res => { this.bills = res.data }) },
    showAddDialog() {
      this.billForm = { roomCharge: 0, otherCharges: 0, notes: '' }
      this.dialogVisible = true
    },
    handleSave() {
      this.billForm.totalAmount = this.billForm.roomCharge + this.billForm.otherCharges
      api.createBill(this.billForm).then(() => {
        ElMessage.success('创建成功')
        this.dialogVisible = false
        this.loadBills()
      })
    },
    handlePay(row) {
      row.isPaid = 1
      row.paymentMethod = 'cash'
      api.payBill(row.id, row).then(() => {
        ElMessage.success('支付成功')
        this.loadBills()
      })
    }
  }
}
</script>

<style scoped>
.billing h2 { margin-bottom: 20px; }
.toolbar { margin-bottom: 15px; }
</style>