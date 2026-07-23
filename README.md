\# AI Log Analyzer



AI-powered log analysis using Claude API.



\## Quick Start



\### Step 1: Set API Key
set ANTHROPIC\_API\_KEY=sk-ant-YOUR-KEY



step 2: Build

mvn clean package



\### Step 3: Run

java -jar target/ai-log-analyzer-1.0.0-jar-with-dependencies.jar



\### Step 4: Open Browser

http://localhost:8080



\## Features

\- Error Detection

\- Performance Analysis

\- Security Threat Detection

\- Pattern Recognition



This deployment uses \*\*simulated AI responses\*\* to demonstrate 

the full-stack architecture without API costs.



To use with real Claude API:

1\. Get API key from https://console.anthropic.com/keys

2\. Add credits to your account

3\. Set `USE\_MOCK = false` in ClaudeApiClient.java

4\. Redeploy



\## Features Demonstrated



Full-stack web application (Frontend + Backend)

&#x20;REST API design with Javalin

&#x20;Interactive UI with real-time analysis

&#x20;Docker containerization

&#x20;Production deployment on Railway

&#x20;Mock AI responses for realistic demo

