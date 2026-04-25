import { useState, useRef, useEffect } from 'react'

function ChatBox({ messages, onSend, loading }) {
  const [input, setInput] = useState('')
  const bottomRef = useRef(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, loading])

  const handleSend = () => {
    if (!input.trim() || loading) return
    onSend(input.trim())
    setInput('')
  }

  const handleKey = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div className="chat-container">
      <div className="chat-messages">
        {messages.length === 0 && (
          <div className="empty-state">
            <p>🔐 Your prompts never leave this machine.</p>
            <p>Ask anything — it runs 100% locally.</p>
          </div>
        )}
        {messages.map((msg, i) => (
          <div key={i} className={`message ${msg.role}`}>
            <div className="message-bubble">
              <span className="message-role">{msg.role === 'user' ? '👤 You' : '🤖 LocalGPT'}</span>
              <p>{msg.text}</p>
              {msg.role === 'assistant' && msg.tokens && (
                <span className="message-meta">
                  ~{msg.tokens} tokens · {msg.timeMs}ms · saved ${msg.costSaved?.toFixed(4)} vs GPT-4o
                </span>
              )}
            </div>
          </div>
        ))}
        {loading && (
          <div className="message assistant">
            <div className="message-bubble">
              <span className="message-role">🤖 LocalGPT</span>
              <p className="thinking">Thinking locally<span className="dots">...</span></p>
            </div>
          </div>
        )}
        <div ref={bottomRef} />
      </div>

      <div className="chat-input-row">
        <textarea
          className="chat-input"
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={handleKey}
          placeholder="Ask something... (Enter to send)"
          rows={2}
          disabled={loading}
        />
        <button
          className="send-btn"
          onClick={handleSend}
          disabled={loading || !input.trim()}
        >
          {loading ? '⏳' : 'Send →'}
        </button>
      </div>
    </div>
  )
}

export default ChatBox