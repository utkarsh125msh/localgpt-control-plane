function CostCounter({ totalTokens, totalCostSaved }) {
  return (
    <div className="cost-bar">
      <div className="cost-item">
        <span className="cost-label">Tokens Processed Locally</span>
        <span className="cost-value">{totalTokens.toLocaleString()}</span>
      </div>
      <div className="cost-divider" />
      <div className="cost-item">
        <span className="cost-label">Estimated Cost Saved vs GPT-4o</span>
        <span className="cost-value green">${totalCostSaved.toFixed(4)}</span>
      </div>
      <div className="cost-divider" />
      <div className="cost-item">
        <span className="cost-label">Data Sent to OpenAI</span>
        <span className="cost-value red">$0.00 · 0 bytes</span>
      </div>
    </div>
  )
}

export default CostCounter