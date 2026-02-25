<script setup>
import { computed } from 'vue'

const props = defineProps({
  conversations: {
    type: Array,
    default: () => [],
  },
  activeId: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['select', 'new-chat'])

const hasConversations = computed(() => props.conversations.length > 0)
</script>

<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <button class="new-chat-btn" type="button" @click="emit('new-chat')">
        + 新对话
      </button>
    </div>
    <div class="sidebar-body">
      <div v-if="hasConversations" class="conversation-list">
        <button
          v-for="item in conversations"
          :key="item.id"
          class="conversation-item"
          :class="{ active: item.id === activeId }"
          type="button"
          @click="emit('select', item.id)"
        >
          <span class="conversation-title">{{ item.title }}</span>
        </button>
      </div>
      <div v-else class="conversation-empty">暂无会话，点击上方按钮开始聊天</div>
    </div>
    <div class="sidebar-footer">
      <div class="sidebar-footer-text">智能助手 · Powered by Your API</div>
    </div>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 260px;
  display: flex;
  flex-direction: column;
  background-color: #020617;
  border-right: 1px solid rgba(30, 64, 175, 0.65);
}

.sidebar-header {
  padding: 12px 10px;
  border-bottom: 1px solid rgba(30, 64, 175, 0.65);
}

.new-chat-btn {
  width: 100%;
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid rgba(148, 163, 184, 0.6);
  background: linear-gradient(135deg, #0b1120, #111827);
  color: #e5e7eb;
  font-size: 13px;
  cursor: pointer;
}

.new-chat-btn:hover {
  border-color: #60a5fa;
  background: linear-gradient(135deg, #020617, #111827);
}

.sidebar-body {
  flex: 1;
  overflow-y: auto;
  padding: 4px 6px 4px 6px;
}

.conversation-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.conversation-item {
  width: 100%;
  padding: 8px 10px;
  border-radius: 6px;
  border: none;
  background: transparent;
  text-align: left;
  color: #e5e7eb;
  font-size: 13px;
  cursor: pointer;
}

.conversation-item:hover {
  background-color: rgba(30, 64, 175, 0.4);
}

.conversation-item.active {
  background: linear-gradient(135deg, #1d4ed8, #4f46e5);
}

.conversation-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.conversation-empty {
  padding: 16px 10px;
  font-size: 12px;
  color: #9ca3af;
}

.sidebar-footer {
  padding: 8px 10px;
  border-top: 1px solid rgba(30, 64, 175, 0.65);
  font-size: 11px;
  color: #6b7280;
}

.sidebar-footer-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
