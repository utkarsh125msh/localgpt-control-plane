function HealthBar({ health }) {
  const status = health?.status || 'CHECKING...'
  const isUp = status === 'UP'

  return (
    <div className="health-bar">
      <div className="health-item">
        <span className={`dot ${isUp ? 'dot-green' : 'dot-red'}`}></span>
        <span>System: <strong>{status}</strong></span>
      </div>
      <div className="health-item">
        <span>Ollama: <strong>{health?.ollamaRunning ? '✅ Running' : '❌ Offline'}</strong></span>
      </div>
      <div className="health-item">
        <span>RAM Used: <strong>{health?.ramUsedMb ?? '---'} MB</strong></span>
      </div>
      <div className="health-item">
        <span>Data Leaves Network: <strong style={{color: '#ff4d4d'}}>NEVER</strong></span>
      </div>
    </div>
  )
}

export default HealthBar