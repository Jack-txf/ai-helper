<script setup>
import { computed, ref } from 'vue'
import ChatSidebar from './components/ChatSidebar.vue'
import ChatMessages from './components/ChatMessages.vue'
import ChatInput from './components/ChatInput.vue'

const API_BASE_URL = 'http://localhost:9999'

const conversations = ref([
  {
    id: '1',
    title: '示例对话',
    messages: [
      {
        id: 'm1',
        role: 'assistant',
        content: '你好，我是你的智能助手，有什么可以帮你？',
      },
    ],
  },
])

const activeId = ref('1')
const loading = ref(false)

const activeConversation = computed(() => {
  return conversations.value.find((c) => c.id === activeId.value) || null
})

const activeMessages = computed(() => activeConversation.value?.messages || [])

const handleSelectConversation = (id) => {
  activeId.value = id
}

const handleNewChat = () => {
  const id = Date.now().toString()
  const conv = {
    id,
    title: '新对话',
    messages: [],
  }
  conversations.value.unshift(conv)
  activeId.value = id
}

const appendMessage = (role, content = '') => {
  if (!activeConversation.value) return
  const newMessage = {
    id: `${Date.now()}-${Math.random().toString(16).slice(2)}`,
    role,
    content,
  }
  activeConversation.value.messages.push(newMessage)
  return newMessage
}

const callAssistantStream = async (content) => {
  loading.value = true

  // 先在当前会话中追加一条占位的 AI 消息
  appendMessage('assistant', '（正在获取 AI 回复…）')

  const conv = activeConversation.value
  if (!conv) {
    loading.value = false
    return
  }

  // 记录这条 AI 消息在数组中的下标，通过数组访问来保持响应式
  const assistantIndex = conv.messages.length - 1

  try {
    const url = `${API_BASE_URL}/zhipu1/chat2Stream?message=${encodeURIComponent(
      content
    )}`

    const source = new EventSource(url)

    source.onmessage = (event) => {
      // 每条 SSE 消息的 data 追加到同一条 AI 消息上
      console.log('SSE message:', event.data)
      if (!event.data) return

      const target = conv.messages[assistantIndex]
      if (!target) return

      // 直接修改响应式数组里的对象，确保触发视图更新
      target.content += event.data
    }

    source.onerror = () => {
      // 出错或服务端关闭连接时触发，当作本轮对话结束
      source.close()
      loading.value = false
    }
  } catch (e) {
    const target = conv.messages[assistantIndex]
    if (target) {
      target.content = '调用后端接口出错，请检查服务是否启动以及 CORS 配置。'
    }
    loading.value = false
  }
}

const handleSend = async (content) => {
  if (!content) return
  if (!activeConversation.value) {
    handleNewChat()
  }
  appendMessage('user', content)
  await callAssistantStream(content)
}
</script>

<template>
  <div class="app-root">
    <ChatSidebar
      :conversations="conversations"
      :active-id="activeId"
      @select="handleSelectConversation"
      @new-chat="handleNewChat"
    />

    <main class="chat-main">
      <header class="chat-header">
        <div class="chat-title">
          <span class="chat-title-main">智能对话助手</span>
          <span class="chat-title-sub">类似 ChatGPT 的前端界面示例</span>
        </div>
      </header>

      <ChatMessages :messages="activeMessages" />

      <ChatInput :loading="loading" @send="handleSend" />
    </main>
  </div>
</template>

<style scoped>
.app-root {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background-color: #050509;
  color: #e5e7eb;
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: radial-gradient(circle at top, #1f2933 0, #020617 55%);
}

.chat-header {
  padding: 16px 24px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.35);
  background-color: rgba(15, 23, 42, 0.9);
  backdrop-filter: blur(12px);
}

.chat-title-main {
  font-size: 16px;
  font-weight: 600;
}

.chat-title-sub {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  color: #9ca3af;
}
</style>
