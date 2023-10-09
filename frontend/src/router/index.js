// Composables
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/default/Default.vue'),
    children: [
      {
        path: '',
        alias: '/home',
        name: 'home',
        component: () => import('@/views/Home.vue'),
      },
      {
        path: '/upload',
        name: 'upload',
        component: () => import('@/views/Upload.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
