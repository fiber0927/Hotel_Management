<template>
  <div class="main-layout">
    <div class="sidebar">
      <div class="logo"></div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/main/dashboard">
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/main/rooms">
          <span>房间管理</span>
        </el-menu-item>
        <el-menu-item index="/main/bookings">
          <span>预订管理</span>
        </el-menu-item>
        <el-menu-item index="/main/checkin">
          <span>入住退房</span>
        </el-menu-item>
        <el-menu-item index="/main/housekeeping">
          <span>客房服务</span>
        </el-menu-item>
        <el-menu-item index="/main/bills">
          <span>账单管理</span>
        </el-menu-item>
        <el-menu-item index="/main/guests">
          <span>客人管理</span>
        </el-menu-item>
      </el-menu>
      <div class="logout" @click="handleLogout">
        <span>退出</span>
      </div>
    </div>
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Main',
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleLogout() {
      sessionStorage.removeItem('admin')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.main-layout {
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

.el-menu-item:hover, .el-menu-item.is-active {
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
