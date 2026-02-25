<script setup>
import { onUpdated, ref } from 'vue'

const props = defineProps({
  messages: {
    type: Array,
    default: () => [],
  },
})

const containerRef = ref(null)

onUpdated(() => {
  const el = containerRef.value
  if (el) {
    el.scrollTop = el.scrollHeight
  }
})
</script>

<template>
  <section ref="containerRef" class="chat-messages">
    <div
      v-for="msg in messages"
      :key="msg.id"
      class="message-row"
      :class="msg.role"
    >
      <div class="avatar">
        <span v-if="msg.role === 'assistant'">AI</span>
        <span v-else>你</span>
      </div>
      <div class="bubble">
        <div class="bubble-content">{{ msg.content }}</div>
      </div>
    </div>
    <div v-if="messages.length === 0" class="chat-empty-hint">
      发送一条消息，开始和你的智能助手对话吧。
    </div>
  </section>
</template>

<style scoped>
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px 24px 8px 24px;
}

.message-row {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.message-row.assistant .bubble {
  background: linear-gradient(135deg, #020617, #020617);
  border: 1px solid rgba(55, 65, 81, 0.9);
}

.message-row.user {
  flex-direction: row-reverse;
}

.message-row.user .bubble {
  background: linear-gradient(135deg, #1d4ed8, #4f46e5);
  border: none;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  background-color: #111827;
  color: #e5e7eb;
}

.message-row.user .avatar {
  background-color: #0369a1;
}

.bubble {
  max-width: min(720px, 100% - 80px);
  border-radius: 12px;
  padding: 10px 12px;
  font-size: 14px;
  line-height: 1.6;
  color: #e5e7eb;
}

.bubble-content {
  white-space: pre-wrap;
  word-break: break-word;
}

.chat-empty-hint {
  margin-top: 40px;
  text-align: center;
  font-size: 14px;
  color: #9ca3af;
}
</style>
