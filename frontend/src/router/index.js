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
      {
        path: '/:id',
        name: 'play',
        component: () => import('@/views/Play.vue'),
      },
      {
        path: '/myVideos',
        name: 'myVideos',
        component: () => import('@/views/MyVideos.vue'),
      },
      {
        path: '/welcome',
        name: 'welcome',
        component: () => import('@/views/Welcome.vue'),
      },
      {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login.vue'),
      },
      {
        path: '/signUp',
        name: 'signUp',
        component: () => import('@/views/SignUp.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
