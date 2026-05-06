<template>
  <div class="customer-layout">
    <div class="sidebar">
      <div class="logo"></div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/customer/home">首页</el-menu-item>
        <el-menu-item index="/customer/rooms">浏览房间</el-menu-item>
        <el-menu-item index="/customer/bookings">我的预订</el-menu-item>
      </el-menu>
      <div class="logout" @click="handleLogout">退出</div>
    </div>
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: 'CustomerMain',
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleLogout() {
      sessionStorage.removeItem('user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.customer-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  background: #2c3e50;
  color: white;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  border-bottom: 1px solid #34495e;
}

.el-menu {
  border: none;
  background: #2c3e50;
}

.el-menu-item {
  color: #bfcbd9;
}

.el-menu-item:hover,
.el-menu-item.is-active {
  background: #34495e;
  color: #3498db;
}

.logout {
  position: absolute;
  bottom: 20px;
  width: 200px;
  text-align: center;
  padding: 10px;
  cursor: pointer;
  color: #bfcbd9;
}

.logout:hover {
  color: #3498db;
}

.content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}
</style>
