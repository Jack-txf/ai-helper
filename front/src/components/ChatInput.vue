<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  loading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['send'])

const text = ref('')
const canSend = ref(false)

watch(
  text,
  (val) => {
    canSend.value = val.trim().length > 0
  },
  { immediate: true }
)

const handleSend = () => {
  if (!canSend.value || props.loading) return
  emit('send', text.value.trim())
  text.value = ''
}

const handleKeyDown = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    handleSend()
  }
}
</script>

<template>
  <footer class="chat-input">
    <textarea
      v-model="text"
      class="chat-textarea"
      placeholder="给 AI 发送一条消息，Enter 发送，Shift+Enter 换行"
      rows="1"
      @keydown="handleKeyDown"
    ></textarea>
    <div class="chat-input-actions">
      <button
        class="send-btn"
        type="button"
        :disabled="!canSend || loading"
        @click="handleSend"
      >
        {{ loading ? '思考中...' : '发送' }}
      </button>
    </div>
  </footer>
</template>

<style scoped>
.chat-input {
  padding: 12px 24px 16px 24px;
  border-top: 1px solid rgba(30, 64, 175, 0.65);
  background: linear-gradient(180deg, #020617, #020617);
}

.chat-textarea {
  width: 100%;
  resize: none;
  border-radius: 12px;
  border: 1px solid rgba(55, 65, 81, 0.9);
  background-color: #020617;
  color: #e5e7eb;
  padding: 10px 12px;
  font-size: 14px;
  line-height: 1.6;
  outline: none;
}

.chat-textarea:focus {
  border-color: #60a5fa;
}

.chat-input-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.send-btn {
  min-width: 86px;
  padding: 6px 14px;
  border-radius: 999px;
  border: none;
  font-size: 13px;
  font-weight: 500;
  color: #e5e7eb;
  background: linear-gradient(135deg, #22c55e, #16a34a);
  cursor: pointer;
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.send-btn:not(:disabled):hover {
  filter: brightness(1.05);
}
</style>
