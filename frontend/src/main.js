import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import zhCn from './i18n/zh-CN'

// Global styles - Design tokens & global CSS
import './assets/styles/design-tokens.css'
import './assets/styles/global.css'

const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.mount('#app')