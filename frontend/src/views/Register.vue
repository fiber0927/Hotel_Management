<template>
  <div class="register-container">
    <!-- Background decoration -->
    <div class="bg-decoration">
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>

    <!-- Register card -->
    <div class="register-card">
      <!-- Header -->
      <div class="register-header">
        <div class="register-logo">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 21V7H7V21H3Z" fill="currentColor"/>
            <path d="M7 21V11H11V21H7Z" fill="currentColor"/>
            <path d="M11 21V9H15V21H11Z" fill="currentColor"/>
            <path d="M15 21V13H19V21H15Z" fill="currentColor"/>
          </svg>
        </div>
        <h1 class="register-title">用户注册</h1>
        <p class="register-subtitle">创建账户开始您的酒店预订之旅</p>
      </div>

      <!-- Form -->
      <el-form
        :model="regForm"
        :rules="rules"
        ref="formRef"
        class="register-form"
        @submit.prevent="handleRegister"
      >
        <el-form-item prop="username">
          <div class="input-wrapper">
            <label class="input-label">用户名</label>
            <el-input v-model="regForm.username" placeholder="请输入用户名（3-20字符）" size="large" />
          </div>
        </el-form-item>

        <el-form-item prop="phone">
          <div class="input-wrapper">
            <label class="input-label">手机号</label>
            <el-input v-model="regForm.phone" placeholder="请输入手机号" size="large" maxlength="11" />
          </div>
        </el-form-item>

        <el-form-item prop="password">
          <div class="input-wrapper">
            <label class="input-label">密码</label>
            <el-input v-model="regForm.password" type="password" placeholder="请输入密码（至少6位）" size="large" show-password />
          </div>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <div class="input-wrapper">
            <label class="input-label">确认密码</label>
            <el-input v-model="regForm.confirmPassword" type="password" placeholder="请再次输入密码" size="large" show-password @keyup.enter="handleRegister" />
          </div>
        </el-form-item>

        <el-form-item>
          <button
            type="submit"
            class="register-btn"
            :class="{ 'is-loading': loading }"
            :disabled="loading"
          >
            <span v-if="!loading">注册</span>
            <span v-else class="loading-spinner"></span>
          </button>
        </el-form-item>
      </el-form>

      <!-- Login link -->
      <div class="login-section">
        <span class="login-text">已有账号？</span>
        <router-link to="/login" class="login-link">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage } from 'element-plus'

export default {
  name: 'Register',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.regForm.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      regForm: { username: '', phone: '', password: '', confirmPassword: '' },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名3-20个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码至少6个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleRegister() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return

        this.loading = true
        const { username, password, phone } = this.regForm

        api.customerRegister(username, password, phone).then(() => {
          ElMessage.success('注册成功，请登录')
          this.$router.push('/login')
        }).catch(err => {
          ElMessage.error(err.response?.data?.message || '注册失败')
        }).finally(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style scoped>
/* ===== CONTAINER ===== */
.register-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, var(--color-primary-dark) 0%, var(--color-primary) 50%, var(--color-secondary) 100%);
  overflow: hidden;
}

/* ===== BACKGROUND DECORATION ===== */
.bg-decoration {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.15;
}

.bg-shape-1 {
  width: 600px;
  height: 600px;
  background: var(--color-accent);
  top: -200px;
  right: -100px;
}

.bg-shape-2 {
  width: 400px;
  height: 400px;
  background: var(--color-accent-light);
  bottom: -100px;
  left: -50px;
}

.bg-shape-3 {
  width: 300px;
  height: 300px;
  background: var(--color-success);
  bottom: 20%;
  right: 10%;
}

/* ===== REGISTER CARD ===== */
.register-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: var(--space-10);
  background: var(--color-surface);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

/* ===== REGISTER HEADER ===== */
.register-header {
  text-align: center;
  margin-bottom: var(--space-8);
}

.register-logo {
  width: 64px;
  height: 64px;
  margin: 0 auto var(--space-4);
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.register-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.register-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
  margin: 0;
}

/* ===== REGISTER FORM ===== */
.register-form {
  margin-bottom: var(--space-6);
}

.input-wrapper {
  width: 100%;
}

.input-label {
  display: block;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

/* ===== REGISTER BUTTON ===== */
.register-btn {
  width: 100%;
  height: 48px;
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--color-text-inverse);
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
}

.register-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-lg);
}

.register-btn:active:not(:disabled) {
  transform: translateY(0);
}

.register-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-btn.is-loading {
  background: var(--color-secondary);
}

/* ===== LOADING SPINNER ===== */
.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid transparent;
  border-top-color: currentColor;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ===== LOGIN SECTION ===== */
.login-section {
  text-align: center;
}

.login-text {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
}

.login-link {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-accent);
  margin-left: var(--space-1);
  transition: color var(--transition-fast);
}

.login-link:hover {
  color: var(--color-accent-dark);
  text-decoration: underline;
}

/* ===== ELEMENT PLUS OVERRIDES ===== */
:deep(.el-input__wrapper) {
  border-radius: var(--radius-md);
  padding: var(--space-3) var(--space-4);
}

:deep(.el-input__inner) {
  height: 24px;
}

:deep(.el-form-item) {
  margin-bottom: var(--space-5);
}
</style>