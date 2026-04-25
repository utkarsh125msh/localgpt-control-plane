# 🔒 LocalGPT Control Plane

> Run AI models 100% on-premises. No data leaves your network. Ever.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![Ollama](https://img.shields.io/badge/Ollama-llama3.2:1b-orange)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![License](https://img.shields.io/badge/license-MIT-green)

---

## 🧩 The Problem

Enterprises want to use AI — but **can't send sensitive data to OpenAI, Google, or any cloud API.**

Healthcare records. Legal documents. Financial data. Defense contracts.

> "You can't put patient data into ChatGPT." — Every enterprise compliance team, ever.

---

## ✅ The Solution

**LocalGPT Control Plane** is a minimal proof-of-concept of air-gapped AI inference:

- 🧠 LLM runs **locally** via Ollama — no internet required after setup
- ⚙️ Java Spring Boot REST API acts as the **control plane** between your apps and the model
- 🖥️ React dashboard gives a **real-time view** of system health, model status, and cost savings
- 🐳 Ships as a single `docker compose up` command

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│                  CLIENT NETWORK                      │
│                                                      │
│   ┌──────────┐    ┌─────────────────┐   ┌────────┐  │
│   │  React   │───▶│ Spring Boot API │──▶│ Ollama │  │
│   │Dashboard │    │   :8080         │   │ :11434 │  │
│   │  :3000   │    │                 │   │llama3.2│  │
│   └──────────┘    └─────────────────┘   └────────┘  │
│                                                      │
│              ✅ Zero data leaves this box             │
└─────────────────────────────────────────────────────┘
```

---

## 🚀 Quick Start

### Prerequisites
- Docker + Docker Compose
- 8GB RAM minimum
- 4GB free disk space

### One-command deploy
```bash
git clone https://github.com/utkarsh125msh/localgpt-control-plane.git
cd localgpt-control-plane
docker compose up --build
```

### Pull the model (first time only)
```bash
docker exec -it ollama ollama pull llama3.2:1b
```

### Open the dashboard
```
http://localhost:3000
```

---

## 📡 API Reference

### `GET /api/health`
Returns system status. No auth required.

```json
{
  "status": "UP",
  "ramUsedMb": 220,
  "ramTotalMb": 2048,
  "ollamaRunning": true,
  "model": "llama3.2:1b"
}
```

### `POST /api/chat`
Sends prompt to local LLM. Requires API key.

**Headers:**
```
Content-Type: application/json
X-API-KEY: localgpt-demo-key-2024
```

**Request:**
```json
{ "prompt": "What is air-gapped AI inference?" }
```

**Response:**
```json
{
  "response": "Air-gapped AI inference refers to...",
  "processingTimeMs": 4200,
  "estimatedTokens": 87,
  "costSavedVsGpt4": 0.0004
}
```

---

## 🗂️ Project Structure

```
localgpt-control-plane/
├── backend/                          # Spring Boot API
│   ├── src/main/java/com/localgpt/
│   │   ├── controller/               # REST endpoints
│   │   ├── service/                  # Ollama HTTP client
│   │   ├── model/                    # Request/Response DTOs
│   │   └── config/                   # CORS + API key filter
│   └── Dockerfile
├── frontend/                         # React + Vite dashboard
│   ├── src/components/
│   │   ├── ChatBox.jsx
│   │   ├── HealthBar.jsx
│   │   └── CostCounter.jsx
│   ├── nginx.conf
│   └── Dockerfile
└── docker-compose.yml
```

---

## 🔐 Security

| Feature | Implementation |
|---|---|
| API Key Auth | `X-API-KEY` header filter on `/api/chat` |
| CORS Policy | Configurable via `CorsConfig.java` |
| No Cloud Calls | Ollama runs fully offline |
| Request Logging | Timestamped logs on every inference call |


## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| LLM Runtime | Ollama + llama3.2:1b |
| Backend | Java 17, Spring Boot 3.2, Maven |
| Frontend | React 18, Vite, plain CSS |
| Deployment | Docker, Docker Compose, nginx |
| Auth | Custom servlet filter (X-API-KEY) |

---

---

## 👨‍💻 Author

**Utkarsh** — Java Spring Boot + React Developer  
Built as a demonstration of on-premises AI inference architecture.

---

