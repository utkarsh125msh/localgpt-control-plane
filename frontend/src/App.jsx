import { useState, useEffect } from 'react'
import ChatBox from './components/ChatBox'
import HealthBar from './components/HealthBar.jsx'
import CostCounter from './components/CostCounter'
import './App.css'

const API_KEY = 'localgpt-demo-key-2024'

function App() {
  const [health, setHealth] = useState(null)
  const [messages, setMessages] = useState([])
  const [totalTokens, setTotalTokens] = useState(0)
  const [totalCostSaved, setTotalCostSaved] = useState(0)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    fetchHealth()
    const interval = setInterval(fetchHealth, 10000)
    return () => clearInterval(interval)
  }, [])

  const fetchHealth = async () => {
    try {
      const res = await fetch('/api/health')
      const data = await res.json()
      setHealth(data)
    } catch (e) {
      setHealth(null)
    }
  }

  const sendMessage = async (prompt) => {
    const userMsg = { role: 'user', text: prompt }
    setMessages(prev => [...prev, userMsg])
    setLoading(true)

    try {
      const res = await fetch('/api/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-API-KEY': API_KEY
        },
        body: JSON.stringify({ prompt })
      })
      const data = await res.json()

      const assistantMsg = {
        role: 'assistant',
        text: data.response,
        tokens: data.estimatedTokens,
        costSaved: data.costSavedVsGpt4,
        timeMs: data.processingTimeMs
      }
      setMessages(prev => [...prev, assistantMsg])
      setTotalTokens(prev => prev + (data.estimatedTokens || 0))
      setTotalCostSaved(prev => prev + (data.costSavedVsGpt4 || 0))
    } catch (e) {
      setMessages(prev => [...prev, {
        role: 'assistant',
        text: '⚠️ Error reaching the local API. Is Spring Boot running?'
      }])
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app">
      <header className="header">
        <div className="header-left">
          <h1>🔒 LocalGPT Control Plane</h1>
          <span className="badge">100% On-Prem</span>
          <span className="badge badge-air">Air-Gapped</span>
        </div>
        <div className="header-right">
          <span className="powered">Powered by {health?.model || 'llama3.2:1b'} · Local Ollama</span>
        </div>
      </header>

      <HealthBar health={health} />
      <CostCounter totalTokens={totalTokens} totalCostSaved={totalCostSaved} />
      <ChatBox messages={messages} onSend={sendMessage} loading={loading} />
    </div>
  )
}

export default App