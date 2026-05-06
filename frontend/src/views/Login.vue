<template>
  <div class="login-container">
    <div class="login-box">
      <h1>酒店管理系统</h1>
      <el-form :model="loginForm" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
        <div class="register-link">
          <span>没有账号？</span>
          <router-link to="/register">点击注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import api from '../api'
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return

        this.loading = true
        const { username, password } = this.loginForm

        // 先尝试管理员登录
        api.login(username, password).then(res => {
          sessionStorage.setItem('user', JSON.stringify({ ...res.data, role: 'admin' }))
          this.$router.push('/main/dashboard')
        }).catch(() => {
          // 管理员失败，尝试顾客
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
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 350px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
}

.login-box h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.register-link {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
