<template>
  <div class="billing">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">账单管理</h1>
        <p class="page-description">管理客户账单和支付</p>
      </div>
      <button class="btn btn--primary" @click="showAddDialog">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        创建账单
      </button>
    </div>

    <!-- Table -->
    <div class="table-container">
      <el-table :data="bills" border stripe :header-cell-style="headerStyle">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="checkInId" label="入住记录ID" width="120" />
        <el-table-column prop="roomCharge" label="房费" width="100">
          <template #default="{ row }">
            <span class="price">¥{{ row.roomCharge }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="otherCharges" label="其他费用" width="100">
          <template #default="{ row }">
            <span class="price">¥{{ row.otherCharges }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总费用" width="100">
          <template #default="{ row }">
            <span class="price price--total">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isPaid" label="支付状态" width="100">
          <template #default="{ row }">
            <span class="status-badge" :class="row.isPaid ? 'status--paid' : 'status--unpaid'">
              {{ row.isPaid ? '已支付' : '未支付' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="支付方式" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <button
              v-if="!row.isPaid"
              class="btn btn--small btn--primary"
              @click="handlePay(row)"
            >
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="1" y="4" width="22" height="16" rx="2" ry="2"/>
                <line x1="1" y1="10" x2="23" y2="10"/>
              </svg>
              支付
            </button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" title="创建账单" width="500px" class="custom-dialog">
      <el-form :model="billForm" label-width="100px" class="dialog-form">
        <el-form-item label="入住记录ID">
          <el-input-number v-model="billForm.checkInId" :min="1" class="full-width" />
        </el-form-item>
        <el-form-item label="房费">
          <el-input-number v-model="billForm.roomCharge" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="其他费用">
          <el-input-number v-model="billForm.otherCharges" :min="0" :precision="2" class="full-width" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="billForm.notes" type="textarea" />
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
import { ElMessage } from 'element-plus'

export default {
  name: 'Billing',
  data() {
    return {
      bills: [],
      dialogVisible: false,
      billForm: { checkInId: null, roomCharge: 0, otherCharges: 0, totalAmount: 0, notes: '' },
      headerStyle: {
        background: 'var(--color-muted)',
        color: 'var(--color-text-primary)',
        fontWeight: '600'
      }
    }
  },
  mounted() { this.loadBills() },
  methods: {
    loadBills() { api.getBills().then(res => { this.bills = res.data }) },
    showAddDialog() {
      this.billForm = { checkInId: null, roomCharge: 0, otherCharges: 0, notes: '' }
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

.price {
  font-family: var(--font-mono);
  font-weight: var(--font-semibold);
  color: var(--color-text-primary);
}

.price--total {
  color: var(--color-primary);
  font-weight: var(--font-bold);
}

.status-badge {
  display: inline-block;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.status--paid { background: #D1FAE5; color: #065F46; }
.status--unpaid { background: #FEE2E2; color: #991B1B; }

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

.btn--small {
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
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