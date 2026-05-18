<template>
  <div class="main-layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <!-- Logo Area -->
      <div class="sidebar-logo">
        <div class="logo-icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 21V7H7V21H3Z" fill="currentColor"/>
            <path d="M7 21V11H11V21H7Z" fill="currentColor"/>
            <path d="M11 21V9H15V21H11Z" fill="currentColor"/>
            <path d="M15 21V13H19V21H15Z" fill="currentColor"/>
          </svg>
        </div>
        <span class="logo-text">酒店管理</span>
      </div>

      <!-- Navigation Menu -->
      <nav class="sidebar-nav">
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ 'is-active': isActive(item.path) }"
        >
          <span class="nav-icon">
            <component :is="item.icon" />
          </span>
          <span class="nav-label">{{ item.label }}</span>
        </router-link>
      </nav>

      <!-- Logout Button -->
      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <span class="logout-icon">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16,17 21,12 16,7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </span>
          <span>退出登录</span>
        </button>
      </div>
    </aside>

    <!-- Main Content Area -->
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'Main',
  data() {
    return {
      menuItems: [
        { path: '/main/dashboard', label: '首页概览' },
        { path: '/main/rooms', label: '房间管理' },
        { path: '/main/bookings', label: '预订管理' },
        { path: '/main/checkin', label: '入住退房' },
        { path: '/main/housekeeping', label: '客房服务' },
        { path: '/main/bills', label: '账单管理' },
        { path: '/main/guests', label: '客人管理' }
      ]
    }
  },
  methods: {
    isActive(path) {
      return this.$route.path === path
    },
    handleLogout() {
      sessionStorage.removeItem('user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
/* ===== LAYOUT ===== */
.main-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background-color: var(--color-background);
}

/* ===== SIDEBAR ===== */
.sidebar {
  width: var(--sidebar-width);
  min-width: var(--sidebar-width);
  height: 100vh;
  background-color: var(--sidebar-bg);
  display: flex;
  flex-direction: column;
  transition: width var(--transition-normal);
}

/* ===== LOGO ===== */
.sidebar-logo {
  height: var(--header-height);
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: 0 var(--space-5);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, var(--color-accent) 0%, var(--color-accent-light) 100%);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.logo-text {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-inverse);
  letter-spacing: -0.01em;
}

/* ===== NAVIGATION ===== */
.sidebar-nav {
  flex: 1;
  padding: var(--space-4) var(--space-3);
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-md);
  color: var(--sidebar-text-muted);
  text-decoration: none;
  transition: all var(--transition-fast);
  cursor: pointer;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  background-color: var(--sidebar-hover);
  color: var(--sidebar-text);
}

.nav-item.is-active {
  background-color: var(--sidebar-active-bg);
  color: var(--color-text-inverse);
  border-left-color: var(--sidebar-active-border);
}

.nav-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0.8;
}

.nav-item.is-active .nav-icon {
  opacity: 1;
}

.nav-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  white-space: nowrap;
}

/* ===== FOOTER / LOGOUT ===== */
.sidebar-footer {
  padding: var(--space-4) var(--space-3);
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-md);
  color: var(--sidebar-text-muted);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.logout-btn:hover {
  background-color: var(--sidebar-hover);
  color: var(--color-danger-light);
}

.logout-icon {
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ===== CONTENT ===== */
.content {
  flex: 1;
  overflow-y: auto;
  padding: var(--space-6);
  background-color: var(--color-background);
}
</style>