<template>
  <div class="login-container">
    <!-- Background decoration -->
    <div class="bg-decoration">
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>

    <!-- Login card -->
    <div class="login-card">
      <!-- Logo & Title -->
      <div class="login-header">
        <div class="login-logo">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 21V7H7V21H3Z" fill="currentColor"/>
            <path d="M7 21V11H11V21H7Z" fill="currentColor"/>
            <path d="M11 21V9H15V21H11Z" fill="currentColor"/>
            <path d="M15 21V13H19V21H15Z" fill="currentColor"/>
          </svg>
        </div>
        <h1 class="login-title">酒店管理系统</h1>
        <p class="login-subtitle">专业、高效、可靠的酒店管理解决方案</p>
      </div>

      <!-- Login Form -->
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="formRef"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <div class="input-wrapper">
            <label class="input-label">用户名</label>
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              :prefix-icon="UserIcon"
            />
          </div>
        </el-form-item>

        <el-form-item prop="password">
          <div class="input-wrapper">
            <label class="input-label">密码</label>
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              :prefix-icon="LockIcon"
              show-password
              @keyup.enter="handleLogin"
            />
          </div>
        </el-form-item>

        <el-form-item>
          <button
            type="submit"
            class="login-btn"
            :class="{ 'is-loading': loading }"
            :disabled="loading"
          >
            <span v-if="!loading">登录</span>
            <span v-else class="loading-spinner"></span>
          </button>
        </el-form-item>
      </el-form>

      <!-- Register link -->
      <div class="register-section">
        <span class="register-text">还没有账号？</span>
        <router-link to="/register" class="register-link">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage } from 'element-plus'

const UserIcon = {
  template: `
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
      <circle cx="12" cy="7" r="4"/>
    </svg>
  `
}

const LockIcon = {
  template: `
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
      <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
    </svg>
  `
}

export default {
  name: 'Login',
  data() {
    return {
      loginForm: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false,
      UserIcon,
      LockIcon
    }
  },
  methods: {
    handleLogin() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return

        this.loading = true
        const { username, password } = this.loginForm

        api.login(username, password).then(res => {
          sessionStorage.setItem('user', JSON.stringify({ ...res.data, role: 'admin' }))
          this.$router.push('/main/dashboard')
        }).catch(() => {
          return api.customerLogin(username, password)
        }).then(res => {
          if (res) {
            const userData = {
              id: res.data.id,
              username: res.data.username,
              name: res.data.name || '',
              phone: res.data.phone || '',
              role: 'customer'
            }
            sessionStorage.setItem('user', JSON.stringify(userData))
            this.$router.push('/customer/home')
          }
        }).catch(() => {
          ElMessage.error('用户名或密码错误')
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
.login-container {
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

/* ===== LOGIN CARD ===== */
.login-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: var(--space-10);
  background: var(--color-surface);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

/* ===== LOGIN HEADER ===== */
.login-header {
  text-align: center;
  margin-bottom: var(--space-8);
}

.login-logo {
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

.login-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-primary);
  margin-bottom: var(--space-2);
}

.login-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
  margin: 0;
}

/* ===== LOGIN FORM ===== */
.login-form {
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

/* ===== LOGIN BUTTON ===== */
.login-btn {
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

.login-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-lg);
}

.login-btn:active:not(:disabled) {
  transform: translateY(0);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-btn.is-loading {
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

/* ===== REGISTER SECTION ===== */
.register-section {
  text-align: center;
}

.register-text {
  font-size: var(--text-sm);
  color: var(--color-text-muted);
}

.register-link {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-accent);
  margin-left: var(--space-1);
  transition: color var(--transition-fast);
}

.register-link:hover {
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